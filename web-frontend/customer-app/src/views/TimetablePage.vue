<template>
  <div class="row">
    <div class="col" style="margin: 24px">
      <div class="timetable">
        <h1>Timetable</h1>
        <Timetable></Timetable>
      </div>
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
