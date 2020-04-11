import axios from "axios";
import { getInstance } from "@/plugins/auth.plugin";
//TODO may not be instance we want
import store from "@/store";

const authHttp = axios.create({
  baseURL: "http://localhost:8000" // TODO: getBaseUrl()
  // headers:
});

// TODO: increment and decrement loading state

authHttp.interceptors.request.use(
  config => {
    const instance = getInstance();
    if (instance.isAuthenticated) {
      config.headers.Authorization = `Authorization ${instance.getTokenSilently()}`;
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
    if (error.response.status === 400) {
      store.dispatch("validation/setValidationErrors", error.response.data);
    }
    return Promise.reject(error);
  }
);

export default authHttp;
