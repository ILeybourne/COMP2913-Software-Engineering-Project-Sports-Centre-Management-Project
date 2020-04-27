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
        ><b-col v-bind:class="{ 'd-none': hideGuest }">
          <GuestInformation
                  :activityType="this.selectedActivityId"
            class="guest-info"
            @submitCustomerDetails="showBillingInfo"
          ></GuestInformation> </b-col
        ><b-col v-bind:class="{ 'd-none': hideCard }">
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
                    Pay £{{ price }}
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
          Amount paid: {{ price }}<br />
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

#buttonDiv {
  display: flex;
  align-items: center;
  justify-content: center;
}
#paymentButton {
  margin-top: 3%;
  width: 50%;
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
      stripeOptions: {}
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"]),
    ...mapGetters("auth", ["user"]),
    selectedActivityName: function() {
      if (this.selectedActivityId != null) {
        return this.activities.find(act => (act.id = this.selectedActivityId))
          .name;
      }
      return null;
    }
  },
  methods: {
    ...mapActions("facilities", ["getActivities"]),

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

    //To test stripe use the card: 4242 4242 4242 4242 and any postcode/cvc
    async submitPayment(e) {
      e.preventDefault();
      // Talk to our server to get encrpyted prices
      // eslint-disable-next-line no-undef
      const paymentIntent = await this.$http.post(
        `/payments/intent/` + this.selectedActivityId,
        {
          payment_method: {
            card: this.card,
            billing_details: {
              name: this.firstName
            }
          }
        }
      );
      this.sendTokenToServer(paymentIntent.data.clientSecret);
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
          },
        },
        setup_future_usage: "off_session"
      });

      if (result.error) {
        // Show error to your customer (e.g., insufficient funds)
        //console.log(result.error.message);
      } else {
        //console.log("first else)");
        // The payment has been processed!
        if (result.paymentIntent.status === "succeeded") {
          await this.postAllFormData();
          //TODO Set payment amount

          this.hideBooking = true;
          this.hideGuest = true;
          this.hideCard = true;
          this.hideSuccess = false;
          //TODO Redirect
          successBol = true;
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

        this.hideBooking = true;
        this.hideGuest = true;
        this.hideCash = true;
        this.hideSuccess = false;
      } else {
        //invalid amount of cash given
      }
    },
    async postAllFormData() {
      try {
        /* TODO: Validate and check server response */
        let bookedActivity = this.activities.find(
          activity => activity.id == this.selectedActivityId
        );
        //console.log(this.$auth._uid);

        const body = {
          //TODO PASS USER
          account: null,
          activity: bookedActivity,
          createdAt: Date.now(),
          receipt: null,
          updatedAt: null,
          type: "booking",
          amount: this.price
        };

        await this.$http.post(`/bookings/`+this.selectedActivityId, body);

        // await this.$router.push({
        //   name: "BookingPage",
        //   query: { status: "success" }
        // });
      } catch (e) {
        //console.log(e);
      }
    },
    showBillingInfo(value) {
      //console.log(value);
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
    showGuestInfo(value) {
      if (value.userType == "guest") {
        // debugger
        this.selectedFacility = value.selectedFacilityId;
        this.selectedActivity = value.selectedActivityName;
        this.selectedActivityId = value.selectedActivityId;
        this.date = value.selectedDate;
        this.selectedTime = value.selectedTime;
        this.price = value.price;
        // this.setCashPrice()
        //Shows guest component
        this.hideGuest = false;
      }
      if (value.userType == "account") {
        this.hideGuest = false;
        //TODO autofill with account information
        // this.firstName =
        // this.surname =
        // this.email =
        // this.phone =
      }
    }
  },
  mounted() {
    //console.log("this.user)");
    //console.log(this.user);
    this.getActivities();
    this.configureStripe();
  }
};
</script>
