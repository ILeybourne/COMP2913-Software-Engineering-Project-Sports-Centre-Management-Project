package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RestController
@RequestMapping("/booking")
public class BookingController {
    private final JavaMailSender javaMailSender;

    public BookingController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @GetMapping("/send_email")
    public Booking booking() throws MessagingException {

        // To address
        final String EMAIL_ADDRESS = "tom_oddy@live.co.uk";

        // Subject
        final String EMAIL_SUBJECT = "Test Email";

        // Body (can be HTML)
        final String EMAIL_BODY = "This is a test email.";

        // Name of attached file
        final String EMAIL_ATTACHMENT_NAME = "receipt.pdf";

        // Path of file to attach
        final String EMAIL_ATTACHMENT_PATH = "info/bookings/receipt_123.pdf";

        // Create message object
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        // Set address, subject, and body
        mimeMessageHelper.setTo(EMAIL_ADDRESS);
        mimeMessageHelper.setSubject(EMAIL_SUBJECT);
        mimeMessageHelper.setText(EMAIL_BODY);

        // Add attachment to email
//        mimeMessageHelper.addAttachment(EMAIL_ATTACHMENT_NAME, new File(EMAIL_ATTACHMENT_PATH));

        // Send email
        javaMailSender.send(mimeMessage);
        return null;
    }
}
