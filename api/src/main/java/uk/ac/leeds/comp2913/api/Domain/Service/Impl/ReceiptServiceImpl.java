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

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

@Service
public class ReceiptServiceImpl implements ReceiptService {
    private final JavaMailSender javaMailSender;

    @Value("${COMP2913.iTextLicensePath}")
    private String iTextLicensePath;
    private TemplateEngine templateEngine;
    private ReceiptRepository receiptRepository;

    @Autowired
    public ReceiptServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine, ReceiptRepository receiptRepository) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.receiptRepository = receiptRepository;
    }

    // TODO Remove constant variables
    private static final String OUTPUT_PATH = "C:\\Users\\Tom\\Downloads\\receipt.pdf";

    /**
     * TODO: upload PDF to S3
     *
     * @param sales list of sales to create a new receipt
     * @return receipt generated from sales
     */
    @Override
    @Transactional
    public Receipt invoice(String transactionId, Collection<Sale> sales, Customer customer) {
        Receipt receipt = new Receipt(sales, transactionId);

        customer.addReceipt(receipt);

        receipt.invoice();

        this.receiptRepository.save(receipt);

        String htmlContent = createInvoiceHtmlTemplate(receipt, transactionId);

        String pdfFilePath = null;

        try {
            pdfFilePath = createPdfFromHtml(htmlContent, customer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s3Path = uploadReceiptPdfToS3(pdfFilePath);

        receipt.setPdfLocation(s3Path);

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
    public Page<Receipt> findAll(Pageable pageable){
        return receiptRepository.findAll(pageable);
    }

    private String uploadReceiptPdfToS3(String localPdfFilePath) {
        /*TODO: upload to S3*/
        return OUTPUT_PATH;
    }

    private String createPdfFromHtml(String htmlContent, Customer customer) throws IOException {
        // Load license file and convert html to pdf
        LicenseKey.loadLicenseFile(ResourceUtils.getFile(this.iTextLicensePath).getPath());
        HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(OUTPUT_PATH));
        return OUTPUT_PATH;
    }

    private void sendReceiptToCustomer(Receipt receipt) {
        // Create mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(receipt.getCustomer().getEmailAddress());
            mimeMessageHelper.setSubject("Receipt Reference " + receipt.getId());
            mimeMessageHelper.setText("Your receipt is attached to this email.");
            mimeMessageHelper.addAttachment(receipt.getId() + ".pdf", new File(receipt.getPdfLocation()));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // Send email
        javaMailSender.send(mimeMessage);
    }


    public String createInvoiceHtmlTemplate(Receipt receipt, String transactionId) {
        /*TODO: make this return transactionId*/
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
