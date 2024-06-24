import {StateManager} from "../../StateManager";
import {AxiosService} from "../AxiosService";

export class HttpAuthApi {
    public static async login(name: string, password: string): Promise<number> {
        AxiosService.setAuthToken(null)

        let result: { data: { token: string, id: number } } = await StateManager.axios.request('POST', '/auth/login', {
            login: name,
            password: password
        })

        if (result.data.token !== null && result.data.token !== undefined) {
            AxiosService.setAuthToken(result.data.token)
        }
        return result.data.id;
    }

    public static async register(name: string, password: string): Promise<number> {
        AxiosService.setAuthToken(null)

        let result: { data: { id: number } } = await StateManager.axios.request('POST', '/auth/register', {
            login: name,
            password: password
        })

        return result.data.id;
    }
}