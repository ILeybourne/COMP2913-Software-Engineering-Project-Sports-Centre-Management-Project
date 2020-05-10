<template>
  <v-app class="usage-container">
    <v-container class="new-activity">
      <v-btn
        class="black--text"
        color="yellow"
        type="submit"
        value="submit"
        v-on:click="showNewActivity()"
      >
        <b>New Activity</b>
      </v-btn>
    </v-container>

    <v-data-table
      :headers="headers"
      :items="sorted"
      class="white--text"
      :loding="true"
      :footer-props="footerProps"
      :items-per-page="10"
      dark
    >
      <template v-slot:top>
        <v-toolbar flat color="white">
          <v-spacer></v-spacer>
          <v-dialog max-width="500px"></v-dialog>
        </v-toolbar>
        <b-modal
          id="new-activity-modal"
          title="New Activity Type"
          :ok-disabled="!nameState"
          @ok="addActivity()"
        >
          <b-form>
            <b-form-group
              id="activity"
              label="Activity Name"
              label-for="Activity"
              ><b-form-input
                id="Activity"
                v-model="newActivity.name"
                placeholder="Activity Name"
                :state="nameState"
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
          :ok-disabled="!currentNameState"
          @ok="updateActivity()"
        >
          <b-form>
            <b-form-group
              id="activity"
              label="Activity Name"
              label-for="Activity"
              ><b-form-input
                id="Activity"
                v-model="selectedActivity.name"
                :state="currentNameState"
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
        <v-icon small @click="deleteItem(item)">
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>
  </v-app>
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
      footerProps: {
        "items-per-page-options": [5, 10, 20, 100]
      },
      headers: [
        {
          value: "name",
          text: "ACTIVITY",
          sortable: true,
          class: "yellow--text heading font-weight-bold"
        },
        {
          value: "totalCapacity",
          text: "CAPACITY",
          sortable: true,
          class: "yellow--text heading font-weight-bold"
        },
        {
          value: "facility.name",
          text: "FACILITY",
          class: "yellow--text heading font-weight-bold"
        },
        {
          value: "formattedCost",
          text: "COST",
          class: "yellow--text heading font-weight-bold"
        },
        {
          value: "actions",
          text: "ACTIONS",
          sortable: false,
          class: "yellow--text heading font-weight-bold"
        }
      ],
      selectedActivity: {
        id: null,
        name: " ",
        capacity: null,
        facility: null,
        cost: null
      },
      newActivity: {
        id: null,
        name: " ",
        capacity: null,
        facility: null,
        cost: null
      }
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"]),
    ...mapGetters("facilities", ["facilities"]),
    dataWithFacilities() {
      let facilityArr = [];
      for (const activity of this.activities) {
        const facilityId = activity.facility_id;
        const facilities = this.facilities;
        activity.facility = facilities.find(
          facility => Number(facility.id) === Number(facilityId)
        );
        facilityArr.push(activity);
      }
      return facilityArr;
    },
    sorted() {
      return this.dataWithFacilities.slice().sort(function(a, b) {
        return a.id - b.id;
      });
    },
    nameState() {
      const str = this.newActivity.name;
      return str.length < 21;
    },
    currentNameState() {
      const str = this.selectedActivity.name;
      return str.length < 21;
    }
  },

  methods: {
    dtEditClick: props => alert("Click props:" + JSON.stringify(props)),
    ...mapActions("facilities", {
      getActivity: "getActivities",
      getFacilities: "getFacilities",
      updateActivityTypes: "updateActivityTypes",
      createActivityType: "createActivityType",
      deleteActivityType: "deleteActivity"
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
      const activityId = Number(this.selectedActivity.id);
      this.dataWithFacilities.find(activity => activity.id === activityId);
      const body = {
        name: this.selectedActivity.name, //need to be dynamic
        cost: this.selectedActivity.cost,
        totalCapacity: this.selectedActivity.capacity
      };
      await this.updateActivityTypes({ activityId, body });
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
      await this.createActivityType({ facilityId, body });
    },
    async deleteItem(item) {
      const id = item.id;
      const index = this.sorted.indexOf(item);
      try {
        confirm("Are you sure you want to delete this item?") &&
          this.sorted.splice(index, 1) &&
          this.deleteActivityType(id);
      } catch (error) {
        console.log(error.response.status);
      }
    }
  },

  created: async function() {
    await this.getActivity();
    await this.getFacilities();
    console.log(this.activities);
  }
};
</script>
