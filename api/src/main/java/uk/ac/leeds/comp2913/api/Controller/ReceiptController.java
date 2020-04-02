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
    public Page<Receipt> getReceipts(Pageable pageable) {
        return receiptRepository.findAll(pageable);
    }

    /**
     * TODO: add endpoint that return file download of the receipt PDF
     *
     * @param receipt_id
     * @return
     */
    @GetMapping("/{receipt_id}")
    public Receipt getReceipt(@PathVariable Long receipt_id) {
        return receiptRepository.findById(receipt_id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receipt not found with id " + receipt_id)
                );
    }


    /**
     * @param receipt_id
     * @return
     */
    @DeleteMapping("/{receipt_id}")
    public ResponseEntity<?> deleteReceipt(@PathVariable Long receipt_id) {
        return receiptRepository.findById(receipt_id)
                .map(receipt -> {
                    receiptRepository.delete(receipt);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Receipt not found with id " + receipt_id));
    }

}
