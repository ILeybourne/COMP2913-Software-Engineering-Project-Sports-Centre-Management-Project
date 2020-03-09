<template>
  <div class="booking-info">
    <div class="booking-container">
      <form @submit="postData">
        <label for="facility">Facility:</label>
        <b-form-select
          v-model="selectedFacil"
          :options="facility"
          name="facility"
          id="facility"
          @change="getActivities"
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
      time: ["Please Select"],
      price: 0,
      date: null,
      selectedFacil: null,
      selectedActivity: null,
      selectedTime: null
    };
  },
  computed: {},
  methods: {
    async getResourceContent() {
      const token = await this.$auth.getTokenSilently();

      const { data } = await axios.get("http://localhost:8000/resources", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      const content = data.content;
      const facilities = ["Please Select"];

      for (const facility of content) {
        facilities.push(facility.name);
      }

      this.facility = facilities;
      this.contents = content;
    },
    getActivities() {
      const facilities = this.contents;
      let activityArray = [];
      const selectOption = ["Please Select"];

      for (const facility of facilities) {
        if (facility.name === this.selectedFacil) {
          activityArray = selectOption.concat(
            facility.activities.map(a => a.name)
          );
        }
      }
      this.activity = activityArray;
    },
    getTimes() {
      const facilities = this.contents;
      let timeArray = [];
      const selectOption = ["Please Select"];

      for (const facility of facilities) {
        if (facility.name === this.selectedFacil) {
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
        // facility: this.selectedFacil,
        // date: this.date,
        // price: this.price,
        // userType: e.toElement.name
      });
    }
  },
  async mounted() {
    await this.getResourceContent();
  }
};
</script>
