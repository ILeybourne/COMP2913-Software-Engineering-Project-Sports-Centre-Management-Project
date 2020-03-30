package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ReceiptRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Service.CustomerService;
import uk.ac.leeds.comp2913.api.Domain.Service.ReceiptService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;
import uk.ac.leeds.comp2913.api.ViewModel.CreateReceiptDto;

import javax.validation.Valid;

@PreAuthorize("hasAuthority('SCOPE_customer')")
@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptRepository receiptRepository;
    private ReceiptService receiptService;
    private CustomerService customerService;


    public ReceiptController(ReceiptRepository receiptRepository, ReceiptService receiptService, CustomerService customerService) {
        this.receiptRepository = receiptRepository;
        this.receiptService = receiptService;
        this.customerService = customerService;
    }

    @GetMapping("")
    public Page<Receipt> getReceipts(Pageable pageable) {
        return receiptRepository.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('SCOPE_create:receipt')")
    @PostMapping("")
    public Receipt createReceipt(@Valid @RequestBody CreateReceiptDto receipt, @AuthenticationPrincipal Jwt oidcUser) {
        System.out.println(oidcUser.getClaims());
        System.out.println(oidcUser.getSubject());
        Customer c = this.customerService.getCustomerByEmailAddress(oidcUser.getSubject());
        return this.receiptService.invoice(receipt.getTransactionId(), receipt.getSales(), c);
    }

    @PutMapping("/{receipt_id}")
    public Receipt updateReceipt(@PathVariable Long receipt_id, @Valid @RequestBody Receipt receiptRequest) {
        return receiptRepository.findById(receipt_id)
                .map(receipt -> {
                    receipt.setProductDescription(receiptRequest.getProductDescription());
                    receipt.setTotal(receiptRequest.getTotal());
                    receipt.setSales(receiptRequest.getSales());
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
