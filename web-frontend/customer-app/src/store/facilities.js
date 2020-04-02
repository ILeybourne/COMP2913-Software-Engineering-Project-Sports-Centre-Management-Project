import axios from "@/services/auth-http.service";

const state = {
  facilities: []
};

const getters = {
  facilities: state => state.facilities
};

const mutations = {
  SET_FACILITIES: (state, payload) => (state.facilities = payload)
};

const actions = {
  async getFacilities({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/resources");
    commit("SET_FACILITIES", data.content);
    commit("loading/FINISH_LOADING", null, { root: true });
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
