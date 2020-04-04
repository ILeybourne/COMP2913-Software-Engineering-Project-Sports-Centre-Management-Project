<template>
  <div>
    <ul>
      <li v-for="(activity) in facility.activities" :key="activity.id">
        <p>{{ activity.name }} : {{ activity.cost }}</p>
      </li>
    </ul>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "CentrePriceListing",
  computed: {
    ...mapGetters("facilities", ["facilities"])
  }
}
</script>

<style scoped></style>