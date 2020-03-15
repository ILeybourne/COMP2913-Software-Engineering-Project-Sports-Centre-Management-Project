<template>
  <div class="booking-container">
    <div class="padding-div">
      <button
        type="button"
        class="btn btn-outline-secondary"
      >
        Back
      </button>
    </div>
    <div class="heading-div">
      <h1>Bookingasdsds</h1>
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
  button{
    padding: 15px;
  }
</style>

<script>
import BookingInformation from "@/components/BookingInformation.vue";

// @ is an alias to /src
export default {
  name: "BookingPage",
  components: {
    BookingInformation
  }
};

// (){
//   console.log()
// }
</script>
