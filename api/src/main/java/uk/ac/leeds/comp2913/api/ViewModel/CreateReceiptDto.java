package uk.ac.leeds.comp2913.api.ViewModel;

import java.util.List;

import uk.ac.leeds.comp2913.api.Domain.Model.Sale;

public class CreateReceiptDto {
    public CreateReceiptDto() {
    }

    //    @JsonCreator
    public CreateReceiptDto(String transactionId, List<Sale> sales) {
        this.transactionId = transactionId;
        this.sales = sales;
    }

    private String transactionId;
    private List<Sale> sales;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }
}
