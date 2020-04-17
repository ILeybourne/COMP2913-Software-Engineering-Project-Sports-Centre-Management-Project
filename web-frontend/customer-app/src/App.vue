<template>
  <div id="app" v-if="!blocking">
    <NavBar />
    <div class="container">
      <router-view />
    </div>
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
  },
  watch: {
    // eslint-disable-next-line no-unused-vars
    $route(to, from) {
      document.title = to.meta.title || "Zenergy";
    },
    immediate: true
  }
};
</script>
