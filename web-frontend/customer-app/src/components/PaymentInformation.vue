<template>
  <div class="payment-info">
    <div
      class="payment-container"
      v-bind:style="{ width: this.componentWidth + '%' }"
    >
      <form @submit="[setPaymentInfoToParent]">
        <!--        TODO Fill from customer details-->
        <div class="form-row">
          <label for="name">Name On Card:</label>
          <input
            type="text"
            id="name"
            name="name"
            v-model="name"
            v-bind:class="{
              'is-valid': nameValid,
              'is-invalid': !nameValid
            }"
            class="form-control"
            @keyup="validateName"
            @change="validateName"
          />
        </div>
        <div class="form-row">
          <label for="cardType">Card Type:</label>
          <input
            type="cardType"
            id="cardType"
            name="cardType"
            v-model="cardType"
            v-bind:class="{
              'is-valid': cardTypeValid,
              'is-invalid': !cardTypeValid
            }"
            class="form-control"
            @keyup="validateCardType"
            @change="validateCardType"
          />
        </div>
        <div class="form-row">
          <label for="cardNumber">Card Number:</label>
          <input
            type="text"
            id="cardNumber"
            name="cardNumber"
            v-model="cardNumber"
            v-bind:class="{
              'is-valid': cardNumberValid,
              'is-invalid': !cardNumberValid
            }"
            class="form-control"
            @keyup="validateCardNumber"
            @change="validateCardNumber"
          />
        </div>
        <div class="form-row">
          <label for="date">Expiry Date:</label>
          <input type="text" id="date" name="date" v-model="date" />
        </div>
        <div class="form-row">
          <label for="secureCode">Security Code:</label>
          <input
            type="text"
            id="secureCode"
            name="secureCode"
            v-model="secureCode"
            v-bind:class="{
              'is-valid': secureCodeValid,
              'is-invalid': !secureCodeValid
            }"
            class="form-control"
            @keyup="validateSecureCode"
            @change="validateSecureCode"
          />
        </div>
        <div class="button-container">
          <button
            type="submit"
            class="btn btn-outline-secondary"
            name="details"
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

/*.payment-info {*/
/*  padding-top: 5%;*/
/*  padding-bottom: 5%;*/
/*}*/

.payment-container {
  margin: auto;
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
  name: "PaymentInformation",
  data() {
    return {
      name: "",
      cardType: "",
      cardNumber: "",
      date: "",
      secureCode: "",
      componentWidth: 90,
      nameValid: "",
      cardTypeValid: "",
      cardNumberValid: "",
      dateValid: "",
      secureCodeValid: ""
    };
  },
  computed: {},
  methods: {
    setPaymentInfoToParent() {
      //TODO Validate before showing 2nd form
      this.componentWidth = 60;
      this.$emit("setPaymentInfoToParent", this.$data);
    },
    validateName() {
      this.nameValid = this.$data.name !== "";
    },
    validateCardType() {
      this.cardTypeValid = this.$data.cardType !== "";
    },
    validateCardNumber() {
      this.cardNumberValid = this.$data.cardNumber !== "";
    },
    validateDate() {
      this.dateValid = this.$data.date !== "";
    },
    validateSecureCode() {
      this.secureCodeValid = this.$data.secureCode !== "";
    },

    callValidation() {
      this.validateName();
      this.validateCardType();
      this.validateCardNumber();
      this.validateDate();
      this.validateSecureCode();
    },

    submitCustomerDetails(e) {
      //TODO Validate before showing 2nd form
      console.log("e");
      console.log(e);
      e.preventDefault();
      //TODO send array of data to parent
      console.log("this.$data");
      console.log(this.$data);
      if (
        this.$data.name !== "" &&
        this.$data.cardType !== "" &&
        this.$data.cardNumber !== "" &&
        this.$data.date !== "" &&
        this.$data.secureCode !== ""
      ) {
        this.$emit("submitCustomerDetails", this.$data);
        this.componentWidth = 60;
        this.nameValid = true;
        this.cardTypeValid = true;
        this.cardNumberValid = true;
        this.dateValid = true;
        this.secureCode = true;
      } else {
        this.callValidation();
      }
    }
  }
};
</script>
