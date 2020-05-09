<template>
  <div class="page-container">
    <div class="padding-div"></div>
    <div class="heading-div">
      <h1>Activity<span> Booking</span></h1>
    </div>
    <b-container class="form-container">
      <b-row class="row">
        <b-col col lg="maxColSize " v-bind:class="{ 'd-none': hideBooking }">
          <BookingInformation
            :sessionInformatiom="sessionInformation"
            class="checkout-container"
            @getUserType="showGuestInfo"
          ></BookingInformation>
        </b-col>
      </b-row>
      <b-row class="row">
        <b-col v-bind:class="{ 'd-none': hideGuest }">
          <GuestInformation
            :activityType="this.selectedActivityId"
            @submitCustomerDetails="showBillingInfo"
          ></GuestInformation>
        </b-col>
      </b-row>
      <b-row class="row">
        <b-col v-bind:class="{ 'd-none': hideCash }">
          <div id="cashDiv">
            <CashInformation
              :activityPrice="this.price"
              @submitCashPayment="handleCashPayment"
            ></CashInformation>
          </div>
        </b-col>
      </b-row>
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
@media screen and (max-width: 600px) {
  .heading-div h1 {
    font-size: 10vw;
  }
}
.heading-div {
  margin-bottom: 20px;
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
</style>

<script>
import BookingInformation from "@/components/BookingInformation.vue";
import GuestInformation from "@/components/GuestInformation.vue";
import CashInformation from "@/components/CashInformation.vue";
import { mapGetters, mapActions } from "vuex";
import { formatCurrency } from "@/util/format.helpers";
import { isEmpty } from "../util/session.helpers";
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
      selectedSessionId: null,
      selectedFacilityName: null,
      participants: null,
      paymentResponse: {
        accountId: null,
        amountPaid: null,
        transactionId: null
      },
      sessionInformation: Object,
      bookingInformation: Object
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"]),
    ...mapGetters("auth", ["user", "isEmployeeOrManager", "permissions"]),
    ...mapGetters("customers", ["customers"]),
    ...mapGetters("timetable", ["sessions"]),

  },
  methods: {
    ...mapActions("facilities", ["getActivities"]),
    ...mapActions("customers", ["getAllCustomers"]),
    ...mapActions("timetable", ["getAllSessions"]),
    formatCurrency: formatCurrency,

    setSessionInformation() {
      if (!isEmpty(this.$route.params))
        this.sessionInformation = this.$route.params.sessionInformation;
    },

    async getCustomer() {
      this.customer = this.customers.find(
        x => x.emailAddress === this.user.email
      );
    },

    async handleCashPayment(value) {
      if (value.changeVal >= 0) {

        console.log(this.bookingInformation)
        let body = {
          email: this.email,
          regularSession: this.bookingInformation.regularSession,
          cost: this.price,
          activityTypeId: this.bookingInformation.selectedActivityId,
          sessionId: this.bookingInformation.selectedSessionId,
          participants: this.bookingInformation.participants
        }

        let cashPaymentResponse = await this.$http.post(`/payments/cash`, body)
        console.log("cashPaymentRespone")
        console.log(cashPaymentRespone)
        this.cashPaymentResponse = cashPaymentResponse
        if(cashPaymentResponse.transactionId === "Cash"){
          await this.createCashBooking();
        }else{
          //booking failed
        }

        // this.routerPushPaymentSuccess(paymentSuccessData);
      } else {
        //invalid amount of cash given
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

    async createCashBooking() {
      console.log("paymentResponse")
      console.log(this.paymentResponse)
      try {
        const body = {
          accountId: this.paymentResponse.accountId, //if card payment then get from payment response body
          //TODO ADD participant field
          participants: this.participants,
          regularBooking: this.regularBooking, //need to be dynamic (cash payment defaulted to false, same for guest)
          transactionId: "cash", //if cash then send "cash" //
          amount: this.price //get from payment response body if card (may vary if regular session) if cash take from online price
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
        this.onSuccess();
        this.hideCard = false;
        this.hideCash = true;
      } else {
        this.hideCash = false;
        this.hideCard = true;
      }
    },

    async showGuestInfo(value) {
      this.bookingInformation = value.bookingInformation
      this.selectedFacility = value.bookingInformation.selectedFacilityId;
      this.selectedActivity = value.bookingInformation.selectedActivityName;
      this.selectedActivityId = value.bookingInformation.selectedActivityId;
      this.selectedActivityName = value.bookingInformation.selectedActivityName;
      this.selectedSessionId = value.bookingInformation.selectedSessionId;
      this.date = value.bookingInformation.selectedDate;
      this.selectedTime = value.bookingInformation.selectedTime;
      this.price = value.price;
      this.selectedFacilityName = value.bookingInformation.selectedFacilityName;
      this.participants = value.bookingInformation.participants;
      this.regularBooking = value.bookingInformation.regularSession;
      //Shows guest component
      this.hideGuest = false;
      if (value.userType == "guest") {
        this.userType = "guest";
      }
      if (value.userType == "account") {
        this.userType = "account";
        this.hideQuickPay = !this.isEmployeeOrManager;
        this.onSuccess();
        //TODO autofill with customer information (Props to guest info)
        // this.firstName =
        // this.surname =
        // this.email = this.user.email
        // this.phone =
      }
    },

    async onSuccess() {
      let formatDate = this.date.toString().split("-");
      let newFormatDate =
        formatDate[2] + "-" + formatDate[1] + "-" + formatDate[0];
      let bookingDetails = {
        facility: this.selectedFacilityName,
        activityTypeId: this.selectedActivityId,
        activity: this.selectedActivity,
        sessionId: this.selectedSessionId,
        date: newFormatDate,
        time: this.selectedTime,
        price: this.price,
        participants: this.participants,
        regularBooking: this.regularBooking
      };
      await this.$router.push({
        name: "Checkout",
        params: {
          bookingDetails: bookingDetails
        }
      });
    }
  },
  async created() {
    if (!isEmpty(this.user)) {
      await this.getAllCustomers();
      this.getCustomer();
    }
  },
  async mounted() {
    await this.getActivities();
    await this.getAllSessions;
    this.setSessionInformation();
  }
};
</script>
