import axios from "@/plugins/axios.plugin";
import { formatDate } from "@/util/format.helpers";
import { formatCurrency } from "../util/format.helpers";
import { dateToString } from "../util/format.helpers";

const state = {
  paging: {
    bookings: {
      pages: [],
      number: null,
      size: null,
      totalElements: null,
      totalPages: null,
      currentPageHref: null,
      nextPageHref: `/bookings?page=${0}&size=${20}`, // just need first page to initialise store
      lastPageHref: null,
      isDataToLoad: true
    }
  },
  sessions: [],
  bookings: []
};

const sessionIsFull = session => {
  if (!session.totalCapacity) {
    // If there is no capacity, then it cannot be full
    return false;
  }
  return session.currentCapacity >= session.totalCapacity;
};

const getters = {
  sessions: state =>
    state.sessions.map(activity => {
      return {
        ...activity,
        formattedStartAt: formatDate(activity.startTime),
        slot:
          formatDate(activity.startTime) + " - " + formatDate(activity.endTime),
        isFull: sessionIsFull(activity),
        formattedDate: dateToString(activity.startTime)
      };
    }),
  bookings: state =>
    state.bookings.map(booking => {
      return {
        ...booking,
        activity: state.sessions.find(
          session => Number(session.id) === Number(booking.session_id)
        ),
        formattedAmount: formatCurrency(booking.amount)
      };
    }),
  getSessionsForFacility: state => facilityId => {
    return state.sessions.filter(
      session => Number(session.resource.id) === Number(facilityId)
    );
  },
  bookingsLoading: state => state.paging.bookings.isDataToLoad
};

const mutations = {
  SET_SESSIONS: (state, payload) => (state.sessions = payload),
  SET_RESOURCES: (state, payload) => (state.resources = payload),
  ADD_SESSION: (state, payload) => state.sessions.push(payload),
  SET_BOOKINGS_LOADING: (state, payload) =>
    (state.paging.bookings.isDataToLoad = payload),
  SET_BOOKINGS: (state, payload) => (state.bookings = payload),
  APPEND_BOOKINGS: (state, { pageId, page }) => {
    if (state.paging.bookings.pages.includes(pageId)) {
      // already got this page, don't want duplicates!!
      return;
    }
    state.paging.bookings.pages.push(pageId);
    state.bookings.push(...page);
  },
  SET_BOOKING_PAGE_INFO: (state, payload) => {
    state.paging.bookings = {
      ...state.paging.bookings,
      ...payload
    };
  }
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
        commit("SET_SESSIONS", state.sessions);
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
  async getBookings({ state, commit, dispatch }) {
    const paging = state.paging.bookings;
    if (!paging.isDataToLoad) {
      // Don't need to do anything, we have all the data, just return what we have in the store
      return state.bookings;
    }
    // We don't have all the data from the server and we need to load it
    commit("loading/START_LOADING", null, { root: true });
    let { data } = await axios.get(paging.nextPageHref);
    const pageIdentifier = data._links.self.href;
    if (data._embedded) {
      const bookingPage = data._embedded.bookingDToes;
      commit("APPEND_BOOKINGS", {
        pageId: pageIdentifier,
        page: bookingPage
      });
    }
    // Which page have we just retrieved
    commit("SET_BOOKING_PAGE_INFO", {
      currentPageHref: pageIdentifier
    });

    // load next pages if there are any
    if (data._links && data._links.last && data._links.next) {
      // Bookkeeping for pagination
      commit("SET_BOOKING_PAGE_INFO", {
        nextPageHref: data._links.next.href,
        lastPageHref: data._links.last.href
      });

      if (pageIdentifier === data._links.last.href) {
        // if this was the last page then we are done
        commit("SET_BOOKING_LOADING", false);
      } else {
        // There are other pages
        // Keep getting the next page, we don't need to wait as we have enough to do a first render of the page
        dispatch("getBookings");
      }
    } else {
      // no pagination
      commit("SET_BOOKINGS_LOADING", false);
    }
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
  async deleteBooking({ state, commit }, bookingId) {
    let result = false;
    commit("loading/START_LOADING", null, { root: true });
    const response = await axios.delete(`/bookings/${bookingId}`);
    if (response.status === 204) {
      // Delete was successful, remove the copy of the session from the store
      commit("SET_BOOKINGS", [
        ...state.bookings.filter(booking => booking.id !== bookingId)
      ]);
    }
    commit("loading/FINISH_LOADING", null, { root: true });
    return result;
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
  },
  async stopRegularSession({ commit }, body) {
    let account_id = body.accountId;
    let activity_id = body.activityId;
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.put(
      `/bookings/cancel/${activity_id}/${account_id}`
    );
    const updatedBookings = data._embedded.bookingDToes;
    commit("SET_BOOKINGS",
      updatedBookings
    );
    commit("loading/START_LOADING", null, { root: true });
  }
};

const namespaced = true;

const bookings = {
  namespaced,
  state,
  getters,
  mutations,
  actions
};

export default bookings;
