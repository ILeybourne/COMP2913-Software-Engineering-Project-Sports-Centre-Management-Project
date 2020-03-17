<template>
  <div class="guest-info">
    <div
      class="guest-container"
      v-bind:style="{ width: this.componentWidth + '%' }"
    >
      <form>
        <div class="form-row">
          <label for="firstName">First Name:</label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            v-model="firstName"
          />
        </div>
        <div class="form-row">
          <label for="surname">Surname:</label>
          <input type="text" id="surname" name="surname" v-model="surname" />
        </div>
        <div class="form-row">
          <label for="email">Email:</label>
          <input type="email" id="email" name="email" v-model="email" />
        </div>
        <div class="form-row">
          <label for="phone">Phone Number:</label>
          <input type="phone" id="phone" name="phone" v-model="phone" />
        </div>
        <div class="form-row">
          <label for="health">Health Issues:</label>
          <input type="health" id="health" name="health" v-model="health" />
        </div>
        <div class="button-container">
          <button
            type="button"
            class="btn btn-outline-secondary"
            name="details"
            @click="submitCustomerDetails($event)"
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

/*.guest-info {*/
/*  padding-top: 5%;*/
/*  padding-bottom: 5%;*/
/*}*/

.guest-container {
  margin: auto;
  /*width: 50%;*/
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
  name: "GuestInformation",
  data() {
    return {
      firstName: "",
      surname: "",
      email: "",
      phone: "",
      health: "",
      componentWidth: 90
    };
  },
  computed: {},
  methods: {
    submitCustomerDetails() {
      //TODO Validate before showing 2nd form
      this.componentWidth = 60;
      this.$emit("submitCustomerDetails", this.$data);
    }
  }
};
</script>
