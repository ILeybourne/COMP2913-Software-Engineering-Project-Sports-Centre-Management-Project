<template>
  <div class="facility">
    <b-card
      class="facility-card"
      :img-src="this.imageUrl"
      @error="handleImageError"
      :img-alt="facility.name"
      img-top
    >
      <b-card-title class="title"
        ><span>{{ facility.name }}</span></b-card-title
      >
      <b-card-text class="description">
        {{ facility.description }}
      </b-card-text>
      <b-row class="facility-details">
        <b-button
          class="button"
          id="my-timetable"
          variant="outline-light"
          :to="{ name: 'FacilityTimetable', params: { id: facility.id } }"
          ><b>See Timetable</b></b-button
        >
        <b-button
          class="button"
          id="button-details"
          :to="{ name: 'FacilityPage', params: { id: facility.id } }"
          variant="outline-light"
          ><b>View Details</b></b-button
        >
      </b-row>
    </b-card>
  </div>
</template>

<style scoped>
.btn-primary {
  color: yellow !important;
}

.btn-outline-primary {
  color: yellow !important;
}
.facility-card {
  max-width: 20rem;
  min-width: 15rem;
  margin: 10px;
  flex-direction: column;
  text-align: center;
  color: #f6f9fa !important;
  background: #1f1f1f;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}
.facility-details {
  text-align: center;
  justify-content: center;
}
.description {
  color: whitesmoke;
}
.button {
  width: auto;
  height: auto;
  margin: 5px !important;
}
#button-timetable {
}
.title {
  color: #f6f9fa !important;
  text-decoration-color: #f6f9fa !important;
}
.title span {
  background: transparent;
  padding: 3px;
}
.facility-card:hover .title span {
  background: #fcff18;
  padding: 3px;
  color: #1f1f1f;
}

#button-details {
  border-color: yellow !important;
  color: yellow !important;
}
#button-details:hover {
  background-color: yellow !important;
  color: #1f1f1f !important;
}
#my-timetable {
  border-color: yellow !important;
  color: yellow !important;
}
#my-timetable:hover {
  background-color: yellow !important;
  color: #1f1f1f !important;
}
</style>

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
