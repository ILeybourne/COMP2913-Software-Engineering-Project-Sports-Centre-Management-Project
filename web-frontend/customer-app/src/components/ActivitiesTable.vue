<template>
  <v-data-table :headers="headers" :items="activities">
    <template v-slot:top>
      <v-toolbar flat color="white">
        <v-spacer></v-spacer>
        <v-dialog max-width="500px">
          <template v-slot:activator="{ on }">
            <v-btn color="primary" dark class="mb-2" v-on="on">New Item</v-btn>
          </template>
        </v-dialog>
      </v-toolbar>
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
          value: "resource.name",
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
      ]
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"]),
    formTitle() {
      return this.editedIndex === -1 ? "New Item" : "Edit Item";
    }
  },

  methods: {
    dtEditClick: props => alert("Click props:" + JSON.stringify(props)),
    ...mapActions("facilities", {
      getActivity: "getActivities"
    }),
    showCancel() {
      this.$bvModal.show("edit-Activity-modal");
    },
    editItem(item) {
      this.editedIndex = this.activities.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },
    deleteItem(item) {
      const index = this.activities.indexOf(item);
      confirm("Are you sure you want to delete this item?") &&
        this.activities.splice(index, 1);
    }
  },

  mounted: async function() {
    await this.getActivity();
  }
};
</script>
