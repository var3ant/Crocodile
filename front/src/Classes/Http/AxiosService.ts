import axios from 'axios';
import {BACKEND_URL} from "./Constants";

export class AxiosService {

    constructor() {
        axios.defaults.baseURL = BACKEND_URL;
        axios.defaults.headers.post['Content-Type'] = 'application/json';
        console.log("ctor")
    }

    public static getAuthToken(): string | null {
        return window.localStorage.getItem("auth_token");
    }

    public static setAuthToken(token: string | null): void {
        if (token !== null) {
            window.localStorage.setItem("auth_token", token);
        } else {
            window.localStorage.removeItem("auth_token");
        }
    }

    public request(method: string, url: string, data: any = null): Promise<any> {
        let headers: any = {};

        if (AxiosService.getAuthToken() !== null) {
            headers = {"Authorization": "Bearer " + AxiosService.getAuthToken()};
        }

        return axios({
            method: method,
            url: url,
            data: data,
            headers: headers,
        });
    }
}