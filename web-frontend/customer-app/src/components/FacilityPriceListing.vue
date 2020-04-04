<template>
  <div class="table">
    <thead>
      <th scope="col">Activity</th>
      <th scope="col">Member Cost</th>
      <th scope="col">Non-Member Cost</th>
    </thead>
    <tbody>
      <tr v-for="item in rowData">
        <th scope="row">{{ item.name }}</th>
        <td scope="row">{{ item.memCost }}</td>
        <td scope="row">{{ item.nonMemCost }}</td>
      </tr>
    </tbody>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { VueTableDynamic } from 'vue-table-dynamic'

export default {
  name: "CentrePriceListing",
  components: { VueTableDynamic },
  data: function() {
    return {
      rowData: []
    }
  },
  created: function() {
    this.formatData();
  },
  methods: {
    formatData: function() {
      for (let resource in facilities) {
        for (let activityType in resource) {
          let object = {
            name:activityType.name,
            memCost:activityType.cost,
            nonMemCost:activityType.cost // TODO: differentiate member and non-member costs
          };
          this.rowData.push(object)
        }
      }
    }
  },
  computed: {
    ...mapGetters("facilities", ["facilities"])
  }
}
</script>

<style scoped></style>