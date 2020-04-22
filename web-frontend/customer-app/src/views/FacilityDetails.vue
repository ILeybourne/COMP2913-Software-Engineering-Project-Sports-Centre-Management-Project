<template>
  <div class="facility-details">
    details....
    <!--    <EditFacility></EditFacility>-->
    <!--    <SingleFacilityTimetable></SingleFacilityTimetable>-->
  </div>
</template>

<script>
import { mapActions } from "vuex";

export default {
  name: "FacilityDetails",
  methods: {
    ...mapActions("facilities", ["getFacilities"])
  }
};
</script>

<style scoped></style>
