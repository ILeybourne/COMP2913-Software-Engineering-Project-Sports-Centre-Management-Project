<template>
  <div class="booking-info" @load="fillByQuery">
    <div class="booking-container">
      <form @submit="postData">
        <label for="facility">Facility:</label>
        <b-form-select
          v-model="selectedFacility"
          :options="facility"
          name="facility"
          id="facility"
          @change="setActivitiesArray(this.activities)"
        >
        </b-form-select>
        <label for="activity">Activity:</label>
        <b-form-select
          v-model="selectedActivity"
          :options="activity"
          name="activity"
          id="activity"
          @change="getTimes"
        >
        </b-form-select>
        <br />
        <label for="date">Date:</label>
        <input type="date" id="date" name="date" :value="date" /><br />
        <label for="time">Time:</label>
        <b-form-select
          v-model="selectedTime"
          :options="time"
          name="time"
          id="time"
        >
        </b-form-select>
        <br />
        <label for="price">Price:</label>
        <input type="text" id="price" name="price" disabled />
        <div class="button-container">
          <button type="submit" class="btn btn-outline-secondary" name="guest">
            Checkout As Guest
          </button>
          <button type="submit" class="btn btn-outline-primary" name="account">
            Checkout With Account
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.booking-info {
  padding-top: 5%;
  padding-bottom: 5%;
}

.booking-container {
  margin: auto;
  width: 50%;
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
  data() {
    return {
      message: [],
      contents: [],
      facility: ["Please Select"],
      activity: ["Please Select"],
      activities: [],
      time: ["Please Select"],
      price: 0,
      date: null,
      selectedFacility: null,
      selectedActivity: null,
      selectedTime: null
    };
  },
  computed: {},
  methods: {
    // keelItWithFire() {
    //   document.querySelector(".sithLord").style.display = "none";
    // },

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

        const {data} = await axios.get(
          "http://localhost:8000/resources/" + facilityId + "/activities",
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
        );
        return data;
      }catch (e) {
        console.log(e)
      }
    },

    async fillByQuery() {
      const facilityId = this.$route.query.facilityId;
      const activityId = this.$route.query.activityId;

      let facilities = [];
      let activities = [];

      await axios
        .all([this.getFacilities(), this.getActivitiesForFacility()])
        .then(responseArray => {
          //this will be executed only when all requests are complete
          console.log();
          console.log("Date created: ", responseArray[0]);
          console.log("Date created: ", responseArray[1]);
          facilities = responseArray[0];
          activities = responseArray[1];
        });

      this.selectedFacility = facilities.content.find(
        x => x.id == facilityId
      ).name;

      this.selectedActivity = activities.find(
        x => x.id == activityId
      ).name;

      // console.log(activities)

      // console.log(activityData);
      //
      // // console.log(data.content.find(x => x.id == this.$route.query.facilityId))
      // this.selectedFacility = facilityData.content.find(x => x.id == facilityId).name;
      // this.selectedActivity= activityData.content.find(x => x.id == activityId).name;

      // this.facility.push(facilityName)
      // this.activity.push(activityName)
      // this.facility = this.$route.query.facilityId;
      // this.activity = this.$route.query.activityId;
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
      const { data } = await axios.get(
        "http://localhost:8000/activities",

        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      console.log("activ")
      console.log(data)
      this.activities =  data

    },
    setActivitiesArray(activities){
      let activityArray = ["Please Select"]
      // const selectOption = ["Please Select"];

      for (const activity of activities) {

        console.log(activity.content.resource + this.selectedFacility)
        if (activity.resource == this.selectedFacility) {
          activityArray.push(activity.name)
        }
      }
      console.log(activityArray)
      this.activity = activityArray;
    },
    getTimes() {
      const facilities = this.contents;
      let timeArray = [];
      const selectOption = ["Please Select"];

      for (const facility of facilities) {
        if (facility.name === this.selectedFacility) {
          for (const activity of facility.activities) {
            if (activity.name === this.selectedActivity) {
              timeArray = selectOption.concat([activity.startTime]);
            }
          }
        }
      }
      this.time = timeArray;
    },
    async postData() {
      const token = await this.$auth.getTokenSilently();
      await axios.post("http://localhost:8000/booking", {
        activity: this.selectedActivity,
        startTime: this.time,
        endTime: this.time,
        token: token
        // facility: this.selectedFacility,
        // date: this.date,
        // price: this.price,
        // userType: e.toElement.name
      });
    }
  },
  async mounted() {
    await this.getResourceContent();
    this.fillByQuery();
    this.getActivities()
    this.setActivitiesArray(this.activities);
  }
};
</script>
