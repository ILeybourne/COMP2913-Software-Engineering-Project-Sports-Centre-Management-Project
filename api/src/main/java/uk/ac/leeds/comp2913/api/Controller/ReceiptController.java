package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@PreAuthorize("hasAuthority('SCOPE_customer')")
@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptRepository receiptRepository;
    private ReceiptService receiptService;


    public ReceiptController(ReceiptRepository receiptRepository, ReceiptService receiptService, CustomerService customerService) {
        this.receiptRepository = receiptRepository;
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
    public Page<Receipt> getReceipts(Pageable pageable) {
        return receiptService.findAll(pageable);
    }

    /**
     * TODO: add endpoint that return file download of the receipt PDF
     *
     * @param receipt_id
     * @return
     */
    @GetMapping("/{receipt_id}")
    @Operation(summary = "Get a receipt",
            description = "Returns a specific receipt")
    public Receipt getReceipt( @Parameter(description = "The ID of the receipt", required = true)@PathVariable Long receipt_id) {
        return receiptService.findById(receipt_id);
    }


    /**
     * @param receipt_id
     * @return
     */
    @DeleteMapping("/{receipt_id}")
    @Operation(summary = "Delete a receipt",
            description = "Delete a receipt")
    public ResponseEntity<?> deleteReceipt( @Parameter(description = "The ID of the receipt", required = true)@PathVariable Long receipt_id) {
        receiptService.delete(receipt_id);
        return ResponseEntity.ok()
                .build();
    }

}
