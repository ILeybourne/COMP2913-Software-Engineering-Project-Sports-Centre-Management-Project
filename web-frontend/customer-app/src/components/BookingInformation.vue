<template>
  <div class="checkout-container" @load="fillByParams">
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
                getTimes(),
                setActivityName($event)
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
            @change="
              [
                validateTime(),
                getSelectedTime($event),
                checkRegularSession(),
                setMaxParticipants()
              ]
            "
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
            @change="[getPrice(), validateParticipants()]"
            required
            step="1"
            type="number"
            min="1"
            :disabled="
              (!activitiesValid) ||
                maxParticipants  < 1
            "
            v-bind:max="maxParticipants"
          >
          </b-form-input>
        </div>
        <div
          class="form-row" style="margin: auto"
          v-if="maxParticipants < 1 && maxParticipants !== null"
        >
            Selected Activity is Full.
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
        <div class="form-row" v-if="isMember && isRegularSession">
          <label for="cardRadio">Regular<br />Booking:</label>
          <b-form-group style="width: 90%;">
            <b-form-radio-group
              id="btn-radios-2"
              v-model="regularBookingOption"
              :options="['Yes', 'No']"
              buttons
              button-variant="outline-primary"
              size="lg"
              name="radio-btn-outline"
              style="width: 100%"
              @change="setRegularSession()"
            ></b-form-radio-group>
          </b-form-group>
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
          <b-col md="3" />
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
  -webkit-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.1);
  -moz-box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.1);
  box-shadow: 10px 10px 24px 5px rgba(0, 0, 0, 0.1);
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
import { isEmpty } from "../util/session.helpers";
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
        selectedSessionId: null,
        regularSession: false
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
      participantsValid: null,
      regularBookingOption: "No",
      isRegularSession: false,
      maxParticipants: null
    };
  },
  props: {
    sessionInformation: Object
  },
  computed: {
    ...mapGetters("facilities", ["facilities", "activities"]),
    ...mapGetters("timetable", ["sessions"]),
    ...mapGetters("auth", ["user", "isEmployeeOrManager"]),
    ...mapGetters("membership", ["memberships"]),

    account: function() {
      return !this.isEmpty(this.user);
    },

    isMember: function() {
      return this.memberships.length > 0;
    }
  },
  methods: {
    ...mapActions("membership", ["getMemberships"]),
    ...mapActions("facilities", ["getFacilities", "getActivities"]),
    ...mapActions("timetable", ["getAllSessions"]),

    setRegularSession() {
      if (this.regularBookingOption === "No"){
        this.bookingInformation.regularSession = false
      } else {
        this.bookingInformation.regularSession = true
      }
    },

    validateParticipants(){
      if (Number(this.bookingInformation.participants) === 0 ){
        this.participantsValid = false
      }else{
        this.participantsValid = true

      }
    },

    checkRegularSession() {
      if (this.bookingInformation.selectedSessionId != null) {
        console.log(this.sessions.find(
                s => s.id === this.bookingInformation.selectedSessionId
        ))
        this.isRegularSession =
          this.sessions.find(
            s => s.id === this.bookingInformation.selectedSessionId
          ).regularSessionId != null;
      }else{

        this.isRegularSession = false;
      }
    },

    async setMaxParticipants() {
      if (this.bookingInformation.selectedActivityId !== null) {
        let data = await this.$http.get("/bookings");
        console.log(data.data._embedded.bookingDToes);

        let bookings = data.data._embedded.bookingDToes;
        let customerCount = 0;

        for (const booking of bookings) {
          console.log( booking.session_id + " " +  this.bookingInformation.selectedSessionId)
          if (
            booking.session_id === this.bookingInformation.selectedSessionId
          ) {
            customerCount = customerCount + booking.participants;
          }
        }
        console.log("customerCount")
        console.log(customerCount)

        console.log("max")
        console.log( this.activities.find(
                x => x.id === this.bookingInformation.selectedActivityId
        ).totalCapacity)

        this.maxParticipants =
          this.activities.find(
            x => x.id === this.bookingInformation.selectedActivityId
          ).totalCapacity - customerCount;

        if (this.maxParticipants < 1) {
          this.bookingInformation.participants = 0;
          this.participantsValid =false
        }
      }
    },

    setActivityName(e) {
      this.bookingInformation.selectedSessionId = null;
      this.bookingInformation.selectedTime = null;
      if (e != null) {
        this.activitiesValid = true
        this.bookingInformation.selectedActivityName = this.activities.find(
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
        let selectedActivity = this.activities.find(
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
      this.bookingInformation.selectedSessionId = null;
      this.bookingInformation.selectedTime = null;
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
      this.timeValid = this.bookingInformation.selectedSessionId !== null;
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

    async fillByParams() {
      this.setFacilityOptions();
      this.activityOptions = [];

      if (!isEmpty(this.$route.params)) {
        const facilityId = this.$route.params.sessionInformation.facilityId;
        const activityTypeId = this.$route.params.sessionInformation
          .activityTypeId;
        const sessionId = this.$route.params.sessionInformation.sessionId;

        this.bookingInformation.selectedFacilityId = facilityId;
        this.setFacilityName(facilityId);
        this.facilityValid = true;
        await this.setActivityTypeOptions(facilityId);

        this.bookingInformation.selectedActivityId = activityTypeId;
        this.bookingInformation.selectedActivityName = this.activities.find(
          x =>
            Number(x.id) === Number(this.bookingInformation.selectedActivityId)
        ).name;
        this.activitiesValid = true;
        this.participantsValid = true;
        this.bookingInformation.participants = 1;

        let selectedDateUnix = this.sessions.find(x => x.id == sessionId)
          .startTime;
        let selectedDate = new Date(selectedDateUnix);
        const year = selectedDate.getFullYear();
        const month = "0" + parseInt(selectedDate.getMonth() + 1).toString();
        const date = "0" + selectedDate.getDate();
        const formattedDate =
          year + "-" + month.substr(-2) + "-" + date.substr(-2);
        this.bookingInformation.selectedDate = formattedDate;
        this.dateValid = true;

        this.getTimes();
        this.bookingInformation.selectedSessionId = sessionId;
        this.getSelectedTime(sessionId);
        this.timeValid = true;
        this.setMaxParticipants();
        this.checkRegularSession();
        this.getPrice();
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
          if (this.bookingInformation.selectedDate != null){
            this.timeOptions = timeArray;
          }else{
            this.timeOptions = null
          }
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
    await this.getMemberships();
    await this.fillByParams();
  }
};
</script>
