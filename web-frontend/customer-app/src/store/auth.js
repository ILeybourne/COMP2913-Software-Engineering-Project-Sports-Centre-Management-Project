import axios from "@/plugins/axios.plugin";

const state = {
  user: null,
  error: null,
  customers: [],
  membership: null
};

const getters = {
  isAuthenticated: state => state.user !== null,
  user: state => state.user || {},
  permissions: state => state.claims,
  customers: state => state.customers
};

const mutations = {
  LOGIN: (state, payload) => (state.user = payload),
  LOGOUT: state => (state.user = null),
  SET_CUSTOMERS: (state, payload) => (state.customers = payload)
};

const actions = {
  login({ commit }, data) {
    commit("LOGIN", data);
  },
  logout({ commit }) {
    commit("LOGOUT");
  },
  loginWithRedirect() {},
  async getCustomers({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/customer");
    commit("SET_CUSTOMERS", data._embedded.customerDToes);
    commit("loading/FINISH_LOADING", null, { root: true });
  }
};

const namespaced = true;

const AuthModule = {
  namespaced,
  state,
  getters,
  mutations,
  actions
};

export default AuthModule;
