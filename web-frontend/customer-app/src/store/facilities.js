import axios from "@/plugins/axios.plugin";
import { formatDate } from "@/util/format.helpers";
import { formatCurrency } from "@/util/format.helpers";

const state = {
  paging: {
    facilities: {
      pages: [],
      number: null,
      size: null,
      totalElements: null,
      totalPages: null,
      currentPageHref: null,
      nextPageHref: `/resources?page=${0}&size=${10}`, // just need first page to initialise store
      lastPageHref: null,
      isDataToLoad: true
    }
  },
  facilities: [], // the facilities in the sports center
  activities: [] // Kinds of activities that we can do in the facilities
};

const getters = {
  facilities: state => state.facilities,
  activities: state =>
    state.activities.map(activity => {
      return {
        ...activity,
        formattedCost: formatCurrency(activity.cost),
        formattedStartAt: formatDate(activity.startTime)
      };
    }),
  getFacilityById: state => id => {
    return state.facilities.find(
      facility => Number(facility.id) === Number(id)
    );
  },
  getActivitiesForFacilityId: state => fid => {
    return state.activities.filter(
      activity => Number(activity.facility_id) === Number(fid)
    );
  },
  facilitiesLoading: state => state.paging.facilities.isDataToLoad
};

const mutations = {
  SET_FACILITIES_LOADING: (state, payload) =>
    (state.paging.facilities.isDataToLoad = payload),
  SET_FACILITIES: (state, payload) => (state.facilities = payload),
  APPEND_FACILITIES: (state, { pageId, page }) => {
    if (state.paging.facilities.pages.includes(pageId)) {
      // already got this page, don't want duplicates!!
      return;
    }
    state.paging.facilities.pages.push(pageId);
    state.facilities.push(...page);
  },
  SET_ACTIVITIES: (state, payload) => (state.activities = payload),
  SET_FACILITY_PAGE_INFO: (state, payload) => {
    state.paging.facilities = {
      ...state.paging.facilities,
      ...payload
    };
  }
};

const actions = {
  /**
   * Get the paginated data in the store
   *
   * If we are doing searching in the store then we may
   * need to load all data in
   *
   * If this isn't the first call and we we haven't reached the last page yet we get
   * the next page until we get to the last page
   * */
  async getFacilities({ state, commit, dispatch }) {
    const paging = state.paging.facilities;
    if (!paging.isDataToLoad) {
      // Don't need to do anything, we have all the data, just return what we have in the store
      return state.facilities;
    }
    // We don't have all the data from the server and we need to load it
    commit("loading/START_LOADING", null, { root: true });
    let { data } = await axios.get(paging.nextPageHref);
    const pageIdentifier = data._links.self.href;
    if (data._embedded) {
      const facilityPage = data._embedded.resourceDToes;
      commit("APPEND_FACILITIES", {
        pageId: pageIdentifier,
        page: facilityPage
      });
    }
    // Which page have we just retrieved
    commit("SET_FACILITY_PAGE_INFO", {
      currentPageHref: pageIdentifier
    });

    // load next pages if there are any
    if (data._links && data._links.last && data._links.next) {
      // Bookkeeping for pagination
      commit("SET_FACILITY_PAGE_INFO", {
        nextPageHref: data._links.next.href,
        lastPageHref: data._links.last.href
      });

      if (pageIdentifier === data._links.last.href) {
        // if this was the last page then we are done
        commit("SET_FACILITIES_LOADING", false);
      } else {
        // There are other pages
        // Keep getting the next page, we don't need to wait as we have enough to do a first render of the page
        dispatch("getFacilities");
      }
    } else {
      // no pagination
      commit("SET_FACILITIES_LOADING", false);
    }
    commit("loading/FINISH_LOADING", null, { root: true });
    return facilities;
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
    commit("SET_FACILITIES", [
      ...state.facilities.filter(facility => facility.id !== facilityId),
      data._embedded
    ]);
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
  async deleteFacility({ state, commit }, facilityId) {
    let result = false;
    commit("loading/START_LOADING", null, { root: true });
    const response = await axios.delete(`/resources/${facilityId}`);
    if (response.status === 204) {
      // Delete was successful, remove the copy of the session from the store
      const index = state.facilities.findIndex(s => s.id === facilityId);
      if (index) {
        state.facilities.splice(index, 1);
        commit("SET_FACILITIES", state.facilities);
        result = true;
      }
    }
    commit("loading/FINISH_LOADING", null, { root: true });
    return result;
  },
  async updateActivityTypes({ commit }, { activityId, body }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.put(`/activitytypes/${activityId}`, body);
    commit("SET_ACTIVITIES", [
      ...state.activities.filter(activity => activity.id !== activityId),
      data
    ]);
    commit("loading/START_LOADING", null, { root: true });
  },
  async createActivityType({ commit }, { facilityId, body }) {
    const { data } = await axios.post(
      `/activitytypes/resource/${facilityId}`,
      body
    );
    commit("SET_ACTIVITIES", [...state.activities, data]);
    commit("loading/START_LOADING", null, { root: true });
    return data;
  },
  async getActivities({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/activitytypes");
    const activities = data._embedded.activityTypeDToes;
    commit("SET_ACTIVITIES", activities);
    commit("loading/FINISH_LOADING", null, { root: true });
    return activities;
  },
  async deleteActivity({ state, commit }, activityId) {
    let result = false;
    commit("loading/START_LOADING", null, { root: true });
    const response = await axios.delete(`/activitytypes/${activityId}`);
    if (response.status === 204) {
      // Delete was successful, remove the copy of the session from the store
      const index = state.activities.findIndex(s => s.id === activityId);
      if (index) {
        state.activities.splice(index, 1);
        commit("SET_ACTIVITIES", state.activities);
        result = true;
      }
    }
    commit("loading/FINISH_LOADING", null, { root: true });
    return result;
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
