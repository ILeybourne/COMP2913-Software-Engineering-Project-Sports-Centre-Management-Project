<template>
  <div class="bookingtable">
    <h1>Booking Table</h1>
    <BookingTable></BookingTable>
  </div>
</template>

<script>
import BookingTable from "@/components/BookingTable.vue";

// @ is an alias to /src
export default {
  name: "BookingTablePage",
  components: {
    BookingTable
  }
};
</script>
