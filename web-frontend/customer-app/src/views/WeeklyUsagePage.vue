<template>
  <div class="weeklyusage">
    <h1>Weekly Usage</h1>
    <WeeklyUsage></WeeklyUsage>
  </div>
</template>

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
