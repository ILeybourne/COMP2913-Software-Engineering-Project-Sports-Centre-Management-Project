<template>
  <div class="card">
    <div class="card-body">
      <h5 class="card-title">{{ activity.name }}</h5>
      <p class="card-text">
        This would be an example of a description that the staff could write
        about the activity for users to read before they book
      </p>
      <p>TODO: need to start storing descriptions</p>

      <!--TODO: if not at capacity -->
      <table class="card-table table">
        <tbody>
          <tr>
            <td>Current Capacity</td>
            <td>{{ this.activity.currentCapacity || 0 }}</td>
          </tr>
          <tr>
            <td>Total Capacity</td>
            <td>{{ this.activity.totalCapacity }}</td>
          </tr>
          <tr>
            <td>Price</td>
            <!--TODO: Price-->
            <td>12.00</td>
          </tr>
        </tbody>
      </table>
      <router-link :to="this.link" class="btn btn-primary"
        >Book Activity</router-link
      >
      <b-button @click="deleteASession(e)">
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
  name: "ActivityInfo",
  props: { activity: Object },
  data() {
    return {
      link: {
        name: "BookingPage",
        query: {
          facilityId: this.activity.resource.id,
          activityId: this.activity.id
        }
      }
    };
  },
  computed: {},
  methods: {
    ...mapActions("timetable", ["deleteSession"]),
    async deleteASession() {
      await this.deleteSession(this.activity.id);
    }
  }
};
</script>
