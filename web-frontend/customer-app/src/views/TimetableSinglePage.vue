<template>
  <div class="timetable">
    <h1>Timetable</h1>
    {{ facilityName }}
    <Timetable></Timetable>
    <!--        <router-link :to="{ name: 'Resource Timetable', params: {facility: detail.name } }">more..</router-link>-->
    <!--        {{}}-->
  </div>
</template>

<script>
import Timetable from "../components/Timetable";

export default {
  name: "TimetableSinglePage",
  data() {
    return {
      facilityName: this.$route.params.facilityName
    };
  },
  components: {
    Timetable
  }
};
</script>
