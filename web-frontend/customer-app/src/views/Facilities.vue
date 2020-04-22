<template>
  <div class="facilities">
    <h1>Our Facilities</h1>
    <v-alert show>
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
  </div>
</template>

<style scoped></style>

<script>
import Facility from "@/components/Facility.vue";
import { mapActions, mapGetters } from "vuex";

export default {
  name: "Facilities",
  components: {
    Facility
  },
  computed: {
    ...mapGetters("facilities", ["facilities"])
  },
  methods: {
    ...mapActions("facilities", ["getFacilities"])
  },
  mounted() {
    this.getFacilities();
  }
};
</script>
