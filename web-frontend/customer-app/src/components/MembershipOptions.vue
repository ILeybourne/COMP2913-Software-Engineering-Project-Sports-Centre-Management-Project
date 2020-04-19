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
                  renewed automatically for as long as you say. Payments would
                  be taken be taken be taken every {{ dto.duration }} days and
                  you can any time.
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
                v-on:click="selectMembershipType(dto.id)"
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
                v-on:click="selectMembershipType(dto.id)"
              >
                <v-icon>mdi-checkbox-marked-circle</v-icon>
              </v-btn>
            </v-col>
          </v-container>
          <v-container class="account-creation-form">
            <v-row>
              <v-col><h4>Account Creation Form</h4></v-col>
            </v-row>
          </v-container>
        </v-row>
      </div>
    </div>
    <v-container class="submit-container">
      <button type="submit" value="submit" class="site-btn">
        Continue to Payment {{ selectedOption }}
      </button>
    </v-container>
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
  width: 30%;
  border: 3px solid #3183e5;
  border-radius: 10px;
  min-height: 50%;
}

.submit-container {
  text-align: right;
  padding-top: 25px;
  padding-right: 90px;
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
      hoverName: null
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
