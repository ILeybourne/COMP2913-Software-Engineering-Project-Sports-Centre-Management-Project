<template>
  <v-data-table :headers="headers" :items="dataWithFacilities" :loding="true">
    <template v-slot:top>
      <v-toolbar flat color="white">
        <v-spacer></v-spacer>
        <v-dialog max-width="500px"></v-dialog>
      </v-toolbar>
      <v-btn color="primary" dark class="mb-2" @click="showNewActivity()"
        >New Activity</v-btn
      >
      <b-modal
        id="new-activity-modal"
        title="New Activity Type"
        @ok="addActivity()"
      >
        <b-form>
          <b-form-group id="activity" label="Activity Name" label-for="Activity"
            ><b-form-input
              id="Activity"
              v-model="newActivity.name"
            ></b-form-input>
          </b-form-group>
          <b-form-group id="facility" label="Facility" label-for="Facility">
            <b-form-select
              id="Facility"
              :options="setFacilityOptions()"
              v-model="newActivity.facility"
            ></b-form-select>
          </b-form-group>
          <b-form-group id="capacity" label="Capacity" label-for="Capacity">
            <b-form-input
              id="Capacity"
              v-model="newActivity.capacity"
            ></b-form-input>
          </b-form-group>
          <b-form-group id="cost" label="Cost" label-for="Cost">
            <b-form-input id="Cost" v-model="newActivity.cost"></b-form-input>
          </b-form-group>
        </b-form>
      </b-modal>
      <b-modal
        id="edit-modal"
        title="Edit Activity Type"
        @ok="updateActivity()"
      >
        <b-form>
          <b-form-group id="activity" label="Activity Name" label-for="Activity"
            ><b-form-input
              id="Activity"
              :options="setActivityOptions()"
              v-model="selectedActivity.name"
            ></b-form-input>
          </b-form-group>
          <b-form-group id="facility" label="Facility" label-for="Facility">
            <b-form-select
              id="Facility"
              :options="[selectedActivity.facility]"
              v-model="selectedActivity.facility"
            ></b-form-select>
          </b-form-group>
          <b-form-group id="capacity" label="Capacity" label-for="Capacity">
            <b-form-input
              id="Capacity"
              v-model="selectedActivity.capacity"
            ></b-form-input>
          </b-form-group>
          <b-form-group id="cost" label="Cost" label-for="Cost">
            <b-form-input
              id="Cost"
              v-model="selectedActivity.cost"
            ></b-form-input>
          </b-form-group>
        </b-form>
      </b-modal>
    </template>
    <template v-slot:item.actions="{ item }">
      <v-icon small class="mr-2" @click="editItem(item)">
        mdi-pencil
      </v-icon>
    </template>
  </v-data-table>
</template>

<style scoped></style>
<script>
import { mapActions, mapGetters } from "vuex";

export default {
  name: "ActivitiesTable",
  components: {},
  // ItemsPerPageDropdown
  data: function() {
    return {
      headers: [
        {
          value: "name",
          text: "Activity",
          sortable: true
        },
        {
          value: "totalCapacity",
          text: "Capacity",
          sortable: true
        },
        {
          value: "facility.name",
          text: "Facility"
        },
        {
          value: "formattedCost",
          text: "Cost"
        },
        {
          value: "actions",
          text: "Actions",
          sortable: false
        }
      ],
      selectedActivity: {
        id: null,
        name: null,
        capacity: null,
        facility: null,
        cost: null
      },
      newActivity: {
        id: null,
        name: null,
        capacity: null,
        facility: null,
        cost: null
      },
      dataWithFacilities: []
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"]),
    ...mapGetters("facilities", ["facilities"])
  },

  methods: {
    dtEditClick: props => alert("Click props:" + JSON.stringify(props)),
    ...mapActions("facilities", {
      getActivity: "getActivityTypes",
      getFacilities: "getFacilities"
    }),
    showNewActivity() {
      this.$bvModal.show("new-activity-modal");
    },
    editItem(item) {
      console.log(item);
      this.selectedActivity.id = item.id;
      this.selectedActivity.name = item.name;
      this.selectedActivity.facility = item.facility.name;
      this.selectedActivity.capacity = item.totalCapacity;
      this.selectedActivity.cost = item.cost;
      this.$bvModal.show("edit-modal");
    },
    setFacilityOptions() {
      let facilities = this.facilities;
      let facilityArray = [{ value: null, text: "Please Select" }];
      for (const facility of facilities) {
        facilityArray.push({ value: facility.name, text: facility.name });
      }
      return facilityArray;
    },
    setActivityOptions() {
      let activities = this.activities;
      let activityArray = [{ value: null, text: "Please Select" }];
      for (const activity of activities) {
        activityArray.push({ value: activity.name, text: activity.name });
      }
      return activityArray;
    },
    async updateActivity() {
      const id = this.selectedActivity.id;
      this.dataWithFacilities.find(activity => activity.id === id);
      const body = {
        name: this.selectedActivity.name, //need to be dynamic
        cost: this.selectedActivity.cost,
        totalCapacity: this.selectedActivity.capacity
      };
      await this.$http
        .put("/activitytypes/" + id, body)
        .then(response => {
          console.log(response);
        })
        .catch(function() {
          //console.log(error);
        });
    },
    async addActivity() {
      let facilityId = null;
      for (const facility of this.facilities) {
        if (facility.name === this.newActivity.facility) {
          facilityId = facility.id;
        }
      }
      const body = {
        name: this.newActivity.name, //need to be dynamic
        cost: this.newActivity.cost,
        totalCapacity: this.newActivity.capacity
      };
      await this.$http
        .post("/activitytypes/resource/" + facilityId, body)
        .then(response => {
          console.log(response);
        })
        .catch(function() {
          //console.log(error);
        });
    },
    async getRelatedFacility() {
      let facilityArr = [];
      for (const activity of this.activities) {
        console.log(activity);
        const facilityId = activity.facility_id;
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
  }
};
</script>
