<template>
    <div class="home">
        <HelloWorld></HelloWorld>
        <Facility></Facility>
        <Facility></Facility>
        <BookingInformation></BookingInformation>
        <Timetable></Timetable>
    </div>
  <div class="home">
    <!-- Check that the SDK client is not currently loading before accessing is methods -->
    <div v-if="!$auth.loading">
      <!-- show login when not authenticated -->
      <button v-if="!$auth.isAuthenticated" @click="login">Log in</button>
      <!-- show logout when authenticated -->
      <button v-if="$auth.isAuthenticated" @click="logout">Log out</button>
    </div>
    <img alt="Vue logo" src="../assets/logo.png" />
    <HelloWorld msg="Welcome to Your Vue.js App Elliot" />
    <Test></Test>
  </div>
</template>

<script>
    // @ is an alias to /src
    //import HelloWorld from "@/components/HelloWorld.vue";
    import Timetable from "@/components/Timetable.vue";
    import BookingInformation from "@/components/BookingInformation.vue"
    import Facility from "@/components/Facility.vue"
    import HelloWorld from "@/components/HelloWorld.vue"


export default {
    name: "Home",
    components: {
        HelloWorld,
        Timetable,
        BookingInformation,
        Facility
  },
  methods: {
    // Log the user in
    login() {
      this.$auth.loginWithRedirect();
    },
    // Log the user out
    logout() {
      this.$auth.logout({
        returnTo: window.location.origin
      });
    }
  }
};
</script>
