import {StateManager} from "../../StateManager";
import {UserNameResponse} from "../Response/UserNameResponse";
import checkForAuthError from "../../Errors/checkForAuthError";

export class HttpFriendRequestApi {
    private static readonly path: string = '/friends/request'

    public static async getSentRequests(): Promise<UserNameResponse[]> {
        const response = await StateManager.axios.request('GET', HttpFriendRequestApi.path + "/sent")
            .catch((e) => checkForAuthError(e));

        if(response.data === undefined) {
            return [];
        }
        return response.data;
    }

    public static async getReceivedRequests(): Promise<UserNameResponse[]> {
        const response = await StateManager.axios.request('GET', HttpFriendRequestApi.path + "/received")
            .catch((e) => checkForAuthError(e));

        if(response.data === undefined) {
            return [];
        }
        return response.data;
    }

    public static async cancelRequest(userId: number) {
        await StateManager.axios.request('POST', HttpFriendRequestApi.path  + '/cancel/' + userId)
            .catch((e) => checkForAuthError(e));
    }

    public static async declineRequest(userId: number) {
        await StateManager.axios.request('POST', HttpFriendRequestApi.path + '/decline/' + userId)
            .catch((e) => checkForAuthError(e));
    }

    public static async sendRequest(userId: number) {
        await StateManager.axios.request('POST', HttpFriendRequestApi.path + '/send/' + userId)
            .catch((e) => checkForAuthError(e));
    }
}