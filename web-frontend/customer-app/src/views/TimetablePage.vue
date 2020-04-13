<template>
  <div id="timetable-container" class="container-flex">
    <div class="timetable">
      <h1>Timetable</h1>
      <Timetable></Timetable>
    </div>
  </div>
</template>

<script>
import Timetable from "../components/Timetable";

export default {
  name: "TimetablePage",
  components: {
    Timetable
  }
};
</script>

<style>
#timetable-container {
  margin: 3%;
}
</style>
