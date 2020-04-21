import axios from "@/plugins/axios.plugin";

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
  activities: []
};

const getters = {
  facilities: state => state.facilities,
  activities: state =>
    state.activities.map(activity => {
      return {
        ...activity,
        formattedCost: "£" + activity.cost.toFixed(2)
      };
    })
};

const mutations = {
  SET_FACILITIES: (state, payload) => (state.facilities = payload),
  SET_ACTIVITIES: (state, payload) => (state.activities = payload)
};

const actions = {
  async getFacilities({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/resources");
    const list = data._embedded.resourceDToes;
    commit("SET_FACILITIES", list);
    commit("loading/FINISH_LOADING", null, { root: true });
    return list;
  },
  async getActivities({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/activitytypes");
    const activities = data._embedded.activityTypeDToes;
    commit("SET_ACTIVITIES", activities);
    commit("loading/FINISH_LOADING", null, { root: true });
    return activities;
  },
  async deleteActivity({ commit }, activityId) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.delete(`/activitytypes/${activityId}`);
    commit("SET_SESSIONS", data);
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
