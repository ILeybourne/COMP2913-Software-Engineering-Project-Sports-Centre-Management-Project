import axios from "@/services/auth-http.service";

const addZero = value => ("0" + value).slice(-2);

const formatDate = value => {
  if (value) {
    const dt = new Date(value);
    return `${addZero(dt.getHours())}:${addZero(dt.getMinutes())}`;
  }
  return "";
};

const state = {
  sessions: []
};

const getters = {
  sessions: state =>
    state.sessions.map(activity => {
      return {
        ...activity,
        formattedStartAt: formatDate(activity.startTime)
      };
    })
};

const mutations = {
  SET_SESSIONS: (state, payload) => (state.sessions = payload)
};

const actions = {
  async getAllSessions({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/timetable");
    commit("SET_SESSIONS", data);
    commit("loading/FINISH_LOADING", null, { root: true });
  },
  async updateSession({ commit }, { newSessionData, sessionId }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.post(`/sessions/${sessionId}`, newSessionData);
    commit("SET_SESSIONS", data);
    commit("loading/FINISH_LOADING", null, { root: true });
  },
  async deleteSession({ commit }, sessionId) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.delete(`/activity/${sessionId}`);
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