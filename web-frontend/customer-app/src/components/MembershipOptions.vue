<template>
  <div class="membership-options">
    <div class="membership-container">
      <v-row class="info-container">
        <v-container
          class="membership-details"
          v-for="type in membershipTypes"
          :key="type.id"
        >
          <v-container class="pricing">
            <v-row class="name" v-if="selectedOption === type.id"
              ><h2>
                <span>{{ type.name }}</span>
              </h2></v-row
            >
            <v-row class="name" v-if="selectedOption !== type.id"
              ><h2>{{ type.name }}</h2></v-row
            >
            <v-row class="membership-price">
              <div class="currency">£</div>
              <h1>{{ type.cost }}</h1></v-row
            >
            <v-row class="duration"
              ><p>/{{ type.duration }} Days</p></v-row
            >
          </v-container>
          <v-spacer></v-spacer>
          <v-row class="text">
            <v-col
              ><ul class="features">
                <li>No Contract, cancel any time</li>
                <li>Access to all facilities</li>
                <li>
                  Book onto regular sessions for a further discounted rate
                </li>
                <li>Any time access</li>
                <li>
                  Pay once for {{ type.duration }} days, or opt for automatic
                  renewal
                </li>
              </ul>
            </v-col>
          </v-row>

          <v-spacer></v-spacer>
          <v-spacer></v-spacer>
          <v-col class="btn-container">
            <v-container class="select" v-if="selectedOption !== type.id">
              <v-btn
                icon
                x-large
                color="#242424"
                label="select"
                class="button"
                v-on:click="
                  selectMembershipType(type.id),
                    getMembershipType(),
                    assignEmail($auth.user.email)
                "
                @mouseover="hoverName = type.name"
                @mouseleave="hoverName = null"
              >
                <v-icon class="icon" v-if="hoverName === type.name"
                  >mdi-checkbox-marked-outline</v-icon
                >
                <v-icon
                  class="icon"
                  v-else-if="hoverName == null || hoverName !== type.name"
                  >mdi-checkbox-blank-outline</v-icon
                >
              </v-btn>
              <br />
              <div class="select-text">SELECT</div>
            </v-container>
            <v-container class="selected" v-if="selectedOption === type.id">
              <v-btn
                icon
                x-large
                color="#242424"
                class="button"
                v-on:click="selectMembershipType(type.id), getMembershipType()"
              >
                <v-icon class="icon">mdi-checkbox-marked</v-icon>
              </v-btn>
              <br />
              <div class="select-text" id="selected-item">SELECTED</div>
            </v-container>
          </v-col>
        </v-container>
        <v-container class="account-creation-form" v-if="selectedOption">
          <v-row>
            <v-col
              ><h2><span>Sign Up</span> Today!</h2></v-col
            >
          </v-row>
          <v-spacer></v-spacer>
          <v-row>
            <v-col>
              <v-form class="form">
                <v-text-field
                  class="text-field"
                  v-model="formBody.email"
                  label="Email Address"
                  required
                  :disabled="!isEmployeeOrManager"
                  :rules="[rules.required, rules.email]"
                ></v-text-field>
                <v-text-field
                  class="text-field"
                  type="date"
                  id="date"
                  name="date"
                  label="Date Of Birth"
                  v-model="formBody.dateOfBirth"
                  required
                  :rules="[rules.required]"
                />
                <v-checkbox
                  class="text-field"
                  v-model="formBody.repeatingPayment"
                  label="Automatic Renewal?"
                ></v-checkbox>
              </v-form>
            </v-col>
          </v-row>

          <v-spacer></v-spacer>
          <ul class="membershipTypeDetails" v-if="selectedOption !== null">
            <li class="form-details-title">MEMBERSHIP DETAILS</li>
            <li>Type: {{ selectedMembershipType.name }}</li>
            <li>Price: {{ formatCurrency(selectedMembershipType.cost) }}</li>
            <li>Start Date: {{ calculateDate(todaysDate) }}</li>
            <li>
              Expiry Date:
              {{ calculateEndDate(selectedMembershipType.duration) }}
            </li>
          </ul>
          <v-container class="response">
            <v-row v-if="postResponse === true"
              ><v-col>You already have a membership with Zenergy</v-col></v-row
            >
            <v-row v-if="postResponse === formError"
              ><v-col> {{ postResponse }}</v-col></v-row
            >
          </v-container>
          <v-container class="submit-container">
            <button
              v-if="selectedOption !== null"
              type="submit"
              value="submit"
              class="site-btn"
              v-on:click="checkForActiveMembership()"
            >
              SIGN UP
            </button>
          </v-container>
        </v-container>
      </v-row>
    </div>
    <div v-if="membershipTypes == null">
      We're sorry, we currently have no memberships for sale
    </div>
  </div>
</template>

<style scoped>
@media screen and (max-width: 400px) {
  .pricing .name h2 {
    font-size: 10vw !important;
  }
  .pricing .membership-price h1 {
    font-size: 10vw !important;
  }
}
.membership-options {
  padding: 59px 0px 59px 0px;
  min-height: 50%;
  height: auto;
}

.membership-container {
  /*margin: auto;*/
  /*width: 50%;*/
  padding: 10px;
  min-height: 50%;
  display: flex;
  justify-content: center;
}

.info-container {
  color: #353535;
  display: flex;
  justify-content: center;
  flex-direction: row;
  width: auto;
  height: max-content;
}

/*form {*/
/*    diplay: flex;*/
/*}*/

.membership-details {
  margin: 20px;
  text-align: center;
  width: 20%;
  min-height: 400px;
  background: #f6f9fa;
  color: #353535;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 150px;
  max-width: 400px;
  flex-basis: auto; /* default value */
  flex-grow: 1;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  padding: 30px 0 30px 0;
}

.membership-details:hover h2 {
  background-color: yellow;
}
.name span {
  background: #fcff18;
}

.account-creation-form {
  display: flex;
  flex-direction: column;
  min-height: 50%;
  text-align: center;
  background: #f6f9fa;
  color: #353535;
  min-width: 300px;
  max-width: 500px;
  flex-basis: auto; /* default value */
  flex-grow: 20;
  margin: 20px;
  padding: 30px 15px 50px 15px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}

.account-creation-form h2 {
  color: #353535;
  padding-bottom: 40px;
}

.form {
  text-align: center;
}
.submit-container {
  bottom: 0;
  margin-top: 30px;
}
.membershipTypeDetails {
  margin: auto;
  height: auto;
  width: 100%;
  background-color: #e6e7e8;
  padding: 15px;
  display: table;
  text-align: center;
  text-overflow: ellipsis;
  list-style-type: none;
}
.text-field {
  text-align: center;
}
.pricing {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: auto;
}
.pricing .name {
  justify-content: center;
}
.pricing .duration {
  justify-content: center;
}
.pricing .membership-price {
  justify-content: center;
}
.select-text {
  font-weight: bolder;
}
#selected-item {
  /*background-color: yellow;*/
  width: min-content;
  text-align: center;
  margin: 0 auto;
}
.features {
  display: table;
  text-align: center;
  text-overflow: ellipsis;
  list-style-type: none;
}
.features li {
  padding: 15px;
  background-color: transparent;
}

.features li:nth-of-type(2n + 1) {
  background-color: #e6e7e8;
}
.account-creation-form span {
  background: #fcff18;
}

.form-details-title {
  font-weight: bolder;
}
.response {
  height: 15px;
  width: auto;
  color: red;
}
</style>

<script>
import { mapActions, mapGetters } from "vuex";
import { formatCurrency } from "@/util/format.helpers";

export default {
  ...mapActions("membership", [
    "getMembershipTypes",
    "getSelectedMembershipType"
  ]),

  name: "Memberships",
  components: {},
  data: function() {
    return {
      selectedOption: null,
      todaysDate: new Date(),
      hoverName: null,
      formError: "Please Check Form Data",
      formBody: {
        email: null,
        dateOfBirth: null,
        repeatingPayment: null
      },
      postResponse: [],
      rules: {
        required: value => !!value || "Required.",
        email: value => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || "Invalid e-mail.";
        }
      },
      membershipDetails: {
        name: null,
        startDate: null,
        endDate: null,
        amount: null,
        repeatingPayment: null
      }
    };
  },
  computed: {
    ...mapGetters("membership", ["membershipTypes", "selectedMembershipType"]),
    ...mapGetters("auth", ["isEmployeeOrManager"])
  },
  methods: {
    formatCurrency: formatCurrency,
    ...mapActions("membership", [
      "getMembershipTypes",
      "getSelectedMembershipType"
    ]),
    selectMembershipType(id) {
      this.selectedOption = id;
    },
    assignEmail(email) {
      this.formBody.email = email;
    },
    async getMembershipType() {
      await this.getSelectedMembershipType(this.selectedOption);
    },
    calculateDate(date) {
      return (
        date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
      );
    },
    calculateEndDate(duration) {
      const date = new Date();
      date.setDate(date.getDate() + duration);
      this.membershipDetails.endDate = this.calculateDate(date);
      return this.membershipDetails.endDate;
    },

    async checkForActiveMembership() {
      if (this.formBody.email !== null && this.formBody.dateOfBirth !== null) {
        const body = {
          accountId: 0,
          repeatingPayment: this.formBody.repeatingPayment,
          email: this.formBody.email
        };
        await this.$http
          .post("/membership/membercheck", body)
          .then(response => {
            this.postResponse = response.data;
            if (this.postResponse === false) {
              this.setMembershipDetails();
              this.onSuccess();
            }
          })
          .catch(function() {});
      } else {
        this.postResponse = this.formError;
      }
    },
    setMembershipDetails() {
      this.membershipDetails.name = this.selectedMembershipType.name;
      this.membershipDetails.startDate = this.calculateDate(this.todaysDate);
      this.membershipDetails.endDate = this.calculateEndDate(
        this.selectedMembershipType.duration
      );
      this.membershipDetails.amount = this.selectedMembershipType.cost;
      this.membershipDetails.repeatingPayment = this.formBody.repeatingPayment;
    },
    async onSuccess() {
      await this.$router.push({
        name: "Checkout",
        params: {
          formBody: this.formBody,
          membershipDetails: this.membershipDetails,
          selectedOption: this.selectedOption
        }
      });
    }
  },
  async mounted() {
    await this.getMembershipTypes();
  }
};
</script>
