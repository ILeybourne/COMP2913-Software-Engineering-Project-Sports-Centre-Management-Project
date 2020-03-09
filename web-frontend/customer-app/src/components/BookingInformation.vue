<template>
  <div class="booking-info">
    <div class="booking-container">
      <form>
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
        <input type="date" id="date" name="date" @change="getDate" /><br />
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
          <button
            type="button"
            class="btn btn-outline-secondary"
            @click="postData"
            name="guest"
          >
            Checkout As Guest
          </button>
          <button
            type="button"
            class="btn btn-outline-primary"
            @click="postData"
            name="account"
          >
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
  methods: {
    async getResourceContent() {
      const token = await this.$auth.getTokenSilently();

      const { data } = await axios.get("http://localhost:8000/resources", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      this.message = data;

      const content = data.content;
      const facils = ["Please Select"];

      for (const facil in content) {
        facils.push(content[facil].name);
      }

      this.facility = facils;
      this.contents = content;
    },
    getActivities() {
      const content = this.contents;
      const activityArray = ["Please Select"];

      for (const facil in content) {
        if (content[facil].name === this.selectedFacil) {
          for (const act in content[facil].activities) {
            activityArray.push(content[facil].activities[act].name);
          }
        }
      }
      this.activity = activityArray;
    },
    getTimes() {
      const content = this.contents;
      const timeArray = ["Please Select"];

      for (const facil in content) {
        if (content[facil].name === this.selectedFacil) {
          for (const act in content[facil].activities) {
            if (content[facil].activities[act].name === this.selectedActivity) {
              timeArray.push(content[facil].activities[act].startTime);
            }
          }
        }
      }
      this.time = timeArray;
    },
    getDate() {
      const newDate = document.getElementById("date").value;
      this.date = newDate;
    },
    async postData(e) {
      const token = await this.$auth.getTokenSilently();
      await axios.post("http://localhost:8000/booking", {
        facility: this.selectedFacil,
        activity: this.selectedActivity,
        date: this.date,
        time: this.time,
        price: this.price,
        token: token,
        userType: e.toElement.name
      });
    }
  },
  beforeMount() {
    this.getResourceContent();
  }
};
</script>
