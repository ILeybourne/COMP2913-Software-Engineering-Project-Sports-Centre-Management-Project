package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import uk.ac.leeds.comp2913.api.Domain.Model.Customer;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Model.Sale;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface ReceiptService {

  /**
   *
   * @param sales list of sales to create a new receipt
   * @return receipt generated from sales
   */
  @Transactional
  public Receipt invoice(String transactionId, List<Sale> sales, Customer customer) throws IOException, MessagingException;

  public void delete(Long receiptId);

  Receipt findById(Long receipt_id);

  Page<Receipt> findAll(Pageable pageable);

  void sendReceiptToCustomer(Receipt receipt) throws MessagingException;

  File downloadPdf(Long receipt_id) throws IOException;
}
