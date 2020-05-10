<template>
  <div class="facilities" justify="center">
    <div class="heading-div">
      <h1>Our <span>Facilities</span></h1>
    </div>
    <v-alert show v-if="isEmployeeOrManager">
      <b-button
        variant="primary"
        :to="{ name: 'FacilityPage', params: { id: 'create' } }"
      >
        Create a new Facility
      </b-button>
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
.heading-div span {
  background: #fcff18;
}
  .cards{
    display: flex;
    justify-content: center;
    text-align: center;
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
