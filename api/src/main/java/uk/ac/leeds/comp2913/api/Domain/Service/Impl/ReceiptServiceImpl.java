package uk.ac.leeds.comp2913.api.Domain.Service.Impl;

import org.springframework.stereotype.Service;
import uk.ac.leeds.comp2913.api.Domain.Model.Receipt;
import uk.ac.leeds.comp2913.api.Domain.Model.Sale;
import uk.ac.leeds.comp2913.api.Domain.Service.QrCodeGenerator;
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

  public String createInvoiceTemplate(String transactionId, Collection<Sale> sales, Receipt receipt) {
    final Context ctx = new Context(locale);
    ctx.setVariable("receipt", receipt);
    ctx.setVariable("sales", receipt.getSales());
    ctx.setVariable("transactionId", transactionId);

    QrCodeGenerator qrCodeGenerator = new QrCodeGenerator(200,200);
    String qrCodeFilename = "qrCode" + transactionId;
    qrCodeGenerator.generateQRCodeImage(transactionId, qrCodeFilename);

    ctx.setVariable("qrCode", qrCodeFilename);

    final String htmlContent = this.templateEngine.process("receipt.html", ctx);
    return htmlContent;
  }

  @Override
  @Transactional
  public Receipt invoice(String transactionId, Collection<Sale> sales) {
    createInvoiceTemplate(transactionId, sales, receipt);
  }
}
