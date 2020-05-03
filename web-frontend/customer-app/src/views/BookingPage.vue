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
          ></BookingInformation>
        </b-col>
      </b-row>
      <b-row class="row">
        <b-col v-bind:class="{ 'd-none': hideGuest }">
          <GuestInformation
            :activityType="this.selectedActivityId"
            class="guest-info"
            @submitCustomerDetails="showBillingInfo"
          ></GuestInformation>
        </b-col>
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
                    Pay {{ formatCurrency(price) }}
                  </button>
                </div>
                <h2 id="cardText" v-if="showQuickPay">Or</h2>
                <div class="buttonDiv">
                  <button
                    @click="submitQuickPayment()"
                    type="button"
                    class="btn btn-outline-primary"
                    id="quickPayButton"
                    v-if="showQuickPay"
                    :disabled="paymentSubmit"
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
import { formatCurrency } from "@/util/format.helpers";
//TODO If regular booking (auto booking) they MUST pay by card only (need it to charge in auto payments)
//TODO Get session ID for posting bookings
//TODO fix guest checkout (error to do with email)
//TODO quick pay checkout is taking payment but not posting bookings
//TODO add validation on guest checkout to ensure the email doesn't exist in our customer DB
//TODO body of booking post will vary on cash or card payment (if card most details can be taken from intent/response)

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
      selectedSessionId:null,
      paymentResponse: {
        accountId: null,
        amountPaid: null,
        transactionId: null
      }
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"]),
    ...mapGetters("auth", ["user", "isEmployeeOrManager", "permissions"]),
    ...mapGetters("customers", ["customers"]),
    ...mapGetters("timetable", ["sessions"]),
    showQuickPay: function() {
      if (this.customer) {
        return this.customer.stripeId !== null;
      } else {
        return false;
      }
    }
  },
  methods: {
    ...mapActions("facilities", ["getActivities"]),
    ...mapActions("customers", ["getAllCustomers"]),
    ...mapActions("timetable", ["getAllSessions"]),
    formatCurrency: formatCurrency,

    //assumes that duplicate session at the same facility, with the same name, at the same time do not exist
    getSessionSelected() {
      let activity = null;
      console.log(this.sessions);
      for (const session of this.sessions) {
        if (this.selectedFacility == session.resource.id) {
          if (this.selectedActivityName === session.name) {
            let date = session.startTime.substring(0, 10);
            let time = session.startTime.split("T")[1].substring(0, 5);
            if (this.date.toString() === date.toString()) {
              if (this.selectedTime === time) {
                activity = session;
              }
            }
          }
        }
      }
      return activity;
    },

    getActivitySelected() {
      let activity = null;
      for (const session of this.sessions) {
        if (this.selectedFacility == session.resource.id) {
          if (this.selectedActivityName === session.name) {
            let date = session.startTime.substring(0, 10);
            let time = session.startTime.split("T")[1].substring(0, 5);
            if (this.date.toString() === date.toString()) {
              if (this.selectedTime === time) {
                activity = session;
              }
            }
          }
        }
      }
      console.log(activity);
    },
    async getCustomer() {
      this.customer = this.customers.find(
        x => x.emailAddress === this.user.email
      );
    },
    showTempPage() {
      this.hideBooking = true;
      this.hideGuest = true;
      this.hideCard = true;
      this.hideCash = true;
      this.hideSuccess = false;
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
          activityTypeId: this.selectedActivityId,
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
      if (this.userType === "guest" || !this.customer) {
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
            activityTypeId: this.selectedActivityId,
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
      if (this.userType === "account" && this.customer) {
        // eslint-disable-next-line no-undef
        console.log(this.customer);
        console.log(this.customerId);
        paymentIntent = await this.$http.post(
          `/payments/intent/card/` + this.customer.id,
          {
            payment_method: {
              card: this.card,
              billing_details: {
                name: this.firstName
              }
            },
            activityTypeId: this.selectedActivityId,
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
          console.log(paymentIntent);
        } else {
          this.hideCardError = false;
        }
      }
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
          await this.createBooking();
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
        await this.createBooking();
        this.showTempPage();
      } else {
        //invalid amount of cash given
      }
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
        await this.$http.post(`/bookings/` + this.selectedSessionId, body); //needs to post session id
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
      this.selectedSessionId = value.selectedSessionId;
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
        this.hideQuickPay = !this.isEmployeeOrManager;
        //TODO autofill with customer information (Props to guest info)
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
    }
  },
  async created() {
    if (!this.isEmpty(this.user)) {
      await this.getAllCustomers();
      this.getCustomer();
    }
  },
  async mounted() {
    await this.getActivities();
    await this.getAllSessions;
    await this.getActivitySelected();
    this.configureStripe();
  }
};
</script>
