<template>
  <div class="payment-info">
    <div
      class="payment-container"
      v-bind:style="{ width: this.componentWidth + '%' }"
    >
      <form>
        <!--        TODO Fill from customer details-->
        <label for="name">Name On Card:</label>
        <input type="text" id="name" name="name" v-model="name" /><br />
        <label for="cardType">Card Type:</label>
        <input
          type="cardType"
          id="cardType"
          name="cardType"
          v-model="cardType"
        /><br />
        <label for="cardNumber">Card Number:</label>
        <input
          type="text"
          id="cardNumber"
          name="cardNumber"
          v-model="cardNumber"
        /><br />
        <label for="date">Expiry Date:</label>
        <input type="text" id="date" name="date" v-model="date" /><br />
        <label for="secureCode">Security Code:</label>
        <input
          type="text"
          id="secureCode"
          name="secureCode"
          v-model="secureCode"
        /><br />
        <div class="button-container">
          <button
            type="button"
            class="btn btn-outline-secondary"
            name="details"
            @click="setPaymentInfoToParent()"
          >
            Submit
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.booking-info {
  padding-top: 5%;
  padding-bottom: 5%;
}

.payment-container {
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
  name: "PaymentInformation",
  // props: ["content", "facilities"],
  data() {
    return {
      name: "",
      cardType: "",
      cardNumber: "",
      date: "",
      secureCode: "",
      componentWidth: 40
    };
  },
  computed: {},
  methods: {
    setPaymentInfoToParent() {
      //TODO Validate before showing 2nd form
      this.componentWidth = 25;
      this.$emit("setPaymentInfoToParent", this.$data);
    }
  }
};
</script>
