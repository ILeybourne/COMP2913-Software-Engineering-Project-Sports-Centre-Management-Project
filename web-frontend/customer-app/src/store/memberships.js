import axios from "@/plugins/axios.plugin";

const state = {
  memberships: [],
  accountMemberships: []
};

const getters = {
  memberships: state => state.memberships,
  accountMemberships: state => state.accountMemberships
};

const mutations = {
  SET_MEMBERSHIPS: (state, payload) => (state.memberships = payload),
  SET_ACCOUNTMEMBERSHIPS: (state, payload) =>
    (state.accountMemberships = payload)
};

const actions = {
  async getMemberships({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/membership");
    commit("SET_MEMBERSHIPS", data._embedded.membershipDToes);
    commit("loading/FINISH_LOADING", null, { root: true });
    return data;
  },
  async getAccountMemberships({ commit }, accountId) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get(
      `/membership/members/account/${accountId}`
    );
    commit("SET_ACCOUNTMEMBERSHIPS", data._embedded.membershipDToes);
    commit("loading/FINISH_LOADING", null, { root: true });
    return data;
  }
};

const namespaced = true;

const MembershipModule = {
  namespaced,
  state,
  getters,
  mutations,
  actions
};

export default MembershipModule;
