<template
  ><div class="small">
    <line-chart v-if="startDate" :chart-data="datacollection"></line-chart>
    <button @click="fillData()"></button>
    <div class="input-group">
      <v-date-picker @change="fillData" v-model="startDate"></v-date-picker>
    </div>
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

const groupBy = (xs, key) => {
  return xs.reduce(function(rv, x) {
    (rv[x[key]] = rv[x[key]] || []).push(x);
    return rv;
  }, {});
};

export default {
  components: {
    LineChart
  },
  data() {
    return {
      datacollection: {},
      startDate: null
    };
  },
  computed: {
    ...mapGetters("timetable", ["sessions"])
  },
  methods: {
    ...mapActions("timetable", {
      getActivity: "getAllSessions"
    }),
    async fillData() {
      const endDate = this.$moment(this.startDate);
      const startDate = endDate.clone().subtract("days", 6);
      const data = {};
      const response = await this.getActivity();
      const thisWeek = response
        .map(session => {
          const startTimestamp = this.$moment(session.startTime);

          return {
            ...session,
            startTimestamp,
            dayOfYear: startTimestamp.dayOfYear()
          };
        })
        .filter(session => {
          return (
            session.startTimestamp.isAfter(startDate) &&
            session.startTimestamp.isBefore(endDate)
          );
        });
      const grouped = groupBy(thisWeek, "dayOfYear");
      let day0 = startDate.dayOfYear();
      const weeklyUsage = [];
      const dates = [
        startDate,
        this.$moment().dayOfYear(day0 + 1),
        this.$moment().dayOfYear(day0 + 2),
        this.$moment().dayOfYear(day0 + 3),
        this.$moment().dayOfYear(day0 + 4),
        this.$moment().dayOfYear(day0 + 5),
        this.$moment().dayOfYear(day0 + 6)
      ];

      for (const date of dates) {
        const day = date.dayOfYear();
        let y = grouped[day];
        if (!y) {
          y = [];
        }
        weeklyUsage.push(y.length);
      }

      data.labels = dates.map(date => date.format("DD-MMM"));
      data.datasets = [
        {
          label: "Weekly Usage",
          data: weeklyUsage
        }
      ];
      this.datacollection = data;
      console.log(thisWeek);
    }
  }
};
</script>
