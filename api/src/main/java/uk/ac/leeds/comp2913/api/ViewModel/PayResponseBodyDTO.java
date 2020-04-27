package uk.ac.leeds.comp2913.api.ViewModel;

//Not sure whether this is required. See if it can be incorporated into existing dto?
public class PayResponseBodyDTO {
    private String clientSecret;
    private Boolean requiresAction;
    private String error;

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
}
