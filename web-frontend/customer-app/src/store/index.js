import Vue from "vue";
import Vuex from "vuex";
import LoadingModule from "@/store/loader";
import AuthModule from "@/store/auth";
import CustomerModule from "@/store/customers";
import FacilitiesModule from "@/store/facilities";
import TimetableModule from "@/store/timetable";
import ValidationModule from "@/store/validation";
import CustomerModule from "@/store/customers";
import MembershipsModule from "@/store/memberships";

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
    validation: ValidationModule,
<<<<<<< HEAD
    customers: CustomerModule,
    memberships: MembershipsModule
=======
    customers: CustomerModule
>>>>>>> 7a38788275c60f202ea7a9f32c5681e205694072
  }
});
