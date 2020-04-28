import axios from "@/plugins/axios.plugin";

const state = {
<<<<<<< HEAD
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
=======
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
>>>>>>> 7a38788275c60f202ea7a9f32c5681e205694072
  }
};

const namespaced = true;

<<<<<<< HEAD
const customers = {
=======
const CustomerModule = {
>>>>>>> 7a38788275c60f202ea7a9f32c5681e205694072
  namespaced,
  state,
  getters,
  mutations,
  actions
};

<<<<<<< HEAD
export default customers;
=======
export default CustomerModule;
>>>>>>> 7a38788275c60f202ea7a9f32c5681e205694072
