<template>
  <div class="membership-options">
    <div class="membership-container">
      <div class="info-container">
        <div class="spacer"></div>
        <v-row v-for="type in membershipTypes._embedded" :key="type.id">
          <v-container
            class="membership-details"
            v-for="dto in type"
            :key="dto.id"
            ><v-row>
              <v-col
                ><h4>{{ dto.name }}</h4></v-col
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
            <v-spacer></v-spacer>
            <v-spacer></v-spacer>
            <label>
              <div class="btn-container">
                <v-btn
                  v-if="selectedOption !== dto.id"
                  class="mx-2"
                  fab-dark color="yellow"
                  v-on:click="selectMembershipType(dto.id)"
                >
                  <v-icon dark>mdi-checkbox-blank-circle-outline</v-icon>
                </v-btn>
                <v-btn
                  v-if="selectedOption === dto.id"
                  class="mx-2"
                  v-on:click="selectMembershipType(dto.id)"
                >
                  <v-icon>mdi-checkbox-marked-circle</v-icon>
                </v-btn>
              </div>
              Select {{ dto.name }} Membership
            </label>
          </v-container>
        </v-row>



        <div class="account-creation-form">
          Account Creation Form
        </div>
      </div>

      <div class="spacer"></div>
      <div class="submit-container">
        <button type="submit" value="submit" class="site-btn">
          Continue to Payment {{ selectedOption }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.membership-options {
  background: #f6f9fa;
  padding: 59px 0 5px;
  min-height: 50%;
  max-height: 50%;
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
  width: 100%;
  height: 500px;
}

/*form {*/
/*    diplay: flex;*/
/*}*/

.membership-details {
  text-align: center;
  width: auto;
  min-height: 50%;
  background: #242424;
  color: #fff;
}

.membership-details h4 {
  color: #fff;
  text-decoration-color: #242424;
}

.membership-details h4:hover {
  color: #fff;
  background: #242424;
}

.membership-details:hover h4 {
  background: #fcff18;
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

</style>

<script>
import axios from "@/plugins/axios.plugin";

export default {
  name: "Memberships",
  components: {},
  data: function() {
    return {
      membershipTypes: [],
      selectedOption: null
    };
  },
  methods: {
    selectMembershipType(id) {
      console.log(id);
      this.selectedOption = id;
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
