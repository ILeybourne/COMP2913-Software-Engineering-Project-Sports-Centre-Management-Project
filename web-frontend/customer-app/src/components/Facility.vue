<template>
  <div class="facility" id="facilityComponent">
    <div class="facility-container">
      <div class="image">
        <!-- check licence-->
        <img
          src="https://images.unsplash.com/photo-1534438327276-14e5300c3a48?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80"
          width="800"
          height="500"
        />
      </div>
      <div class="facility-info">
        info
        <div class="facility-btn">
          <button type="button" class="btn btn-outline-secondary" id="facilityButton">
            Book Now
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.facility {
  min-width: 25%;
  min-height: 300px;
  max-width: 50%;
  max-height: 50%;
}

.facility-container {
  width: 100%;
  height: 100%;
}

.image {
  width: 100%;
  margin: 0 auto;
  overflow: hidden;
  position: relative;
  height: 300px;
}

img {
  position: absolute;
}

.facility-info {
  width: 100%;
  height: 20%;
  background-color: #dcdcdc;
  text-align: center;
}

.facility-btn {
  margin: auto;
  width: 50%;
}

button {
  width: 100%;
}
</style>

<!--TODO pass image to component-->
<script>
export default {
  name: "Facility"
};
</script>
