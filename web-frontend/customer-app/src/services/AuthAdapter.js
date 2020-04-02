import {getInstance} from "@/services/auth.service";

const authService = getInstance();

/**
 * @class AuthAdapter standardise the api for authentication tokens
 * */
export class AuthAdapter {
    authService;

    constructor(auth0Client) {
        this.authService = auth0Client;
    }

    getToken() {
        return authService.getTokenSilently();
    }
}

export const auth0Adapter = new AuthAdapter(authService);
