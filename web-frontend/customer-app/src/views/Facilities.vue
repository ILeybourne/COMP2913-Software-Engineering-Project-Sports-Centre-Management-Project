<template>
  <div class = "facility-container">
    <div style="padding: 10px;"></div>
    <div style = "display: flex; width: 100%; ">
        <div style = " width: 5%;"></div>
        <Facility style = " width: 40%; "></Facility>
        <div style = " width: 10%;"></div>
        <Facility style = " width: 40%;"></Facility>

    </div>
    <div style="padding: 10px;"></div>
    <div style = "display: flex; width: 100%; ">
          <div style = " width: 5%;"></div>
          <Facility style = " width: 40%; "></Facility>
          <div style = " width: 10%;"></div>
          <Facility style = " width: 40%;"></Facility>
    </div>
  </div>
</template>

<script>
import Facility from "@/components/Facility.vue";

// @ is an alias to /src
export default {
  name: "Facilities",
  components: {
    Facility
  }
};
</script>