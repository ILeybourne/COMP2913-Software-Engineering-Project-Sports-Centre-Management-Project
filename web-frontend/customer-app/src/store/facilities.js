import axios from "@/services/auth-http.service";


const state = {
  facilities: [],
  /*
    account: null
    activity: null
    createdAt: 1584288689000
    id: 1
    receipt: null
    updatedAt: 1584288692000
    * */
  activities: [],
};

const getters = {
  facilities: state => state.facilities,
  activities: state => state.activities,

};

const mutations = {
  SET_FACILITIES: (state, payload) => (state.facilities = payload),
  SET_ACTIVITIES: (state, payload) => (state.activities = payload)
};

const actions = {
  async getAllFacilities({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/resources");
    commit("SET_FACILITIES", data.content);
    commit("loading/FINISH_LOADING", null, { root: true });
  },
  async getAllActivities({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/activitytypes");
    commit("SET_ACTIVITIES", data);
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
