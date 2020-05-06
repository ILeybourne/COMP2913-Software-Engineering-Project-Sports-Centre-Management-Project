<template>
  <v-app class="usage-container" align="center" justify="center">
    <v-row class="inner-container" align="center" justify="center">
      <v-card
        xs="12"
        align="center"
        justify="center"
        class="usage-contents"
        dark
      >
        <v-container class="date">
          <v-dialog ref="dialog" v-model="modal" persistent width="290px" dark>
            <template v-slot:activator="{ on }">
              <v-subheader
                id="header"
                class="yellow--text title font-weight-bold"
                >WEEK</v-subheader
              >
              <v-text-field
                class="yellow--text title"
                title="week"
                v-model="calculateDateRange"
                readonly
                v-on="on"
                color="yellow"
              ></v-text-field>
            </template>
            <v-date-picker
              dark
              v-model="startDate"
              type="date"
              no-title
              scrollable
              color="yellow"
              event-color="black"
              show-week
              @select="fillData()"
              @input="fillData()"
            >
              <v-spacer></v-spacer>
              <v-btn class="yellow--text" @click="modal = false">Close</v-btn>
            </v-date-picker>
          </v-dialog>
        </v-container>
        <v-col>
          <v-container class="chart-container">
            <line-chart :chart-data="datacollection"></line-chart> </v-container
        ></v-col>
      </v-card>
    </v-row>
  </v-app>
</template>

<style scoped>
.v-subheader {
  padding-bottom: 0;
  margin-bottom: 0;
}
.v-input {
  padding: 0;
  margin: 0;
}
.usage-container {
  min-height: 50%;
  height: auto;
  display: flex;
  width: 100%;
}
.inner-container {
  /*margin: auto;*/
  /*width: 50%;*/
  min-height: 50%;
  height: min-content;
  margin: 20px;
  width: 75%;
  display: flex;
  justify-content: flex-start;
  flex-direction: column;
}
.chart-container {
  justify-content: center;
  text-align: center;
  display: flex;
  flex-direction: column;
  flex-basis: auto; /* default value */
  flex-grow: 5;
  height: auto;
  width: 100%;
  min-height: 0;
}
.usage-contents {
  padding: 15px 0 15px 0;
  margin: 0;
  width: 90%;
  background-color: #1e1e1e;
}
.usage-contents span {
  background: #fcff18;
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
      endDate: null,
      menu: false,
      modal: false
    };
  },
  computed: {
    ...mapGetters("timetable", ["sessions"]),
    calculateDateRange() {
      const dateString =
        this.formatDate(this.startDate) + " - " + this.formatDate(this.endDate);
      return dateString;
    }
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
          pointBackgroundColor: "yellow",
          borderWidth: 3,
          pointBorderColor: "white",
          borderColor: "yellow",
          fill: false
        }
      ];
      this.datacollection = data;
      // eslint-disable-next-line no-undef
      Chart.defaults.global.defaultFontColor = "white";
      // eslint-disable-next-line no-undef
      Chart.defaults.global.defaultFontSize = 12;
      // eslint-disable-next-line no-undef
      Chart.defaults.global.defaultFontFamily = "'PT Sans', 'sans-serif'";
      // eslint-disable-next-line no-undef
      Chart.defaults.global.defaultFontStyle = "bold";
      // eslint-disable-next-line no-undef
      Chart.defaults.scale.ticks.stepSize = 5;
      // eslint-disable-next-line no-undef
      Chart.defaults.scale.ticks.min = 0;
      // eslint-disable-next-line no-undef
      Chart.defaults.scale.ticks.max = 30;
      console.log(thisWeek);
    }
  },
  async mounted() {
    await this.defaultStartDate();
    await this.fillData();
  }
};
</script>
