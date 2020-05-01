const state = {
  user: null,
  error: null,
  permissions: null
};

const getters = {
  isEmployeeOrManager: function(state) {
    if (state.permissions !== null) {
      return (
        state.permissions.includes("Manager") ||
        state.permissions.includes("Employee")
      );
    } else {
      return false;
    }
  },
  isEmployee: function(state) {
    if (state.permissions !== null) {
      return state.permissions.includes("Employee");
    } else {
      return false;
    }
  },
  isManager: function(state) {
    if (state.permissions !== null) {
      return state.permissions.includes("Manager");
    } else {
      return false;
    }
  },
  isCustomer: function(state) {
    if (state.permissions !== null) {
      return state.permissions.includes("Customer");
    } else {
      return false;
    }
  },
  isAuthenticated: state => state.user !== null,
  user: state => state.user || {},
  permissions: state => state.permissions
};

const mutations = {
  LOGIN: (state, payload) => (state.user = payload),
  SET_PERMISSIONS: (state, payload) => (state.permissions = payload),
  LOGOUT: state => (state.user = null)
};

const namespace = "https://customer-app.com/userRoles";

const actions = {
  login({ commit }, data) {
    commit("LOGIN", data);
    commit("SET_PERMISSIONS", data[namespace]);
  },
  logout({ commit }) {
    commit("LOGOUT");
  },
  loginWithRedirect() {
    // Deliberately empty
  }
};

const namespaced = true;

const AuthModule = {
  namespaced,
  state,
  getters,
  mutations,
  actions
};

export default AuthModule;
