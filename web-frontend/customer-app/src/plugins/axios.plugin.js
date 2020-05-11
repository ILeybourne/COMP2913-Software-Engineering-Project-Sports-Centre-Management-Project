import axios from "axios";
import { getInstance } from "@/plugins/auth.plugin";
import router from "@/router";
//TODO may not be instance we want
import store from "@/store";

const authHttp = axios.create({
  baseURL: "http://localhost:8000" // TODO: getBaseUrl()
  // headers:
});

// TODO: increment and decrement loading state

authHttp.interceptors.request.use(
  async config => {
    const instance = getInstance();
    if (instance.isAuthenticated) {
      config.headers.Authorization = `Bearer ${await instance.getTokenSilently()}`;
    }

    return config;
  },
  error => Promise.reject(error)
);

authHttp.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    if (!error.response) {
      router.push({ name: "ServerError" });
    } else {
      if (error.response.status === 400) {
        store.dispatch("validation/setValidationErrors", error.response.data);
      }
      if (error.response.status === 401) {
        store.dispatch("auth/loginWithRedirect");
      }
      if (error.response.status === 403) {
        router.push({ name: "Unauthorised" });
      }
      if (error.response.status === 500) {
        router.push({ name: "ServerError", params:error.response.data });
      }
    }

    return Promise.reject(error);
  }
);

export default authHttp;
