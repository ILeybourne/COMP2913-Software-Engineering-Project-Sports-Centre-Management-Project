<template>
  <div id="app" v-if="!blocking">
    <NavBar />
    <div id="content" class="container">
      <router-view />
    </div>
    <Footer id="footer" />
  </div>
  <PreLoadSpinner v-else></PreLoadSpinner>
</template>

<style scoped>
#app {
  position: relative;
  min-height: 80vh;
}

#content {
  padding-bottom: 2.5rem; /* Footer height */
}

#footer {
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 2.5rem; /* Footer height */
}
</style>

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
