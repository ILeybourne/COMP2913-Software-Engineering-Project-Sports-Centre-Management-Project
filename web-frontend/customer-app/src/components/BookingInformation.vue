<template>
  <div class="booking-info" @load="fillByQuery">
    <div
      class="booking-container"
      v-bind:style="{ width: this.componentWidth + '%' }"
    >
      <form @submit="submitForm($event)">
        <div class="form-row">
          <label for="facility">Facility:</label>
          <b-form-select
            v-model="selectedFacilityId"
            :options="facilityOptions"
            name="facility"
            id="facility"
            @change="[setActivityTypeOptions($event), validateFacility()]"
            @load="setFormDefaults($event)"
            v-bind:state="facilityValid"
            required
          >
          </b-form-select>
        </div>
        <div class="form-row">
          <label for="activity">Activity:</label>
          <b-form-select
            v-model="selectedActivityId"
            :options="activityOptions"
            name="activity"
            id="activity"
            @change="
              [
                selectActivity(),
                validateActivity(),
                getPrice($event),
                getTimes()
              ]
            "
            v-bind:state="activitiesValid"
            required
          >
          </b-form-select>
        </div>
        <div class="form-row">
          <label for="date">Date:</label>
          <input
            type="date"
            id="date"
            name="date"
            v-model="selectedDate"
            @change="getTimes($event)"
            required
          />
        </div>
        <div class="form-row">
          <label for="time">Time:</label>
          <b-form-select
            v-model="selectedTime"
            :options="timeOptions"
            name="time"
            id="time"
            v-bind:state="timeValid"
            @change="validateTime()"
            required
          >
          </b-form-select>
        </div>
        <div class="form-row">
          <label for="price">Price:</label>
          <input type="text" id="price" name="price" v-model="price" disabled />
        </div>
        <div class="button-container">
          <!--          TODO function call on enter press-->
          <button
            type="button"
            class="btn btn-outline-secondary"
            name="guest"
            @click="getUserType($event)"
          >
            Checkout As Guest
          </button>
          <button
            type="button"
            class="btn btn-outline-primary"
            name="account"
            @click="getUserType($event)"
          >
            Checkout With Account
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.form-row {
  padding: 5px;
}

.booking-container {
  margin: auto;
  /*width: 50%;*/
  border: 3px solid #3183e5;
  padding: 10px;
  border-radius: 10px;
}

.button-container {
  display: flex;
  justify-content: space-between;
  padding-left: 20%;
  padding-right: 20%;
  padding-top: 10px;
}

input {
  width: 90%;
}

select {
  width: 90%;
}

label {
  width: 10%;
}
</style>

<script>
import { mapActions, mapGetters } from "vuex";
export default {
  ...mapActions("facilities", ["getAllFacilities", "getAllActivities"]),
  ...mapActions("timetable", ["getAllSessions"]),
  name: "BookingInformation",
  // props: ["content", "facilities"],
  // props:
  data() {
    return {
      facilityOptions: [],
      activityOptions: [],
      timeOptions: ["Please select"],

      selectedActivityId: null,
      selectedFacilityId: null,
      selectedActivityName: null,
      selectedTime: null,
      price: null,
      selectedDate: null,

      userType: null,
      componentWidth: 90,

      facilityValid: null,
      activitiesValid: null,
      dateValid: null,
      timeValid: null
    };
  },
  computed: {
    ...mapGetters("facilities", ["facilities", "activities"]),
    ...mapGetters("timetable", ["sessions"])
  },
  methods: {
    ...mapActions("facilities", ["getAllFacilities", "getAllActivities"]),
    ...mapActions("timetable", ["getAllSessions"]),
    getPrice(e) {
      //console.log(e);
      //console.log(this.activities.find(x => x.id == e));
      if (this.selectedActivityId != null) {
        let selectedActivity = this.activities.find(x => x.id == e);
        this.price = selectedActivity.cost;
      }
    },

    setActivityTypeOptions(e) {
      let activityArray = [{ value: null, text: "Please Select" }];
      let activities = this.activities;

      if (!(e == null)) {
        const filter = activity => Number(activity.resource.id) === Number(e);
        activities = this.activities.filter(filter);
        for (const activity of activities) {
          activityArray.push({ value: activity.id, text: activity.name });
        }
      } else {
        this.activityOptions = activityArray;
        this.selectedActivityName = "Please Select";
      }

      this.activityOptions = activityArray;
      this.selectedTime = "Please Select";
    },

    setFacilityOptions() {
      let facilities = this.facilities;
      let facilityArray = [{ value: null, text: "Please Select" }];
      for (const facility of facilities) {
        facilityArray.push({ value: facility.id, text: facility.name });
      }
      this.facilityOptions = facilityArray;
    },
    setFormDefaults() {},

    getUserType(e) {
      this.userType = e.toElement.name;
      this.submitForm(1);
    },

    validateFacility() {
      this.facilityValid = !(this.$data.selectedFacilityId == null);
    },
    validateActivity() {
      this.activitiesValid = !(this.$data.selectedActivityId == null);
    },
    validateDate() {
      this.dateValid = this.$data.date != null;
    },
    validateTime() {
      this.timeValid = this.$data.selectedTime != null;
    },

    callValidation() {
      this.validateFacility();
      this.validateActivity();
      this.validateDate();
      this.validateTime();
    },

    // eslint-disable-next-line no-unused-vars
    submitForm(e) {
      if (
        !(this.$data.selectedFacilityId == null) &&
        !(this.$data.selectedActivityId == null) &&
        this.$data.selectedDate != null &&
        this.$data.selectedTime != null
      ) {
        this.facilityValid = true;
        this.activitiesValid = true;
        this.dateValid = true;
        this.timeValid = true;
        this.$emit("getUserType", this.$data);
        this.componentWidth = 60;
      } else {
        //Dont pass data and call validators
        this.callValidation();
      }
    },
    isEmpty(obj) {
      if (Object.keys(obj).length === 0) {
        return true;
      } else {
        if (Object.keys(obj)[0] == "success") {
          return false;
        } else {
          return false;
        }
      }
    },
    fillByQuery() {
      this.setFacilityOptions();
      this.activityOptions = [];
      const facilityId = this.$route.query.facilityId;
      const activityTypeId = this.$route.query.activityTypeId;
      console.log(activityTypeId);
      const activityId = this.$route.query.activityId;
      if (!this.isEmpty(this.$route.query)) {
        //If query isn't empty fill ids, selectedDate and timeOptions
        this.selectedFacilityId = facilityId;
        this.setActivityTypeOptions(facilityId);
        this.selectedActivityId = activityTypeId;
        console.log(this.selectedActivityId);
        this.selectedActivityName = this.activities.find(
          x => x.id == activityTypeId
        ).name;

        let selectedDateUnix = this.sessions.find(x => x.id == activityId)
          .startTime;
        let selectedDate = new Date(selectedDateUnix);
        const year = selectedDate.getFullYear();
        const month = "0" + parseInt(selectedDate.getMonth() + 1).toString();
        const date = "0" + selectedDate.getDate();
        const hours = "0" + selectedDate.getHours();
        const mins = "0" + selectedDate.getMinutes();
        const formattedDate =
          year + "-" + month.substr(-2) + "-" + date.substr(-2);
        const forrmattedTime = hours.substr(-2) + ":" + mins.substr(-2);

        this.selectedDate = formattedDate;

        this.timeOptions.push(forrmattedTime);
        this.selectedTime = forrmattedTime;
      }
    },
    getTimes() {
      if (this.selectedDate != null) {
        if (
          this.selectedFacilityId != null &&
          this.selectedActivityId != null
        ) {
          let timeArray = ["Please Select"];

          for (const activity of this.sessions) {
            // //console.log(activity)
            let selectedTime = new Date(activity.startTime);
            const year = selectedTime.getFullYear();
            const month = ("0" + (parseInt(selectedTime.getMonth()) + 1))
              .toString()
              .substr(-2);
            const date = ("0" + selectedTime.getDate()).substr(-2);
            const hours = ("0" + selectedTime.getHours()).substr(-2);
            const mins = ("0" + selectedTime.getMinutes()).substr(-2);
            let formattedDate = year + "-" + month + "-" + date;
            if (
              activity.name == this.selectedActivityName &&
              // formattedDate == event.target.value
              formattedDate == this.selectedDate
            ) {
              let formattedTime = hours.substr(-2) + ":" + mins.substr(-2);
              timeArray.push(formattedTime);
            }
          }
          this.timeOptions = timeArray;
        }
      }
    },
    selectActivity() {
      if (this.selectedActivityId != null) {
        this.selectedActivityName = this.activities.find(
          x => x.id === this.selectedActivityId
        ).name;
      } else {
        this.selectedActivityName = "Please Select";
      }
    }
  },
  async mounted() {
    await this.getAllActivities();
    await this.getAllFacilities();
    await this.getAllSessions();
    this.fillByQuery();
  }
};
</script>
