<template>
  <div class="page-container">
    <div class="padding-div">
      <button type="button" class="btn btn-outline-secondary">
        Back
      </button>
    </div>
    <div class="heading-div">
      <h1>Bookings</h1>
    </div>
    <div class="form-container">
      <div class="row">
        <div
          v-bind:class="{
            'col-lg-12': bookingCol12,
            'col-lg-6': bookingCol6,
            'col-lg-4': bookingCol4,
            'col-lg-3': bookingCol3
          }"
        >
          <BookingInformation
            class="booking-info"
            @getUserType="showGuestInfo"
          ></BookingInformation>
        </div>
        <div
          v-bind:class="{
            'col-lg-6': guestCol6,
            'col-lg-4': guestCol4,
            'col-lg-3': guestCol3
          }"
        >
          <GuestInformation
            class="guest-info"
            @submitCustomerDetails="showBillingInfo"
            v-show="showGuestInfoComponent"
          ></GuestInformation>
        </div>

        <div
          v-bind:class="{ 'col-lg-4': billingCol4, 'col-lg-3': billingCol3 }"
        >
          <BillingInformation
            class="billing-info"
            @submitBillingDetails="showPaymentInfo"
            v-show="showBillingInfoComponent"
          ></BillingInformation>
        </div>

        <div class="col-lg-3">
          <PaymentInformation
            class="payment-info"
            v-show="showPaymentInfoComponent"
            @setPaymentInfoToParent="fillPaymentInfo"
          ></PaymentInformation>
        </div>
      </div>
    </div>
    <!--    </span>-->
  </div>
</template>

<style scoped>
.padding-div {
  padding: 15px;
}

.guest-info {
  /*display: none;*/
  display: inline;
}
.booking-info {
  display: inline;
  /*display: none;*/
}

.billing-info {
  display: inline;
  /*display: none;*/
}

.payment-info {
  display: inline;
  /*display: none;*/
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
</style>

<script>
import axios from "axios";
import BookingInformation from "@/components/BookingInformation.vue";
import GuestInformation from "@/components/GuestInformation.vue";
import BillingInformation from "@/components/BillingInformation.vue";
import PaymentInformation from "@/components/PaymentInformation.vue";

// @ is an alias to /src
export default {
  name: "BookingPage",
  components: {
    GuestInformation,
    BookingInformation,
    BillingInformation,
    PaymentInformation
  },
  data() {
    return {
      showGuestInfoComponent: false,
      showBillingInfoComponent: false,
      showPaymentInfoComponent: false,
      selectedFacility: null,
      selectedActivity: null,
      selectedActivityId: null,
      date: new Date(),
      selectedTime: null,
      price: 10.0,
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
      bookingCol12: true,
      bookingCol6: false,
      bookingCol4: false,
      bookingCol3: false,
      guestCol6: true,
      guestCol4: false,
      guestCol3: false,
      billingCol4: true,
      billingCol3: false,
      bookings: [],
      activitiesFromServer: []
    };
  },
  methods: {
    async postAllFormData() {
      try {
        /* TODO: Validate and check server response */
        const token = await this.$auth.getTokenSilently();
        let activities = this.activitiesFromServer;
        console.log(activities);
        let bookedActivity = this.activitiesFromServer.find(
          activity => activity.id == this.selectedActivityId
        );

        const body = {
          //TODO PASS USER
          // account: parseInt(this.$auth._uid),
          activity: bookedActivity
        };
        const { data } = await this.$http.post(
          `/bookings/`,

          body,

          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
        );
        console.log("data");
        console.log(data);

        await this.$router.push({
          name: "BookingPage",
          query: { status: "success" }
        });
      } catch (e) {
        console.log(e);
      }
    },
    fillPaymentInfo(value) {
      console.log(value);
      this.nameCard = value.nameCard;
      this.cardType = value.cardType;
      this.cardNumber = value.cardNumber;
      this.expiryDate = value.date;
      this.secureCode = value.secureCode;
      this.postAllFormData();
    },
    showPaymentInfo(value) {
      this.showPaymentInfoComponent = true;
      this.name = value.name;
      this.emailBilling = value.email;
      this.houseNumber = value.houseNumber;
      this.streetName = value.streetName;
      this.city = value.city;
      this.postCode = value.postCode;
      this.bookingCol4 = false;
      this.bookingCol3 = true;
      this.guestCol4 = false;
      this.guestCol3 = true;
      this.billingCol4 = false;
      this.billingCol3 = true;
    },
    showBillingInfo(value) {
      this.showBillingInfoComponent = true;
      this.firstName = value.name;
      this.surname = value.surname;
      this.email = value.email;
      this.phone = value.phone;
      this.health = value.health;
      this.bookingCol6 = false;
      this.bookingCol4 = true;
      this.guestCol6 = false;
      this.guestCol4 = true;
    },
    showGuestInfo(value) {
      if (value.userType == "guest") {
        this.showGuestInfoComponent = true;
        console.log(this.showGuestInfoComponent);
        this.selectedFacility = value.selectedFacility;
        this.selectedActivity = value.selectedActivity;
        this.selectedActivityId = value.selectedActivityId;
        this.date = value.date;
        this.selectedTime = value.selectedTime;
        this.price = value.price;
        this.bookingCol12 = false;
        this.bookingCol6 = true;
      }
      this.getBookings();
    },

    async getBookings() {
      const token = await this.$auth.getTokenSilently();
      const { data } = await axios.get(
        "http://localhost:8000/bookings",

        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      console.log("bbokings");
      console.log(data);
      this.bookings = data;
    },

    async getActivities() {
      const token = await this.$auth.getTokenSilently();
      const { data } = await axios.get(
        "http://localhost:8000/activities",

        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      this.activitiesFromServer = data.content;
    }
  },
  mounted() {
    this.getActivities();
  }
};
</script>
