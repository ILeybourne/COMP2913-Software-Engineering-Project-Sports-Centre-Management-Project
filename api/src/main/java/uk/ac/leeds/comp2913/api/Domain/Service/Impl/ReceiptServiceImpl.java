package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.context.Context;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Model.Sale;
import uk.ac.leeds.comp2913.api.Domain.Service.QrCodeGenerator;
import uk.ac.leeds.comp2913.api.Domain.Service.ReceiptService;

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

  @Autowired
  public ReceiptServiceImpl(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
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

    receipt.setCustomer(customer);

    receipt.invoice();

    String htmlContent = createInvoiceHtmlTemplate(receipt);
    String pdfFilePath = createPdfFromHtml(htmlContent, customer);
    String s3Path = uploadReceiptPdfToS3(pdfFilePath);

    receipt.setPdfLocation(s3Path);

    sendReceiptToCustomer(receipt);

    return receipt;
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
      mimeMessageHelper.addAttachment(String.valueOf(receipt.getId()), new File(receipt.getPdfLocation()));
    } catch (MessagingException e) {
      e.printStackTrace();
    }

    // Send email
    javaMailSender.send(mimeMessage);
  }


  public String createInvoiceHtmlTemplate(Receipt receipt) {
    final Context ctx = new Context();
    ctx.setVariable("receiptId", receipt.getId());
    ctx.setVariable("sales", receipt.getSales());
    ctx.setVariable("transactionId", receipt.getTransactionId());

    QrCodeGenerator qrCodeGenerator = new QrCodeGenerator(200, 200);
    String qrCodeFilename = "qrCode-" + receipt.getId();
    qrCodeGenerator.generateQRCodeImage(String.valueOf(receipt.getId()), qrCodeFilename);

    ctx.setVariable("qrCode", qrCodeFilename);

    return emailTemplateEngine().process("receipt.html", ctx);
  }
}
