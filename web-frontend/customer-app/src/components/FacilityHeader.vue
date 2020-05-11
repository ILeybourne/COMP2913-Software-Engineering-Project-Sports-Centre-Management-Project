<template>
  <div class="facility-header">
    <div v-if="error" class="alert alert-warning">{{ error }}</div>
    <b-card
      :title="'Details for ' + facility.name"
      :img-src="imageUrl"
      class="facility-card"
    >
      <b-card-text>{{
        facility.description || "No description available..."
      }}</b-card-text>
      <b-button
        @click.prevent="onDeleteFacility()"
        v-if="isEmployeeOrManager"
        class="float-right"
        variant="danger"
        >Delete</b-button
      >
    </b-card>
  </div>
</template>
<style scoped>
.facility-card {
  flex-direction: column;
  text-align: center;
  background: #f6f9fa;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}
</style>
<script>
import { mapActions, mapGetters } from "vuex";

const defaultImage =
  "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80";

export default {
  name: "FacilityHeader",
  props: {
    facility: {
      type: Object,
      required: true
    }
  },
  methods: {
    ...mapActions("facilities", ["getFacilities", "deleteFacility"]),
    handleImageError() {
      this.fallback = true;
    },
    async onDeleteFacility() {
      const result = await this.deleteFacility(this.facility.id);
      if (result) {
        this.$router.push({ name: "FacilityPage" });
      } else {
        this.error = "Facility could not be deleted";
      }
    }
  },
  computed: {
    ...mapGetters("auth", ["isEmployeeOrManager"]),
    imageUrl() {
      if (!this.fallback) {
        // try download the image from the server
        const link = this.facility._links["View image for resource"];
        if (link) {
          return link.href;
        }
      }
      return defaultImage;
    }
  },
  data() {
    return {
      fallback: false,
      error: null
    };
  }
};
</script>
