<template>
  <div class="activitiestable">
    <h1></h1>
    <ActivitiesTable></ActivitiesTable>
  </div>
</template>

<script>
import ActivitiesTable from "@/components/ActivitiesTable.vue";

// @ is an alias to /src
export default {
  name: "ActivitiesTablePage",
  components: {
    ActivitiesTable
  }
};
</script>
