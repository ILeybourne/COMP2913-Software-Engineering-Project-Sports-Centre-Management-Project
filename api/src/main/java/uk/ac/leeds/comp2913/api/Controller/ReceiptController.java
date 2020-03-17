package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ReceiptRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

import javax.validation.Valid;

public class ReceiptController {

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
        return receiptRepository.save(receipt);
    }

    @PutMapping("/{receipt_id}")
    public Receipt updateResource(@PathVariable Long receipt_id, @Valid @RequestBody Receipt receiptRequest) {
        return receiptRepository.findById(receipt_id)
                .map(receipt -> {
                    receipt.setProductDescription(receiptRequest.getProductDescription());
                    receipt.setCost(receiptRequest.getCost());
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
