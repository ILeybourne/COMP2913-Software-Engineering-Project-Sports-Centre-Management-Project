package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.util.ResourceUtil;
import com.itextpdf.licensekey.LicenseKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Model.Sale;
import uk.ac.leeds.comp2913.api.Domain.Service.QrCodeGenerator;
import uk.ac.leeds.comp2913.api.Domain.Service.ReceiptService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

@Service
public class ReceiptServiceImpl implements ReceiptService {
  /**
   * TODO: generate PDF, email PDF, upload PDF to S3
   *
   * @param sales list of sales to create a new receipt
   * @return receipt generated from sales
   */

  public String createInvoiceTemplate(String transactionId, Collection<Sale> sales, String receiptId) {
    final Context ctx = new Context(locale);
    ctx.setVariable("receiptId", receiptId);
    ctx.setVariable("sales", receipt.getSales());
    ctx.setVariable("transactionId", transactionId);

    QrCodeGenerator qrCodeGenerator = new QrCodeGenerator(200,200);
    String qrCodeFilename = "qrCode" + transactionId;
    qrCodeGenerator.generateQRCodeImage(transactionId, qrCodeFilename);

    ctx.setVariable("qrCode", qrCodeFilename);

    return this.templateEngine.process("receipt.html", ctx);
  }

  @Override
  @Transactional
  public Receipt invoice(String transactionId, Collection<Sale> sales, String receiptId) {
    createInvoiceTemplate(transactionId, sales, receiptId);
  }

    private final JavaMailSender javaMailSender;
    private final BookingRepository bookingRepository;

    @Value("${COMP2913.iTextLicensePath}")
    private String iTextLicensePath;

    @Autowired
    public ReceiptServiceImpl(JavaMailSender javaMailSender, BookingRepository bookingRepository) {
        this.javaMailSender = javaMailSender;
        this.bookingRepository = bookingRepository;
    }

    // TODO Remove constant variables
    private static final String LICENSE_PATH = "C:\\Users\\Tom\\Google Drive (sc18tjo@leeds.ac.uk)\\Year 2\\Semester 2\\C2913 Software Engineering Project\\Sports Centre Management Project\\iTextKey.xml";
    private static final String OUTPUT_PATH = "C:\\Users\\Tom\\Downloads\\receipt.pdf";
    private static final String EMAIL_ADDRESS = "tom_oddy@live.co.uk";

    /**
     * TODO: generate PDF, email PDF, upload PDF to S3
     *
     * @param sales list of sales to create a new receipt
     * @return receipt generated from sales
     */
    @Override
    @Transactional
    public Receipt invoice(String transactionId, Collection<Sale> sales) {
        // TODO Remove example HTML
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <h1>Example Header Message</h1>\n" +
                "    <p>Example Content</p>\n" +
                "  </body>\n" +
                "</html>";

        // Load license file and convert html to pdf
        try {
            LicenseKey.loadLicenseFile(ResourceUtils.getFile(this.iTextLicensePath).getPath());
            HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(OUTPUT_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(EMAIL_ADDRESS);
            mimeMessageHelper.setSubject("Booking Number " + transactionId);
            mimeMessageHelper.setText("Your receipt is attached to this email.");
            mimeMessageHelper.addAttachment(transactionId + ".pdf", new File(OUTPUT_PATH));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // Send email
        javaMailSender.send(mimeMessage);

        // TODO Return receipt object
        return null;
    }
}
