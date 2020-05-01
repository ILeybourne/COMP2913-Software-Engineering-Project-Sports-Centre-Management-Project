<template>
  <div>
    <table class="table">
      <tbody>
        <tr>
          <td>Session:</td>
          <td>{{ session.name }}</td>
        </tr>
        <tr>
          <td>Start Time:</td>
          <td>{{ session.formattedStartAt }}</td>
        </tr>
        <tr>
          <td>Current Capacity</td>
          <td>{{ session.currentCapacity || 0 }}</td>
        </tr>
        <tr v-if="session.totalCapacity">
          <td>Total Capacity</td>
          <td>{{ session.totalCapacity }}</td>
        </tr>
        <tr v-if="session.regularSession">
          <td>Is Regular Session</td>
          <td>{{ session.regularSession ? "Yes" : "No" }}</td>
        </tr>
        <tr v-if="session.interval && session.regularSession">
          <td>Interval (days)</td>
          <td>{{ session.interval }}</td>
        </tr>
        <tr>
          <td>Price</td>
          <td>{{ formatCurrency(session.cost) }}</td>
        </tr>
      </tbody>
    </table>
    <div class="alert alert-danger" v-if="error">{{ error }}</div>
    <b-button variant="primary" v-if="userCanBook" :to="bookingLink"
      >Book this Session</b-button
    >
    <b-button variant="danger" v-if="userCanDelete" @click="deleteASession">
      Delete Session
    </b-button>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import { formatCurrency } from "@/util/format.helpers";
export default {
  name: "SessionInfo",
  props: {
    session: {
      required: true,
      type: Object
    }
  },
  data() {
    return {
      error: null,
      bookingLink: {
        name: "BookingPage",
        query: {
          facilityId: this.session.resource.id,
          activityId: this.session.activityId,
          sessionId: this.session.id
        }
      },
      cancelLink: {
        name: "CancelBooking",
        query: {
          facilityId: this.session.resource.id,
          activityId: this.session.id
        }
      }
    };
  },
  computed: {
    ...mapGetters("auth", ["isEmployeeOrManager"]),
    placesAvailableForSession() {
      if (!this.session) {
        return false;
      }
      return (
        this.session.currentCapacity < this.session.totalCapacity ||
        this.session.totalCapacity === null
      );
    },
    userCanDelete() {
      return this.isEmployeeOrManager;
    },
    userCanBook() {
      // TODO check if member
      return !this.session.isFull;
    }
  },
  methods: {
    ...mapActions("timetable", ["deleteSession"]),
    async deleteASession() {
      const result = await this.deleteSession(this.session.id);
      if (result) {
        this.$emit("sessionDeleted");
      } else {
        this.error = "Could not delete session";
      }
    },
    formatCurrency: formatCurrency
  }
};
</script>
