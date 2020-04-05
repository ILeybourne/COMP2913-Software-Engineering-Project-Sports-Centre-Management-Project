<template>
  <div class="page-container">
    <div class="padding-div"></div>
    <div class="heading-div">
      <h1>Bookings</h1>
    </div>
    <b-container class="form-container">
      <b-row class="row">
        <b-col col lg="maxColSize ">
          <BookingInformation
            class="booking-info"
            @getUserType="showGuestInfo"
          ></BookingInformation> </b-col
        ><b-col v-bind:class="{ 'd-none': hideGuest }">
          <GuestInformation
            class="guest-info"
            @submitCustomerDetails="showBillingInfo"
          ></GuestInformation> </b-col
        ><b-col>
          <!--        v-bind:class="{ 'd-none': hideBilling }"-->
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
                    Pay ${{ amount / 100 }}
                  </button>
                </div>
              </div>
            </form>
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

.billing-info {
  display: inline;
}

.payment-info {
  display: inline;
}

.heading-div {
  margin: auto;
  width: 50%;
}

.form-container {
  /*TODO Display divs inline*/
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

// @ is an alias to /src
export default {
  name: "BookingPage",
  components: {
    GuestInformation,
    BookingInformation
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
      hideGuest: true,
      hideBilling: true,
      hidePayment: true,
      bookings: [],
      activitiesFromServer: [],
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
  methods: {
    configureStripe() {
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
      const token = await this.$http.post(`/payments/intent`, {
        // TODO
      });
      console.log(token);
      this.sendTokenToServer(token.data.clientSecret);
    },
    async sendTokenToServer(client_secret) {
      // let card = {
      //   number: "4242424242424242",
      //   cvc: "123",
      //   exp_month: "01",
      //   exp_year: "21"
      // };

      // let request = {
      //   name: this.name,
      //   email: this.email,
      //   engravingText: this.engravingText,
      //   address:
      //     this.houseNumber +
      //     " " +
      //     this.streetName +
      //     " " +
      //     this.city +
      //     " " +
      //     this.postCode,
      //   card: card,
      //   token_from_stripe: token.id
      // };

      //uses client secret from  payment intent to make payment
      // eslint-disable-next-line no-undef
      const result = await this.stripe.confirmCardPayment(client_secret, {
        payment_method: {
          // card: this.card,
          card: this.card,
          billing_details: {
            name: "Jenny Rosen"
          }
        }
      });

      if (result.error) {
        // Show error to your customer (e.g., insufficient funds)
        console.log(result.error.message);
      } else {
        // The payment has been processed!
        if (result.paymentIntent.status === "succeeded") {
          console.log("success");
          // await  this.postAllFormData()
        }
      }
    },

    tokenCreated(token) {
      // console.log(token);
      this.card = token.card;
      this.token = token;
      // for additional charge objects go to https://stripe.com/docs/api/charges/object
      this.charge = {
        source: token.card,
        amount: this.amount,
        currency: "gbp",
        description: this.description
      };
      // console.log(this.charge + " sent to server");
      // const stripe = new Stripe("sk_test_m83VCMEjNPihns7LtK9BGD3z00Br6la5RX");
      this.sendTokenToServer(token);
    },
    async postAllFormData() {
      try {
        /* TODO: Validate and check server response */
        this.getActivities();
        const token = await this.$auth.getTokenSilently();
        // let activities = this.activitiesFromServer;
        //console.log(activities);
        // let bookedActivity = this.activitiesFromServer.find(
        //   activity => activity.name == this.selectedActivity
        // );

        const body = {
          //TODO PASS USER
          // account: parseInt(this.$auth._uid),
          activity: 1,
          account: 1
        };
        await this.$http.post(
          `/bookings`,

          body,

          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
        );

        await this.$router.push({
          name: "BookingPage",
          query: { status: "success" }
        });
      } catch (e) {
        //console.log(e);
      }
    },
    // fillPaymentInfo(value) {
    //   this.nameCard = value.nameCard;
    //   this.cardType = value.cardType;
    //   this.cardNumber = value.cardNumber;
    //   this.expiryDate = value.date;
    //   this.secureCode = value.secureCode;
    //   this.postAllFormData();
    // },
    // showPaymentInfo(value) {
    //   this.name = value.name;
    //   this.emailBilling = value.email;
    //   this.houseNumber = value.houseNumber;
    //   this.streetName = value.streetName;
    //   this.city = value.city;
    //   this.postCode = value.postCode;
    //   this.hidePayment = false;
    // },
    showBillingInfo(value) {
      this.firstName = value.name;
      this.surname = value.surname;
      this.email = value.email;
      this.phone = value.phone;
      this.health = value.health;
      this.hideBilling = false;
    },
    showGuestInfo(value) {
      if (value.userType == "guest") {
        this.showGuestInfoComponent = true;
        this.selectedFacility = value.selectedFacility;
        this.selectedActivity = value.selectedActivity;
        this.selectedActivityId = value.selectedActivityId;
        this.date = value.date;
        this.selectedTime = value.selectedTime;
        this.price = value.price;
        this.hideGuest = false;
      }
      this.getBookings();
    },

    async getBookings() {
      const { data } = await this.$http.get("http://localhost:8000/bookings");
      this.bookings = data;
    },

    async getActivities() {
      const { data } = await this.$http.get("http://localhost:8000/activities");
      this.activitiesFromServer = data.content;
    }
  },
  mounted() {
    this.getActivities();
    this.configureStripe();
  }
};
</script>
