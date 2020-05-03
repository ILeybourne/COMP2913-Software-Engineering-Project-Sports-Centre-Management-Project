<template>
  <div class="facilities">
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
    <b-card-group>
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
</style>

<script>
import Facility from "@/components/Facility.vue";
import { mapGetters } from "vuex";

export default {
  name: "Facilities",
  components: {
    Facility
  },
  computed: {
    ...mapGetters("facilities", ["facilities", "facilitiesLoading"]),
    ...mapGetters("auth", ["isEmployeeOrManager"])
  }
};
</script>
