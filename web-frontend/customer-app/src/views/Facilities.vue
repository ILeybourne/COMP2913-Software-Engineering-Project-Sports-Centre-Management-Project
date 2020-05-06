<template>
  <div class="facilities">
    <h1>Our Facilities</h1>
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

<style scoped></style>

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
