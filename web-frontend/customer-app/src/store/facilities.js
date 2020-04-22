import axios from "@/plugins/axios.plugin";

const state = {
  paging: {
    facilities: {
      number: 0,
      size: 50
    }
  },
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
    // commit("SET_FACILITIES_PAGE_NUMBER", p + 1);
    commit("SET_FACILITIES", data._embedded.resourceDToes);
    commit("loading/FINISH_LOADING", null, { root: true });
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
  async getActivities({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/activitytypes");
    commit("SET_ACTIVITIES", data._embedded.activityTypeDToes);
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
