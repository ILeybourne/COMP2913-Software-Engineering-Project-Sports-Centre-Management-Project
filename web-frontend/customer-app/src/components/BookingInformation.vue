<template>
  <div class="booking-info" @load="fillByQuery">
    <div
      class="booking-container"
      v-bind:style="{ width: this.componentWidth + '%' }"
    >
      <form>
        <div class="form-row">
          <label for="facility">Facility:</label>
          <b-form-select
            v-model="selectedFacility"
            :options="facility"
            name="facility"
            id="facility"
            @change="setActivitiesArray"
          >
          </b-form-select>
        </div>
        <div class="form-row">
          <label for="activity">Activity:</label>
          <b-form-select
            v-model="selectedActivity"
            :options="activity"
            name="activity"
            id="activity"
            @change="selectActivity($event)"
          >
          </b-form-select>
        </div>
        <div class="form-row">
          <label for="date">Date:</label>
          <input
            type="date"
            id="date"
            name="date"
            v-model="date"
            @change="getTimes($event)"
          />
        </div>
        <div class="form-row">
          <label for="time">Time:</label>
          <b-form-select
            v-model="selectedTime"
            :options="time"
            name="time"
            id="time"
          >
          </b-form-select>
        </div>
        <div class="form-row">
          <label for="price">Price:</label>
          <input type="text" id="price" name="price" disabled />
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

/*.booking-info {*/
/*  padding-top: 5%;*/
/*  padding-bottom: 5%;*/
/*}*/

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
import axios from "axios";

export default {
  name: "BookingInformation",
  // props: ["content", "facilities"],
  // props:
  data() {
    return {
      message: [],
      contents: [],
      facility: ["Please Select"],
      activity: ["Please Select"],
      activities: [],
      time: ["Please select"],
      price: 10.0,
      date: new Date(),
      selectedFacility: null,
      selectedActivity: null,
      selectedActivityName: null,
      selectedActivityId: null,
      selectedTime: null,
      userType: null,

      componentWidth: 90
    };
  },
  computed: {},
  methods: {
    getUserType(e) {
      //TODO Validate before showing 2nd form
      this.componentWidth = 60;
      this.userType = e.toElement.name;
      //TODO send array of data to parent
      this.$emit("getUserType", this.$data);
    },

    async getFacilities() {
      const token = await this.$auth.getTokenSilently();
      const { data } = await axios.get(
        "http://localhost:8000/resources",

        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      return data;
    },

    async getActivitiesForFacility() {
      try {
        const token = await this.$auth.getTokenSilently();
        const facilityId = this.$route.query.facilityId;

        const { data } = await axios.get(
          "http://localhost:8000/resources/" + facilityId + "/activities",
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
        );
        return data;
      } catch (e) {
        console.log(e);
      }
    },

    isEmpty(obj) {
      if( Object.keys(obj).length === 0){
        return true
      }else{
        if (Object.keys(obj)[0] == "success"){
          return false
        }else {
          return false
        }
      }
    },

    async fillByQuery() {
      const facilityId = this.$route.query.facilityId;
      const activityId = this.$route.query.activityId;
      debugger
      let facilities = [];
      let activities = [];
      if (!this.isEmpty(this.$route.query)) {
        try {
          await axios
            .all([this.getFacilities(), this.getActivitiesForFacility()])
            .then(responseArray => {
              //this will be executed only when all requests are complete
              facilities = responseArray[0];
              activities = responseArray[1];
              this.activities = activities;
            });

          this.selectedFacility = facilities.content.find(
            x => x.id == facilityId
          ).name;
          this.setActivitiesArray();

          this.selectedActivity = activities.find(x => x.id == activityId).name;
          this.selectedActivityId = activityId;

          let selectedDateUnix = activities.find(x => x.id == activityId)
            .startTime;

          let selectedDate = new Date(selectedDateUnix);
          const year = selectedDate.getFullYear();
          const month = "0" + parseInt(selectedDate.getMonth() + 1).toString();
          const date = "0" + selectedDate.getDate();
          const hours = "0" + selectedDate.getHours();
          const mins = "0" + selectedDate.getMinutes();
          var formattedDate =
            year + "-" + month.substr(-2) + "-" + date.substr(-2);
          var forrmattedTime = hours.substr(-2) + ":" + mins.substr(-2);
          this.date = formattedDate;

          //TODO loop through same named activities in same facility and append times to time array
          this.time.push(forrmattedTime);
          this.selectedTime = forrmattedTime;
        } catch (e) {
          console.log(e);
        }
      }
    },

    async getResourceContent() {
      const token = await this.$auth.getTokenSilently();

      const { data } = await axios.get("http://localhost:8000/resources", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      const content = data.content;
      const facilities = this.facility;

      for (const facility of content) {
        facilities.push(facility.name);
      }

      this.facility = facilities;
      this.contents = content;
    },

    async getActivities() {
      const token = await this.$auth.getTokenSilently();
      const { data } = await axios.get("http://localhost:8000/activities", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      this.activities = data.content;
    },

    setActivitiesArray() {
      let activities = this.activities;
      let activityArray = [{ value: null, text: "Please Select" }];
      try {
        for (const activity of activities) {
          if (activity.resource.name == this.selectedFacility) {
            activityArray.push({ value: activity.id, text: activity.name });
          }
        }
      } catch (e) {
        console.log(e);
      }
      console.log(activityArray);
      this.activity = activityArray;
    },

    getTimes(event) {
      const activities = this.activities;
      let timeArray = ["Please Select"];

      for (const activity of activities) {
        let selectedTime = new Date(activity.startTime);
        const year = selectedTime.getFullYear();
        const month = ("0" + (parseInt(selectedTime.getMonth()) + 1))
          .toString()
          .substr(-2);
        const date = ("0" + selectedTime.getDate()).substr(-2);
        const hours = ("0" + selectedTime.getHours()).substr(-2);
        const mins = ("0" + selectedTime.getMinutes()).substr(-2);
        let formattedDate = year + "-" + month + "-" + date;
        this.selectedActivityName = this.activity.find(
          a => a.value == this.selectedActivity
        ).text;
        if (
          activity.name == this.selectedActivityName &&
          formattedDate == event.target.value
        ) {
          let formattedTime = hours.substr(-2) + ":" + mins.substr(-2);
          timeArray.push(formattedTime);
        }
      }
      this.time = timeArray;
    },

    selectActivity(event) {
      this.selectedActivityId = event;
    }
  },
  async mounted() {
    await this.getResourceContent();
    this.fillByQuery();
    this.getActivities();
    this.setActivitiesArray(this.activities);
  }
};
</script>
