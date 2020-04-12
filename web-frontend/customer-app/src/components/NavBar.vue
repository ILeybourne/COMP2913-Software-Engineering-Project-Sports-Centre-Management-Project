<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="info">
      <b-navbar-brand to="/">Home</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item to="/about">About</b-nav-item>
          <b-nav-item to="/timetable">Timetable</b-nav-item>
          <b-nav-item to="/facilities">Facilities</b-nav-item>
          <b-nav-item to="/bookings">Bookings</b-nav-item>
          <b-nav-item to="/membership">Membership</b-nav-item>
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          <b-nav-item-dropdown v-if="isAuthenticated" right>
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
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";

export default {
  name: "NavBar",
  computed: {
    ...mapGetters("auth", ["isAuthenticated", "user"])
  },
  methods: {
    ...mapActions("auth", { login: "loginWithRedirect", logout: "logout" })
  }
};
</script>
