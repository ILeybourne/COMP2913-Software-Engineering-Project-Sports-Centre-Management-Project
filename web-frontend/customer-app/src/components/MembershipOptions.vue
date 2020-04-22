<template>
  <div class="membership-options">
    <div class="membership-container">
      <v-row
        v-for="type in membershipTypes._embedded"
        :key="type.id"
        class="info-container"
      >
        <v-container
          class="membership-details"
          v-for="dto in type"
          :key="dto.id"
          v-on:load="assignEmail($auth.user.email)"
          ><v-row>
            <v-col
              ><h3>{{ dto.name }}</h3></v-col
            >
          </v-row>
          <v-spacer></v-spacer>
          <v-row>
            <v-col><b>Duration</b> (Days): </v-col>
            <v-col>{{ dto.duration }}</v-col>
          </v-row>
          <v-spacer></v-spacer>
          <v-row>
            <v-col><b>Cost</b> (£): </v-col>
            <v-col>{{ dto.cost }}</v-col>
          </v-row>
          <v-row class="text">
            <v-col
              ><p>
                Purchase today and pay once to be a member for
                {{ dto.duration }} days, or subscribe to have your membership
                renewed automatically for as long as you like. Payments would be
                taken every {{ dto.duration }} days and you can stop them any
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
              v-if="selectedOption !== dto.id"
              class="button"
              v-on:click="
                selectMembershipType(dto.id),
                  getMembershipType(),
                  assignEmail($auth.user.email)
              "
              @mouseover="hoverName = dto.name"
              @mouseleave="hoverName = null"
            >
              <v-icon class="icon" v-if="hoverName === dto.name"
                >mdi-checkbox-marked-circle-outline</v-icon
              >
              <v-icon
                class="icon"
                v-else-if="hoverName == null || hoverName !== dto.name"
                >mdi-checkbox-blank-circle-outline</v-icon
              >
            </v-btn>
            <v-btn
              icon
              x-large
              color="#242424"
              v-if="selectedOption === dto.id"
              class="button"
              v-on:click="selectMembershipType(dto.id), getMembershipType()"
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
                  v-model="emailAddress"
                  label="Email Address"
                  required
                ></v-text-field>
                <v-text-field
                  class="text-field"
                  type="date"
                  id="date"
                  name="date"
                  label="Date Of Birth"
                  v-model="selectedDateOfBirth"
                  required
                />
                <v-checkbox
                  class="text-field"
                  v-model="repeatingPayment"
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
            <v-row>
              <v-col>Type: </v-col>
              <v-col>{{ membershipTypeDetails.name }}</v-col>
            </v-row>
            <v-row>
              <v-col v-if="postResponse.id === 0"
                >You already have a membership with Zenergy</v-col
              >
              <v-col v-if="postResponse.id > 0"
                >New Membership created with id {{ postResponse.id }}</v-col
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
import axios from "@/plugins/axios.plugin";

export default {
  name: "Memberships",
  components: {},
  data: function() {
    return {
      membershipTypes: [],
      selectedOption: null,
      hoverName: null,
      repeatingPayment: null,
      membershipTypeDetails: [],
      selectedDateOfBirth: null,
      emailAddress: null,
      menu: false,
      date: null,
      postResponse: []
    };
  },
  methods: {
    selectMembershipType(id) {
      console.log(id);
      this.selectedOption = id;
    },
    assignEmail(email) {
      console.log(email);
      this.emailAddress = email;
    },
    getMembershipType() {
      axios.get(`/membership/types/` + this.selectedOption).then(response => {
        this.membershipTypeDetails = response.data;
      });
      return this.membershipTypeDetails;
    },
    addMember() {
      axios
        .post("/membership/" + this.selectedOption, {
          email: this.emailAddress,
          dateOfBirth: this.selectedDateOfBirth,
          repeatingPayment: this.repeatingPayment
        })
        .then(response => {
          console.log(response);
          this.postResponse = response.data;
        })
        .catch(function(error) {
          console.log(error);
        });
    }
  },
  beforeCreate() {
    axios
      .get(`/membership/types`)
      .then(response => {
        this.membershipTypes = response.data;
      })
      .catch(e => {
        this.errors.push(e);
      });
  }
};
</script>
