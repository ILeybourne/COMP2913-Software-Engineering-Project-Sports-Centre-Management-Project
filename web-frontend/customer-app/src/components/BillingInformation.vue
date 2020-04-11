<template>
  <div class="billing-info">
    <div
      class="billing-container"
      v-bind:style="{ width: this.componentWidth + '%' }"
    >
      <form @submit="callValidation">
        <!--        TODO Fill from customer details-->
        <div class="form-row">
          <label for="name">Billing Name:</label>
          <input
            type="text"
            id="name"
            name="name"
            v-model="name"
            required
            v-bind:class="{
              'is-valid': billingNameValid,
              'is-invalid': !billingNameValid
            }"
            class="form-control"
            @keyup="validateBillingName"
            @change="validateBillingName"
          />
        </div>
        <div class="form-row">
          <label for="email">Email:</label>
          <input
            type="email"
            id="email"
            name="email"
            v-model="email"
            required
            v-bind:class="{
              'is-valid': emailValid,
              'is-invalid': !emailValid
            }"
            class="form-control"
            @keyup="validateEmail"
            @change="validateEmail"
          />
        </div>
        <div class="form-row">
          <label for="houseNumber">House Number:</label>
          <input
            type="text"
            id="houseNumber"
            name="houseNumber"
            v-model="houseNumber"
            v-bind:class="{
              'is-valid': houseNumberValid,
              'is-invalid': !houseNumberValid
            }"
            class="form-control"
            @keyup="validateHouseNumber"
            @change="validateHouseNumber"
            required
          />
        </div>
        <div class="form-row">
          <label for="streetName">Street Name:</label>
          <input
            type="text"
            id="streetName"
            name="streetName"
            v-model="streetName"
            v-bind:class="{
              'is-valid': streetNameValid,
              'is-invalid': !streetNameValid
            }"
            class="form-control"
            @keyup="validateStreetName"
            @change="validateStreetName"
            required
          />
        </div>
        <div class="form-row">
          <label for="city">City:</label>
          <input
            type="text"
            id="city"
            name="city"
            v-model="city"
            required
            v-bind:class="{
              'is-valid': cityValid,
              'is-invalid': !cityValid
            }"
            class="form-control"
            @keyup="validateCity"
            @change="validateCity"
          />
        </div>
        <div class="form-row">
          <label for="postCode">Post Code:</label>
          <input
            type="text"
            id="postCode"
            name="postCode"
            v-model="postCode"
            required
            v-bind:class="{
              'is-valid': postCodeValid,
              'is-invalid': !postCodeValid
            }"
            class="form-control"
            @keyup="validatePostCode"
            @change="validatePostCode"
          />
        </div>
        <div class="button-container">
          <button
            type="submit"
            class="btn btn-outline-secondary"
            name="details"
            @click="submitBillingDetails($event)"
          >
            Submit
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.form-row {
  padding: 5px;
}

/*.booking-info {*/
/*  padding-top: 5%;*/
/*  padding-bottom: 5%;*/
/*}*/

.billing-container {
  margin: auto;
  width: 50%;
  border: 3px solid #3183e5;
  padding: 10px;
  border-radius: 10px;
}

.button-container {
  display: flex;
  justify-content: space-between;
  padding-left: 20%;
  padding-right: 20%;
  padding-top: 10px;
}

input {
  width: 90%;
}

select {
  width: 90%;
}

label {
  width: 10%;
}

button {
  margin: auto;

  width: 50%;
}
</style>

<script>
export default {
  name: "BillingInformation",
  data() {
    return {
      name: "",
      email: "",
      houseNumber: "",
      streetName: "",
      city: "",
      postCode: "",
      componentWidth: 90,
      billingNameValid: null,
      emailValid: null,
      houseNumberValid: null,
      streetNameValid: null,
      cityValid: null,
      postCodeValid: null
    };
  },
  computed: {},
  methods: {
    validateBillingName() {
      this.billingNameValid = this.$data.firstName !== "";
    },
    validateEmail() {
      this.emailValid = this.$data.surname !== "";
    },
    validateHouseNumber() {
      this.houseNumberValid = this.$data.email !== "";
    },
    validateStreetName() {
      this.streetNameValid = this.$data.phone !== "";
    },
    validateCity() {
      this.cityValid = this.$data.email !== "";
    },
    validatePostCode() {
      this.postCodeValid = this.$data.phone !== "";
    },

    callValidation() {
      this.validateBillingName();
      this.validateEmail();
      this.validateHouseNumber();
      this.validateStreetName();
      this.validateCity();
      this.validatePostCode();
    },

    submitBillingDetails(e) {
      e.preventDefault();
      if (
        this.$data.name !== "" &&
        this.$data.email !== "" &&
        this.$data.houseNumber !== "" &&
        this.$data.streetName !== "" &&
        this.$data.city !== "" &&
        this.$data.postCode !== ""
      ) {
        this.$emit("submitBillingDetails", this.$data);
        this.componentWidth = 60;
        this.billingNameValid = true;
        this.emailValid = true;
        this.houseNumberValid = true;
        this.streetNameValid = true;
        this.cityValid = true;
        this.postCodeValid = true;
      } else {
        this.callValidation();
      }
    }
  }
};
</script>
