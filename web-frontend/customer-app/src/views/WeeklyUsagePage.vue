<template>
  <div class="weeklyusage">
    <div class="heading-div">
      <h1>Weekly <span>Usage</span></h1>
    </div>
    <WeeklyUsage></WeeklyUsage>
  </div>
</template>
<style scoped>
@media screen and (max-width: 600px) {
  .heading-div h1 {
    font-size: 10vw;
  }
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
import WeeklyUsage from "@/components/WeeklyUsage.vue";

// @ is an alias to /src
export default {
  name: "WeeklyUsagePage",
  components: {
    WeeklyUsage
  }
};
</script>
