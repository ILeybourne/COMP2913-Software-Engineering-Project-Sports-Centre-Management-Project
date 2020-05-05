import { getInstance } from "@/plugins/auth.plugin";
import store from "@/store";

export const authGuard = (to, from, next) => {
  const authService = getInstance();
  /*TODO may be able to remove function wrapper */
  const fn = () => {
    // If the user is authenticated, continue with the route
    if (authService.isAuthenticated) {
      return next();
    }

    // Otherwise, log in
    authService.loginWithRedirect({ appState: { targetUrl: to.fullPath } });
  };
  /*TODO may be able to remove*/

  ////console.log("In auth guard is loading: ", authService.loading);
  ////console.log("auth guard user is ", authService.user);

  // If loading has already finished, check our auth state using `fn()`
  if (!authService.loading) {
    return fn();
  }

  // Watch for the loading property to change before we check isAuthenticated
  authService.$watch("loading", loading => {
    if (loading === false) {
      return fn();
    }
  });
};

export const manager = (to, from, next) => {
  if (store.getters["auth/isManager"]) {
    return next();
  } else {
    return next("/unauthorised");
  }
};

export const employee = (to, from, next) => {
  if (store.getters["auth/isEmployee"]) {
    return next();
  } else {
    return next("/unauthorised");
  }
};
