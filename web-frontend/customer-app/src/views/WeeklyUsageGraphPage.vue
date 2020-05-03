<template>
  <div class="weeklyusagegraph">
    <div class="heading-div">
      <h1>Weekly <span>Usage</span></h1>
    </div>
    <v-row align="center" justify="center" class="info-container">
      <WeeklyUsageGraph></WeeklyUsageGraph>
    </v-row>
  </div>
</template>
<style scoped>
.info-container {
  display: flex;
  justify-content: center;
  text-align: center;
  flex-direction: column;
  width: 100%;
  height: auto;
}
@media screen and (max-width: 600px) {
  .heading-div h1 {
    font-size: 10vw;
  }
}
.heading-div {
  margin-bottom: 20px;
}
.heading-div h1 {
  width: 50%;
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
import WeeklyUsageGraph from "@/components/WeeklyUsageGraph.vue";

// @ is an alias to /src
export default {
  name: "WeeklyUsageGraphPage",
  components: {
    WeeklyUsageGraph
  }
};
</script>
