<template>
  <div class="activitiestable">
    <div class="heading-div">
      <h1><span>Activity</span> Management</h1>
    </div>
    <ActivitiesTable></ActivitiesTable>
  </div>
</template>
<style scoped>
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
import ActivitiesTable from "@/components/ActivitiesTable.vue";

// @ is an alias to /src
export default {
  name: "ActivitiesTablePage",
  components: {
    ActivitiesTable
  }
};
</script>
