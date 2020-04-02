<template>
  <div id="app" v-if="!blocking">
    <NavBar />
    <router-view />
    <Footer />
  </div>
  <PreLoadSpinner v-else></PreLoadSpinner>
</template>

<style lang="scss"></style>

<script>
import PreLoadSpinner from "@/components/PreLoadComponent.vue";
import NavBar from "@/components/NavBar.vue";
import Footer from "@/components/Footer.vue";
import { mapGetters } from "vuex";

export default {
  name: "Home",
  components: {
    NavBar,
    Footer,
    PreLoadSpinner
  },
  computed: {
    ...mapGetters("loading", ["blocking"])
  }
};
</script>
