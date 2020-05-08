package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ReceiptRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Model.Sale;
import uk.ac.leeds.comp2913.api.Domain.Service.QrCodeGenerator;
import uk.ac.leeds.comp2913.api.Domain.Service.ReceiptService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.Util.S3Client;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Service
public class ReceiptServiceImpl implements ReceiptService {
    private final JavaMailSender javaMailSender;

    @Value("${COMP2913.iTextLicensePath}")
    private String iTextLicensePath;
    private TemplateEngine templateEngine;
    private ReceiptRepository receiptRepository;
    private S3Client s3Client;

    @Autowired
    public ReceiptServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine, ReceiptRepository receiptRepository, S3Client s3Client) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.receiptRepository = receiptRepository;
        this.s3Client = s3Client;
    }

    /**
     * @param sales list of sales to create a new receipt
     * @return receipt generated from sales
     */
    @Override
    @Transactional
    public Receipt invoice(String transactionId, List<Sale> sales, Customer customer) throws IOException, MessagingException {
        Receipt receipt = new Receipt(sales, transactionId);

        customer.addReceipt(receipt);

        // Save the receipt and get the ID from the Database
        this.receiptRepository.save(receipt);

        String htmlContent = createInvoiceHtmlTemplate(receipt, transactionId);

        String filePath = "receipt-" + receipt.getId() + ".pdf";
        File pdf = new File(filePath);
        createPdfFromHtml(htmlContent, customer, pdf);
        s3Client.uploadFile(pdf);

        receipt.setPdfLocation(filePath);
//        this.receiptRepository.save(receipt);

        sendReceiptToCustomer(receipt);

        return receipt;
    }

    @Override
    public void delete(Long receiptId) {
        this.receiptRepository.deleteById(receiptId);
    }

    @Override
    public Receipt findById(Long receiptId) {
        return receiptRepository.findById(receiptId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receipt not found with id " + receiptId)
                );
    }

    @Override
    public Page<Receipt> findAll(Pageable pageable) {
        return receiptRepository.findAll(pageable);
    }

    private void createPdfFromHtml(String htmlContent, Customer customer, File file) throws IOException {
        // Load license file and convert html to pdf
        LicenseKey.loadLicenseFile(ResourceUtils.getFile(this.iTextLicensePath).getPath());
        HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(file));
    }

    private void sendReceiptToCustomer(Receipt receipt) throws MessagingException {
        // Create mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(receipt.getCustomer().getEmailAddress());
        mimeMessageHelper.setSubject("Receipt Reference " + receipt.getId());
        mimeMessageHelper.setText("Your receipt is attached to this email.");
        mimeMessageHelper.addAttachment(receipt.getId() + ".pdf", new File(receipt.getPdfLocation()));

        // Send email
        javaMailSender.send(mimeMessage);
    }


    public String createInvoiceHtmlTemplate(Receipt receipt, String transactionId) {
        QrCodeGenerator qrCodeGenerator = new QrCodeGenerator(200, 200);
        String qrCodeFilename = "qrCode-" + receipt.getId() + ".jpeg";
        qrCodeGenerator.generateQRCodeImage(String.valueOf(receipt.getId()), qrCodeFilename);

        final Context ctx = new Context();
        ctx.setVariable("receipt", receipt);
        ctx.setVariable("transactionId", transactionId);
        ctx.setVariable("qrCode", qrCodeFilename);


        return this.templateEngine.process("receipt.html", ctx);
    }
}
