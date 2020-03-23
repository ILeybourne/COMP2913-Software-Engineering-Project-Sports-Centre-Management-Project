package uk.ac.leeds.comp2913.api.Domain.Service;

import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Model.Sale;

import java.util.Collection;

public interface ReceiptService {

  /**
   * TODO: generate PDF, email PDF, upload PDF to S3
   *
   * @param sales list of sales to create a new receipt
   * @return receipt generated from sales
   */
  public Receipt invoice(String transactionId, Collection<Sale> sales);
}
