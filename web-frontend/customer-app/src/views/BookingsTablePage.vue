<template>
  <div class="bookingtable">
    <div class="heading-div">
      <h1><span>Booking </span> Management</h1>
    </div>
    <BookingTable></BookingTable>
  </div>
</template>

<style scoped>
@media screen and (max-width: 600px) {
  .heading-div h1 {
    font-size: 10vw;
  }
}
.heading-div {
  margin-bottom: 20px;
}
.heading-div h1 {
  width: 60%;
  margin: auto;
}
.heading-div p {
  width: 100%;
  padding: 10px;
}
.heading-div span {
  background: #fcff18;
}
</style>
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
