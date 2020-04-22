<template>
  <div class="facility">
    <b-card
      :title="facility.name"
      :img-src="this.imageUrl"
      @error="handleImageError"
      :img-alt="facility.name"
      img-top
      style="max-width: 20rem;"
    >
      <b-card-text>
        {{ facility.description }}
      </b-card-text>
      <b-row>
        <b-button
          :to="{ name: 'FacilityTimetable', params: { id: facility.id } }"
          variant="info"
          >See Timetable</b-button
        >
        <b-button
          :to="{ name: 'FacilityPage', params: { id: facility.id } }"
          variant="outline-primary"
          >View Details</b-button
        >
      </b-row>
    </b-card>
  </div>
</template>

<style scoped></style>

<script>
const defaultImage =
  "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80";

export default {
  name: "Facility",
  props: {
    facility: {
      type: Object,
      required: true
    }
  },
  methods: {
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
