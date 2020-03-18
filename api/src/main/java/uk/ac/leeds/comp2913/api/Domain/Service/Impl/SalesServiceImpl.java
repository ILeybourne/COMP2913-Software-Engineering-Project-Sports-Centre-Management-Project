package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.stereotype.Service;
import uk.ac.leeds.comp2913.api.Domain.Model.Sale;
import uk.ac.leeds.comp2913.api.Domain.Service.SalesService;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
public class SalesServiceImpl implements SalesService {
  /**
   * @param receiptId the receipt you associated with sales
   * @return the sales listed in the receipt
   */
  @Override
  @Transactional
  public Collection<Sale> salesInReceipt(Long receiptId) {
    return null;
  }
}
