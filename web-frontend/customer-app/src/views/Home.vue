<template>
  <div class="home">
    <Facilities></Facilities>
    <BookingInformation></BookingInformation>
    <Timetable></Timetable>
  </div>
</template>

<script>
import Timetable from "@/components/Timetable.vue";
import BookingInformation from "@/components/BookingInformation.vue";
import Facilities from "@/views/Facilities.vue";

export default {
  name: "Home",
  components: {
    Timetable,
    BookingInformation,
    Facilities
  }
};
</script>
