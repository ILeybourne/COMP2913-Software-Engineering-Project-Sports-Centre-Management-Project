import axios from "@/plugins/axios.plugin";

const state = {
  customer: null
};

const getters = {
  customer: state => state.customer
};

const mutations = {
  SET_CUSTOMER: (state, payload) => (state.customer = payload)
};

const actions = {
  async getCustomerByEmail({ commit }, user_email) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get(`/customer/user?email=${user_email}`);
    commit("SET_CUSTOMER", data._embedded.customerDToes);
    commit("loading/FINISH_LOADING", null, { root: true });
  }
};

const namespaced = true;

const customers = {
  namespaced,
  state,
  getters,
  mutations,
  actions
};

export default customers;
