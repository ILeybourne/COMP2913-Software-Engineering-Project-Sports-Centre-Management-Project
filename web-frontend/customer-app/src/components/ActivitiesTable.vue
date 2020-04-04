<template>
  <div id="app">
    <h3 class="title">Activities Table</h3>
    <v-data-table
      v-model="selected"
      :headers="headers"
      :items="activities"
      :single-select="true"
      item-key="name"
      show-select
      class="elevation-1"
    >
      <template v-slot:top>
        <v-switch
          v-model="singleSelect"
          label="Single select"
          class="pa-3"
        ></v-switch>
      </template>
    </v-data-table>

    <b-modal id="edit-Activity-modal" title="Create Activity" hide-footer>
      <div class="d-flex justify-content-between">
        <b-button
          type="reset"
          variant="danger"
          @click="$bvModal.hide('edit-Activity-modal')"
          >Delete
        </b-button>
      </div>
    </b-modal>
  </div>
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
          value: "id",
          text: "Booking reference",
          sortable: true
        },
        {
          value: "name",
          text: "Activity",
          sortable: true
        },
        {
          value: "receipt",
          text: "Receipt",
          sortable: false
        }
      ],
      singleSelect: false,
      selected: []
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities"])
  },
  methods: {
    dtEditClick: props => alert("Click props:" + JSON.stringify(props)),
    ...mapActions("facilities", {
      getActivity: "getAllActivities"
    }),
    showCancel() {
      this.$bvModal.show("edit-Activity-modal");
    }
  },

  mounted: async function() {
    await this.getActivity();
  }
};
</script>
