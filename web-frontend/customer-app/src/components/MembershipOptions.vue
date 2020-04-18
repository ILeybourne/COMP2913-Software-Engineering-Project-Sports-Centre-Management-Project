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
              <v-col><b>Duration</b><br />(Days)</v-col>
              <v-col>{{ dto.duration }}</v-col>
            </v-row>
            <v-spacer></v-spacer>
            <v-row>
              <v-col><b>Cost</b><br />(£)</v-col>
              <v-col>{{ dto.cost }}</v-col>
            </v-row>
            <form class="form-inline">
              <div class="spacer"></div>
              <div class="btn-container">
                <input
                  type="radio"
                  :id=dto.name
                  name="membership-type"
                  :value=dto.id
                />
              </div>
            </form>
          </v-container>
        </v-row>
        <div class="spacer"></div>

        <div class="account-creation-form">
          Account Creation Form
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

.membership-details {
  width: auto;
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
import axios from "@/plugins/axios.plugin";

export default {
  name: "Memberships",
  components: {},
  data: function() {
    return {
      membershipTypes: []
    };
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
