package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Model.Sale;

import javax.transaction.Transactional;
import java.util.Collection;

public interface ReceiptService {

  /**
   *
   * @param sales list of sales to create a new receipt
   * @return receipt generated from sales
   */
  @Transactional
  public Receipt invoice(String transactionId, Collection<Sale> sales, Customer customer);

  public void delete(Long receiptId);

  Receipt findById(Long receipt_id);

  Page<Receipt> findAll(Pageable pageable);
}
