<template>
  <div>
    <h1>All Facility Price Listings</h1>
    <v-data-table
      :headers="headers"
      :items="activities"
      class="elevation-1"
      item-key="name"
      show-select
    ></v-data-table>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";

export default {
  name: "FacilityPriceListing",
  data: function() {
    return {
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
          sortable: true
        },
        {
          value: "totalCapacity",
          text: "Total Capacity",
          sortable: true
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
