<template
  ><div class="small">
    <line-chart :chart-data="datacollection"></line-chart>
    <button @click="fillData()">Randomize</button>
  </div>
</template>

<style scoped>
.small {
  max-width: 600px;
  margin: 150px auto;
}
</style>
<script>
import { mapActions, mapGetters } from "vuex";
import LineChart from "./LineChart.js";

export default {
  components: {
    LineChart
  },
  data() {
    return {
      datacollection: null
    };
  },

  computed: {
    ...mapGetters("timetable", ["sessions"])
  },
  methods: {
    ...mapActions("timetable", {
      getActivity: "getAllSessions"
    }),
    fillData() {
      this.datacollection = {
        labels: [
          "Monday",
          "Tuesday",
          "Wednesday",
          "Thursday",
          "Friday",
          "Saturday",
          "Sunday"
        ],
        datasets: [
          {
            label: "Data One",
            backgroundColor: "#17a2b8",
            data: [
              this.getRandomInt(),
              this.getRandomInt(),
              this.getRandomInt(),
              this.getRandomInt(),
              this.getRandomInt(),
              this.getRandomInt(),
              this.getRandomInt()
            ]
          }
        ]
      };
    },
    getRandomInt() {
      return Math.floor(Math.random() * (50 - 5 + 1)) + 5;
    }
  },
  async mounted() {
    this.fillData();
    await this.getActivity();
  }
};
</script>
