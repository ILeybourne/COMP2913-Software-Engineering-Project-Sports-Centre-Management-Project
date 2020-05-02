package uk.ac.leeds.comp2913.api.ViewModel;

import java.math.BigDecimal;

//Not sure whether this is required. See if it can be incorporated into existing dto?
public class PayResponseBodyDTO {
    private String clientSecret;
    private Boolean requiresAction;
    private String error;
    private BigDecimal amountPaid;
    private Long accountId;
    private String transactionId;

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public PayResponseBodyDTO() {
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setRequiresAction(Boolean requiresAction) {
        this.requiresAction = requiresAction;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Boolean getRequiresAction() {
        return requiresAction;
    }

    public String getError() {
        return error;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
