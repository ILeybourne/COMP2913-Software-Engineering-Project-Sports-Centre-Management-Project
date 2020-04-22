<template>
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

    <b-form-group id="activityType" label="Activity" label-for="activitySelect">
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

    <b-form-group id="startTime" label="Start Time:" label-for="startTimeInput">
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

    <b-form-group label="Social: ">
      <b-form-checkbox v-model="form.social" name="flavour-3a">
      </b-form-checkbox>
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
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import { required } from "vuelidate";

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
      form: {
        startTime: this.startTime,
        endTime: this.endTime,
        facilityId: this.facilityId,
        activityId: null,
        social: true,
        regularSession: false,
        interval: 1
      },
      activity: null
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities", "facilities"]),
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
      return this.activities.filter(
        a => Number(a.id) === Number(this.form.facilityId)
      );
    },
    activitiesSelect() {
      return [{ text: "Please Select", value: -1 }].concat(
        this.filteredActivities.map(activity => {
          return { value: activity.id, text: activity.name };
        })
      );
    },
    activitiesAvailable() {
      return this.activitiesSelect.length > 1;
    }
  },
  validations: {
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
      this.activity = this.activities.find(a => a.id === id) || null;
    }
  },
  mounted() {
    this.getActivities();
    this.getFacilities();
  }
};
</script>

<style scoped></style>
