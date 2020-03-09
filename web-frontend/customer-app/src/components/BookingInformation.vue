<template>
  <div class="booking-info">
    <div class="booking-container">
      <data>
        {{ facility }}
      </data> <br />
      <data>
        {{ contents }}
      </data>
      <button @click="callApi">Call</button>

      <form>
        <!--                TODO set input types and import select options-->
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
          v-model="selected"
          :options="activity"
          name="activity"
          id="activity"
        >
        </b-form-select>
<!--        <select id="activity" name="activity"></select-->
        ><br />
        <label for="date">Date:</label>
        <input type="date" id="date" name="date" /><br />
        <label for="time">Time:</label>
        <select id="time" name="time"></select
        ><br />
        <label for="price">Price:</label>
        <input type="text" id="price" name="price" disabled />
        <div class="button-container">
          <button type="button" class="btn btn-outline-secondary">
            Checkout As Guest
          </button>
          <button type="button" class="btn btn-outline-primary">
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
      selectedFacil: null,
      selected: null
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

      const content = data.content;
      const facils = ["Please Select"];

      for (const facil in content) {
        console.log(content[facil].name);
        facils.push(content[facil].name);
      }

      this.facility = facils;
      this.contents = content;
      console.log(content);
      console.log(facils);
    },
    getActivities() {
      console.log("activiesi")
      const content = this.contents;
      const activityArray = ["Please Select"]

      for (const facil in content){
        console.log(content[facil].name + "   " +  this.selectedFacil)
        if (content[facil].name === this.selectedFacil) {

          for (const act in content[facil].activities) {
            activityArray.push(content[facil].activities[act].name)
          }
          console.log(content[facil].name)
        }
      }
      this.activity = activityArray;
    },
  },
  beforeMount() {
    this.callApi();
    this.getActivities();
  }
};


</script>
