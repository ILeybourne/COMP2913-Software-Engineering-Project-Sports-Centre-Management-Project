<template>
  <div class="card">
    <div class="card-body">
      <h5 class="card-title">{{ session.name }}</h5>
      <p class="card-text">
        This would be an example of a description that the staff could write
        about the activity for users to read before they book
      </p>
      <table class="card-table table">
        <tbody>
          <tr>
            <td>Current Capacity</td>
            <td>{{ this.session.currentCapacity || 0 }}</td>
          </tr>
          <tr v-if="session.totalCapacity">
            <td>Total Capacity</td>
            <td>{{ this.session.totalCapacity }}</td>
          </tr>
          <tr>
            <td>Price</td>
            <!--TODO: Price-->
            <td>12.00</td>
          </tr>
        </tbody>
      </table>
      <router-link
        v-if="this.placesAvailableForSession"
        :to="this.link"
        class="btn btn-primary"
        >Book Activity</router-link
      >
      <b-button class="btn btn-danger" @click="deleteASession(e)">
        Delete
      </b-button>
    </div>
  </div>
</template>

<style scoped></style>
<!--TODO: Cancel booking if already booked on-->
<script>
import { mapActions } from "vuex";

export default {
  name: "SessionInfo",
  props: { session: Object },
  data() {
    return {
      link: {
        name: "BookingPage",
        query: {
          facilityId: this.session.resource.id,
          activityId: this.session.id
        }
      }
    };
  },
  computed: {
    placesAvailableForSession() {
      if (!this.session) {
        return false;
      }
      return (
        this.session.currentCapacity < this.session.totalCapacity ||
        this.session.totalCapacity === null
      );
    }
  },
  methods: {
    ...mapActions("timetable", ["deleteSession"]),
    async deleteASession() {
      await this.deleteSession(this.session.id);
      // TODO, close modal and clear from state.
    }
  }
};
</script>
