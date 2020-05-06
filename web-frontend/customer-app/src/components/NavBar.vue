<template>
  <header class="header-section">
    <b-navbar type="light" toggleable="lg">
      <b-navbar-brand to="/"
        ><img
          src="../assets/logo.png"
          style="height: 84.8px; width: 264.8px"
          alt=""
        />
      </b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
      <b-collapse class="main-menu" id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item to="/">Home</b-nav-item>
          <b-nav-item to="/timetable">Timetable</b-nav-item>
          <b-nav-item to="/facilities">Facilities</b-nav-item>
          <b-nav-item to="/activitiestable" v-if="isEmployeeOrManager"
            >Activities</b-nav-item
          >
          <b-nav-item to="/membership">Membership</b-nav-item>
          <b-nav-item-dropdown toggle-class="text-dark" text="Bookings">
            <b-dropdown-item to="/bookings">Create</b-dropdown-item>
            <b-dropdown-item to="/bookingtable">Manage</b-dropdown-item>
          </b-nav-item-dropdown>
          <b-nav-item-dropdown
            toggle-class="text-dark"
            text="Usage"
            v-if="isEmployeeOrManager"
          >
            <b-dropdown-item to="/weeklyusage">Table</b-dropdown-item>
            <b-dropdown-item to="/weeklyusagegraph">Graph</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          <div class="hr-box">
            <b-nav-item-dropdown
              toggle-class="text-dark"
              v-if="isAuthenticated"
              right
            >
              <!-- Using 'button-content' slot -->
              <template v-slot:button-content>
                <em>{{ user.name }}</em>
              </template>
              <div>
                <b-dropdown-item v-if="isAuthenticated" to="/profile"
                  >Profile
                </b-dropdown-item>
                <b-dropdown-item v-if="isAuthenticated" @click="logout">
                  Log out
                </b-dropdown-item>
              </div>
            </b-nav-item-dropdown>
            <b-nav-item v-else @click="login">Log in </b-nav-item>
          </div>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </header>
</template>
<style scoped>
.navbar-light .navbar-nav .nav-link {
  color: #1e1e1e !important;
  font-family: "PT Sans", sans-serif !important;
}
</style>
<script>
import { mapActions, mapGetters } from "vuex";

export default {
  name: "NavBar",
  computed: {
    ...mapGetters("auth", ["isAuthenticated", "user", "isEmployeeOrManager"])
  },
  methods: {
    ...mapActions("auth", { login: "loginWithRedirect", logout: "logout" })
  }
};
</script>
