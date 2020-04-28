<template>
  <div class="payment-container">
    <div class="heading-div">
      <h1>Membership Options</h1>
    </div>
    <div>
      <MembershipOptions
        @submitCustomerDetails="receiveSubscriptionInfo"
      ></MembershipOptions>
      <form id="payment-form">
        <div id="cardDiv">
          <div id="card-element"></div>
          <div id="cardError" v-bind:class="{ 'd-none': hideCardError }">
            An error has occurred please try again.
          </div>
          <div class="buttonDiv">
            <!--           TODO change price to dynamic & disable button-->
            <button
              type="button"
              class="btn btn-outline-primary"
              id="paymentButton"
              @click="submitMembershipPayment()"
            >
              Pay £10
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.heading-div {
  margin: auto;
  width: 50%;
}

#cardDiv {
  width: 50%;
}
</style>

<script>
import MembershipOptions from "@/components/MembershipOptions.vue";

// @ is an alias to /src
export default {
  name: "MembershipPage",
  components: {
    MembershipOptions
  },
  data: function() {
    return {
      hideCardError: true,
      subscriptionType: null
    };
  },

  methods: {
    configureStripe() {
      //TODO get from env
      // eslint-disable-next-line no-undef
      this.stripe = Stripe("pk_test_crv9Zb7tvQtSJ82FhQwrnb8k00v3eIOvj8");
      this.elements = this.stripe.elements();

      this.card = this.elements.create("card");
      this.card.mount("#card-element");
    },
    receiveSubscriptionInfo(value) {
      this.subscriptionType = value.pickedSubscription;
      this.firstName = value.firstName;
      this.surname = value.surname;
      this.email = value.email;
      this.phone = value.phone;
    },
    async getCustomer() {
      this.customer = this.customers.find(
        x => x.emailAddress === this.user.email
      );
    },
    async submitMembershipPayment() {
      let plan = null;
      if (this.subscriptionType === "annually") {
        plan = "prod_HAUwmE1Fe2eW0O";
      } else if (this.subscriptionType === "monthly") {
        plan = "prod_HAUv0ylHXQNAYo";
      } else {
        //TODO Handle error
        return;
      }
      console.log(plan);

      let paymentIntent = await this.$http.post(
        `/subscription/saved/` + "23", // this.customer.id, //this.customerId,
        {
          payment_method: {
            card: this.card,
            billing_details: {
              name: this.firstName
            }
          },
          activityTypeId: this.selectedActivityId,
          regularSession: 1,
          email: this.email,
          customerId: this.customer.id,
          stripeId: this.customer.stripeId
        }
      );
      console.log(paymentIntent);
    }
  },
  async mounted() {
    this.configureStripe();
    this.getCustomer();
  }
};
</script>
