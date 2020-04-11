import Vue from "vue";
import Vuex from "vuex";
import LoadingModule from "@/store/loader";
import AuthModule from "@/store/auth";
import FacilitiesModule from "@/store/facilities";
import TimetableModule from "@/store/timetable";
import ValidationModule from "@/store/validation";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {},
  mutations: {},
  actions: {},
  modules: {
    loading: LoadingModule,
    auth: AuthModule,
    facilities: FacilitiesModule,
    timetable: TimetableModule,
    validation: ValidationModule
  }
});
