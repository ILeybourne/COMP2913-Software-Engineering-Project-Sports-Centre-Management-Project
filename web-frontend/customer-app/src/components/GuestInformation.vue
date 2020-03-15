<template>
  <div class="booking-info">
    <div class="booking-container">
      <form>
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" v-model="surname"/><br />
        <label for="surname">Surname:</label>
        <input type="text" id="surname" name="surname" v-model="surname"/><br />
        <label for="email">Email:</label>
        <input type="email" id="email" name="email"  v-model="email"/><br />
        <label for="phone">Phone Number:</label>
        <input type="phone" id="phone" name="phone" v-model="phone"/><br />
        <label for="health">Health Issues:</label>
        <input type="health" id="health" name="health" v-model="health"/><br />
        <div class="button-container">
          <button type="submit" class="btn btn-outline-secondary" name="guest">
            Submit
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
export default {
  name: "GuestInformation",
  // props: ["content", "facilities"],
  data() {
    return {
      // message: [],
      // contents: [],
      // facility: ["Please Select"],
      // activity: ["Please Select"],
      // activities: [],
      // time: ["Please select"],
      // price: 10.00,
      // date: new Date(),
      // selectedFacility: null,
      // selectedActivity: null,
      // selectedTime: null
      firstName: "",
      surname: "",
      email: "",
      phone: "",

      health: ""

    };
  },
  computed: {},
  methods: {}};
</script>
