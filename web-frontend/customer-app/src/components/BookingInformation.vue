<template>
  <div class="booking-info">
    <div class="booking-container">
      <data >
        {{facilities}}
      </data>
      <data >
        {{content}}
      </data>
      <button @click="callApi">Call</button>

      <form>
        <!--                TODO set input types and import select options-->
        <label for="facility">Facility:</label>
<!--        <select id="facility" name="facility" v-model="selectedFacilty">-->
<!--        <select id="facility" name="facility">-->
<!--&lt;!&ndash;          <option disabled value="">Please select one</option>&ndash;&gt;-->
<!--        </select><br />-->
<!--        <v-select :options="facilities"></v-select>-->
        <b-form-select v-model="selected" :options="facilities" name="facility"></b-form-select>
        <label for="activity">Activity:</label>

        <select id="activity" name="activity">
        </select><br />
        <label for="date">Date:</label>
        <input type="date" id="date" name="date" /><br />
        <label for="time">Time:</label>
        <select id="time" name="time">
        </select><br />
        <label for="price">Price:</label>
        <input type="text" id="price" name="price" disabled/>
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
  props: ['content','facilities'],
  data() {
    return {
      message: [],
      contents: [],
      facility: [],
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
      const facils = [];

      for (const facil in content){
        console.log(content[facil].name);
        facils.push(content[facil].name);

      }

      this.facilities = facils;
      this.contents = content;
      console.log(content);
      console.log(facils);
    }

  }
};


</script>
