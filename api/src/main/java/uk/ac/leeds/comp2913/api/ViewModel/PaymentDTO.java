package uk.ac.leeds.comp2913.api.ViewModel;


import java.math.BigDecimal;

public class PaymentDTO {
    PaymentDTO(){}
    private String email;
    private Long customerId;
    private Boolean regularSession;
    private Long membershipTypeId;
    private Long activityTypeId;

    public Long getMembershipTypeId() {
        return membershipTypeId;
    }

    public void setMembershipTypeId(Long membershipTypeId) {
        this.membershipTypeId = membershipTypeId;
    }

    public Long getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(Long activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
