<template>
  <div class="usage-container">
    <div class="inner-container">
      <v-container class="date-picker-container">
        <h2>Console</h2>
        <p>
          <b>From {{ formatDate(startDate) }} to {{ formatDate(endDate) }}</b>
        </p>
        <v-date-picker
          dark
          no-title
          class="date-picker"
          @change="fillData"
          v-model="startDate"
        >
        </v-date-picker>
      </v-container>

      <v-container class="data-table-container">
        <v-data-table
          :headers="headers"
          :items="dataWithFacilities"
          item-key="id"
          group-by="facility.name"
        >
          <template v-slot:group.header="{ items, isOpen, toggle }">
            <th colspan="3">
              <v-icon @click="toggle"
                >{{ isOpen ? "mdi-minus" : "mdi-plus" }}
              </v-icon>
              {{ items[0].facility.name }}
            </th>
          </template>
        </v-data-table>
      </v-container>
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
  max-width: min-content;
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
.date-picker-container p {
  color: white;
}
.data-table-container {
  justify-content: center;
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
      endDate: null,
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
          value: " "
        }
      ],
      dataWithFacilities: [],
      bookingWithActivity: [],
      bookingData: []
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
    async fillData() {
      const startDate = this.$moment(this.startDate);
      const endDate = startDate.clone().add("days", 6);
      this.endDate = endDate.toJSON();
    },
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
        for (const activity of activities) {
          console.log(activity.id + " " + Number(ActivityId));
        }
        booking.activity = activities.find(
          activity => Number(activity.id) === Number(ActivityId)
        );
        ActivityArr.push(booking);
      }
      console.log("Activity Array 194 " + ActivityArr);
      return ActivityArr;
    },
    async defaultStartDate() {
      const endDate = this.$moment(new Date());
      const startDate = endDate.clone().subtract("days", 6);
      this.startDate = startDate.toJSON();
    },
    formatDate(stringDate) {
      var date = new Date(stringDate);
      return (
        date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear()
      );
    },
    async getNumberOfBookings() {
      //let Arr = [];
      for (const booking of this.bookingWithActivity) {
        const resourceID = booking.id;
        console.log(resourceID);
      }
    }
  },
  created: async function() {
    await this.getActivity();
    await this.getFacilities();
    await this.getBookings();
    console.log(this.getBookings());
    await this.getResources();
    //console.log(this.resources);
    //console.log(this.bookings);
    this.bookingWithActivity = await this.getRelatedBookingActivity();
    console.log(this.bookingWithActivity);
    this.dataWithFacilities = await this.getRelatedFacility();
    this.bookingData = await this.getNumberOfBookings();
    await this.defaultStartDate();
    await this.fillData();
    console.log(this.dataWithFacilities);
  }
};
</script>
