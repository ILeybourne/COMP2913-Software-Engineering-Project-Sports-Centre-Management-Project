<template>
  <div class="page-container">
    <div class="padding-div"></div>
    <div class="heading-div">
      <h1>Bookings</h1>
    </div>
    <b-container class="form-container">
      <b-row class="row">
        <b-col col lg="maxColSize " v-bind:class="{ 'd-none': hideBooking }">
          <BookingInformation
            class="booking-info"
            @getUserType="showGuestInfo"
          ></BookingInformation> </b-col
        >
      </b-row>
      <b-row class="row">
        <b-col v-bind:class="{ 'd-none': hideGuest }">
      <GuestInformation
            :activityType="this.selectedActivityId"
            class="guest-info"
            @submitCustomerDetails="showBillingInfo"
          ></GuestInformation> </b-col
        >
      </b-row>
      <b-row class="row">

      <b-col v-bind:class="{ 'd-none': hideCard }">

          <div>
            <div v-bind:class="{ 'd-none': hideQuickPay }">
              <!--               v-if="customer.stripeId != null"-->
            </div>
            <form id="payment-form">
              <div id="cardDiv">
                <div id="card-element"></div>
                <div id="cardError" v-bind:class="{ 'd-none': hideCardError }">
                  An error has occurred please try again.
                </div>
                <div class="buttonDiv">
                  <button
                    type="button"
                    class="btn btn-outline-primary"
                    id="paymentButton"
                    @click="submitPayment()"
                    :disabled="paymentSubmit"
                  >
                    Pay £{{ price }}
                  </button>
                </div>
                <h2 id="cardText">Or</h2>
                <div class="buttonDiv">
                  <button
                    @click="submitQuickPayment()"
                    :disabled="paymentSubmit"
                    type="button"
                    class="btn btn-outline-primary"
                    id="quickPayButton"
                    v-if="showQuickPay"
                  >
                    Quick Pay
                  </button>
                </div>
              </div>
            </form>
          </div>
        </b-col>
        <b-col v-bind:class="{ 'd-none': hideCash }">
          <div id="cashDiv">
            <CashInformation
              :activityPrice="this.price"
              @submitCashPayment="handleCashPayment"
            ></CashInformation>
          </div>
        </b-col>
      </b-row>
      <div v-bind:class="{ 'd-none': hideSuccess }" id="successDiv">
        <h1>Success</h1>
        <p>
          Booking confirmation Activity: {{ selectedActivityName }}<br />
          Booked on: {{ date }}<br />
          Time: {{ selectedTime }}<br />
          Amount paid: £{{ price }}<br />
          Customer: {{ firstName }} {{ surname }}<br />
          Receipt send to: {{ email }}<br />
        </p>
        <p>Please wait to be redirected</p>
      </div>
    </b-container>
  </div>
</template>

<style scoped>
.padding-div {
  padding: 15px;
}

.guest-info {
  display: inline;
}
.booking-info {
  display: inline;
}

.heading-div {
  margin: auto;
  width: 50%;
}

.form-container {
  width: 100%;
  padding-top: 5%;
  padding-bottom: 5%;
  display: inline;
}

h1 {
  text-align: center;
}

.buttonDiv {
  display: flex;
  align-items: center;
  justify-content: center;
}
#paymentButton {
  margin-top: 3%;
  width: 50%;
}

#quickPayButton {
  margin-top: 3%;
  width: 50%;
}

#cardText {
  text-align: center;
  padding-top: 3%;
  padding-bottom: 3%;
}

#cardDiv {
  padding: 5%;
  border: 3px solid #3183e5;
  border-radius: 10px;
}
</style>

<script>
import BookingInformation from "@/components/BookingInformation.vue";
import GuestInformation from "@/components/GuestInformation.vue";
import CashInformation from "@/components/CashInformation.vue";
import { mapGetters, mapActions } from "vuex";
//TODO If regular booking they MUST pay by card only

// @ is an alias to /src
export default {
  name: "BookingPage",
  components: {
    GuestInformation,
    BookingInformation,
    CashInformation
  },
  props: {
    activityPrice: Number,
    activityType: Number
  },
  data() {
    return {
      selectedFacility: null,
      selectedActivity: null,
      selectedActivityId: null,
      date: new Date(),
      selectedTime: null,
      price: null,
      userType: null,
      firstName: "",
      surname: "",
      email: "",
      phone: "",
      health: "",
      name: "",
      emailBilling: "",
      houseNumber: "",
      streetName: "",
      city: "",
      postCode: "",
      nameCard: "",
      cardType: "",
      cardNumber: "",
      expiryDate: "",
      secureCode: "",
      hideBooking: false,
      hideGuest: true,
      hideCard: true,
      hideCash: true,
      hideSuccess: true,
      hidePayment: true,
      bookings: [],
      loading: false,
      amount: 1000,
      // publishableKey: "pk_test_crv9Zb7tvQtSJ82FhQwrnb8k00v3eIOvj8",
      token: null,
      charge: null,
      stripe: "",
      elements: "",
      card: "",
      complete: false,
      stripeOptions: {},
      selectedActivityName: "",
      hideQuickPay: true,
      paymentSubmit: false,
      hideCardError: true,
      authEmail: String,
      customer: null,
      paymentResponse: {
        accountId: null,
        amountPaid: null,
        transactionId: null
      }
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"]),
    ...mapGetters("auth", ["user", "isEmployeeOrManager"]),
    ...mapGetters("customers", ["customers"]),
    showQuickPay: function() {
      if (this.customer !== null) {
        return this.customer.stripeId !== null;
      } else {
        return false;
      }
    }
  },
  methods: {
    ...mapActions("facilities", ["getActivities"]),
    ...mapActions("customers", ["getAllCustomers"]),

    async getCustomer() {
      this.customer = this.customers.find(
        x => x.emailAddress === this.user.email
      );
    },
    showTempPage() {
      this.hideBooking = true;
      this.hideGuest = true;
      this.hideCard = true;
      this.hideSuccess = false;
    },

    async checkCustomerStripeId() {
      const hasStripeId = await this.$http.post(
        //TODO Remove static customer id
        `/payments/customer/stripe_id/14`
      );
      return hasStripeId.data;
    },

    bookByCash() {
      this.hideBooking = true;
      this.hideGuest = true;
      this.hideCard = true;
      this.hideSuccess = false;
    },

    configureStripe() {
      //TODO get from env
      // eslint-disable-next-line no-undef
      this.stripe = Stripe("pk_test_crv9Zb7tvQtSJ82FhQwrnb8k00v3eIOvj8");
      this.elements = this.stripe.elements();

      this.card = this.elements.create("card");
      this.card.mount("#card-element");
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
          regularSession: this.regularBooking //If true (a regular session booking) then server will calculate and charge 70% of the passed cost
        }
      );
      if (paymentIntent.status === 200) {
        this.paymentResponse.accountId = paymentIntent.data.accountId;
        this.paymentResponse.amountPaid = paymentIntent.data.amountPaid;
        this.paymentResponse.transactionId = paymentIntent.data.transactionId;
        this.showTempPage();
      }
    },

    //To test stripe use the card: 4242 4242 4242 4242 and any postcode/cvc
    async submitPayment() {
      // Talk to our server to get encrpyted prices
      this.paymentSubmit = true;
      let paymentIntent = null;
      if (this.userType === "guest") {
        // eslint-disable-next-line no-undef
        paymentIntent = await this.$http.post(
          `/payments/guest-intent/`, // + this.selectedActivityId,
          {
            payment_method: {
              card: this.card,
              billing_details: {
                name: this.firstName
              }
            },
            cost: this.price,
            email: this.email,
            regularSession: false //guests cannot book onto reg sessions
          }
        );
        if (paymentIntent != null) {
          this.sendTokenToServer(paymentIntent.data.clientSecret);
          this.paymentResponse.accountId = paymentIntent.data.accountId;
          this.paymentResponse.amountPaid = paymentIntent.data.amountPaid;
          this.paymentResponse.transactionId = paymentIntent.data.transactionId;
        }
      }
      if (this.userType === "account") {
        // eslint-disable-next-line no-undef
        paymentIntent = await this.$http.post(
          `/payments/intent/card/` + this.customer.id, //this.customerId,
          {
            payment_method: {
              card: this.card,
              billing_details: {
                name: this.firstName
              }
            },
            cost: this.price,
            email: this.email,
            regularSession: this.regularBooking //If true (a regular session booking) then server will calculate and charge 70% of the passed cost
          }
        );
        if (paymentIntent.status === 200) {
          this.sendTokenToServer(paymentIntent.data.clientSecret);
          this.showTempPage();
          this.paymentResponse.accountId = paymentIntent.data.accountId;
          this.paymentResponse.amountPaid = paymentIntent.data.amountPaid;
          this.paymentResponse.transactionId = paymentIntent.data.transactionId;
          console.log(paymentIntent)
        } else {
          this.hideCardError = false;
        }
      }
      console.log(this.selectedActivityId);
    },

    async sendTokenToServer(client_secret) {
      let successBol = false;
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
          await this.postAllFormData();
          //TODO Set payment amount
          this.showTempPage();
          //TODO Redirect
          successBol = true;
        } else {
          this.hideCardError = false;
        }
      }
      if (successBol == true) {
        //Change url
        // await this.$router.push({
        //   name: "BookingPage",
        //   query: { status: "success" }
        // });
      }
    },

    async handleCashPayment(value) {
      if (value.change >= 0) {
        await this.postAllFormData();
        this.showTempPage();
      } else {
        //invalid amount of cash given
      }
    },

    async postAllFormData() {
      try {
        /* TODO: Validate and check server response */
       // let bookedActivity = this.activities.find(
       //   activity => activity.id == this.selectedActivityId
       // );
        const body = {
          //TODO PASS USER
        //  account: null,
        //  activity: bookedActivity,
        //  createdAt: Date.now(),
        //  receipt: null,
        //  updatedAt: null,
        //  type: "booking",
        //  amount: this.price,
        //  regularBooking: false,
        //  participants: 1,
        //  accountId: 1
          accountId: this.paymentResponse.accountId, //if card payment then get from payment response body
          participants: 1,
          regularBooking: false,
          transactionId: this.paymentResponse.transactionId, //if cash then send cash // can we get the card transaction from stripe in the response body?
          amount: this.paymentResponse.amountPaid //get from payment response body
        };
        await this.$http.post(`/bookings/` + 18561, body); //needs to post session id
      } catch (e) {
        console.log(e);
      }
    },

    showBillingInfo(value) {
      this.firstName = value.firstName;
      this.surname = value.surname;
      this.email = value.email;
      this.phone = value.phone;
      this.health = value.health;
      if (value.cardCashSelection == "card") {
        this.hideCard = false;
        this.hideCash = true;
      } else {
        this.hideCash = false;
        this.hideCard = true;
      }
    },

    async showGuestInfo(value) {
      this.selectedFacility = value.selectedFacilityId;
      this.selectedActivity = value.selectedActivityName;
      this.selectedActivityId = value.selectedActivityId;
      this.selectedActivityName = value.selectedActivityName;
      this.date = value.selectedDate;
      this.selectedTime = value.selectedTime;
      this.price = value.price;
      if (value.userType == "guest") {
        // this.setCashPrice()
        //Shows guest component
        this.hideGuest = false;
        this.userType = "guest";
      }
      if (value.userType == "account") {
        this.hideGuest = false;
        this.userType = "account";
        this.hideQuickPay = !(await this.checkCustomerStripeId());
        //TODO autofill with customer information
        // this.firstName =
        // this.surname =
        // this.email = this.user.email
        // this.phone =
      }
    },

    isEmpty(obj) {
      if (Object.keys(obj).length === 0) {
        return true;
      } else {
        if (Object.keys(obj)[0] == "success") {
          return false;
        } else {
          return false;
        }
      }
    },

    getRoles() {
      let roles = this.$http
        .get(
          "https://prod-comp2931/api/v2/users/" + this.customer.id + "/roles"
        )
        .header("authorization", "Bearer MGMT_API_ACCESS_TOKEN");

      console.log(roles);
    }
  },
  async created() {
    if (!this.isEmpty(this.user)) {
      await this.getAllCustomers();
      this.getCustomer();
    }
  },
  async mounted() {
    // this.getRoles()
    console.log(this.$auth)
    console.log(this.isEmployeeOrManager)
    await this.getActivities();
    this.configureStripe();
  }
};
</script>
