import Vue from "vue";
import App from "@/App.vue";
import router from "@/router";
import store from "@/store";
import { BootstrapVue, BootstrapVueIcons } from "bootstrap-vue";

import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";

import { domain, clientId, audience } from "../auth_config.json";
import { Auth0Plugin } from "@/services/auth.service";
import authHttp from "@/services/auth-http.service";
import VueAxios from "vue-axios";
import { mapActions, mapMutations } from "vuex";
// import { ServicesPlugin } from "@/services/ServicesPlugin";
import vuetify from "@/plugins/vuetify";
import VueGraph from "vue-graph";
Vue.use(VueGraph);

Vue.use(Auth0Plugin, {
  domain,
  clientId,
  audience,
  onRedirectCallback: appState => {
    if (appState && appState.targetUrl) {
      // take the redirect url from Auth0
      router.push(appState.targetUrl);
    } else {
      // use url from the page you called login from
      router.push(window.location.pathname);
    }
  }
});
Vue.use(VueAxios, authHttp);
Vue.use(BootstrapVue);
Vue.use(BootstrapVueIcons);
// Vue.use(ServicesPlugin);
Vue.config.productionTip = false;

const vm = new Vue({
  router,
  vuetify,
  store,
  render: h => h(App),
  methods: {
    ...mapActions("auth", {
      setUser: "login",
      logoutAuth: "logout"
    }),
    ...mapMutations("loading", { unblock: "ENABLE_LOAD" })
  },
  created() {
    // for every request we initialise the auth, asynchronously
    // Hence we need to block everything, until that has been fulfilled
    this.$auth.$watch("loading", loading => {
      if (loading === false) {
        if (this.$auth.error) {
          // debugger;
        }
        // When the auth0 client loads, we can unblock the UI
        // debugger;
        this.unblock();
        this.setUser(this.$auth.user || null);
      }
    });

    this.$store.subscribeAction(action => {
      if (action.type === "auth/logout") {
        this.$auth.logout();
      } else if (action.type === "auth/loginWithRedirect") {
        this.$auth.loginWithRedirect();
      }
    });
  }
}).$mount("#app");

store.$app = vm;
