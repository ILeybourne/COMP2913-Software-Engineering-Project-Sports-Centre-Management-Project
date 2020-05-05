import axios from "@/plugins/axios.plugin";
import { formatDate } from "@/util/format.helpers";

const state = {
  sessions: [],
  bookings: [],
  resources: []
};

function sessionIsFull(session) {
  if (!session.totalCapacity) {
    // If there is no capacity, then it cannot be full
    return false;
  }
  return session.currentCapacity >= this.session.totalCapacity;
}

const getters = {
  sessions: state =>
    state.sessions.map(activity => {
      return {
        ...activity,
        formattedStartAt: formatDate(activity.startTime),
        isFull: sessionIsFull(activity)
      };
    }),
  bookings: state => state.bookings,
  resources: state => state.resources,
  getSessionsForFacility: state => facilityId => {
    return state.sessions.filter(
      session => Number(session.resource.id) === Number(facilityId)
    );
  }
};

const mutations = {
  SET_SESSIONS: (state, payload) => (state.sessions = payload),
  SET_BOOKINGS: (state, payload) => (state.bookings = payload),
  SET_RESOURCES: (state, payload) => (state.resources = payload),
  ADD_SESSION: (state, payload) => state.sessions.push(payload)
};

const actions = {
  async getAllSessions({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/timetable");
    let session = data._embedded.activityDToes;
    commit("SET_SESSIONS", session);
    commit("loading/FINISH_LOADING", null, { root: true });
    return session;
  },
  async updateSession({ commit }, { newSessionData, sessionId }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.post(`/sessions/${sessionId}`, newSessionData);
    commit("SET_SESSIONS", data);
    commit("loading/FINISH_LOADING", null, { root: true });
  },
  async deleteSession({ commit }, sessionId) {
    let result = false;
    commit("loading/START_LOADING", null, { root: true });
    const response = await axios.delete(`/activities/${sessionId}`);
    if (response.status === 204) {
      // Delete was successful, remove the copy of the session from the store
      const index = state.sessions.findIndex(s => s.id === sessionId);
      if (index) {
        state.sessions.splice(index, 1);
        result = true;
      }
    }
    commit("loading/FINISH_LOADING", null, { root: true });
    return result;
  },
  async getActivities({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/activities");
    let session = [];
    if (data._embessed) {
      session = data._embedded.activityDToes;
    }
    commit("SET_SESSIONS", session);
    commit("loading/FINISH_LOADING", null, { root: true });
    return session;
  },
  async getBookings({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/bookings");
    let bookings = [];
    if (data._embedded) {
      bookings = data._embedded.bookingDToes;
    }
    commit("SET_BOOKINGS", bookings);
    commit("loading/FINISH_LOADING", null, { root: true });
    return bookings;
  },
  async getBooking({ commit, BookingId }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get(`/bookings/${BookingId}`);
    let bookings = [];
    if (data._embedded) {
      bookings = data._embedded.bookingDToes;
    }
    commit("SET_BOOKINGS", bookings);
    commit("loading/FINISH_LOADING", null, { root: true });
    return bookings;
  },
  async deleteBooking({ commit }, bookingId) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.delete(`/bookings/${bookingId}`);
    commit("SET_BOOKINGS", data);
    commit("loading/FINISH_LOADING", null, { root: true });
  },
  async getResources({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/resources");
    let resources = [];
    if (data._embedded) {
      resources = data._embedded.resourceDToes;
    }
    commit("SET_RESOURCES", resources);
    commit("loading/FINISH_LOADING", null, { root: true });
    return resources;
  },
  async createSession({ commit }, { activityId, ...session }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.post(
      `/activities/activitytype/${activityId}`,
      session
    );
    commit("ADD_SESSION", data);
    commit("loading/FINISH_LOADING", null, { root: true });
    return data;
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
