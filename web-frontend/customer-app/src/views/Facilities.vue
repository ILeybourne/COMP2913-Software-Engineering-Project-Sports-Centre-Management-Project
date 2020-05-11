<template>
  <div class="facilities" justify="center">
    <div class="heading-div">
      <h1>Our <span>Facilities</span></h1>
    </div>
    <v-alert show v-if="isEmployeeOrManager">
      <v-container class="create-button">
        <b-btn
          class="button-create"
          variant="white"
          :to="{ name: 'FacilityPage', params: { id: 'create' } }"
          ><span>
            <v-icon color="#1f1f1f" x-large title="Create New Facility"
              >mdi-plus</v-icon
            ></span
          >
        </b-btn>
      </v-container>
    </v-alert>
    <b-card-group class="cards">
      <Facility
        v-for="facility in facilities"
        :key="facility.id"
        :facility="facility"
      ></Facility>
    </b-card-group>
    <div class="row">
      <div class="col text-center">
        <b-button v-if="facilitiesLoading" variant="outline-primary"
          >Load more</b-button
        >
      </div>
    </div>
  </div>
</template>

<style scoped>
@media screen and (max-width: 600px) {
  .heading-div h1 {
    font-size: 10vw;
  }
}
.heading-div {
  margin-bottom: 20px;
}
.heading-div h1 {
  width: 60%;
  margin: auto;
}
.heading-div p {
  width: 100%;
  padding: 10px;
}
span {
  background: #fcff18;
}
.cards {
  display: flex;
  justify-content: center;
  text-align: center;
  flex-wrap: wrap;
}
.create-button {
  width: 85%;
  text-align: right;
  justify-content: flex-end;
  margin-right: 100px;
}
.button-create {
  color: white !important;
}
.button-create span {
  padding: 15px;
}
.button-create:hover span {
  padding: 15px;
  background: rgba(255, 255, 0, 0.5);
}
</style>

<script>
import Facility from "@/components/Facility.vue";
import { mapGetters } from "vuex";
// import orderBy from "lodash.orderby";

export default {
  name: "Facilities",
  components: {
    Facility
  },
  computed: {
    ...mapGetters("facilities", {
      unsorted: "facilities",
      facilitiesLoading: "facilitiesLoading"
    }),
    ...mapGetters("auth", ["isEmployeeOrManager"]),
    facilities() {
      return this.unsorted.slice().sort((a, b) => {
        return ("" + a.name).localeCompare(b.name);
      });
    }
  }
};
</script>
