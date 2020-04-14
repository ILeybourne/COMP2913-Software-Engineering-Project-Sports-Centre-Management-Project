<template>
  <div class="small">
    <v-data-table
      v-if="startDate"
      :headers="headers"
      :items="activities"
      group-by="resource.name"
      class="elevation-1"
    >
    </v-data-table>
    <button @click="fillData()">Randomize</button>
    <div class="input-group">
      <label>Start Date</label>
      <v-date-picker @change="fillData" v-model="startDate"></v-date-picker>
    </div>
  </div>
</template>

<style scoped></style>

<script>
import { mapActions, mapGetters } from "vuex";
/*
const groupBy = (xs, key) => {
  return xs.reduce(function(rv, x) {
    (rv[x[key]] = rv[x[key]] || []).push(x);
    return rv;
  }, {});
};

 */

export default {
  name: "BookingTable",
  components: {},
  // ItemsPerPageDropdown
  data: function() {
    return {
      bookings: [],
      startDate: null,
      headers: [
        {
          text: "Resource",
          value: "name"
        },
        {
          text: "Cost",
          value: "formattedCost"
        }
      ]
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"]),
    ...mapGetters("timetable", ["sessions"])
  },
  methods: {
    ...mapActions("facilities", {
      getActivity: "getActivities"
    }),
    ...mapActions("timetable", {
      getSessions: "getAllSessions"
    }),
    async fillData() {}
  },
  mounted: async function() {
    await this.getActivity();
  }
};
</script>
