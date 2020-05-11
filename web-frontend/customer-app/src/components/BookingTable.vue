<template>
  <v-app class="usage-container">
    <v-layout child-flex>
      <v-card dark class="card">
        <v-container class="new-booking">
          <v-btn
            title="Create a new booking"
            class="black--text"
            color="yellow"
            type="submit"
            value="submit"
            v-on:click="directToBookingPage()"
          >
            <b>Place New Booking</b>
          </v-btn>
        </v-container>
        <v-data-table
          no-data-text="You do not have any bookings placed"
          must-sort
          dense
          :items-per-page="20"
          :headers="headers"
          :items="dataWithActivities"
          class="white--text"
          :footer-props="footerProps"
          dark
        >
          <template v-slot:item.actions="{ item }">
            <v-icon title="Cancel Booking" @click="showDelete(item)">
              mdi-delete
            </v-icon>
            <v-icon title="View Receipt" @click="showReceipt(item)">
              mdi-printer
            </v-icon>
            <v-icon title="Email Receipt" @click="emailReceipt(item)">
              mdi-email
            </v-icon>
            <v-icon
              title="Unsubscribe From Session"
              v-if="item.regularBooking === true"
              color="red"
              @click="stopRegularSessionPayments(item)"
            >
              mdi-stop
            </v-icon>
          </template>
        </v-data-table>
      </v-card>
    </v-layout>
  </v-app>
</template>
<style scoped>
.usage-container {
  min-height: 50%;
  height: auto;
  display: flex;
  width: 100%;
}
.new-booking {
  padding-bottom: 20px;
}
.card {
  margin-top: 50px;
}
</style>
<script>
import { mapActions, mapGetters } from "vuex";
export default {
  name: "BookingTable",
  components: {},
  totalBookings: null,
  data: function() {
    return {
      footerProps: {
        "items-per-page-options": [20, 40, 80, 100]
      },
      singleSelect: false,
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
          value: "formattedAmount",
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
      dataWithActivities: []
    };
  },
  computed: {
    ...mapGetters("timetable", ["sessions"]),
    ...mapGetters("timetable", ["totalElements"]),
    ...mapGetters("timetable", ["pages"]),
    ...mapGetters("timetable", {
      bookings: "bookings"
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
      if (
        confirm("Are you sure you want to cancel this booking?") &&
        this.dataWithActivities.splice(index, 1) &&
        this.deleteBooking(item.id) &&
        (await this.getBookings())
      );
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
      if (
        confirm("Are you sure you want to unsubscribe from this session?") &&
        (await this.stopRegularSession(body))
      );
      await this.collectData();
    },

    async collectData() {
      await this.getSessions();
      await this.getBookings();
      //Struggled to apply this data in the store...
      for (const booking of this.bookings) {
        for (const session of this.sessions) {
          if (booking.session_id === session.id) {
            booking.activity.formattedDate = session.formattedDate;
            booking.activity.slot = session.slot;
          }
        }
      }
      this.dataWithActivities = this.bookings;
    }
  },
  // emailReceipt(item){}dat
  // showReceipt(item){}

  watch: {
    handler() {
      this.dataWithActivities();
    },
    deep: true
  },

  async created() {
    await this.collectData();
  }
};
</script>
