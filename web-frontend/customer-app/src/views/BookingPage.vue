<template>
  <div class="page-container">
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
    <div class="form-container">
<!--    <span>-->
      <BookingInformation  class="booking-info" @getUserType="showGuestInfo"></BookingInformation>
      <GuestInformation class="guest-info"></GuestInformation>
<!--      <GuestInformation class="guest-info" v-show="showGuestInfoComponent"></GuestInformation>-->
    </div>
<!--    </span>-->
  </div>
</template>

<style scoped>
.padding-div {
  padding: 15px;
}

.guest-info {
  /*display: none;*/
  display: inline-block;
}
.booking-info {
  /*display: none;*/
  display: inline;
}

.heading-div {
  margin: auto;
  width: 50%;
}
.form-container{
    /*TODO Display divs inline*/
  /*margin: auto;*/
  width: 100%;

  display: inline;
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
};
</script>
