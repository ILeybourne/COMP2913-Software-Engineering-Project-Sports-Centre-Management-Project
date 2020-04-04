<template>
  <div id="app">
    <h3 class="title">Bookings Table</h3>
    <v-data-table
      v-model="selected"
      :headers="headers"
      :items="formattedData"
      :single-select="true"
      item-key="name"
      show-select
      class="elevation-1"
    >
      <template v-slot:top>
        <v-switch
          v-model="singleSelect"
          label="Single select"
          class="pa-3"
        ></v-switch>
      </template>
    </v-data-table>

    <b-modal id="edit-booking-modal" title="Create Activity" hide-footer>
      <div class="d-flex justify-content-between">
        <b-button
          type="reset"
          variant="danger"
          @click="$bvModal.hide('edit-booking-modal')"
          >Delete
        </b-button>
      </div>
    </b-modal>
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
          value: "formattedStartTime",
          text: "Booking Time",
          sortable: true
        },
        {
          value: "activity.name",
          text: "Activity",
          sortable: true
        },
        {
          value: "account",
          text: "Account",
          sortable: true
        },
        {
          value: "email",
          text: "Email",
          sortable: true
        },
        {
          value: "receipt",
          text: "Receipt",
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
    /*
    async getBooking() {
      const token = await this.$auth.getTokenSilently();

      const { data } = await this.$http.get(`/bookings`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      this.bookings = data.content;
    },
    */
    showCancel() {
      this.$bvModal.show("edit-booking-modal");
    }
  },

  async mounted() {
    await this.getBooking();
  }
};
</script>
