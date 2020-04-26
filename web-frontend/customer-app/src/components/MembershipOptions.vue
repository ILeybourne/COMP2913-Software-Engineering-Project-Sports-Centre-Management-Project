<template>
  <div class="membership-options">
    <div class="membership-container">
      <div class="info-container">
        <div class="spacer"></div>
        <div class="annual-membership-details">
          Annual Membership Details
        </div>
        <div class="spacer"></div>

        <div class="monthly-membership-details">
          Monthly Membership Details
        </div>
        <div class="spacer"></div>

        <div class="account-creation-form">
          <h2>Sign up</h2>
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
                'is-valid': !$v.surname.$invalid,
                'is-invalid': $v.surname.$invalid
              }"
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
              v-bind:class="{
                'is-valid': !$v.email.$invalid,
                'is-invalid': $v.email.$invalid
              }"
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
              v-bind:class="{
                'is-valid': !$v.phone.$invalid,
                'is-invalid': !$v.phone.$invalid
              }"
              required
            />
          </div>
        </div>
      </div>
      <div class="form-container">
        <form class="form-inline">
          <div class="spacer"></div>
          <div class="btn-container">
            <input
              type="radio"
              id="annual"
              name="membership-type"
              value="annual"
            />
          </div>

          <div class="spacer"></div>
          <div class="btn-container">
            <input
              type="radio"
              id="monthly"
              name="membership-type"
              value="monthly"
            />
          </div>
          <div class="spacer"></div>
          <div class="submit-container">
            <button type="submit" value="submit" class="btn btn-default">
              Buy Membership And Create Account
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.form-row {
  padding: 20px;
}

input {
  width: 80%;
}

label {
  width: 20%;
}

.membership-options {
  padding-top: 5%;
  padding-bottom: 5%;
  min-height: 50%;
  max-height: 50%;
}

.membership-container {
  /*margin: auto;*/
  /*width: 50%;*/
  border: 3px solid #3183e5;
  border-radius: 10px;

  padding: 10px;
  min-height: 50%;
}

.info-container {
  display: flex;
  width: 100%;
  height: 500px;
}

.btn-container {
  margin: 0 auto;
}

/*form {*/
/*    diplay: flex;*/
/*}*/

.annual-membership-details {
  width: 20%;
  border: 3px solid #3183e5;
  border-radius: 10px;
  min-height: 50%;
}

.monthly-membership-details {
  width: 20%;
  border: 3px solid #3183e5;
  border-radius: 10px;
  min-height: 50%;
}

.account-creation-form {
  width: 40%;
  border: 3px solid #3183e5;
  border-radius: 10px;
  min-height: 50%;
}

.spacer {
  width: 4%;
  height: 1%;
}

.form-container {
  display: flex;
}

.btn-container {
  margin: 0 auto;
  width: 100%;
}

.form-inline {
  display: flex;
  flex-flow: row wrap;
  align-items: center;
}
</style>

<script>
import Vue from "vue";
import Vuelidate from "vuelidate";
Vue.use(Vuelidate);
import {
  required,
  minLength,
  maxLength,
  email,
  numeric
} from "vuelidate/lib/validators";

export default {
  name: "BookingInformation",

  data() {
    return {
      firstName: "",
      surname: "",

      email: "",
      phone: ""
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
      email,
      minLength: minLength(3),
      maxLength: maxLength(20)
    },
    phone: {
      required,
      numeric,
      minLength: minLength(9),
      maxLength: maxLength(13)
    }
  }
};
</script>
