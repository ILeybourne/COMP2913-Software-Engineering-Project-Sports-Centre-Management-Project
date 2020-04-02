<template>
  <div id="app">
    <h3 class="title">Activities Table</h3>
    <v-data-table
      v-model="selected"
      :headers="headers"
      :items="formattedData"
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
const addZero = value => ("0" + value).slice(-2);

const formatDate = value => {
  if (value) {
    const dt = new Date(value);
    return `${addZero(dt.getHours())}:${addZero(dt.getMinutes())}`;
  }
  return "";
};

export default {
  name: "ActivitiesTable",
  components: {},
  // ItemsPerPageDropdown
  data: function() {
    return {
      /*
        account: null
        activity: null
        createdAt: 1584288689000
        id: 1
        receipt: null
        updatedAt: 1584288692000
        * */
      activities: [],
      headers: [
        {
          value: "id",
          text: "Booking reference",
          sortable: true
        },
        {
          value: "formattedStartAt",
          text: "Activity Time",
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
    formattedData() {
      return this.activities.map(activity => {
        return {
          ...activity,
          formattedStartAt: formatDate(activity.startTime)
        };
      });
    }
  },
  methods: {
    dtEditClick: props => alert("Click props:" + JSON.stringify(props)),
    async getActivity() {
      const token = await this.$auth.getTokenSilently();

      const { data } = await this.$http.get(`/activities`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      this.activities = data.content;
    },
    showCancel() {
      this.$bvModal.show("edit-Activity-modal");
    }
  },

  mounted: async function() {
    await this.getActivity();
  }
};
</script>
