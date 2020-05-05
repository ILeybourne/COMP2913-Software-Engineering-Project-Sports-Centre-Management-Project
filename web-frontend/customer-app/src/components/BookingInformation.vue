<template>
  <div class="checkout-container" @load="fillByQuery">
    <div
      class="booking-container"
      v-bind:style="{ width: this.componentWidth + '%' }"
    >
      <h4 class="centered-text">Create Booking</h4>
      <form @submit="submitForm($event)">
        <div class="form-row">
          <label for="facility">Facility:</label>
          <b-form-select
            v-model="bookingInformation.selectedFacilityId"
            :options="facilityOptions"
            name="facility"
            id="facility"
            @change="
              [
                setActivityTypeOptions($event),
                validateFacility(),
                setFacilityName($event)
              ]
            "
            @load="setFormDefaults($event)"
            v-bind:state="facilityValid"
            required
          >
          </b-form-select>
        </div>
        <div class="form-row">
          <label for="activity">Activity:</label>
          <b-form-select
            v-model="bookingInformation.selectedActivityId"
            :options="activityOptions"
            name="activity"
            id="activity"
            @change="
              [
                // selectActivityName(),
                validateActivity(),
                getPrice(),
                getTimes(),
                setActivityName($event),
                setMaxParticipants($event)
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
            v-model="bookingInformation.selectedDate"
            @change="getTimes($event)"
            required
          />
        </div>
        <div class="form-row">
          <label for="time">Time:</label>
          <b-form-select
            v-model="bookingInformation.selectedSessionId"
            :options="timeOptions"
            name="time"
            id="time"
            v-bind:state="timeValid"
            @change="[validateTime(), getSelectedTime($event)]"
            required
          >
          </b-form-select>
        </div>
        <div class="form-row">
          <label for="participants">Participants:</label>
          <b-form-input
            v-model="bookingInformation.participants"
            name="bookingInformation.participants"
            id="bookingInformation.participants"
            v-bind:state="participantsValid"
            @change="[getPrice()]"
            required
            step="1"
            type="number"
            min="1"
            :disabled="!activitiesValid"
            v-bind:max="bookingInformation.maxParticipants"
          >
          </b-form-input>
        </div>
        <div class="form-row">
          <label for="price" style="padding-top: 10px">Price:</label>
          <b-input-group size="lg" prepend="£" style="width: 90%">
            <b-form-input
              type="text"
              step="0.01"
              id="price"
              name="price"
              v-model="price"
              class="form-control"
              readonly
            ></b-form-input>
          </b-input-group>
        </div>

        <div class="button-container">
          <!--          TODO function call on enter press-->
          <button
            type="button"
            class="btn btn-outline-secondary"
            name="guest"
            @click="getUserType($event)"
            :disabled="!timeValid"
            v-if="!account || isEmployeeOrManager"
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
.centered-text {
  text-align: center;
  padding-bottom: 10px;
}

.checkout-container {
  display: flex;
  flex-direction: column;
  padding: 30px 0px 30px 0px;
  min-height: 50%;
  height: auto;
  width: auto;
  margin: 20px;
  background: #f6f9fa;
  color: #242424;
  justify-content: center;
  flex-basis: auto; /* default value */
  flex-grow: 1;
}

.form-row {
  padding: 5px;
}

.booking-container {
  margin: auto;
  padding: 10px;
}

.button-container {
  display: flex;
  justify-content: space-between;
  padding-left: 20%;
  padding-right: 20%;
  padding-top: 30px;
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
  name: "BookingInformation",
  data() {
    return {
      bookingInformation: {
        selectedFacilityId: null,
        selectedFacilityName: null,
        selectedActivityId: null,
        selectedActivityName: null,
        selectedDate: null,
        selectedTime: null,
        participants: null,
        selectedSessionId: null
      },

      facilityOptions: [],
      activityOptions: [],
      timeOptions: ["Please Select"],
      price: null,
      userType: null,
      componentWidth: 90,
      facilityValid: null,
      activitiesValid: null,
      dateValid: null,
      timeValid: null,
      participantsValid: null
    };
  },
  computed: {
    ...mapGetters("facilities", ["facilities", "activityTypes"]),
    ...mapGetters("timetable", ["sessions"]),
    ...mapGetters("auth", ["user"]),
    ...mapGetters("auth", ["isEmployeeOrManager"]),
    account: function() {
      return !this.isEmpty(this.user);
    }
  },
  methods: {
    ...mapActions("facilities", ["getFacilities", "getActivityTypes"]),
    ...mapActions("timetable", ["getAllSessions"]),
    setMaxParticipants(activityId) {
      this.maxParticipants = this.activityTypes.find(
        x => x.id === activityId
      ).totalCapacity;
    },

    setActivityName(e) {
      if (e != null) {
        this.bookingInformation.selectedActivityName = this.activityTypes.find(
          x => x.id === e
        ).name;
      }
    },

    setFacilityName(e) {
      if (e != null)
        this.bookingInformation.selectedFacilityName = this.facilityOptions.find(
          x => x.value === e
        ).text;
    },

    getPrice() {
      if (!this.bookingInformation.participants) {
        this.bookingInformation.participants = 1;
        this.participantsValid = true;
      }

      if (this.bookingInformation.selectedActivityId != null) {
        let selectedActivity = this.activityTypes.find(
          x => x.id === this.bookingInformation.selectedActivityId
        );
        this.price =
          selectedActivity.cost * Number(this.bookingInformation.participants);
      } else {
        this.price = 0;
      }
    },

    getSelectedTime(e) {
      if (e != null)
        this.bookingInformation.selectedTime = this.timeOptions.find(
          x => x.value === e
        ).text;
    },

    async setActivityTypeOptions(e) {
      this.bookingInformation.selectedActivityId = null;
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
        } else {
          this.activityOptions = [
            { value: null, text: "No Activities found for this facility" }
          ];
        }
        this.bookingInformation.selectedTime = "Please Select";
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
      this.facilityValid = !(
        this.$data.bookingInformation.selectedFacilityId == null
      );
    },
    validateActivity() {
      this.activitiesValid = !(
        this.$data.bookingInformation.selectedActivityId == null
      );
    },
    validateDate() {
      this.dateValid = this.$data.date != null;
    },
    validateTime() {
      this.timeValid = !(
        this.bookingInformation.selectedTime == null ||
        this.bookingInformation.selectedTime === this.timeOptions[0]
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
        !(this.$data.bookingInformation.selectedFacilityId == null) &&
        !(this.$data.bookingInformation.selectedActivityId == null) &&
        this.$data.bookingInformation.selectedDate != null &&
        this.$data.bookingInformation.selectedTime != null
      ) {
        this.facilityValid = true;
        this.activitiesValid = true;
        this.dateValid = true;
        this.timeValid = true;
        // this.bookingData = {
        //   facility: this.fac;
        //   activity: this.$route.params.bookingDetails.activity;
        //   date: this.$route.params.bookingDetails.date;
        //   time: this.$route.params.bookingDetails.time;
        //   price: this.$route.params.bookingDetails.price;
        // }
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
        //If query isn't empty fill ids, bookingInformation.selectedDate and timeOptions
        this.bookingInformation.selectedFacilityId = facilityId;
        this.setActivityTypeOptions(facilityId);
        this.bookingInformation.selectedActivityId = activityTypeId;
        this.bookingInformation.selectedActivityName = this.activities.find(
          x =>
            Number(x.id) === Number(this.bookingInformation.selectedActivityId)
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

        this.bookingInformation.selectedDate = formattedDate;

        this.timeOptions.push(forrmattedTime);
        this.bookingInformation.selectedTime = forrmattedTime;
        this.getPrice(activityTypeId);
        this.callValidation();
      }
    },

    getTimes() {
      if (this.bookingInformation.selectedDate != null) {
        if (
          this.bookingInformation.selectedFacilityId != null &&
          this.bookingInformation.selectedActivityId != null
        ) {
          let timeArray = [
            {
              value: null,
              text: "Please Select"
            }
          ];
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

            let activityName = this.activityOptions.find(
              x => x.value == this.bookingInformation.selectedActivityId
            ).text;

            if (
              activity.name == activityName &&
              formattedDate == this.bookingInformation.selectedDate
            ) {
              let formattedTime = hours.substr(-2) + ":" + mins.substr(-2);
              if (!timeArray.includes(formattedDate)) {
                timeArray.push({
                  value: activity.id,
                  text: formattedTime
                });
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
    await this.getActivityTypes();
    await this.getAllSessions();
    this.fillByQuery();
  }
};
</script>
