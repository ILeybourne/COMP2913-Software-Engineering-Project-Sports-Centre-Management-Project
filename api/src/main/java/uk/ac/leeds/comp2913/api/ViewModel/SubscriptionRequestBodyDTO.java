package uk.ac.leeds.comp2913.api.ViewModel;

//Not sure whether this is required. See if it can be incorporated into existing dto?
public class SubscriptionRequestBodyDTO {
        private String planType;
        private String email;
        private Long customerId;
        private String stripeId;

        public String getPlanType() {
            return planType;
        }

        public void setPlanType(String planType) {
            this.planType = planType;
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

        public String getStripeId() {
            return stripeId;
        }

        public void setStripeId(String stripeId) {
            this.stripeId = stripeId;
        }
}
