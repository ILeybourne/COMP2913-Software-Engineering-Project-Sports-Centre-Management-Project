<template>
  <div>
    <v-text-field
      v-model="query"
      append-icon="mdi-magnify"
      label="Search"
      single-line
      hide-details
    ></v-text-field>              <v-btn @click="search">search</v-btn>

    <v-data-table :headers="headers" :items="dataWithActivities">
      <template v-slot:item.actions="{ item }">
        <v-icon small @click="showDelete(item)">
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>
  </div>
</template>

<style scoped></style>

<script>
import { mapActions, mapGetters } from "vuex";

export default {
  name: "BookingTable",
  components: {},
  // ItemsPerPageDropdown
  data: function() {
    return {
      /*
      account: null
      activity: null
      createdAt: 1584288689000
      id: 1
      receipt: null
      updatedAt: 1584288692000
      * */
      booking: [],
      singleSelect: false,
      selected: [],
      headers: [
        {
          value: "id",
          text: "Booking Reference",
          sortable: true
        },
        {
          value: "customer_id",
          text: "Customer Id",
          sortable: false
        },
        {
          value: "activity.formattedStartAt",
          text: "Booking Time",
          sortable: true
        },
        {
          value: "activity.name",
          text: "Session",
          sortable: true
        },
        {
          value: "activity.resource.name",
          text: "Facility",
          sortable: true
        },
        {
          value: "regularBooking",
          text: "Subscribed",
          sortable: true
        },
        {
          value: "actions",
          text: "Actions",
          sortable: false
        }
      ],
      selectedBooking: {
        id: null,
        name: null,
        facility: null,
        startTime: null,
        endTime: null
      },
      newBooking: {
        id: null,
        name: null,
        facility: null,
        startTime: null,
        endTime: null
      },
      dataWithActivities: [],
      email: null,
      query: null
    };
  },
  computed: {
    ...mapGetters("timetable", ["bookings"]),
    ...mapGetters("timetable", ["sessions"]),
    ...mapGetters("facilities", ["activities"]),
    ...mapGetters("facilities", ["facilities"]),
    ...mapGetters("auth", ["isAuthenticated", "user", "isEmployeeOrManager"])
  },
  methods: {
    dtEditClick: props => alert("Click props:" + JSON.stringify(props)),
    ...mapActions("timetable", {
      getBooking: "getBookings",
      deleteBooking: "deleteBooking",
      getBookingByEmail: "getBookingByEmail",
      getSessions: "getAllSessions"
    }),
    ...mapActions("facilities", {
      getActivity: "getActivities",
      getFacilities: "getFacilities"
    }),
    showNewBooking() {
      this.$router.push("/bookings");
    },
    editItem(item) {
      console.log(item);
      this.selectedBooking.id = item.id;
      this.selectedBooking.name = item.activity.name;
      this.selectedBooking.facility = item.activity.resource.name;
      this.selectedBooking.startTime = item.activity.startTime;
      this.selectedBooking.endTime = item.activity.endTime;
      console.log(this.selectedBooking);
      this.$bvModal.show("edit-modal");
    },
    showDelete(item) {
      console.log(item);
      const id = item.id;
      const index = this.dataWithActivities.indexOf(item);
      confirm("Are you sure you want to delete this item?") &&
        this.bookings.splice(index, 1) &&
        this.deleteBooking(id);
    },
    setFacilityOptions() {
      let facilities = this.facilities;
      let facilityArray = [{ value: null, text: "Please Select" }];
      for (const facility of facilities) {
        facilityArray.push({ value: facility.name, text: facility.name });
      }
      return facilityArray;
    },
    async getRelatedActivity() {
      let ActivityArr = [];
      for (const booking of this.bookings) {
        console.log("sessions");
        booking.activity = this.sessions.find(
          activity => Number(activity.id) === Number(booking.session_id)
        );
        ActivityArr.push(booking);
      }
      console.log("Activity Arr");
      console.log(ActivityArr);
      return ActivityArr;
    },
    updateTable() {
      const id = this.selectedBooking.id;
      this.dataWithActivities.find(booking => booking.id === id);
      console.log("updateBooking");
    },
    addBooking() {
      console.log("addBooking");
    },
    search() {
      console.log("query");
      let query = this.query;
      let filteredBookings = [];
      for (const booking of this.dataWithActivities) {
        console.log("data");
        console.log(booking);
        if (booking.accountId === query){
          console.log("found booking");
          console.log(booking)
          filteredBookings.push(booking)
        }
      }
      console.log("filtered")
      console.log(filteredBookings)
      this.dataWithActivities = filteredBookings;
    }
 },

  async mounted() {
    await this.getActivity();
    await this.getSessions();
  //  let body = {
  //    email: this.$auth.user.email,
  //    isAuthorised: this.isEmployeeOrManager
  //  };
    await this.getBooking();
    await this.getFacilities();
    //console.log(this.bookings);
    this.dataWithActivities = await this.getRelatedActivity();
    //console.log(this.dataWithActivities);
  }
};
</script>
