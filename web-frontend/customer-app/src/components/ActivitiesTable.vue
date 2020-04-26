<template>
  <v-data-table :headers="headers" :items="dataWithFacilities" :loding="true" >
    <template v-slot:top>
      <v-toolbar flat color="white">
        <v-spacer></v-spacer>
        <v-dialog max-width="500px"></v-dialog>
      </v-toolbar>
      <b-modal id="edit-modal" title="Edit Booking">
        <b-form>
          <b-form-group id="activity" label="Activity" label-for="Activity"
            ><b-form-input
              id="Activity"
              v-model="selectedActivity.name"
            ></b-form-input>
          </b-form-group>
          <b-form-group id="facility" label="Facility" label-for="Facility">
            <b-form-input
              id="Facility"
              v-model="selectedActivity.facility"
            ></b-form-input>
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
      deleteActivity: "deleteActivity",
      getFacilities: "getFacilities"
    }),
    showCancel() {
      this.$bvModal.show("edit-Activity-modal");
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
    deleteItem(item) {
      const index = this.activities.indexOf(item);
      console.log(index);
      confirm("Are you sure you want to delete this item?") &&
        this.deleteActivity(index);
    },
    async getRelatedFacility(){
      let facilityArr = [];
      for (const activity of this.activities) {
        console.log(activity);
        const facilityId = activity._links.resource.href.split("/").slice(-1)[0];
        const facilities = this.facilities;
        console.log(
          facilities.find(
            facility => Number(facility.id) === Number(facilityId)
          )
        );
        activity.facility = facilities.find(
          facility => Number(facility.id) === Number(facilityId)
        );
        facilityArr.push(activity)
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
