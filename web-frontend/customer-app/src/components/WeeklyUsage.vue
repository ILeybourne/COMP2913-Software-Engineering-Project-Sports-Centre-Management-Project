<template>
  <div class="small">
    <v-data-table
      v-if="startDate"
      :headers="headers"
      :items="dataWithFacilities"
      item-key="id"
      group-by="facility.name"
    >
      <template v-slot:group.header="{items, isOpen, toggle}">
        <th colspan="3">
          <v-icon @click="toggle"
          >{{ isOpen ? 'mdi-minus' : 'mdi-plus' }}
          </v-icon>
          {{ items[0].facility.name }}
        </th>
      </template>
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
          text: "Facility",
          value: "name"
        },
        {
          text: "Cost",
          value: "formattedCost"
        },
        {
          text: "Income",
          value:" "
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
        console.log(
          facilities.find(
            facility => Number(facility.id) === Number(facilityId)
          )
        );
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
        //console.log(Number(booking.id) === Number(ActivityId));
        //console.log(Number(booking.id) + " " + Number(ActivityId));
        //console.log(activities);
        for (const activity of activities){
          console.log(activity.id + " " + Number(ActivityId));
        }
        booking.activity = activities.find(
                activity => Number(activity.id) === Number(ActivityId)
        );
        ActivityArr.push(booking);
      }
      return ActivityArr;
    },
    async getNumberOfBookings(){
      //let Arr = [];
      for (const booking of this.bookingWithActivity){
        const resourceID = booking.id;
        console.log(resourceID);
      }

    }
  },
  created: async function() {
    await this.getActivity();
    await this.getFacilities();
    await this.getBookings();
    await this.getResources();
    //console.log(this.resources);
    //console.log(this.bookings);
    this.bookingWithActivity = await this.getRelatedBookingActivity();
    console.log(this.bookingWithActivity);
    this.dataWithFacilities = await this.getRelatedFacility();
    this.bookingData = await this.getNumberOfBookings()
    console.log(this.dataWithFacilities);
  }
};
</script>
