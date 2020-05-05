<template>
  <v-app class="usage-container" align="center" justify="center">
    <v-row class="inner-container" align="center" justify="flex-start">
      <v-card xs="12" align="center" justify="center" class="usage-contents">
        <v-container class="date">
          <v-dialog ref="dialog" v-model="modal" persistent width="290px" dark>
            <template v-slot:activator="{ on }">
              <v-subheader class="yellow--text title font-weight-bold"
                >WEEK</v-subheader
              >
              <v-text-field
                dark
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
        <v-container class="data-table-container">
          <v-data-table
            class="white--text"
            dark
            :headers="headers"
            :items="dataWithFacilities"
            item-key="id"
            group-by="facility.name"
          >
            <template
              class="white"
              v-slot:group.header="{ items, isOpen, toggle }"
            >
              <th colspan="4" bgcolor="#404040" class="white--text">
                <v-icon class="yellow--text" @click="toggle"
                  >{{ isOpen ? "mdi-minus" : "mdi-plus" }}
                </v-icon>
                {{ items[0].facility.name }}
              </th>
            </template>
          </v-data-table>
        </v-container>
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
.data-table-container {
  justify-content: center;
  text-align: center;
  display: table;
  flex-direction: column;
  flex-basis: auto; /* default value */
  flex-grow: 5;
  height: auto;
  width: 100%;
}
.data-table-container th {
  text-transform: uppercase;
}
.v-text-field .v-label {
  font-size: 20em;
}
.usage-contents {
  padding: 15px 0 15px 0;
  margin: 0;
  width: 90%;
  background-color: #1e1e1e;
  min-width: 50%;
}
.usage-contents span {
  background: #fcff18;
}
.usage-contents h3 {
  padding-bottom: 15px;
}
</style>

<script>
import { mapActions, mapGetters } from "vuex";
import { formatCurrency } from "../util/format.helpers";


export default {
  name: "BookingTable",
  components: {},
  // ItemsPerPageDropdown
  data: function() {
    return {
      booking: [],
      startDate: null,
      endDate: null,
      menu: false,
      modal: false,
      headers: [
        {
          text: "FACILITY",
          value: "name",
          class: "yellow--text title font-weight-bold"
        },
        {
          text: "COST",
          value: "formattedCost",
          class: "yellow--text title font-weight-bold"
        },
        {
          text: "INCOME",
          value: "formattedIncome",
          class: "yellow--text title font-weight-bold"
        }
      ],
      dataWithFacilities: [],
      bookingWithActivity: [],
      bookingData: [],
      weekData: []
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"]),
    ...mapGetters("facilities", ["facilities"]),
    ...mapGetters("timetable", ["sessions"]),
    ...mapGetters("timetable", ["bookings"]),
    ...mapGetters("timetable", ["resources"]),
    calculateDateRange() {
      const dateString =
        this.formatDate(this.startDate) + " - " + this.formatDate(this.endDate);
      return dateString;
    }
  },
  methods: {
    ...mapActions("facilities", {
      getActivityTypes: "getActivityTypes",
      getFacilities: "getFacilities",
      getActivities: "getActivities"
    }),
    ...mapActions("timetable", {
      getSessions: "getAllSessions",
      getBookings: "getBookings",
      getResources: "getResources"
    }),
    async fillData() {
      const endDate = this.$moment(this.startDate);
      const startDate = endDate.clone().subtract("days", 6);
      const response = await this.getActivities();
      const thisWeek = response
        .map(activity => {
          const startTimestamp = this.$moment(activity.startTime);

          return {
            ...activity,
            startTimestamp,
            dayOfYear: startTimestamp.dayOfYear()
          };
        })
        .filter(activity => {
          return (
            activity.startTimestamp.isAfter(startDate) &&
            activity.startTimestamp.isBefore(endDate)
          );
        });
      console.log(thisWeek);
      this.weekData = thisWeek;
      this.bookingData = await this.getNumberOfBookings();
    },








    formatDate(stringDate) {
      var date = new Date(stringDate);
      return (
        date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear()
      );
    },
    async defaultStartDate() {
      const endDate = this.$moment(new Date());
      const startDate = endDate.clone().subtract("days", 6);
      this.startDate = startDate.toJSON();
    },
    async fillData() {
      const startDate = this.$moment(this.startDate);
      const endDate = startDate.clone().add("days", 6);
      this.endDate = endDate.toJSON();
    },
    async getRelatedFacility() {
      let facilityArr = [];
      for (const activity of this.activities) {
        // console.log(activity);
        const facilityId = activity.facility_id;
        const facilities = this.facilities;
        activity.facility = facilities.find(
          facility => Number(facility.id) === Number(facilityId)
        );
        facilityArr.push(activity);
      }
      return facilityArr;
    },
    async getRelatedbookingsActivity() {
      let ActivityArr = [];
      for (const booking of this.bookings) {
        const ActivityId = booking._links.Activity.href.split("/").slice(-1)[0];
        const activities = this.activities;
        booking.activity = activities.find(
          activity => Number(activity.id) === Number(ActivityId)
        );
        ActivityArr.push(booking);
      }
      //console.log(ActivityArr);
      return ActivityArr;
    },
    async getNumberOfBookings() {
      let Arr = [];
      const activityTypes = await this.getActivityTypes();
      for(const activity of activityTypes){
        activity.income = 0;
      }
      for (const activity of activityTypes) {
        for (const booking of this.weekData) {
          //console.log(activity);
          if (booking.activityTypeId === activity.id) {
            activity.income = activity.income + activity.cost;
          }
        }
        Arr.push(activity);
      }
      for (const activity of Arr) {
        activity.formattedIncome = formatCurrency(activity.income);
      }
      console.log(Arr);
    }
  },
  created: async function() {
    await this.getActivityTypes();
    await this.defaultStartDate();
    await this.fillData();
    await this.getActivity();
    await this.getFacilities();
    await this.getBookings();
    console.log(this.bookings);
    await this.getResources();

    this.bookingWithActivity = await this.getRelatedbookingsActivity();
    this.dataWithFacilities = await this.getRelatedFacility();
    this.bookingData = await this.getNumberOfBookings();
    this.dataWithFacilities = await this.getRelatedFacility();
    console.log(this.dataWithFacilities);
  }
};
</script>
