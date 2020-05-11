<template>
  <v-app>
    <div class="container">
      <div class="row">
        <div class="col-8">
          <div class="heading-div">
            <h1><span>Price</span> List</h1>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <v-data-table
            class="white--text"
            dark
            :headers="headers"
            :items="activities"
            item-key="id"
            group-by="facility.name"
          >
            <template
              class="white"
              v-slot:group.header="{ items, isOpen, toggle }"
            >
              <th colspan="5" bgcolor="#404040" class="white--text">
                <v-icon class="yellow--text" @click="toggle"
                  >{{ isOpen ? "mdi-minus" : "mdi-plus" }}
                </v-icon>
                {{ items[0].facility.name }}
              </th>
            </template>
          </v-data-table>
        </div>
      </div>
    </div>
  </v-app>
</template>
<style scoped>
@media screen and (max-width: 600px) {
  .heading-div h1 {
    font-size: 10vw;
  }
}
.heading-div {
  margin-bottom: 20px;
}
.heading-div h1 {
  width: 60%;
  margin: auto;
}
.heading-div p {
  width: 100%;
  padding: 10px;
}
.heading-div span {
  background: #fcff18;
}
</style>
<script>
import { mapActions, mapGetters } from "vuex";

export default {
  name: "FacilityPriceListing",
  data: function() {
    return {
      headers: [
        {
          value: "name",
          text: "ACTIVITY",
          sortable: true,
          class: "yellow--text title font-weight-bold"
        },
        {
          value: "formattedCost",
          text: "FULL PRICE",
          sortable: false,
          class: "yellow--text title font-weight-bold"
        },
        {
          value: "memberPricing",
          text: "MEMBER PRICE",
          sortable: false,
          class: "yellow--text title font-weight-bold"
        },
        {
          value: "totalCapacity",
          text: "TOTAL CAPACITY",
          sortable: false,
          class: "yellow--text title font-weight-bold"
        }
      ]
    };
  },
  computed: {
    ...mapGetters("facilities", ["activities", "facilities"])
  },
  methods: {
    ...mapActions("facilities", ["getActivities", "getFacilities"])
  },
  async mounted() {
    await this.getFacilities();
    await this.getActivities();
    console.log(this.activities);
  }
};
</script>
