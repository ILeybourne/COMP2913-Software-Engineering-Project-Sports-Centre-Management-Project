<template>
  <div class="payment-container">
    <div class="heading-div">
      <h1><span>Checkout</span></h1>
    </div>
    <v-row class="info-container">
      <CheckoutItem class="checkout-container" id="details"></CheckoutItem>
      <BillingInformation
        class="checkout-container"
        id="billing"
        v-if="!billingSuccess"
        @submitBillingDetails="billingSuccessStatus"
      ></BillingInformation>
      <v-container
        class="checkout-container"
        id="billing-details"
        v-if="billingSuccess"
        ><v-row
          ><v-col><h3>Billing Info</h3><P>{{this.formData.email}}, {{this.formData.selectedOption}}</P></v-col></v-row
        >
        <v-row
          ><v-col>Name: </v-col> <v-col>{{ name }}</v-col></v-row
        >
        <v-row
          ><v-col>Email </v-col> <v-col>{{ email }} </v-col></v-row
        >

        <v-row
          ><v-col>House Number: </v-col> <v-col>{{ houseNumber }}</v-col></v-row
        >
        <v-row
          ><v-col>Street Name: </v-col> <v-col>{{ streetName }}</v-col></v-row
        >
        <v-row
          ><v-col>City </v-col> <v-col>{{ city }}</v-col></v-row
        >
        <v-row
          ><v-col>Post code </v-col> <v-col>{{ postCode }}</v-col></v-row
        >
      </v-container>
      <b-col v-bind:class="{ 'd-none': !billingSuccess }">
        <div>
          <form id="payment-form">
            <div id="cardDiv">
              <div id="card-element"></div>
              <div id="buttonDiv">
                <button
                  type="button"
                  class="btn btn-outline-primary"
                  id="paymentButton"
                  @click="submitPayment($event)"
                >
                  Pay {{ formatCurrency(membershipSaleDetails.cost) }}
                </button>
              </div>
            </div>
          </form>
        </div>
      </b-col>
    </v-row>
  </div>
</template>

<style scoped>
#cardDiv {
  padding: 5%;
  border: 3px solid #3183e5;
  border-radius: 10px;
}
.checkout-container {
  display: flex;
  flex-direction: column;
  padding: 59px 0px 59px 0px;
  min-height: 50%;
  height: auto;
  width: auto;
  margin: 20px;
  background: #f6f9fa;
  color: #242424;
  justify-content: center;
  flex-basis: auto; /* default value */
  flex-grow: 1;
}

.checkout-container h3 {
  color: #242424;
}
#details {
  max-width: 20%;
}

#billing-details {
  margin: 20px;
  text-align: center;
  width: 20%;
  min-height: 400px;
  background: #f6f9fa;
  color: #242424;
  display: flex;
  flex-direction: column;
  justify-content: center;
  max-width: 20%;
  flex-basis: auto; /* default value */
  flex-grow: 1;
}
#billing {
  min-width: 30%;
}
.info-container {
  display: flex;
  justify-content: center;
  flex-direction: row;
  width: 100%;
  height: max-content;
}
.padding-div {
  padding: 15px;
}
.heading-div {
  padding: 10px;
  margin: auto;
}
@media screen and (max-width: 600px) {
  .heading-div h1 {
    font-size: 10vw;
  }
}
.heading-div h1 {
  width: 60%;
  margin: auto;
}
.heading-div p {
  width: 100%;
  padding: 10px;
}
.heading-div span {
  background: #fcff18;
}

#buttonDiv {
  padding-top: 5%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>

<script>
import CheckoutItem from "@/components/CheckoutItem.vue";
import BillingInformation from "@/components/BillingInformation.vue";
import { formatCurrency } from "@/util/format.helpers";
//TODO plug in payment and post membership on success

// @ is an alias to /src
export default {
  name: "Checkout",
  components: {
    CheckoutItem,
    BillingInformation
  },
  data() {
    return {
      billingSuccess: false,
      name: "",
      email: "",
      houseNumber: "",
      streetName: "",
      city: "",
      postCode: "",
      membershipSaleDetails: {
        id: null,
        name: null,
        startDate: null,
        endDate: null,
        cost: null,
        repeatingPayment: null
      },
      paymentResponse: {
        accountId: null,
        amountPaid: null,
        transactionId: null
      },
      formData: {
        email: null,
        dateOfBirth: null,
        repeatingPayment: null,
        selectedOption: null
      },
      postMembershipResponse: []
    };
  },
  methods: {
    formatCurrency: formatCurrency,
    billingSuccessStatus(value) {
      this.billingSuccess = true;
      console.log(value);
      this.name = value.name;
      this.email = value.email;
      this.houseNumber = value.houseNumber;
      this.streetName = value.streetName;
      this.city = value.city;
      this.postCode = value.postCode;
    },
    configureStripe() {
      //TODO get from env
      // eslint-disable-next-line no-undef
      this.stripe = Stripe("pk_test_crv9Zb7tvQtSJ82FhQwrnb8k00v3eIOvj8");
      this.elements = this.stripe.elements();
      this.card = this.elements.create("card");
      this.card.mount("#card-element");
      console.log("this");
      console.log(this);
    },
    setMembershipDetails() {
      this.membershipSaleDetails.name = this.$route.params.membershipDetails.name;
      this.membershipSaleDetails.startDate = this.$route.params.membershipDetails.startDate;
      this.membershipSaleDetails.endDate = this.$route.params.membershipDetails.endDate;
      this.membershipSaleDetails.cost = this.$route.params.membershipDetails.amount;
      this.membershipSaleDetails.repeatingPayment = this.$route.params.membershipDetails.repeatingPayment;
    },
    setFormData() {
      this.formData.dateOfBirth = this.$route.params.formBody.dateOfBirth;
      this.formData.email = this.$route.params.formBody.email;
      this.formData.repeatingPayment = this.$route.params.formBody.repeatingPayment;
      this.formData.selectedOption = this.$route.params.selectedOption;
    },
    submitPayment() {
      this.checkCustomerStripeId();
    },
    async checkCustomerStripeId() {
      const hasStripeId = await this.$http.post(
        //TODO Remove static customer id
        `/payments/customer/stripe_id/14`
      );
      return hasStripeId.data;
    }
  },
  async submitQuickPayment() {
    this.paymentSubmit = true;
    let paymentIntent = null;
    // eslint-disable-next-line no-undef
    paymentIntent = await this.$http.post(
      `/payments/intent/saved/` + this.customer.id, //this.customerId,
      {
        payment_method: {
          card: this.card,
          billing_details: {
            name: this.firstName
          }
        },
        cost: this.price,
        email: this.email,
        regularSession: false
      }
    );
    if (paymentIntent.status === 200) {
      this.paymentResponse.accountId = paymentIntent.data.accountId;
      this.paymentResponse.amountPaid = paymentIntent.data.amountPaid;
      this.paymentResponse.transactionId = paymentIntent.data.transactionId;
      await this.addMember();
    }
  },
  async addMember() {
    const body = {
      accountId: this.paymentResponse.accountId,
      transactionId: this.paymentResponse.transactionId,
      repeatingPayment: this.formData.repeatingPayment,
      email: this.formData.email
    };
    await this.$http
      .post("/membership/" + this.formData.selectedOption, body)
      .then(response => {
        //console.log(response);
        this.postMembershipResponse = response.data;
      })
      .catch(function() {
        //console.log(error);
      });
  },

  async mounted() {
    if (this.$route.params.membershipDetails !== null) {
      this.setMembershipDetails();
    }
    if (this.$route.params.formBody !== null) {
      this.setFormData();
    }
    this.configureStripe();
  }
};
</script>
