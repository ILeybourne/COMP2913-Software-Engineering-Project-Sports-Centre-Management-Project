<template>
  <div class = "booking-container">
    <div style = "padding:15px;">
        <button type="button" class="btn btn-outline-secondary" style = "padding:15px;" >Back</button>
    </div>
    <div style="   margin: auto; width: 50%;">
        <h1>Bookings</h1>
    </div>
        <div>
            <BookingInformation></BookingInformation>
        </div>
  </div>
</template>

<script>
import BookingInformation from "@/components/BookingInformation.vue";

// @ is an alias to /src
export default {
  name: "BookingPage",
  components: {
    BookingInformation
  }
};
</script>