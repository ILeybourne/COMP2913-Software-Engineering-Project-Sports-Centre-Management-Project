import axios from "@/plugins/axios.plugin";

const state = {
  accounts: []
};

const getters = {
  accounts: state => state.accounts
};

const mutations = {
  SET_ACCOUNTS: (state, payload) => (state.accounts = payload)
};

const actions = {
  async getAccounts({ commit }) {
    commit("loading/START_LOADING", null, { root: true });
    const { data } = await axios.get("/account");
    commit("SET_ACCOUNTS", data._embedded.accountDToes);
    commit("loading/FINISH_LOADING", null, { root: true });
    return data;
  }
};

const namespaced = true;

const AccountModule = {
  namespaced,
  state,
  getters,
  mutations,
  actions
};

export default AccountModule;
