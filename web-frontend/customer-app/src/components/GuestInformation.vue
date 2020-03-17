<template>
  <div class="guest-info">
    <div
      class="guest-container"
      v-bind:style="{ width: this.componentWidth + '%' }"
    >
      <form @submit="submitCustomerDetails($event)">
        <div class="form-row">
          <label for="firstName">First Name:</label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            v-model="firstName"
            v-bind:class="{
              'is-valid': firstNameValid,
              'is-invalid': !firstNameValid
            }"
            class="form-control"
            @keyup="validateFirstName"
            required
          />
        </div>
        <div class="form-row">
          <label for="surname">Surname:</label>
          <input
            type="text"
            id="surname"
            name="surname"
            class="form-control"
            v-model="surname"
            v-bind:class="{
              'is-valid': surnameValid,
              'is-invalid': !surnameValid
            }"
            @keyup="validateSurname"
            required
          />
        </div>
        <div class="form-row">
          <label for="email">Email:</label>
          <input
            type="email"
            id="email"
            name="email"
            v-model="email"
            class="form-control"
            v-bind:class="{ 'is-valid': emailValid, 'is-invalid': !emailValid }"
            @keyup="validateEmail"
            required
          />
        </div>
        <div class="form-row">
          <label for="phone">Phone Number:</label>
          <input
            type="phone"
            id="phone"
            name="phone"
            v-model="phone"
            class="form-control"
            pattern="[0-9]{11}"
            v-bind:class="{ 'is-valid': phoneValid, 'is-invalid': !phoneValid }"
            @keyup="validatePhone"
            required
          />
        </div>
        <div class="form-row">
          <label for="health">Health Issues:</label>
          <input
            type="health"
            id="health"
            name="health"
            v-model="health"
            class="form-control"
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
      componentWidth: 90,
      firstNameValid: null,
      surnameValid: null,
      emailValid: null,
      phoneValid: null
    };
  },
  computed: {},
  methods: {
    getUserType(e) {
      this.userType = e.toElement.name;
    },

    validateFirstName() {
      console.log(this.$data.selectedFacility !== "");
      this.firstNameValid = this.$data.firstName !== "";
    },
    validateSurname() {
      this.surnameValid = this.$data.surname !== "";
    },
    validateEmail() {
      this.emailValid = this.$data.email !== "";
    },
    validatePhone() {
      this.phoneValid =
        this.$data.phone !== "" && this.$data.phone.length == 11;
    },

    callValidation() {
      this.validateFirstName();
      this.validateSurname();
      this.validateEmail();
      this.validatePhone();
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
        this.$data.firstName !== "" &&
        this.$data.surname !== "" &&
        this.$data.email !== "" &&
        this.$data.phone !== ""
      ) {
        this.$emit("submitCustomerDetails", this.$data);
        this.componentWidth = 60;
        this.facilityValid = true;
        this.activitiesValid = true;
        this.dateValid = true;
        this.timeValid = true;
      } else {
        console.log("wont emit");
        this.callValidation();
      }
    }
  }
};
</script>
