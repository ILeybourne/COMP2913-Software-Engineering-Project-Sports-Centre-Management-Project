<template>
  <div class="membership-options">
    <div class="membership-container">
      <v-row class="info-container">
        <v-container
          class="membership-details"
          v-for="type in membershipTypes"
          :key="type.id"
          ><v-row>
            <v-col
              ><h3>{{ type.name }}</h3></v-col
            >
          </v-row>
          <v-spacer></v-spacer>
          <v-row>
            <v-col><b>Duration</b> (Days): </v-col>
            <v-col>{{ type.duration }}</v-col>
          </v-row>
          <v-spacer></v-spacer>
          <v-row>
            <v-col><b>Cost</b> (£): </v-col>
            <v-col>{{ type.cost }}</v-col>
          </v-row>
          <v-row class="text">
            <v-col
              ><p>
                Purchase today and pay once to be a member for
                {{ type.duration }} days, or subscribe to have your membership
                renewed automatically for as long as you like. Payments would be
                taken every {{ type.duration }} days and you can stop them any
                time.
              </p></v-col
            >
          </v-row>

          <v-spacer></v-spacer>
          <v-spacer></v-spacer>
          <v-col class="btn-container">
            <v-btn
              icon
              x-large
              color="#242424"
              v-if="selectedOption !== type.id"
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
                >mdi-checkbox-marked-circle-outline</v-icon
              >
              <v-icon
                class="icon"
                v-else-if="hoverName == null || hoverName !== type.name"
                >mdi-checkbox-blank-circle-outline</v-icon
              >
            </v-btn>
            <v-btn
              icon
              x-large
              color="#242424"
              v-if="selectedOption === type.id"
              class="button"
              v-on:click="selectMembershipType(type.id), getMembershipType()"
            >
              <v-icon class="icon">mdi-checkbox-marked-circle</v-icon>
            </v-btn>
          </v-col>
        </v-container>
        <v-container class="account-creation-form">
          <v-row>
            <v-col><h3>Sign Up Today!</h3></v-col>
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
          <v-container
            class="membershipTypeDetails"
            v-if="selectedOption === null"
            >Select a Membership</v-container
          >
          <v-container
            class="membershipTypeDetails"
            v-if="selectedOption !== null"
          >
            <v-row><v-col>Membership Chosen</v-col></v-row>
            <v-col>Type: </v-col>
            <v-col>{{ selectedMembershipType.name }}</v-col>
            <v-row>
              <v-col v-if="postResponse.id === 0"
                >You already have a membership with Zenergy</v-col
              >
              <v-col v-if="postResponse.id > 0"
                >New Membership created with id {{ postResponse.id }}</v-col
              >
              <v-col v-if="postResponse === formError">
                > {{ postResponse }}</v-col
              >
            </v-row>
          </v-container>
          <v-spacer></v-spacer>
          <v-container class="submit-container">
            <button
              v-if="selectedOption !== null"
              type="submit"
              value="submit"
              class="site-btn"
              v-on:click="addMember()"
            >
              Continue to Payment {{ selectedOption }}
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
  display: flex;
  justify-content: center;
  flex-direction: row;
  width: 100%;
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
  color: #242424;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 150px;
  max-width: 400px;
  flex-basis: auto; /* default value */
  flex-grow: 1;
}

.membership-details h3 {
  color: #242424;
}

.membership-details:hover h3 {
  background: #fcff18;
}

.account-creation-form {
  display: flex;
  flex-direction: column;
  min-height: 50%;
  text-align: center;
  background: #f6f9fa;
  color: #242424;
  min-width: 300px;
  max-width: 500px;
  flex-basis: auto; /* default value */
  flex-grow: 20;
  margin: 20px;
}

.account-creation-form h3 {
  background: #fcff18;
  color: #242424;
}

/*.submit-container {
  text-align: right;
  padding-top: 25px;
  padding-right: 90px;
}*/
.form {
  text-align: center;
}
.submit-container {
  bottom: 0;
}
.membershipTypeDetails {
  margin: auto;
  border: 2px solid #fff;
  height: 100%;
}
.text-field {
  text-align: center;
}
</style>

<script>
import { mapActions, mapGetters } from "vuex";

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
      hoverName: null,
      formError: "Please Check From Data",
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
      }
    };
  },
  computed: {
    ...mapGetters("membership", ["membershipTypes", "selectedMembershipType"])
  },
  methods: {
    ...mapActions("membership", [
      "getMembershipTypes",
      "getSelectedMembershipType"
    ]),
    selectMembershipType(id) {
      console.log(id);
      this.selectedOption = id;
    },
    assignEmail(email) {
      console.log(email);
      this.formBody.email = email;
    },
    async getMembershipType() {
      await this.getSelectedMembershipType(this.selectedOption);
    },
    async addMember() {
      if (this.formBody.email !== null && this.formBody.dateOfBirth !== null) {
        console.log(this.formBody);
        const body = {
          ...this.formBody
        };
        await this.$http
          .post("/membership/" + this.selectedOption, body)
          .then(response => {
            console.log(response);
            this.postResponse = response.data;
          })
          .catch(function(error) {
            console.log(error);
          });
      } else {
        this.postResponse = this.formError;
      }
    }
  },
  async mounted() {
    await this.getMembershipTypes();
  }
};
</script>
