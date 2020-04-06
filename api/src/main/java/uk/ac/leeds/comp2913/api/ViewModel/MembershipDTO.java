package uk.ac.leeds.comp2913.api.ViewModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class MembershipDTO {

    @NotBlank(message = "account id is required")
    private Long accountId;
    @NotBlank(message = "Membership Type is required")
    private Long membershipTypeId;
    private boolean repeatingPayment;

    public MembershipDTO(){}

    @JsonCreator
    public MembershipDTO(@JsonProperty("membershipTypeId") Long membershipTypeId,
                         @JsonProperty("accountId") Long accountId,
                         @JsonProperty(value = "repeatingPayment") Boolean repeatingPayment){
        if (repeatingPayment == null) {
            repeatingPayment = false;
        }
        this.accountId = accountId;
        this.membershipTypeId = membershipTypeId;
        this.repeatingPayment = (boolean) repeatingPayment;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getMembershipTypeId() {
        return membershipTypeId;
    }

    public void setMembershipTypeId(Long membershipTypeId) {
        this.membershipTypeId = membershipTypeId;
    }

    public boolean isRepeatingPayment() {
        return repeatingPayment;
    }

    public void setRepeatingPayment(boolean repeatingPayment) {
        this.repeatingPayment = repeatingPayment;
    }
}
