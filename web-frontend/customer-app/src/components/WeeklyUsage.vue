<template>
  <div class="small">
    <v-data-table
      v-if="startDate"
      :headers="headers"
      :items="dataWithFacilities"
      group-by="facility.name"
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
      ],
      dataWithFacilities: []
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"]),
    ...mapGetters("facilities", ["facilities"]),
    ...mapGetters("timetable", ["sessions"])
  },
  methods: {
    ...mapActions("facilities", {
      getActivity: "getActivities",
      getFacilities: "getFacilities"
    }),
    ...mapActions("timetable", {
      getSessions: "getAllSessions"
    }),
    async fillData() {},
    async getRelatedFacility() {
      let facilityArr = [];
      for (const activity of this.activities) {
        console.log(activity);
        const facilityId = activity._links.resource.href
          .split("/")
          .slice(-1)[0];
        const facilities = this.facilities;
        console.log(
          facilities.find(
            facility => Number(facility.id) === Number(facilityId)
          )
        );
        activity.facility = facilities.find(
          facility => Number(facility.id) === Number(facilityId)
        );
        facilityArr.push(activity);
      }
      return facilityArr;
    }
  },
  created: async function() {
    await this.getActivity();
    await this.getFacilities();
    this.dataWithFacilities = await this.getRelatedFacility();
    console.log(this.dataWithFacilities);
  }
};
</script>
