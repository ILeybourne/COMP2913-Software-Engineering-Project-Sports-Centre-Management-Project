import Vue from "vue";
import Vuex from "vuex";
import LoadingModule from "@/store/loader";
import AuthModule from "@/store/auth";
import FacilitiesModule from "@/store/facilities";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {},
  mutations: {},
  actions: {},
  modules: {
    loading: LoadingModule,
    auth: AuthModule,
    facilities: FacilitiesModule
  }
});
