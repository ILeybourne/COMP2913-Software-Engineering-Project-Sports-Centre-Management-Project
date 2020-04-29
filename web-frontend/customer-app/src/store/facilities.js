import axios from "@/plugins/axios.plugin";


const addZero = value => ("0" + value).slice(-2);

const formatDate = value => {
  if (value) {
    const dt = new Date(value);
    return `${addZero(dt.getHours())}:${addZero(dt.getMinutes())}`;
  }
  return "";
};
const state = {
  paging: {
    facilities: {
      number: 0,
      size: 50
    }
  },
  facilities: [],
  activities: []
};

const getters = {
  facilities: state => state.facilities,
  activities: state =>
    state.activities.map(activity => {
      return {
        ...activity,
        formattedCost: "£" + activity.cost.toFixed(2),
        formattedStartAt: formatDate(activity.startTime)
      };
    }),
  getFacilityById: state => id => {
    return state.facilities.find(
      facility => Number(facility.id) === Number(id)
    );
  }
};

const mutations = {
  SET_FACILITIES: (state, payload) => (state.facilities = payload),
  SET_ACTIVITIES: (state, payload) => (state.activities = payload)
};

const actions = {
  async getFacilities({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const p = state.paging.facilities.number;
    const s = state.paging.facilities.size;
    const url = `/resources?page=${p}&size=${s}`;
    const { data } = await axios.get(url);
    const list = data._embedded.resourceDToes;
    // commit("SET_FACILITIES_PAGE_NUMBER", p + 1);
    commit("SET_FACILITIES", list);
    commit("loading/FINISH_LOADING", null, { root: true });
    return list;
  },
  async createFacility({ commit, state, dispatch }, request) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.post("/resources", request);

    if (request.file) {
      await dispatch("updateFacilityImage", {
        facilityId: data.id,
        file: request.file
      });
      // error handling
    }
    commit("SET_FACILITIES", [...state.facilities, data]);
    commit("loading/START_LOADING", null, { root: true });
    return data;
  },
  async updateFacility({ commit }, facilityId, request) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.post("/resources", request);
    commit("SET_FACILITIES", [...state.facilities, data._embedded]);
    commit("loading/START_LOADING", null, { root: true });
  },
  async updateFacilityImage({ commit }, { facilityId, file }) {
    commit("loading/START_LOADING", null, { root: true });
    const request = new FormData();
    request.append("image", file);
    const url = `/resources/upload/${facilityId}`;
    const { data } = await axios.post(url, request);
    commit("loading/START_LOADING", null, { root: true });
    return data;
  },
  async getActivityTypes({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/activitytypes");
    const activities = data._embedded.activityTypeDToes;
    commit("SET_ACTIVITIES", activities);
    commit("loading/FINISH_LOADING", null, { root: true });
    return activities;
  },
  async getActivities({ commit }){
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/activities");
    const activities = data._embedded.activityDToes;
    commit("SET_ACTIVITIES", activities);
    commit("loading/FINISH_LOADING", null, { root: true });
    return activities;
  },
  async deleteActivity({ commit }, activityId) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.delete(`/activitytypes/${activityId}`);
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
