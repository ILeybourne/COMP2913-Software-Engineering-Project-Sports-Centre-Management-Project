<template>
  <div>
    <graph-line
      :width="600"
      :height="400"
      :shape="'normal'"
      :axis-min="0"
      :axis-max="50"
      :axis-full-mode="true"
      :labels="['1Q', '2Q', '3Q', '4Q']"
      :names="names"
      :values="values"
    >
      <note :text="'Line Chart'"></note>
      <tooltip :names="names" :position="'right'"></tooltip>
      <legends :names="names"></legends>
      <guideline :tooltip-y="true"></guideline>
    </graph-line>
  </div
></template>

<style scoped></style>
<script>

import { mapActions, mapGetters } from "vuex";

export default {
  data: function() {
    return {
      names: ["MS", "Apple", "Google"],
      values: [
        [10, 5, 5, 5],
        [40, 10, 10, 10],
        [30, 30, 30, 30]
      ]
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
