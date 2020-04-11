const state = {
  validation: []
};

const getters = {
  sessions: state => state.validation
};

const mutations = {
  SET_VALIDATION_ERRORS: (state, payload) => (state.validation = payload)
};

const actions = {
  setValidationErrors({ commit }, errors) {
    commit("SET_VALIDATION_ERRORS", errors);
  },
  clearValidationErrors({ commit }) {
    commit("SET_VALIDATION_ERRORS", []);
  }
};

const namespaced = true;

const facilities = {
  namespaced,
  state,
  getters,
  mutations,
  actions
};

export default facilities;
