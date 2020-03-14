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
  name: "BookingTable",





  async mounted() {
    await this.getResourceContent();
  }
};
</script>
