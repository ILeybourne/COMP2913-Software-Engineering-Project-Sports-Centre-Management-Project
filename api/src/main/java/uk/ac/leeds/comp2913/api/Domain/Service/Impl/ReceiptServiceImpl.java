package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.stereotype.Service;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Model.Sale;
import uk.ac.leeds.comp2913.api.Domain.Service.ReceiptService;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
public class ReceiptServiceImpl implements ReceiptService {
  /**
   * TODO: generate PDF, email PDF, upload PDF to S3
   *
   * @param sales list of sales to create a new receipt
   * @return receipt generated from sales
   */
  @Override
  @Transactional
  public Receipt invoice(Collection<Sale> sales) {
    return null;
  }
}
