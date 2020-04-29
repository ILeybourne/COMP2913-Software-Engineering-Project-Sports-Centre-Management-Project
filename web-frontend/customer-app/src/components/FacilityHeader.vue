<template>
  <div class="facility-header">
    <b-card :title="'Details for ' + facility.name" :img-src="imageUrl">
      <b-card-text>{{ facility.description || "No description available..." }}</b-card-text>
      <b-button v-if="isEmployeeOrManager" class="float-right" variant="danger">Delete</b-button>
    </b-card>
  </div>
</template>

<script>
import { mapActions } from "vuex";

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
    ...mapActions("facilities", ["getFacilities"]),
    handleImageError() {
      this.fallback = true;
    }
  },
  computed: {
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
      fallback: false
    };
  }
};
</script>

<style scoped></style>
