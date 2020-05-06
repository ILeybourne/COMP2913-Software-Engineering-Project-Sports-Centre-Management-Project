<template>
  <div>
    <div v-if="error" class="alert alter-warning">{{ error }}</div>
    <b-form @submit.prevent="submitNewSession($event)">
      <b-form-group id="facility" label="Facility" label-for="facilitySelect">
        <b-select
          id="facilitySelect"
          :options="facilitiesSelect"
          v-model="form.facilityId"
          required
        >
        </b-select>
      </b-form-group>

      <b-form-group
        id="activityType"
        label="Activity"
        label-for="activitySelect"
      >
        <b-select
          @change="setActivity()"
          id="activitySelect"
          :options="activitiesSelect"
          v-model="form.activityId"
          required
          :disabled="!activitiesAvailable"
        ></b-select>
      </b-form-group>

      <b-form-group
        v-if="activity"
        id="capacity"
        label="Capacity for Session:"
        label-for="capacityInput"
      >
        <b-form-input
          id="capacityInput"
          v-model="activity.capacity"
          type="text"
          readonly
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group
        id="startTime"
        label="Start Time:"
        label-for="startTimeInput"
      >
        <b-form-input
          id="startTimeInput"
          v-model="form.startTime"
          type="datetime-local"
          readonly
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="endTime" label="End Time:" label-for="endTimeInput">
        <b-form-input
          id="endTimeInput"
          v-model="form.endTime"
          type="datetime-local"
          readonly
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group label="Private Session:">
        <b-form-select
          :options="privateSessionOptions"
          v-model="form.social"
        ></b-form-select>
      </b-form-group>

      <b-form-group label="Regular Session: ">
        <b-form-checkbox
          v-model="form.regularSession"
          name="regularSession"
          switches
        >
        </b-form-checkbox>
      </b-form-group>

      <b-form-group label="Days between repeat: " v-if="form.regularSession">
        <b-form-input v-model="form.interval" type="number"></b-form-input>
      </b-form-group>

      <div class="d-flex justify-content-between">
        <b-button type="submit" variant="primary">Create Session</b-button>
      </div>
    </b-form>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import { required } from "vuelidate";
import store from "@/store";

export default {
  name: "SessionCreate",
  props: {
    facilityId: {
      default: null,
      type: Number,
      required: false
    },
    startTime: {
      required: true,
      type: String
    },
    endTime: {
      required: true,
      type: String
    }
  },
  data() {
    return {
      error: null,
      form: {
        startTime: this.startTime,
        endTime: this.endTime,
        facilityId: this.facilityId,
        activityId: -1,
        social: true,
        regularSession: false,
        interval: 1
      },
      activity: null,
      privateSessionOptions: [
        { text: "Yes", value: true },
        { text: "No", value: false }
      ]
    };
  },
  computed: {
    ...mapGetters("facilities", ["facilities"]),
    facilitiesSelect() {
      return [{ text: "Please Select", value: -1 }].concat(
        this.facilities.map(facility => {
          return {
            text: facility.name,
            value: facility.id
          };
        })
      );
    },
    filteredActivities() {
      if (!this.form.facilityId) {
        return [];
      }
      const key = "facilities/getActivitiesForFacilityId";
      return store.getters[key](this.form.facilityId);
    },
    activitiesSelect() {
      return [{ text: "Please Select", value: -1 }].concat(
        this.filteredActivities.map(activity => {
          return { value: activity.id, text: activity.name };
        })
      );
    },
    activitiesAvailable() {
      return this.filteredActivities.length > 0;
    }
  },
  validations: {
    error: null,
    form: {
      facilityId: {
        required
      },
      activity: {
        required
      },
      startTime: {
        required
      },
      endTime: {
        required
      }
    }
  },
  methods: {
    ...mapActions("facilities", ["getActivities", "getFacilities"]),
    ...mapActions("timetable", ["createSession"]),
    async submitNewSession() {
      /* TODO: Validate and check server response */

      const body = {
        ...this.form,
        currentCapacity: 0
      };

      const data = await this.createSession(body);

      this.$emit("post", {
        facilityId: this.form.facilityId,
        activityId: this.form.activityId,
        sessionId: data.id,
        redirectToBooking: false
      });
    },
    setActivity() {
      const id = this.form.activityId;
      this.activity = this.filteredActivities.find(a => a.id === id) || null;
    }
  },
  mounted() {
    this.getActivities();
    this.getFacilities();
  }
};
</script>

<style scoped></style>
