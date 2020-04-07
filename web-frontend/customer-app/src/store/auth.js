const state = {
  user: null,
  error: null
};

const getters = {
  isAuthenticated: state => state.user !== null,
  user: state => state.user || {},
  permissions: state => state.claims
};

const mutations = {
  LOGIN: (state, payload) => (state.user = payload),
  LOGOUT: state => (state.user = null)
};

const actions = {
  login({ commit }, data) {
    commit("LOGIN", data);
  },
  logout({ commit }) {
    commit("LOGOUT");
  },
  loginWithRedirect() {}
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
