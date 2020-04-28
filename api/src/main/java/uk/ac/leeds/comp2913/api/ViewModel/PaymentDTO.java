package uk.ac.leeds.comp2913.api.ViewModel;


import java.math.BigDecimal;

//Not sure whether this is required. See if it can be incorporated into existing dto?
public class PaymentDTO {
    PaymentDTO(){}
    private String email;
    private BigDecimal cost;
    private Long customerId;
    private Boolean regularSession;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Boolean getRegularSession() {
        return regularSession;
    }

    public void setRegularSession(Boolean regularSession) {
        this.regularSession = regularSession;
    }
}
