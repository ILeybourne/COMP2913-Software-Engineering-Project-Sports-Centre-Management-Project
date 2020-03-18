package uk.ac.leeds.comp2913.api.Domain.Service;

import uk.ac.leeds.comp2913.api.Domain.Model.Sale;

import java.util.Collection;

public interface SalesService {
  /**
   * @param receiptId the receipt you associated with sales
   * @return the sales listed in the receipt
   */
  public Collection<Sale> salesInReceipt(Long receiptId);
}
