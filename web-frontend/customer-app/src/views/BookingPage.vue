<template>
  <div class="booking-container">
    <button @click="callApi">Call</button>
    <pre>{{ JSON.stringify(message) }}</pre>
    <div class="padding-div">
      <button
        type="button"
        class="btn btn-outline-secondary"
        style="padding:15px;"
      >
        Back
      </button>
    </div>
    <div class="heading-div">
      <h1>Bookings</h1>
    </div>
    <div>
      <BookingInformation></BookingInformation>
    </div>
  </div>
</template>

<style scoped>
.padding-div {
  padding: 15px;
}

.heading-div {
  margin: auto;
  width: 50%;
}
</style>

<script>
import BookingInformation from "@/components/BookingInformation.vue";
import axios from "axios";

// @ is an alias to /src
export default {
  name: "BookingPage",
  components: {
    BookingInformation
  },
  data() {
    return {
      message: ""
    };
  },
  methods: {
    async callApi() {
      const token = await this.$auth.getTokenSilently();

      const { data } = await axios.get("http://localhost:8000/resources", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      console.log(data);
      this.message = data;
    }
  }
};
</script>
