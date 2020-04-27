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
        v-if="billingSuccess === false"
        @submitBillingDetails="billingSuccessStatus"
      ></BillingInformation>
      <v-container
        class="checkout-container"
        id="billing-details"
        v-if="billingSuccess === true"
      ><v-row><v-col><h3>Billing Info</h3></v-col></v-row>
        <v-row
        ><v-col>Name: </v-col> <v-col>{{name}}</v-col></v-row
        >
        <v-row
        ><v-col>Email </v-col>
          <v-col>{{email}} </v-col></v-row
        >
        <v-row
        ><v-col>House Number: </v-col>
          <v-col>{{houseNumber}}</v-col></v-row
        >
        <v-row
        ><v-col>Street Name: </v-col> <v-col>{{streetName}}</v-col></v-row
        >
        <v-row
        ><v-col>City </v-col>
          <v-col>{{city}}</v-col></v-row
        >
        <v-row
        ><v-col>Post code </v-col>
          <v-col>{{postCode}}</v-col></v-row
        >
      </v-container>
      <PaymentInformation
        class="checkout-container"
        id="payment"
        v-if="billingSuccess ===true"
      ></PaymentInformation>

    </v-row>
  </div>
</template>

<style scoped>
.checkout-container{
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
#details{
max-width: 20%;
}

#billing-details{
  display: flex;
  flex-direction: column;
  justify-content: center;
  max-width: 20%;
}
#billing{
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
  width: 50%;
  margin: auto;
}
.heading-div p {
  width: 100%;
  padding: 10px;
}
.heading-div span {
  background: #fcff18;
}
.billing-info {

}
</style>

<script>
import CheckoutItem from "@/components/CheckoutItem.vue";
import BillingInformation from "@/components/BillingInformation.vue"
import PaymentInformation from "@/components/PaymentInformation.vue";

// @ is an alias to /src
export default {
  name: "Checkout",
  components: {
    CheckoutItem,
    BillingInformation,
    PaymentInformation
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

    };
  },
  methods: {
    billingSuccessStatus(value){
      this.billingSuccess = true;
      console.log(value)
      this.name  = value.name
      this.email = value.email
      this.houseNumber = value.houseNumber
      this.streetName = value.streetName
      this.city = value.city
      this.postCode = value.postCode
    }
  }
};
</script>
