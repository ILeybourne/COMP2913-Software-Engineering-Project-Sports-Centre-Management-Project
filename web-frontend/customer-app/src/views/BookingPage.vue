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
      <!--    <span>-->
      <BookingInformation
        class="booking-info"
        @getUserType="showGuestInfo"
      ></BookingInformation>
      <!--      <GuestInformation class="guest-info"></GuestInformation>-->
      <GuestInformation
        class="guest-info"
        @submitCustomerDetails="showBillingInfo"
        v-show="showGuestInfoComponent"
      ></GuestInformation>
      <BillingInformation
        class="billing-info"
        @submitBillingDetails="showPaymentInfo"
        v-show="showBillingInfoComponent"
      ></BillingInformation>
      <PaymentInformation
        class="payment-info"
        v-show="showPaymentInfoComponent"
        @setPaymentInfoToParent="fillPaymentInfo"
      ></PaymentInformation>
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
  display: inline-block;
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

  display: inline;
}
</style>

<script>
import BookingInformation from "../components/BookingInformation.vue";
import GuestInformation from "../components/GuestInformation.vue";
import BillingInformation from "../components/BillingInformation.vue";
import PaymentInformation from "../components/PaymentInformation.vue";

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
      secureCode: ""
    };
  },
  methods: {
    async postAllFormData() {
      try {
        /* TODO: Validate and check server response */
        // const token = await this.$auth.getTokenSilently();
        const body = {
          ...this.selectedFacility,
          ...this.selectedActivity,
          ...this.date,
          ...this.selectedTime,
          ...this.price,
          ...this.userType,
          ...this.firstName,
          ...this.surname,
          ...this.email,
          ...this.phone,
          ...this.health,
          ...this.name,
          ...this.emailBilling,
          ...this.houseNumber,
          ...this.streetName,
          ...this.city,
          ...this.postCode,
          ...this.nameCard,
          ...this.cardType,
          ...this.cardNumber,
          ...this.expiryDate,
          ...this.secureCode
        };
        console.log(body);
        // const { data } = await this.$http.post(
        //   `URL`,
        //   body,
        //   {
        //     headers: {
        //       Authorization: `Bearer ${token}`
        //     }
        //   }
        // );
        await this.$router.push({
          name: "BookingPage"
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
    },
    showBillingInfo(value) {
      this.showBillingInfoComponent = true;
      this.firstName = value.name;
      this.surname = value.surname;
      this.email = value.email;
      this.phone = value.phone;
      this.health = value.health;
    },
    showGuestInfo(value) {
      if (value.userType == "guest") {
        this.showGuestInfoComponent = true;
        console.log(this.showGuestInfoComponent);
        this.selectedFacility = value.selectedFacility;
        this.selectedActivity = value.selectedActivity;
        this.date = value.date;
        this.selectedTime = value.selectedTime;
        this.price = value.price;
      }
    }
  }
};
</script>
