<template>
  <div class="billing-info">
    <div
      class="billing-container"
      v-bind:style="{ width: this.componentWidth + '%' }"
    >
      <form>
        <!--        TODO Fill from customer details-->
        <div class="form-row">
          <label for="name">Billing Name:</label>
          <input type="text" id="name" name="name" v-model="name" />
        </div>
        <div class="form-row">
          <label for="email">Email:</label>
          <input type="email" id="email" name="email" v-model="email" />
        </div>
        <div class="form-row">
          <label for="houseNumber">House Number:</label>
          <input
            type="houseNumber"
            id="houseNumber"
            name="houseNumber"
            v-model="houseNumber"
          />
        </div>
        <div class="form-row">
          <label for="streetName">Street Name:</label>
          <input
            type="streetName"
            id="streetName"
            name="streetName"
            v-model="streetName"
          />
        </div>
        <div class="form-row">
          <label for="city">City:</label>
          <input type="city" id="city" name="city" v-model="city" />
        </div>
        <div class="form-row">
          <label for="postCode">Post Code:</label>
          <input
            type="postCode"
            id="postCode"
            name="postCode"
            v-model="postCode"
          />
        </div>
        <div class="button-container">
          <button
            type="button"
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
      componentWidth: 90
    };
  },
  computed: {},
  methods: {
    submitBillingDetails(e) {
      //TODO Validate before showing 2nd form

      this.componentWidth = 60;
      // this.userType = e.toElement.name;
      this.$emit("submitBillingDetails", this.$data);

      console.log(e);
      console.log(this.data);
    }
  }
};
</script>
