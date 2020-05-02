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
    <button @click="fillData()">Randomize</button>
    <div class="input-group">
      <label>Start Date</label>
      <v-date-picker @change="fillData" v-model="startDate"></v-date-picker>
    </div>
  </div>
</template>

<style scoped></style>

<script>
import { mapActions, mapGetters } from "vuex";
/*
const groupBy = (xs, key) => {
  return xs.reduce(function(rv, x) {
    (rv[x[key]] = rv[x[key]] || []).push(x);
    return rv;
  }, {});
};

 */

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
          value: "income"
        }
      ],
      dataWithFacilities: [],
      bookingWithActivity: [],
      bookingData:[]
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
      getActivity: "getActivityTypes",
      getFacilities: "getFacilities"
    }),
    ...mapActions("timetable", {
      getSessions: "getAllSessions",
      getBookings: "getBookings",
      getResources: "getResources"
    }),
    async fillData() {},
    async getRelatedFacility() {
      let facilityArr = [];
      for (const activity of this.activities) {
        // console.log(activity);
        const facilityId = activity._links.resource.href
          .split("/")
          .slice(-1)[0];
        const facilities = this.facilities;
        /*
        console.log(
          facilities.find(
            facility => Number(facility.id) === Number(facilityId)
          )
        );
        */
        activity.facility = facilities.find(
          facility => Number(facility.id) === Number(facilityId)
        );
        facilityArr.push(activity);
      }
      return facilityArr;
    },
    async getRelatedBookingActivity() {
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
      let income = 0;
      for (const booking of this.bookingWithActivity) {
        for (const activity of this.activities) {
          if (booking.activity.name === activity.name) {
            income = activity.income + activity.cost;
          }
          activity.income = income;
        }
        Arr.push(booking);
      }
      console.log(Arr);
    }
  },
  created: async function() {
    await this.getActivity();
    await this.getFacilities();
    await this.getBookings();
    await this.getResources();

    this.bookingWithActivity = await this.getRelatedBookingActivity();
    this.dataWithFacilities = await this.getRelatedFacility();
    this.bookingData = await this.getNumberOfBookings();
    this.dataWithFacilities = await this.getRelatedFacility();
    console.log(this.dataWithFacilities);
  }
};
</script>
