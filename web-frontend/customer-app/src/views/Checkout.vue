<template>
  <div class="payment-container">
    <div v-if="!this.paymentSuccess">
      <div class="heading-div">
        <h1><span>Checkout</span></h1>
      </div>
      <v-row class="info-container">
        <CheckoutItem class="checkout-container" id="details"></CheckoutItem>
        <BillingInformation
          class="checkout-container"
          id="billing"
          v-if="
            !billingSuccess && !(isBooking == false && isMembership == false)
          "
          @submitBillingDetails="billingSuccessStatus"
        ></BillingInformation>
        <v-container
          class="checkout-container"
          id="billing-details"
          v-if="billingSuccess"
          ><v-row
            ><v-col
              ><h3>Billing Info</h3>
              <P
                >{{ this.formData.email }},
                {{ this.formData.selectedOption }}</P
              ></v-col
            ></v-row
          >
          <v-row
            ><v-col>Name: </v-col> <v-col>{{ name }}</v-col></v-row
          >
          <v-row
            ><v-col>Email </v-col> <v-col>{{ email }} </v-col></v-row
          >

          <v-row
            ><v-col>House Number: </v-col>
            <v-col>{{ houseNumber }}</v-col></v-row
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
      </v-row>

      <v-row>
        <v-container
          class="checkout-container"
          id="card-bg"
          v-bind:class="{ 'd-none': !billingSuccess }"
        >
          <div>
            <form id="payment-form">
              <div id="cardDiv">
                <div id="card-element"></div>
                <div class="buttonDiv">
                  <button
                    type="button"
                    class="btn btn-outline-primary"
                    id="paymentButton"
                    @click="submitMembershipPayment()"
                  >
                    Pay
                    {{
                      formatCurrency(
                        membershipSaleDetails.cost || bookingDetails.price
                      )
                    }}
                  </button>
                </div>
                <hr v-if="showQuickPay" />
                <h4 class="centered-text" v-if="showQuickPay">Or</h4>
                <hr v-if="showQuickPay" />
                <div class="buttonDiv">
                  <button
                    @click="submitQuickPayment()"
                    type="button"
                    class="btn btn-outline-primary"
                    id="quickPayButton"
                    v-if="showQuickPay"
                    :disabled="paymentSubmit"
                    title="'Quick Pay' will charge the card previously used"
                  >
                    Pay
                    {{
                      formatCurrency(
                        membershipSaleDetails.cost || bookingDetails.price
                      )
                    }}
                    Using Quick Pay
                  </button>
                </div>
              </div>
            </form>
          </div>
        </v-container>
      </v-row>
    </div>
  </div>
</template>

<style scoped>
#card-bg {
  max-height: 80%;
  padding-left: 5%;
  padding-right: 5%;
  margin-left: 3%;
  max-width: 90%;
  -webkit-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
  -moz-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
  box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
}
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
  /*max-width: 20%;*/
  min-width: 45%;
  -webkit-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
  -moz-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
  box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
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
  min-width: 45%;
  max-width: 45%;
  flex-basis: auto; /* default value */
  flex-grow: 1;
  -webkit-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
  -moz-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
  box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
}
#billing {
  min-width: 30%;
  max-width: 45%;
  -webkit-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
  -moz-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
  box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.2);
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

.buttonDiv {
  padding-top: 5%;
  display: flex;
  align-items: center;
  justify-content: center;
  alignment: center;
}

.centered-text {
  text-align: center;
}
</style>

<script>
import CheckoutItem from "@/components/CheckoutItem.vue";
import BillingInformation from "@/components/BillingInformation.vue";
import { formatCurrency } from "@/util/format.helpers";
import { mapActions, mapGetters } from "vuex";
import { isEmpty } from "../util/session.helpers";
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
      bookingDetails: {
        facility: null,
        activity: null,
        date: null,
        time: null,
        price: null,
        activityTypeId: null,
        sessionId: null
      },
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
      postMembershipResponse: [],
      price: null,
      customer: null,
      isBooking: false,
      isMembership: false,
      paymentSuccess: false,
      paymentSubmit: false
    };
  },
  computed: {
    ...mapGetters("customers", ["customers"]),
    ...mapGetters("auth", ["user"]),
    showQuickPay: function() {
      if (this.customer) {
        return this.customer.stripeId !== null;
      } else {
        return false;
      }
    }
  },
  methods: {
    ...mapActions("customers", ["getAllCustomers"]),
    formatCurrency: formatCurrency,
    billingSuccessStatus(value) {
      this.billingSuccess = true;
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
    },
    setBookingDetails() {
      this.bookingDetails.facility = this.$route.params.bookingDetails.facility;
      this.bookingDetails.activity = this.$route.params.bookingDetails.activity;
      this.bookingDetails.date = this.$route.params.bookingDetails.date;
      this.bookingDetails.time = this.$route.params.bookingDetails.time;
      this.bookingDetails.price = this.$route.params.bookingDetails.price;
      this.bookingDetails.activityTypeId = this.$route.params.bookingDetails.activityTypeId;
      this.bookingDetails.sessionId = this.$route.params.bookingDetails.sessionId;
      this.bookingDetails.participants = this.$route.params.bookingDetails.participants;
      this.price = this.$route.params.bookingDetails.price;
    },
    setMembershipDetails() {
      this.membershipSaleDetails.id = this.$route.params.selectedOption;
      this.membershipSaleDetails.name = this.$route.params.membershipDetails.name;
      this.membershipSaleDetails.startDate = this.$route.params.membershipDetails.startDate;
      this.membershipSaleDetails.endDate = this.$route.params.membershipDetails.endDate;
      this.membershipSaleDetails.cost = this.$route.params.membershipDetails.amount;
      this.membershipSaleDetails.repeatingPayment = this.$route.params.membershipDetails.repeatingPayment;
      this.price = this.$route.params.membershipDetails.amount;
    },
    setFormData() {
      this.formData.dateOfBirth = this.$route.params.formBody.dateOfBirth;
      this.formData.email = this.$route.params.formBody.email;
      this.formData.repeatingPayment = this.$route.params.formBody.repeatingPayment;
      this.formData.selectedOption = this.$route.params.selectedOption;
    },
    async getCustomer() {
      if (this.user) {
        this.customer = this.customers.find(
          x => x.emailAddress === this.$auth.user.email
        );
      }
    },
    async submitMembershipPayment() {
      this.paymentSubmit = true;
      let paymentIntent = null;

      let body = {
        payment_method: {
          card: this.card,
          billing_details: {
            name: this.firstName
          }
        },
        email: this.email,
        regularSession: false,
        cost: this.price
      };

      if (this.isMembership) {
        body.membershipTypeId = this.membershipSaleDetails.id;
      }
      if (this.isBooking) {
        body.activityTypeId = this.bookingDetails.activityTypeId;
      }
      if (!isEmpty(this.user)) {
        let id = null;
        if (this.customer) {
          id = this.customer.id;
        } else {
          id = -1;
        }
        // eslint-disable-next-line no-undef
        paymentIntent = await this.$http.post(
          `/payments/intent/card/` + id,
          body
        );
      } else {
        paymentIntent = await this.$http.post(`/payments/guest-intent/`, body);
      }
      if (paymentIntent.status === 200) {
        this.paymentResponse.accountId = paymentIntent.data.accountId;
        this.paymentResponse.amountPaid = paymentIntent.data.amountPaid;
        this.paymentResponse.transactionId = paymentIntent.data.transactionId;
        this.sendClientSecretToServer(paymentIntent.data.clientSecret);
      }
    },

    async sendClientSecretToServer(client_secret) {
      //uses client secret from  payment intent to make payment
      // eslint-disable-next-line no-undef
      const result = await this.stripe.confirmCardPayment(client_secret, {
        payment_method: {
          card: this.card,
          billing_details: {
            name: this.firstName
          }
        },
        setup_future_usage: "off_session"
      });
      if (result.error) {
        // Show error to your customer (e.g., insufficient funds)
      } else {
        // The payment has been processed!
        if (result.paymentIntent.status === "succeeded") {
          this.paymentSuccess = true;

          if (this.isMembership) {
            await this.addMember();
          }
          if (this.isBooking) {
            await this.createBooking();
          }

          let paymentSuccessData = {
            bookingDetails: this.bookingDetails,
            membershipSaleDetails: this.membershipSaleDetails,
            formData: this.formData,
            paymentResponse: this.paymentResponse,
            postMembershipResponse: this.postMembershipResponse
          };

          this.routerPushPaymentSuccess(paymentSuccessData);
        }
        // this.hideCardError = false;
      }
    },

    async routerPushPaymentSuccess(paymentSuccessData) {
      await this.$router.push({
        name: "PaymentSuccess",
        params: {
          paymentSuccessData: paymentSuccessData
        }
      });
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
          this.postMembershipResponse = response.data;
        })
        .catch(function() {});
    },

    async createBooking() {
      try {
        const body = {
          accountId: this.paymentResponse.accountId, //if card payment then get from payment response body
          //TODO ADD participant field
          participants: 1,
          regularBooking: false, //need to be dynamic (cash payment defaulted to false, same for guest)
          transactionId: this.paymentResponse.transactionId, //if cash then send "cash" //
          amount: this.paymentResponse.amountPaid //get from payment response body if card (may vary if regular session) if cash take from online price
        };
        await this.$http.post(
          `/bookings/` + this.bookingDetails.sessionId,
          body
        ); //needs to post session id
      } catch (e) {
        console.log(e);
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
          activityTypeId: this.bookingDetails.activityTypeId,
          email: this.email,
          regularSession: false //If true (a regular session booking) then server will calculate and charge 70% of the passed cost
        }
      );
      if (paymentIntent.status === 200) {
        this.paymentResponse.accountId = paymentIntent.data.accountId;
        this.paymentResponse.amountPaid = paymentIntent.data.amountPaid;
        this.paymentResponse.transactionId = paymentIntent.data.transactionId;

        await this.createBooking();
        let paymentSuccessData = {
          bookingDetails: this.bookingDetails,
          membershipSaleDetails: this.membershipSaleDetails,
          formData: this.formData,
          paymentResponse: this.paymentResponse,
          postMembershipResponse: this.postMembershipResponse
        };

        await this.routerPushPaymentSuccess(paymentSuccessData);
      }
    }
  },

  async mounted() {
    if (this.$route.params.membershipDetails != null) {
      this.setMembershipDetails();
      this.isMembership = true;
    }
    if (this.$route.params.bookingDetails != null) {
      this.setBookingDetails();
      this.isBooking = true;
    }
    if (this.$route.params.formBody != null) {
      this.setFormData();
    }
    this.configureStripe();
    await this.getAllCustomers();
    this.getCustomer();
  }
};
</script>
