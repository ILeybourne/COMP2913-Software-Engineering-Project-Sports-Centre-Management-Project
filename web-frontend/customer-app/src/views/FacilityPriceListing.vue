<template>
  <div class="container">
    <div class="row">
      <div class="col-8">
        <h1>All Facility Price Listings</h1>
      </div>
      <div class="col-4">
        <v-text-field
          v-model="search"
          append-icon="mdi-magnify"
          label="Search"
          single-line
          hide-details
        ></v-text-field>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <v-data-table
          :headers="headers"
          :items="activities"
          :search="search"
          item-key="name"
          items-per-page="10"
        ></v-data-table>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";

export default {
  name: "FacilityPriceListing",
  data: function() {
    return {
      search: "",
      rowData: [],
      headers: [
        {
          value: "name",
          text: "Activity Type",
          sortable: true
        },
        {
          value: "formattedCost",
          text: "Cost",
          sortable: false
        },
        {
          value: "totalCapacity",
          text: "Total Capacity",
          sortable: false
        }
      ]
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"])
  },
  methods: {
    ...mapActions("facilities", ["getActivities"])
  },
  mounted() {
    this.getActivities();
  }
};
</script>

<style scoped></style>
