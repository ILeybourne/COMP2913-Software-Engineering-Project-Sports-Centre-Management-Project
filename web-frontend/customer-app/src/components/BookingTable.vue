<template>
  <v-app class="app">
    <v-card dark>
      <v-container class="new-booking">
        <button
          type="submit"
          value="submit"
          class="site-btn"
          v-on:click="directToBookingPage()"
        >
          Place New Booking
        </button>
      </v-container>
      <v-data-table
        dense
        :headers="headers"
        :items="dataWithActivities"
        class="white--text"
        dark
      >
        <template v-slot:item.actions="{ item }">
          <v-icon @click="showDelete(item)">
            mdi-delete
          </v-icon>
          <v-icon @click="showReceipt(item)">
            mdi-printer
          </v-icon>
          <v-icon @click="emailReceipt(item)">
            mdi-email
          </v-icon>
          <v-icon
            v-if="item.regularBooking === true"
            color="red"
            @click="stopRegularSessionPayments(item)"
          >
            mdi-stop
          </v-icon>
        </template>
      </v-data-table>
    </v-card>
  </v-app>
</template>
<style scoped>
.app {
  height: min-content;
}
</style>
<script>
import { mapActions, mapGetters } from "vuex";

export default {
  name: "BookingTable",
  components: {},
  // ItemsPerPageDropdown
  data: function() {
    return {
      booking: [],
      singleSelect: false,
      selected: [],
      headers: [
        {
          class: "yellow--text heading font-weight-bold",
          value: "id",
          text: "BOOKING #",
          sortable: true
        },
        {
          value: "customer_id",
          text: "CUSTOMER #",
          sortable: true,
          class: "yellow--text heading font-weight-bold"
        },
        {
          value: "activity.resource.name",
          text: "FACILITY",
          sortable: true,
          class: "yellow--text heading font-weight-bold"
        },
        {
          value: "activity.name",
          text: "ACTIVITY",
          sortable: true,
          class: "yellow--text heading font-weight-bold"
        },
        {
          value: "activity.formattedDate",
          text: "DATE",
          sortable: true,
          class: "yellow--text heading font-weight-bold"
        },
        {
          value: "activity.slot",
          text: "TIME SLOT",
          sortable: true,
          class: "yellow--text heading font-weight-bold"
        },
        {
          value: "participants",
          text: "PARTICIPANTS",
          sortable: true,
          class: "yellow--text heading font-weight-bold"
        },
        {
          value: "amount",
          text: "CHARGE",
          sortable: true,
          class: "yellow--text heading font-weight-bold"
        },
        {
          value: "actions",
          text: "ACTIONS",
          sortable: false,
          class: "yellow--text heading font-weight-bold"
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
    ...mapGetters("timetable", ["sessions"]),
    ...mapGetters("timetable", {
      bookings: "bookings",
      bookingsLoading: "bookingsLoading",
      paging: "paging"
    })
  },
  methods: {
    ...mapActions("timetable", {
      getSessions: "getAllSessions",
      getBookings: "getBookings",
      deleteBooking: "deleteBooking",
      stopRegularSession: "stopRegularSession"
    }),
    async showDelete(item) {
      const index = this.dataWithActivities.indexOf(item);
      if (confirm("Are you sure you want to cancel this booking?") && this.dataWithActivities.splice(index,1)) {
        this.deleteBooking(item.id);
      }
      await this.collectData();
    },

    async directToBookingPage() {
      await this.$router.push({
        name: "BookingPage"
      });
    },
    async stopRegularSessionPayments(booking) {
      let body = {
        bookingId: booking.id,
        accountId: booking.accountId,
        activityId: booking.activity.id
      };
      await this.stopRegularSession(body);
      await this.collectData();
    },
    // emailReceipt(item){}
    // showReceipt(item){}

    async collectData() {
      await this.getBookings();
      console.log("collectData");
      //await this.getSessions();
      //await this.getBookings();
      for (const booking of this.bookings) {
        for (const session of this.sessions) {
          if (booking.session_id === session.id) {
            //booking.activity = session;
            booking.activity.formattedDate = session.formattedDate;
            booking.activity.slot = session.slot;
          }
        }
      }
      console.log(this.bookings);
      this.dataWithActivities = this.bookings;
    }
  },
  async mounted() {
    await this.getSessions();
    await this.getBookings();
    await this.collectData();
  }
};
</script>
