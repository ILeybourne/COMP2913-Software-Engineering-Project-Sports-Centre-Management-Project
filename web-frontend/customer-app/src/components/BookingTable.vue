<template>
  <v-data-table :headers="headers" :items="dataWithActivities">
    <template v-slot:top>
      <v-toolbar flat color="white">
        <v-spacer></v-spacer>
        <v-dialog max-width="500px"> </v-dialog>
      </v-toolbar>
      <v-btn color="primary" dark class="mb-2" @click="showNewBooking()"
      >New Booking</v-btn
      >
      <b-modal id="new-booking-modal" title="Edit Booking" @ok="addBooking()">
        <b-form>
          <b-form-group
                  id="resource.name"
                  label="Facility"
                  label-for="FacilityName"
          ><b-form-select
                  id="FacilityName"
                  :options="setFacilityOptions()"
                  v-model="selectedBooking.facility"
                  required
          ></b-form-select>
          </b-form-group>
          <b-form-group id="name" label="Booking" label-for="BookingName">
            <b-form-input
                    id="BookingName"
                    v-model="selectedBooking.name"
                    required
            ></b-form-input>
          </b-form-group>
          <b-form-group id="startTime" label="Start Time" label-for="StartTime">
            <b-form-input
                    id="StartTime"
                    v-model="selectedBooking.startTime"
                    required
            ></b-form-input>
          </b-form-group>
          <b-form-group id="endTime" label="End Time" label-for="EndTime">
            <b-form-input
                    id="EndTime"
                    v-model="selectedBooking.endTime"
                    required
            ></b-form-input>
          </b-form-group>
        </b-form>
      </b-modal>
      <b-modal id="edit-modal" title="Edit Booking" @ok="updateTable()">
        <b-form>
          <b-form-group
            id="resource.name"
            label="Facility"
            label-for="FacilityName"
            ><b-form-select
              id="FacilityName"
              :options="setFacilityOptions()"
              v-model="selectedBooking.facility"
              required
            ></b-form-select>
          </b-form-group>
          <b-form-group id="name" label="Booking" label-for="BookingName">
            <b-form-input
              id="BookingName"
              v-model="selectedBooking.name"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group id="startTime" label="Start Time" label-for="StartTime">
            <b-form-input
              id="StartTime"
              v-model="selectedBooking.startTime"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group id="endTime" label="End Time" label-for="EndTime">
            <b-form-input
              id="EndTime"
              v-model="selectedBooking.endTime"
              required
            ></b-form-input>
          </b-form-group>
        </b-form>
      </b-modal>
    </template>
    <template v-slot:item.actions="{ item }">
      <v-icon small class="mr-2" @click="editItem(item)">
        mdi-pencil
      </v-icon>
      <v-icon small @click="showDelete(item)">
        mdi-delete
      </v-icon>
    </template>
  </v-data-table>
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
          value: "activity.formattedStartAt",
          text: "Booking Time",
          sortable: true
        },
        {
          value: "activity.name",
          text: "Booking",
          sortable: true
        },
        {
          value: "activity.resource.name",
          text: "Facility",
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
      dataWithActivities: []
    };
  },
  computed: {
    ...mapGetters("timetable", ["bookings"]),
    ...mapGetters("facilities", ["activities"]),
    ...mapGetters("facilities", ["facilities"])
  },
  methods: {
    dtEditClick: props => alert("Click props:" + JSON.stringify(props)),
    ...mapActions("timetable", {
      getBooking: "getBookings",
      deleteBooking: "deleteActivities"
    }),
    ...mapActions("facilities", {
      getActivity: "getActivities",
      getFacilities: "getFacilities"
  }),
    showNewBooking() {
      this.$bvModal.show("new-booking-modal");
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
      const index = item.id;
      confirm("Are you sure you want to delete this item?") &&
        this.bookings.splice(index, 1) &&
        this.deleteBooking(index);
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
        const ActivityId = booking._links.Activity.href.split("/").slice(-1)[0];
        const activities = this.activities;
        //console.log(Number(booking.id) === Number(ActivityId));
        //console.log(Number(booking.id) + " " + Number(ActivityId));
        //console.log(activities);
        booking.activity = activities.find(
          activity => Number(activity.id) === Number(ActivityId)
        );
        ActivityArr.push(booking);
      }
      return ActivityArr;
    },
    updateTable() {
      const id = this.selectedBooking.id;
      this.dataWithActivities.find(booking => booking.id === id);
      console.log("updateBooking");
    },
    addBooking() {
      console.log("addBooking");
    }
  },

  async mounted() {
    await this.getActivity();
    //console.log(this.activities);
    await this.getBooking();
    await this.getFacilities();
    //console.log(this.bookings);
    this.dataWithActivities = await this.getRelatedActivity();
    //console.log(this.dataWithActivities);
  }
};
</script>
