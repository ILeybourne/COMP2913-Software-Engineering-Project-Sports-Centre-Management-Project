<template>
  <v-data-table :headers="headers" :items="sessions">
    <template v-slot:top>
      <v-toolbar flat color="white">
        <v-spacer></v-spacer>
        <v-dialog max-width="500px"> </v-dialog>
      </v-toolbar>
      <b-modal id="edit-modal" title="Edit Booking">
        <b-form>
          <b-form-group
            id="resource.name"
            label="Facility"
            label-for="FacilityName"
            ><b-form-input
              id="FacilityName"
              v-model="selectedBooking.facility"
              required
            ></b-form-input>
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
      bookings: [],
      singleSelect: false,
      selected: [],
      headers: [
        {
          value: "id",
          text: "Booking reference",
          sortable: true
        },
        {
          value: "formattedStartAt",
          text: "Booking Time",
          sortable: true
        },
        {
          value: "name",
          text: "Booking",
          sortable: true
        },
        {
          value: "resource.name",
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
        startDate: null,
        startTime: null,
        endDate:null,
        endTime: null,
        id: null,
        facility: null,
        name: null
      }
    };
  },
  computed: {
    ...mapGetters("timetable", ["sessions"])
  },
  methods: {
    dtEditClick: props => alert("Click props:" + JSON.stringify(props)),
    ...mapActions("timetable", {
      getActivity: "getAllSessions",
      deleteBooking: "deleteActivities"
    }),
    editItem(item) {
      const index = item.id;
      console.log(index);
      this.selectedBooking.id = index;
      this.selectedBooking.name = item.name;
      this.selectedBooking.facility = item.resource.name;
      this.selectedBooking.startTime = item.startTime;
      this.selectedBooking.endTime = item.endTime;
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
    showEdit() {
      this.$bvModal.show("create-activity-modal");
    }
  },

  async mounted() {
    await this.getActivity();
  }
};
</script>
