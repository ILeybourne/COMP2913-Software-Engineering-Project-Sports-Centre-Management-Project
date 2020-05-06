import axios from "@/plugins/axios.plugin";

const state = {
  userMemberships: []
};

const getters = {
  userMemberships: state => state.userMemberships
};

const mutations = {
  SET_USERMEMBERSHIPS: (state, payload) => (state.userMemberships = payload)
};

const actions = {
  async getUserMemberships({ commit }, account_id) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get(
      `/membership/members/account/${account_id}`
    );
    commit("SET_USERMEMBERSHIPS", data._embedded.membershipDToes);
    commit("loading/FINISH_LOADING", null, { root: true });
  }
};

const namespaced = true;

const memberships = {
  namespaced,
  state,
  getters,
  mutations,
  actions
};

export default memberships;
