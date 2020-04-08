<template>
  <div class="weeklyusagegraph">
    <h1>Weekly Usage</h1>
    <WeeklyUsageGraph></WeeklyUsageGraph>
  </div>
</template>

<script>
import WeeklyUsageGraph from "@/components/WeeklyUsageGraph.vue";

// @ is an alias to /src
export default {
  name: "WeeklyUsagePage",
  components: {
    WeeklyUsageGraph
  }
};
</script>
