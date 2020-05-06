<template>
  <div class="small">
    <v-data-table
      v-if="startDate"
      :headers="headers"
      :items="dataWithFacilities"
      group-by="facility.name"
      class="elevation-1"
    >
    </v-data-table>
    <button @click="fillData()"></button>
    <div class="input-group">
      <label></label>
      <v-date-picker @change="fillData" v-model="startDate"></v-date-picker>
    </div>
  </div>
</template>

<style scoped></style>

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
      headers: [
        {
          text: "Resource",
          value: "name"
        },
        {
          text: "Cost",
          value: "formattedCost"
        },
        {
          text: "Income",
          value: "formattedIncome"
        }
      ],
      dataWithFacilities: [],
      bookingWithActivity: [],
      bookingData: [],
      weekData: [],
      tableData: []
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"]),
    ...mapGetters("facilities", ["facilities"]),
    ...mapGetters("timetable", ["sessions"]),
    ...mapGetters("timetable", ["bookings"]),
    ...mapGetters("timetable", ["resources"])
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
      //console.log(thisWeek);
      this.weekData = thisWeek;
      this.bookingData = this.getNumberOfBookings();
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
