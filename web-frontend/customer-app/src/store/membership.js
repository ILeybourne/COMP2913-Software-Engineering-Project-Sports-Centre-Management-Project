import axios from "@/plugins/axios.plugin";

const state = {
  membershipTypes: [],
  selectedMembershipType: []
};

const getters = {
  membershipTypes: state =>
    state.membershipTypes.map(membershipType => {
      return {
        ...membershipType,
        formattedCost: "£" + membershipType.cost.toFixed(2)
      };
    }),
  selectedMembershipType: state => state.selectedMembershipType
};

const mutations = {
  SET_MEMBERSHIPTYPES: (state, payload) => (state.membershipTypes = payload),
  SET_SELECTEDMEMBERSHIPTYPE: (state, payload) =>
    (state.selectedMembershipType = payload)
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
  }
};

const namespaced = true;

const membershipTypes = {
  namespaced,
  state,
  getters,
  mutations,
  actions
};

export default membershipTypes;
