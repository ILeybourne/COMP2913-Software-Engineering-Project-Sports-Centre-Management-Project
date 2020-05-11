package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.mail.MessagingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ReceiptRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Service.CustomerService;
import uk.ac.leeds.comp2913.api.Domain.Service.ReceiptService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

/**
 * TODO: @RESEARCH how we can move the regular booking and memberships into scheduled service
 */
@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    private ReceiptService receiptService;


    public ReceiptController(ReceiptService receiptService) {

        this.receiptService = receiptService;

    }

    /**
     * Get all receipts generated in this application
     *
     * @param pageable
     * @return
     */
    @GetMapping("")
    @Operation(summary = "Get all receipts",
            description = "Get a list of all receipts")
    @PreAuthorize("hasAuthority('SCOPE_read:receipts')")
    public Page<Receipt> getReceipts(Pageable pageable) {
        return receiptService.findAll(pageable);
    }

    @GetMapping("/{receipt_id}")
    @Operation(summary = "Get a receipt",
            description = "Returns a specific receipt")
    @PreAuthorize("hasAuthority('SCOPE_read:receipts')")
    public Receipt getReceipt( @Parameter(description = "The ID of the receipt", required = true)@PathVariable Long receipt_id) {
        return receiptService.findById(receipt_id);
    }

    @GetMapping("/{receipt_id}/pdf")
    @Operation(summary = "Get a receipt in PDF format", description = "Returns a specific receipt")
    public  ResponseEntity<byte[]> getReceiptPdf( @Parameter(description = "The ID of the receipt", required = true)@PathVariable Long receipt_id){
        try {
            File image = receiptService.downloadPdf(receipt_id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(Files.probeContentType(image.toPath())));
            return new ResponseEntity<>(Files.readAllBytes(image.toPath()), headers, HttpStatus.OK);
        } catch (IOException e) {
            // Translate IO exception to a web server error
            throw new ResourceNotFoundException("Image not found");
        }
    }

    @GetMapping("/{receipt_id}/email")
    @Operation(summary = "Send receipt via email", description = "Sends a copy of the receipt via email to the customer of the receipt")
    public  ResponseEntity<?> emailReceipt( @Parameter(description = "The ID of the receipt", required = true)@PathVariable Long receipt_id){
        Receipt receipt = receiptService.findById(receipt_id);
        try {
            // need to ensure file is on file system before we can send it
            receiptService.downloadPdf(receipt_id);
            receiptService.sendReceiptToCustomer(receipt);
        } catch (MessagingException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * @param receipt_id
     * @return
     */
    @DeleteMapping("/{receipt_id}")
    @Operation(summary = "Delete a receipt",
            description = "Delete a receipt")
    @PreAuthorize("hasAuthority('SCOPE_delete:receipts')")
    public ResponseEntity<?> deleteReceipt( @Parameter(description = "The ID of the receipt", required = true)@PathVariable Long receipt_id) {
        receiptService.delete(receipt_id);
        return ResponseEntity.ok()
                .build();
    }

}
