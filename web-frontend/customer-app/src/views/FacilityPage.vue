<template>
  <div class="facility-page">
    <Facilities v-if="id === 'all'" class="facilities"></Facilities>
    <FacilityCreate v-else-if="id === 'create'"></FacilityCreate>
    <div v-else class="facility">
      <div v-if="facility">
        <FacilityHeader :facility="facility"></FacilityHeader>
        <b-card :title="facility.name" no-body>
          <b-card-header header-tag="nav">
            <b-nav card-header tabs>
              <b-nav-item
                v-if="isEmployeeOrManager"
                :to="{ name: 'FacilityEdit', params: { id: facility.id } }"
                ><h5><span>Edit</span></h5></b-nav-item
              >
              <b-nav-item
                title="View Facility Timetable"
                :to="{ name: 'FacilityTimetable', params: { id: facility.id } }"
                ><h5><span>See Timetable</span></h5></b-nav-item
              >
            </b-nav>
          </b-card-header>
          <b-card-body>
            <router-view></router-view>
          </b-card-body>
        </b-card>
      </div>
      <div v-else>
        <h2>Facility not found!</h2>
      </div>
    </div>
  </div>
</template>

<script>
import store from "@/store";
import Facilities from "@/views/Facilities";
import FacilityHeader from "@/components/FacilityHeader";
import FacilityCreate from "@/components/FacilityCreate";
import { mapActions, mapGetters } from "vuex";

export default {
  name: "FacilityPage",
  components: { Facilities, FacilityHeader, FacilityCreate },
  props: {
    id: {
      default: "all"
    }
  },
  computed: {
    ...mapGetters("auth", ["isEmployeeOrManager"]),
    facility() {
      return store.getters["facilities/getFacilityById"](this.id) || null;
    }
  },
  methods: {
    ...mapActions("facilities", ["getFacilities"])
  },
  async mounted() {
    await this.getFacilities();
  }
};
</script>

<style scoped>
.card {
  background: #f6f9fa;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}
span {
  background: #fcff18;
  padding: 4px;
}
</style>
