import axios from "@/plugins/axios.plugin";

const state = {
  customers: []
};

const getters = {
  customers: state => state.customers
};

const mutations = {
  SET_CUSTOMERS: (state, payload) => (state.customers = payload)
};

const actions = {
  async getAllCustomers({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/customer?page=0&size=1000");
    commit("SET_CUSTOMERS", data._embedded.customerDToes);
    commit("loading/FINISH_LOADING", null, { root: true });
    return data;
  },
};

const namespaced = true;

const CustomerModule = {
  namespaced,
  state,
  getters,
  mutations,
  actions
};

export default CustomerModule;
