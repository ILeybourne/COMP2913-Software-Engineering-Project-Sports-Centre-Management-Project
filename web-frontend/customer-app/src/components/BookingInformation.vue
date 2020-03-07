<template>
  <div class="booking-info">
    <div class="booking-container">
      <form>
        <!--                TODO set input types and import select options-->
        <label for="facility">Facility:</label>
        <input type="text" id="facility" name="facility" /><br />
        <label for="activity">Activity:</label>
        <input type="text" id="activity" name="activity" /><br />
        <label for="date">Date:</label>
        <input type="text" id="date" name="date" /><br />
        <label for="time">Time:</label>
        <input type="text" id="time" name="time" /><br />
        <label for="price">Price:</label>
        <input type="text" id="price" name="price" />
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

label {
  width: 10%;
}
</style>

<script>
export default {
  name: "BookingInformation"
};
</script>
