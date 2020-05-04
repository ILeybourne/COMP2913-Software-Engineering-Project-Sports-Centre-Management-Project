import axios from "@/plugins/axios.plugin";
import { formatCurrency } from "../util/format.helpers";

const state = {
  membershipTypes: [],
  selectedMembershipType: [],
  purchasedMembership: [],
  memberships: [],
  accountMemberships: []
};

const getters = {
  membershipTypes: state =>
    state.membershipTypes.map(membershipType => {
      return {
        ...membershipType,
        formattedCost: formatCurrency(membershipType)
      };
    }),
  selectedMembershipType: state => state.selectedMembershipType,
  purchasedMembership: state => state.purchasedMembership,
  memberships: state => state.memberships,
  accountMemberships: state => state.accountMemberships
};

const mutations = {
  SET_MEMBERSHIPTYPES: (state, payload) => (state.membershipTypes = payload),
  SET_SELECTEDMEMBERSHIPTYPE: (state, payload) =>
    (state.selectedMembershipType = payload),
  SET_PURCHASEDMEMBERSHIP:
    (state, payload => (state.purchasedMembership = payload)),
  SET_MEMBERSHIPS: (state, payload) => (state.memberships = payload),
  SET_ACCOUNTMEMBERSHIPS: (state, payload) =>
    (state.accountMemberships = payload)
};

const actions = {
  async getMembershipTypes({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/membership/types");
    commit("SET_MEMBERSHIPTYPES", data._embedded.membershipTypeDToes);
    commit("loading/FINISH_LOADING", null, { root: true });
  },
  async getSelectedMembershipType({ commit }, selectedOption) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get(`/membership/types/${selectedOption}`);
    commit("SET_SELECTEDMEMBERSHIPTYPE", data);
    commit("loading/FINISH_LOADING", null, { root: true });
  },
  async getPurchasedMembership({ commit }, membershipId) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get(`/membership/members/${membershipId}`);
    commit("SET_PURCHASEDMEMBERSHIP", data);
    commit("loading/FINISH_LOADING", null, { root: true });
  },
  async getMemberships({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/membership/members");
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

const membership = {
  namespaced,
  state,
  getters,
  mutations,
  actions
};

export default membership;
