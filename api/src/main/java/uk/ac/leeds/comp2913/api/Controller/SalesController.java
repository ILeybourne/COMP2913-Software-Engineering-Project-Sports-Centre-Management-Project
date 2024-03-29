package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.SalesRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Model.Sale;
import uk.ac.leeds.comp2913.api.Domain.Service.SalesService;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasAuthority('SCOPE_employee')")
@RestController
@RequestMapping("/sales")
public class SalesController {
    private final SalesRepository salesRepository;
    private final SalesService salesService;
    private final PagedResourcesAssembler pagedResourcesAssembler;


    @Autowired
    public SalesController(SalesRepository salesRepository, SalesService salesService, PagedResourcesAssembler pagedResourcesAssembler) {
        this.salesRepository = salesRepository;
        this.salesService = salesService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    /**
     * List of all sales in the system
     *
     * TODO: should only return your sales, or everything if employee
     *
     * TODO: maybe make this a REST query language
     *
     * @param pageable pagination request
     * @return list of all sales in the system
     */
 // @GetMapping("/")
 // @Operation(summary = "Returns a list of sales. INCOMPLETE",
 //         description = "Returns a list of all sales (bookings and memberships)")
 // public Page<Sale> sales(Pageable pageable) {
 //     return salesRepository.findAll(pageable);
 // }

    /**
     * @param sale the sale to mark as paid
     * @return Receipt for transaction
     */
    @PostMapping("/checkout")
    @Operation(summary = "Mark a sale paid INCOMPLETE",
            description = "Marks a sale as paid and add to invoice")
    public Receipt checkout(@Valid @RequestBody List<Sale> sale){
        // TODO: 17/03/2020 mark sale as paid and add to invoice
        return null;
    }

    @DeleteMapping("/refund/{sale_id}")
    @Operation(summary = "Cancel a sale",
            description = "Cancels a sale")
    public ResponseEntity<?> deletePayment(@Parameter(description = "The ID of the sale", required = true)@PathVariable Long sale_id) {

        try {
            salesRepository.deleteById(sale_id);
            return ResponseEntity
                    .noContent()
                    .build();
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Sale not found with that ID " + sale_id);
        }
    }
}
