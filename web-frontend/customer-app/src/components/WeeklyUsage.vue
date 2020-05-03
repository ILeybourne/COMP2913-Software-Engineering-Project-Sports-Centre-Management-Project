<template>
  <v-app class="usage-container" align="center" justify="center">
    <v-row class="inner-container" align="center" justify="center">
      <v-col xs="12" align="center" justify="center">
        <v-container class="date">
          <v-dialog ref="dialog" v-model="modal" persistent width="290px" dark>
            <template v-slot:activator="{ on }">
              <v-text-field
                v-model="calculateDateRange"
                label="Date Range"
                prepend-icon="mdi-calendar-today"
                readonly
                v-on="on"
                color="yellow"
                hint="Select a Week Commencing Date"
                persistent-hint
                outlined
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
              <v-btn text color="white" @click="modal = false">Close</v-btn>
            </v-date-picker>
          </v-dialog>
        </v-container>
        <v-col>
          <v-container class="data-table-container">
            <v-data-table
              :headers="headers"
              :items="dataWithFacilities"
              item-key="id"
              group-by="facility.name"
              dark
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
        </v-col>
      </v-col>
    </v-row>
  </v-app>
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
  padding: 20px;
  min-height: 50%;
  height: min-content;
  margin: 20px;
  width: 75%;
}
.data-table-container {
  justify-content: center;
  text-align: center;
  display: flex;
  flex-direction: column;
  flex-basis: auto; /* default value */
  flex-grow: 5;
  height: auto;
  width: 100%;
  flex-grow: 1;
  min-height: 0;
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
      menu: false,
      modal: false,
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
    ...mapGetters("timetable", ["resources"]),
    calculateDateRange() {
      const dateString =
        this.formatDate(this.startDate) + "-" + this.formatDate(this.endDate);
      return dateString;
    }
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
