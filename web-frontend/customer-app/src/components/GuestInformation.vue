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
              'is-valid': !$v.firstName.$invalid,
              'is-invalid': $v.firstName.$invalid
            }"
            class="form-control"
            @keyup="validateFirstName"
            required
          />
          <div class="error" v-if="!$v.firstName.required">Field is required</div>
          <div class="error" v-if="!$v.firstName.minLength">Name must have at least {{$v.firstName.$params.minLength.min}} letters.</div>
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
              'is-valid': !$v.surname.$invalid,
              'is-invalid': $v.surname.$invalid
            }"
            @keyup="validateSurname"
            required
          />
          <div class="error" v-if="!$v.surname.required">Field is required</div>
          <div class="error" v-if="!$v.surname.minLength">Name must have at least {{$v.surname.$params.minLength.min}} letters.</div>
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
        <div class="form-row">
          <label for="cardRadio">Card or Cash:</label>
          <b-form-group >
            <b-form-radio-group
                    id="btn-radios-2"
                    v-model="cardCashSelection"
                    :options="cardCashOptions"
                    buttons
                    button-variant="outline-primary"
                    size="lg"
                    name="radio-btn-outline"
            ></b-form-radio-group>
          </b-form-group>
<!--          <input type="checkbox" checked data-toggle="toggle" data-on="Card" data-off="Cash">-->
<!--          <div class="form-row" style="width: 70px">-->
<!--            <div class="radio-toolbar">-->
<!--          <label for="cardRadio" class="radio-inline">Card-->

<!--          <input type="radio" id="cardRadio" name="cardCash" value="card"  v-model="cardCash"/>-->
<!--              <span class="checkmark"></span>-->
<!--          </label><br>-->
<!--          <label for="cashRadio" class="radio-inline">Cash-->
<!--          <input type="radio" id="cashRadio" name="cardCash" value="cash" v-model="cardCash"/>-->
<!--            <span class="checkmark"></span>-->

<!--          </label><br>-->

<!--          </div>-->
<!--          </div>-->
        </div>
        <div class="button-container">
          <button
            id="guestSubmitBtn"
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
  .radio-toolbar {
    display: block;
    position: relative;
    padding-left: 35px;
    margin-bottom: 12px;
    cursor: pointer;
    font-size: 22px;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
  }

  /* Hide the browser's default radio button */
  .radio-toolbar  input {
    position: absolute;
    opacity: 0;
    cursor: pointer;
    height: 0;
    width: 0;
  }

  /* Create a custom radio button */
  .checkmark {
    position: absolute;
    top: 0;
    left: 0;
    height: 25px;
    width: 25px;
    background-color: #eee;
    border-radius: 50%;
  }

  /* On mouse-over, add a grey background color */
  .radio-toolbar:hover input ~ .checkmark {
    background-color: #ccc;
  }

  /* When the radio button is checked, add a blue background */
  .radio-toolbar input:checked ~ .checkmark {
    background-color: #2196F3;
  }

  /* Create the indicator (the dot/circle - hidden when not checked) */
  .checkmark:after {
    content: "";
    position: absolute;
    display: none;
  }

  /* Show the indicator (dot/circle) when checked */
  .radio-toolbar input:checked ~ .checkmark:after {
    display: block;
  }

  /* Style the indicator (dot/circle) */
  .radio-toolbar .checkmark:after {
    top: 9px;
    left: 9px;
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: white;
  }

  .error {
    font-size: x-small;
    color: red;
  }

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
  import Vue from 'vue'
  import Vuelidate from 'vuelidate'
  Vue.use(Vuelidate)
  import { required, minLength,  email, numeric } from 'vuelidate/lib/validators'

  export default {
  name: "GuestInformation",
  data() {
    return {
      cardCashOptions: [ { value: 'card', text: 'Card' },
        { value: 'cash', text: 'Cash' },],
      cardCashSelection: "card",
      firstName: "",
      surname: "",
      email: "",
      phone: "",
      health: "",
      // cardCash: "card",
      componentWidth: 90,
      firstNameValid: null,
      surnameValid: null,
      emailValid: null,
      phoneValid: null
    };
  },
    validations: {
      firstName: {
        required,
        minLength: minLength(3)
      },
      surname: {
        required,
        minLength: minLength(2)
      },
      email: {
        required,
        email
      },
      phone: {
        required,
        numeric
      }
    },
  computed: {},
  methods: {
    getUserType(e) {
      this.userType = e.toElement.name;
    },

    validateFirstName() {
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
      e.preventDefault();
      if (
        this.$data.firstName !== "" &&
        this.$data.surname !== "" &&
        this.$data.email !== "" &&
        this.$data.phone !== ""
      ) {
        this.$emit("submitCustomerDetails", this.$data);
        this.componentWidth = 60;
        this.firstNameValid = true;
        this.surnameValid = true;
        this.emailValid = true;
        this.phoneValid = true;
      } else {
        this.callValidation();
      }
    }
  }
};
</script>
