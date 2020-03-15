<template>
  <div class="booking-info" @load="fillByQuery">
    <div class="booking-container">
      <form>
        <label for="facility">Facility:</label>
        <b-form-select
          v-model="selectedFacility"
          :options="facility"
          name="facility"
          id="facility"
          @change="setActivitiesArray"
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
        <input type="date" id="date" name="date" v-model="date"/><br />
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
<!--          TODO function call on enter press-->
          <button type="button" class="btn btn-outline-secondary" name="guest" @click="getUserType($event)" >
            Checkout As Guest
          </button>
          <button type="button" class="btn btn-outline-primary" name="account" @click="getUserType($event)">
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
  // props:
  data() {
    return {
      message: [],
      contents: [],
      facility: ["Please Select"],
      activity: ["Please Select"],
      activities: [],
      time: ["Please select"],
      price: 10.00,
      date: new Date(),
      selectedFacility: null,
      selectedActivity: null,
      selectedTime: null,
      userType: null
    };
  },
  computed: {},
  methods: {
    getUserType(e){
      //TODO Validate before showing 2nd form
      this.userType = e.toElement.name
      this.$emit('getUserType', this.userType)

      console.log(e.toElement.name)

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
      try {
        await axios
          .all([this.getFacilities(), this.getActivitiesForFacility()])
          .then(responseArray => {
            //this will be executed only when all requests are complete
            console.log();
            console.log("Date created: ", responseArray[0]);
            console.log("Date created: ", responseArray[1]);
            facilities = responseArray[0];
            activities = responseArray[1];
            this.activities = activities
          });

        this.selectedFacility = facilities.content.find(
          x => x.id == facilityId
        ).name;
        this.setActivitiesArray()

        this.selectedActivity = activities.find(
          x => x.id == activityId
        ).name;

        let selectedDateUnix = activities.find(
          x => x.id == activityId
        ).startTime

        let selectedDate = new Date (selectedDateUnix)
        console.log(selectedDate)
        const year = selectedDate.getFullYear()
        const month = "0" + selectedDate.getMonth()
        const date = "0" + selectedDate.getDate()
        const hours = "0" + selectedDate.getHours()
        const mins = "0" + selectedDate.getMinutes()
        var formattedDate =  year + "-" + month.substr(-2) + "-" + date.substr(-2)
        var forrmattedTime =  hours.substr(-2) + ":" + mins.substr(-2)
        console.log(formattedDate)
        this.date = formattedDate

        //TODO loop through same named activities in same facility and append times to time array
        this.time.push(forrmattedTime)
        this.selectedTime = forrmattedTime
      }catch (e) {
        console.log(e)
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
      this.activities = data

    },
    setActivitiesArray(){
      let activities = this.activities
      let activityArray = ["Please Select"]
      try {
        for (const activity of activities) {

          if (activity.resource.name == this.selectedFacility) {
            console.log(activity.resource.name + this.selectedFacility)
            activityArray.push(activity.name)
          }
        }
      }catch (e) {
        console.log(e)
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

  // async submitBookingEvent(event) {
  //   event.preventDefault();
  //   let facility = this.selectedFacility;
  //   let activity =  this.selectedActivity;
  //   let startTime = this.time;
  //   let date = this.date;
  //   let price = this.price;
  //   let userType = event.toElement.name;
  //   try {
  //     /* TODO: Validate and check server response */
  //     const token = await this.$auth.getTokenSilently();
  //     const body = {
  //       ...this.selectedActivityForm,
  //       ...facility,
  //       ...startTime,
  //       ...date,
  //       ...price,
  //       ...activity,
  //       ...userType,
  //       currentCapacity: 0
  //     };
  //     console.log(body);
  //     const { data } = await this.$http.post(
  //       `/resources/${this.selectedActivityForm.resourceId}/activities`,
  //       body,
  //       {
  //         headers: {
  //           Authorization: `Bearer ${token}`
  //         }
  //       }
  //     );
  //
  //     console.log(data);
  //
  //     await this.$router.push({
  //       name: "BookingPage",
  //       params: {
  //         facility: String,
  //         activity: String,
  //
  //       },
  //       query: { facilityId: data.resource.id, activityId: data.id }
  //     });
  //   } catch (e) {
  //     console.error(e);
  //   }
  // },
  async mounted() {
    await this.getResourceContent();
    this.fillByQuery();
    this.getActivities();
    this.setActivitiesArray(this.activities);
  }
};
</script>
