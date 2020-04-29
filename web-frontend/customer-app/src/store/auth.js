const state = {
  user: null,
  error: null
};

const getters = {
  isEmployeeOrManager: function(state) {
    if (state.user !== null) {
      return (
        state.user["https://customer-app.com/userRoles"].includes("Manager") ||
        state.user["https://customer-app.com/userRoles"].includes("Employee")
      );
    } else {
      return false;
    }
  },
  isEmployee: function(state) {
    if (state.user !== null) {
      return state.user["https://customer-app.com/userRoles"].includes(
        "Employee"
      );
    } else {
      return false;
    }
  },
  isManager: function(state) {
    if (state.user !== null) {
      return state.user["https://customer-app.com/userRoles"].includes(
        "Manager"
      );
    } else {
      return false;
    }
  },
  isCustomer: function(state) {
    if (state.user !== null) {
      return state.user["https://customer-app.com/userRoles"].includes(
        "Customer"
      );
    } else {
      return false;
    }
  },
  isAuthenticated: state => state.user !== null,
  user: state => state.user || {},
  permissions: state => state.claims
};

const mutations = {
  LOGIN: (state, payload) => (state.user = payload),
  LOGOUT: state => (state.user = null)
};

const actions = {
  login({ commit }, data) {
    commit("LOGIN", data);
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
