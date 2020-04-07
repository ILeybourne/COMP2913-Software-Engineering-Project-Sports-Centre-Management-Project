<template>
  <v-data-table
          :headers="headers"
          :items="sessions"
  >
    <template v-slot:top>
      <v-toolbar flat color="white">
        <v-spacer></v-spacer>
        <v-dialog  max-width="500px">
          <template v-slot:activator="{ on }">
            <v-btn color="primary" dark class="mb-2" v-on="on">New Item</v-btn>
          </template>
        </v-dialog>
      </v-toolbar>
    </template>
    <template v-slot:item.actions="{ item }">
      <v-icon small class="mr-2" @click="editItem(item)">
        mdi-pencil
      </v-icon>
      <v-icon small @click="deleteItem(item)">
        mdi-delete
      </v-icon>
      <v-icon small class="mr-2" @click="printItem(item)">
        midi-print
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
      ]
    };
  },
  computed: {
    ...mapGetters("timetable", ["sessions"])
  },
  methods: {
    dtEditClick: props => alert("Click props:" + JSON.stringify(props)),
    ...mapActions("timetable", {
      getActivity: "getAllSessions"
    }),
    showCancel() {
      this.$bvModal.show("edit-booking-modal");
    },
    editItem(item) {
      this.editedIndex = this.sessions.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },
    deleteItem(item) {
      const index = this.sessions.indexOf(item);
      confirm("Are you sure you want to delete this item?") &&
        this.sessions.splice(index, 1);
    }
  },

  async mounted() {
    await this.getActivity();
  }
};
</script>
