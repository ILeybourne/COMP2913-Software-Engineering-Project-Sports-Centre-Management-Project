package uk.ac.leeds.comp2913.api.ViewModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MembershipDTO {

    @NotNull(message = "account id is required")
    private Long accountId;
    private boolean repeatingPayment;

    public MembershipDTO(){}

    @JsonCreator
    public MembershipDTO(@JsonProperty("accountId") Long accountId,
                         @JsonProperty(value = "repeatingPayment") Boolean repeatingPayment){
        if (repeatingPayment == null) {
            repeatingPayment = false;
        }
        this.accountId = accountId;
        this.repeatingPayment = (boolean) repeatingPayment;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public boolean isRepeatingPayment() {
        return repeatingPayment;
    }

    public void setRepeatingPayment(boolean repeatingPayment) {
        this.repeatingPayment = repeatingPayment;
    }
}
