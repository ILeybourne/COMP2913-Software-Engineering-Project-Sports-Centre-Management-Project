package uk.ac.leeds.comp2913.api.Domain.Service;

import com.sun.istack.Nullable;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EmailProvider {
    private String username, password;

    public EmailProvider(String u, String p) {
        username = u;
        password = p;
    }

    public Boolean SendEmail(String[] recipients, String subject, String body, @Nullable String attachmentPath) {
        // Set properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Iterate through recipient
        for (String recipient : recipients) {
            Message message = new MimeMessage(session);
            try {
                // Set from address
                message.setFrom(new InternetAddress(username));

                // Set recipient and subject
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject(subject);

                // Create multipart message and attach body
                Multipart multipart = new MimeMultipart();
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(body, "text/html");
                multipart.addBodyPart(mimeBodyPart);

                // Add attachment if path is not null
                if (attachmentPath != null) {
                    MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                    attachmentBodyPart.attachFile(new File(attachmentPath));
                    multipart.addBodyPart(attachmentBodyPart);
                }

                // Set message content and send
                message.setContent(multipart);
                Transport.send(message);
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
