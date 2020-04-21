<template>
  <div class="membership-options">
    <div class="membership-container">
      <div class="info-container">
        <v-row v-for="type in membershipTypes._embedded" :key="type.id">
          <v-container
            class="membership-details"
            v-for="dto in type"
            :key="dto.id"
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
                  renewed automatically for as long as you like. Payments would
                  be taken every {{ dto.duration }} days and you can stop them
                  any time.
                </p></v-col
              >
            </v-row>

            <v-spacer></v-spacer>
            <v-spacer></v-spacer>
            <v-col class="btn-container">
              <v-btn
                icon
                x-large
                color="#fcff18"
                v-if="selectedOption !== dto.id"
                class="unselected"
                v-on:click="
                  selectMembershipType(dto.id),
                    getMembershipType(),
                    assignEmail($auth.user.email)
                "
                @mouseover="hoverName = dto.name"
                @mouseleave="hoverName = null"
              >
                <v-icon v-if="hoverName === dto.name"
                  >mdi-checkbox-marked-circle-outline</v-icon
                >
                <v-icon v-else-if="hoverName == null || hoverName !== dto.name"
                  >mdi-checkbox-blank-circle-outline</v-icon
                >
              </v-btn>
              <v-btn
                icon
                x-large
                color="#fcff18"
                v-if="selectedOption === dto.id"
                class="selected"
                v-on:click="selectMembershipType(dto.id), getMembershipType()"
              >
                <v-icon>mdi-checkbox-marked-circle</v-icon>
              </v-btn>
            </v-col>
          </v-container>
          <v-container
            class="account-creation-form"
            v-if="selectedOption !== null"
          >
            <v-row>
              <v-col><h3>Sign Up Today!</h3></v-col>
            </v-row>
            <v-spacer></v-spacer>
            <v-row>
              <v-form class="form">
                <v-row>
                  <v-col>
                    <v-text-field
                      class="text-field"
                      v-model="emailAddress"
                      label="Email Address"
                      required
                    ></v-text-field>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col>Date of Birth: </v-col>
                  <v-col>
                  <input
                  type="date"
                  id="date"
                  name="date"
                  v-model="selectedDateOfBirth"
                  required
                />
                  </v-col>
                </v-row>




                <v-spacer></v-spacer>
                <v-row>
                  <v-col>Renew?</v-col>
                  <v-col
                    ><v-checkbox v-model="repeatingPayment"></v-checkbox
                  ></v-col>
                </v-row>
              </v-form>
            </v-row>

            <v-spacer></v-spacer>
            <v-container class="membershipTypeDetails">
              <v-row><v-col>Membership Details</v-col></v-row>
              <v-row>
                <v-col>Type: </v-col>
                <v-col>{{ membershipTypeDetails.name }}</v-col>
              </v-row>
              <v-row>
                <v-col>Cost: </v-col>
                <v-col>{{ membershipTypeDetails.cost }}</v-col>
              </v-row>
              <v-row>
                <v-col>Duration: </v-col>
                <v-col>{{ membershipTypeDetails.duration }}</v-col>
              </v-row>
              <v-row>
                <v-col>Start Date: </v-col>
                <v-col>Todays date</v-col>
              </v-row>
              <v-row>
                <v-col>End Date: </v-col>
                <v-col>End date</v-col>
              </v-row>
            </v-container>
            <v-spacer></v-spacer>
            <v-container class="submit-container">
              <button
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
  </div>
</template>

<style scoped>
.membership-options {
  background: #f6f9fa;
  padding: 59px 0px 59px 0px;
  min-height: 50%;
  height: auto;
}

.membership-container {
  /*margin: auto;*/
  /*width: 50%;*/

  padding: 10px;
  min-height: 50%;
  background: #f6f9fa;
}

.info-container {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: auto;
}

/*form {*/
/*    diplay: flex;*/
/*}*/

.membership-details {
  text-align: center;
  width: 20%;
  min-height: 50%;
  background: #242424;
  color: #fff;
}

.membership-details h3 {
  color: #fff;
  text-decoration-color: #242424;
}

.membership-details:hover h3 {
  background: #fcff18;
  color: #242424;
}

.account-creation-form {
  display: flex;
  flex-direction: column;
  width: 30%;
  min-height: 50%;
  text-align: center;
  background: #242424;
  color: #fff;
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

.submit-container {
  bottom: 0;
}
.membershipTypeDetails {
  margin: auto;
  border: 2px solid #fff;
  height: 100%;
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
      date: null
    };
  },
  watch: {
    menu(val) {
      val && setTimeout(() => (this.$refs.picker.activePicker = "YEAR"));
    }
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
    save(date) {
      this.$refs.menu.save(date);
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
        .then(function(response) {
          console.log(response);
        })
        .catch(function(error) {
          console.log(error);
        });
    }
  },
  created() {
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
