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
                // selectActivityName(),
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
            :disabled="!timeValid"
            v-if="!account"
          >
            Checkout As Guest
          </button>
          <button
            type="button"
            class="btn btn-outline-primary"
            name="account"
            @click="getUserType($event)"
            :disabled="!timeValid"
            v-if="account"
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

button {
  margin: auto;
  width: 50%;
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
  ...mapActions("facilities", ["getFacilities", "getActivities"]),
  ...mapActions("timetable", ["getAllSessions"]),
  name: "BookingInformation",
  data() {
    return {
      facilityOptions: [],
      activityOptions: [],
      timeOptions: ["Please Select"],
      selectedActivityId: null,
      selectedFacilityId: null,
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
    ...mapGetters("timetable", ["sessions"]),
    ...mapGetters("auth", ["user"]),
    account: function() {
      return !this.isEmpty(this.user);
    },
    // selectedActivityName: function() {
    //   if (this.activityOptions.length === 1){
    //     if (
    //             this.activityOptions[0].text === "No Activities found for this facility"
    //     ) {
    //       return "No Activities found for this facility";
    //     } else {
    //       return "Please Select";
    //     }
    //   }else{
    //     return this.activities.find(
    //             x => Number(x.id) === Number(this.selectedActivityId)
    //     ).name;
    //   }
    // }
  },
  methods: {
    ...mapActions("facilities", ["getFacilities", "getActivities"]),
    ...mapActions("timetable", ["getAllSessions"]),
    getPrice(e) {
      if (this.selectedActivityId != null) {
        let selectedActivity = this.activities.find(x => x.id == e);
        this.price = selectedActivity.cost;
      }
    },

    async setActivityTypeOptions(e) {
      this.selectedActivityId = null
      if (!(e == null)) {
        const activityTypesRequest = await this.$http.get(
          "http://localhost:8000/activitytypes/resource/" + e
        );
        if ("_embedded" in activityTypesRequest.data) {
          const activityTypeArray =
            activityTypesRequest.data._embedded.activityTypeDToes;
          let activityOptionsArray = [{ value: null, text: "Please Select" }];
          for (const activity of activityTypeArray) {
            activityOptionsArray.push({
              value: activity.id,
              text: activity.name
            });
          }
          this.activityOptions = activityOptionsArray;
          // this.selectedActivityName = "Please Select";
        } else {
          this.activityOptions = [
            { value: null, text: "No Activities found for this facility" }
          ];
          // this.selectedActivityName = "No Activities found for this facility";
        }
        this.selectedTime = "Please Select";
      }
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
      console.log(this.user.email);
    },
    validateActivity() {
      this.activitiesValid = !(this.$data.selectedActivityId == null);
    },
    validateDate() {
      this.dateValid = this.$data.date != null;
    },
    validateTime() {
      console.log(this.user);
      console.log(this.$data.selectedTime == null);
      console.log(this.$data.selectedTime === "Please Select");
      this.timeValid = !(
        this.selectedTime == null || this.selectedTime === this.timeOptions[0]
      );
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
    // TODO, shouldn't access routes like this, use props and either inject with router or from booking page
    fillByQuery() {
      this.setFacilityOptions();
      this.activityOptions = [];
      const facilityId = this.$route.query.facilityId;
      const activityTypeId = this.$route.query.activityId;
      const activityId = this.$route.query.sessionId;
      if (!this.isEmpty(this.$route.query)) {
        //If query isn't empty fill ids, selectedDate and timeOptions
        this.selectedFacilityId = facilityId;
        this.setActivityTypeOptions(facilityId);
        this.selectedActivityId = activityTypeId;
        this.selectedActivityName = this.activities.find(
          x => Number(x.id) === Number(this.selectedActivityId)
        ).name;
        this.selectActivityName();
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
        this.getPrice(activityTypeId);
        this.callValidation();
      }
    },
    getTimes() {
      if (this.selectedDate != null) {
        if (
          this.selectedFacilityId != null &&
          this.selectedActivityId != null
        ) {
          let timeArray = ["Please Select"];
          console.log(this.sessions)
          for (const activity of this.sessions) {
            let selectedTime = new Date(activity.startTime);
            const year = selectedTime.getFullYear();
            const month = this.addZero(
              (parseInt(selectedTime.getMonth()) + 1).toString()
            );
            const date = this.addZero(selectedTime.getDate());
            let hours = this.addZero(selectedTime.getUTCHours());
            const mins = this.addZero(selectedTime.getMinutes());
            let formattedDate = year + "-" + month + "-" + date;
            if (
              activity.name == this.activities.find(act => act.id === this.selectedActivityId).name &&
              formattedDate == this.selectedDate
            ) {
              let formattedTime = hours.substr(-2) + ":" + mins.substr(-2);
              if(!timeArray.includes(formattedDate)){

                timeArray.push(formattedTime);
              }
            }
          }
          this.timeOptions = timeArray;
        }
      }
    },
    addZero(value) {
      return ("0" + value.toString()).slice(-2);
    }
  },
  async mounted() {
    await this.getFacilities();
    await this.getActivities();
    await this.getAllSessions();
    this.fillByQuery();
  }
};
</script>
