import axios from "axios";
import {getInstance} from "./auth.service";

const authHttp = axios.create({
    baseURL: "http://localhost:8000" // TODO: getBaseUrl()
    // headers:
});

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

export default authHttp;
