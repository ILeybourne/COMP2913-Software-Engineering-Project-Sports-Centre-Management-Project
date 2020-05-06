<template>
  <div>
    <div class="timetable">
      <div class="heading-div">
        <h1>Activity <span>Timetable</span></h1>
      </div>
      <Timetable></Timetable>
    </div>
  </div>
</template>
<style scope>
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
import Timetable from "@/components/Timetable";

export default {
  name: "TimetablePage",
  components: {
    Timetable
  }
};
</script>
