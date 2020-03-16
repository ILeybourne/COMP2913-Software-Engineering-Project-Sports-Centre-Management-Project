<template>
  <div id="app">
    <h3 class="title">v-Datatable example</h3>
    <DataTable
      :header-fields="headerFields"
      :sort-field="sortField"
      :sort="sort"
      :data="bookings || []"
      not-found-msg="Items not found"
      trackBy="id"
    >
      <!--      @onUpdate="dtUpdateSort"-->

      <!--      <input-->
      <!--        slot="actions"-->
      <!--        slot-scope="props"-->
      <!--        type="button"-->
      <!--        class="btn btn-info"-->
      <!--        value="Edit"-->
      <!--        @click="dtEditClick(props)"-->
      <!--      />-->
      <Pagination
        slot="pagination"
        :page="currentPage"
        :total-items="this.bookings.length"
        :items-per-page="itemsPerPage"
      />
      <!--      @onUpdate="changePage"-->
      <!--      @updateCurrentPage="updateCurrentPage"-->
      <div class="items-per-page" slot="ItemsPerPage">
<!--        <label>Items per page</label>-->
<!--        <ItemsPerPageDropdown-->
<!--          :list-items-per-page="listItemsPerPage"-->
<!--          :items-per-page="itemsPerPage"-->
<!--          :css="itemsPerPageCss"-->
<!--          @onUpdate="updateItemsPerPage"-->
<!--        />-->
      </div>
      <!--      <Spinner slot="spinner" />-->
    </DataTable>
  </div>
</template>

<style scoped></style>

<script>
// import Spinner from "vue-simple-spinner";
import { DataTable, Pagination } from "v-datatable-light";
// import orderBy from "lodash.orderby";

/*
const dummyData = [
  {
    account: "Elliot",
    activity: null,
    createdAt: null,
    id: 1,
    receipt: null,
    updatedAT: null,
  }
];
*/

export default {
  name: "BookingTable",
  components: { DataTable, Pagination },
  // ItemsPerPageDropdown
  data() {
    return {
      /*
      account: null
      activity: null
      createdAt: 1584288689000
      id: 1
      receipt: null
      updatedAt: 1584288692000
      * */
      bookings: [],
      headerFields: [
        "Account",
        "Booking Time",
        "Booking Reference",
        "Receipt"
      ],
      sortField: "id",
      sort: "asc",
      itemsPerPage: 10,
      currentPage: 1
    };
  },
  computed: {},
  methods: {
    async getBooking() {
      const token = await this.$auth.getTokenSilently();

      const { data } = await this.$http.get(`/bookings`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      this.bookings = data.content;
    }
  },
  async mounted() {
    await this.getBooking();
  }
};
</script>
