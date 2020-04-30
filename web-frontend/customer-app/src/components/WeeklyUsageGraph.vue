<template>
  <div class="usage-container">
    <div class="inner-container">
      <v-date-picker
        class="child-container"
        @change="fillData"
        v-model="startDate"
      ></v-date-picker>
      <line-chart class="chart" :chart-data="datacollection"></line-chart>
    </div>
  </div>
</template>

<style scoped>
.usage-container {
  padding: 59px 0px 59px 0px;
  min-height: 50%;
  height: auto;
  display: flex;
}
.inner-container {
  /*margin: auto;*/
  /*width: 50%;*/
  padding: 10px;
  min-height: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.child-container {
  margin: 20px;
  width: 40%;
  min-height: auto;
  background: #f6f9fa;
  color: #353535;
  display: flex;
  flex-direction: column;
  min-width: 150px;
  max-width: 400px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  padding: 30px 0 30px 0;
}
.chart {
  text-align: center;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 500px;
  min-height: 500px;
  flex-basis: auto; /* default value */
  flex-grow: 10;
  padding: 10px;
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
          data: weeklyUsage,
          backgroundColor: '#f87979',
          pointBackgroundColor: 'white',
          borderWidth: 5,
          pointBorderColor: '#249EBF',
          fill: false
        },
      ];
      this.datacollection = data;
    }
  }
};
</script>
