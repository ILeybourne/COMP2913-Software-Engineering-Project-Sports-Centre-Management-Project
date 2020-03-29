<template>
  <div id="app">
    <h3 class="title">Activities Table</h3>
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
import orderBy from "lodash.orderby";

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

const dummyData = [];

export default {
  name: "ActivitiesTable",
  components: {

  },
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
      headerFields: [
        {
          value: "id",
          text: "Booking reference",
          sortable: true
        },
        {
          value: "createdAt",
          text: "Activity Time",
          sortable: true,
          format: formatDate
        },
        {
          value: "activity",
          text: "Activity",
          sortable: true
        },
        {
          value: "receipt",
          text: "Receipt",
          sortable: false
        },
        "__slot:actions"
      ],
      // headerFields: ["Account", "Activity Time", "Activity Reference", "Receipt"],
      data: dummyData.slice(0, 10),
      itemsPerPageCss: {
        select: "item-per-page-dropdown"
      }
    };
  },
  computed: {},
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
    dtUpdateSort: function({ sortField, sort }) {
      const sortedData = orderBy(dummyData, [sortField], [sort]);
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = this.currentPage * this.itemsPerPage;
      this.data = sortedData.slice(start, end);
      console.log("load data based on new sort", this.currentPage);
    },
    updateItemsPerPage: function(itemsPerPage) {
      this.itemsPerPage = itemsPerPage;
      if (itemsPerPage >= dummyData.length) {
        this.data = dummyData;
      } else {
        this.data = dummyData.slice(0, itemsPerPage);
      }
      console.log("load data with new items per page number", itemsPerPage);
    },
    changePage: function(currentPage) {
      this.currentPage = currentPage;
      const start = (currentPage - 1) * this.itemsPerPage;
      const end = currentPage * this.itemsPerPage;
      this.data = dummyData.slice(start, end);
      console.log("load data for the new page", currentPage);
    },
    updateCurrentPage: function(currentPage) {
      this.currentPage = currentPage;
      console.log("update current page without need to load data", currentPage);
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
