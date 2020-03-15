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
      <h1>Bookings</h1>
    </div>
    <div>
      <BookingInformation @getUserType="showGuestInfo"></BookingInformation>
<!--      <GuestInformation  v-bind:class="{ display: showGuestInfoComponent }"></GuestInformation>-->
      <GuestInformation v-show="showGuestInfoComponent"></GuestInformation>
    </div>
  </div>
</template>

<style scoped>
.padding-div {
  padding: 15px;
}

GuestInformation {
  display: none;
}

.heading-div {
  margin: auto;
  width: 50%;


}

/*button{*/
/*  padding: 15px;*/
/*}*/
</style>

<script>
import BookingInformation from "@/components/BookingInformation.vue";
import GuestInformation from "../components/GuestInformation";

// @ is an alias to /src
export default {
  name: "BookingPage",
  components: {
    GuestInformation,
    BookingInformation
  },
  data() {
    return {
    showGuestInfoComponent: false,
  }
  }  ,
  methods: {
    showGuestInfo(value){
      console.log("hello")
      console.log(value + "guest")
      if (value == "guest"){
        this.showGuestInfoComponent = true;
        console.log(this.showGuestInfoComponent)
      }

    }
  }

  // props:
};
</script>
