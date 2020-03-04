<template>
  <div class="home">
    <Facility></Facility>
    <Facility></Facility>
    <BookingInformation></BookingInformation>
    <Timetable></Timetable>
  </div>
</template>

<script>
import Timetable from "@/components/Timetable.vue";
import BookingInformation from "@/components/BookingInformation.vue";
import Facility from "@/components/Facility.vue";

export default {
  name: "Home",
  components: {
    Timetable,
    BookingInformation,
    Facility
  }
};
</script>
