<template>
  <div id="app">
    <h3 class="title">Bookings Table</h3>
    <v-data-table
      v-model="selected"
      :headers="headers"
      :items="bookings"
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

    <b-modal id="edit-booking-modal" title="Create Activity" hide-footer>
      <div class="d-flex justify-content-between">
        <b-button
          type="reset"
          variant="danger"
          @click="$bvModal.hide('edit-booking-modal')"
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
    return `${addZero(dt.getDate())}/${addZero(
      dt.getMonth() + 1
    )}/${dt.getFullYear()}`;
  }
  return "";
};

export default {
  name: "BookingTable",
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
      bookings: [],
      headers: [
        {
          value: "id",
          text: "Booking reference",
          sortable: true
        },
        {
          value: "createdAt",
          text: "Booking Time",
          sortable: true,
          format: formatDate
        },
        {
          value: "activity.name",
          text: "Activity",
          sortable: true
        },
        {
          value: "account",
          text: "Account",
          sortable: true
        },
        {
          value: "email",
          text: "Email",
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
  computed: {},
  methods: {
    dtEditClick: props => alert("Click props:" + JSON.stringify(props)),
    async getBooking() {
      const token = await this.$auth.getTokenSilently();

      const { data } = await this.$http.get(`/bookings`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      this.bookings = data.content;
    },
    showCancel() {
      this.$bvModal.show("edit-booking-modal");
    }
  },

  async mounted() {
    await this.getBooking();
  }
};
</script>
