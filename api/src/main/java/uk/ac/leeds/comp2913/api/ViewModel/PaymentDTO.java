package uk.ac.leeds.comp2913.api.ViewModel;


import java.math.BigDecimal;

public class PaymentDTO {
    PaymentDTO(){}
    private String email;
    private Long customerId;
    private Boolean regularSession;
    private Long membershipTypeId;
    private Long sessionId;
    private Integer participants;
    private Boolean saveCard;

    public Long getMembershipTypeId() {
        return membershipTypeId;
    }

    public void setMembershipTypeId(Long membershipTypeId) {
        this.membershipTypeId = membershipTypeId;
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

    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Boolean getSaveCard() {
        return saveCard;
    }

    public void setSaveCard(Boolean saveCard) {
        this.saveCard = saveCard;
    }
}
