package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ReceiptRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;



import javax.validation.Valid;

public class ReceiptController {

    private final JavaMailSender javaMailSender;
    private final ReceiptRepository receiptRepository;

    public ReceiptController(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @GetMapping("")
    public Page<Receipt> getReceipts(Pageable pageable) {
        return receiptRepository.findAll(pageable);
    }

    @PostMapping("/")
    public Receipt createResource(@Valid @RequestBody Receipt receipt) {
        Receipt newReceipt = receiptRepository.save(receipt);
        //todo produce pdf

        // Send email receipt
        // To address
        final String EMAIL_ADDRESS = "tom_oddy@live.co.uk";

        // Subject
        final String EMAIL_SUBJECT = "Your Sports Centre email receipt";

        // Body (can be HTML)
        final String EMAIL_BODY = "Thank you for your purchase from the Sport Centre. Your receipt is attached.\n" +
                "Do not respond to this email.";

        // Name of attached file
        final String EMAIL_ATTACHMENT_NAME = "receipt.pdf"; //todo make dynamic with receipt pdf member

        // Path of file to attach
        final String EMAIL_ATTACHMENT_PATH = "info/bookings/receipt/receipt_123"; //todo make dynamic

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

        return newReceipt;
    }

    @PutMapping("/{receipt_id}")
    public Receipt updateResource(@PathVariable Long receipt_id, @Valid @RequestBody Receipt receiptRequest) {
        return receiptRepository.findById(receipt_id)
                .map(receipt -> {
                    receipt.setProductDescription(receiptRequest.getProductDescription());
                    receipt.setCost(receiptRequest.getCost());
                    //todo add setPayments
                    return receiptRepository.save(receipt);
                }).orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + receipt_id));
    }

    @DeleteMapping("/{receipt_id}")
    public ResponseEntity<?> deleteReceipt(@PathVariable Long receipt_id) {
        return receiptRepository.findById(receipt_id)
                .map(receipt -> {
                    receiptRepository.delete(receipt);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Receipt not found with id " + receipt_id));
    }

}
