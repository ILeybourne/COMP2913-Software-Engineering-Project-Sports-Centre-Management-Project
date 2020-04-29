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
  sessions: [],
  bookings: [],
  resources: []
};

const getters = {
  sessions: state =>
    state.sessions.map(activity => {
      return {
        ...activity,
        formattedStartAt: formatDate(activity.startTime)
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
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.delete(`/activities/${sessionId}`);
    commit("SET_SESSIONS", data);
    commit("loading/FINISH_LOADING", null, { root: true });
  },
  async getActivities({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/activities");
    let session = data._embedded.activityDToes;
    commit("SET_SESSIONS", session);
    commit("loading/FINISH_LOADING", null, { root: true });
    return session;
  },
  async getBookings({ commit }){
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/bookings");
    let booking = data._embedded.bookingDToes;
    commit("SET_BOOKINGS", booking);
    commit("loading/FINISH_LOADING", null, { root: true });
    return booking;
  },
  async getResources({ commit }){
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/resources");
    let resource = data._embedded.resourceDToes;
    commit("SET_RESOURCES", resource);
    commit("loading/FINISH_LOADING", null, { root: true });
    return resource;
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
