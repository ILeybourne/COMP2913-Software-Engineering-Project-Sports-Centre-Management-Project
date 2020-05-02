<template>
  <div class="usage-container">
    <div class="inner-container">
      <v-container class="date-picker-container">
        <h2>Console</h2>
        <v-date-picker
          dark
          no-title
          class="date-picker"
          @change="fillData"
          v-model="startDate"
        >
        </v-date-picker>
        <p>
          <b>Activities {{ formatDate(startDate) }} to {{ formatDate(endDate) }}</b>
        </p>
      </v-container>
      <line-chart class="chart" :chart-data="datacollection"></line-chart>
    </div>
  </div>
</template>

<style scoped>
.usage-container {
  min-height: 50%;
  height: auto;
  display: flex;
  width: 100%;
}
.inner-container {
  /*margin: auto;*/
  /*width: 50%;*/
  padding: 10px;
  min-height: 50%;
  display: flex;
  justify-content: space-between;
  height: auto;
  margin: 20px;
  width: 100%;
}
.date-picker-container {
  text-align: center;
  width: min-content;
  min-width: min-content;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  min-height: 500px;
  flex-basis: auto; /* default value */
  flex-grow: 10;
  padding: 10px;
  border: 5px solid #353535;
  border-radius: 10px;
  background-color: #353535;
}
.date-picker-container h2 {
  text-align: center;
  margin-bottom: 40px;
  color: white;
}
.date-picker {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-bottom: 40px;
}
.date-picker-container p{
  color: white;
}
.chart {
  justify-content: flex-end;
  text-align: center;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  min-width: 500px;
  min-height: 500px;
  flex-basis: auto; /* default value */
  flex-grow: 10;
  padding: 10px;
  padding-left: 50px !important;
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
      startDate: null,
      endDate: null
    };
  },
  computed: {
    ...mapGetters("timetable", ["sessions"])
  },
  methods: {
    ...mapActions("timetable", {
      getActivity: "getAllSessions"
    }),
    //So data fills table from the previous week- todays date
    async defaultStartDate() {
      const endDate = this.$moment(new Date());
      this.startDate = endDate.clone().subtract("days", 6);
      this.startDate = this.startDate.toJSON();
    },
    formatDate(stringDate) {
      var date = new Date(stringDate);
      return (
        date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear()
      );
    },
    async fillData() {
      const startDate = this.$moment(this.startDate);
      const endDate = startDate.clone().add("days", 6);
      this.endDate = endDate.toJSON();
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
          backgroundColor: "yellow",
          pointBackgroundColor: "#353535",
          borderWidth: 5,
          pointBorderColor: "#353535",
          fill: true
        }
      ];
      this.datacollection = data;
    }
  },
  async mounted() {
    await this.defaultStartDate();
    await this.fillData();
  }
};
</script>
