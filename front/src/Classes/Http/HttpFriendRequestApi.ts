import {StateManager} from "../StateManager";
import {UserNameResponse} from "./Response/UserNameResponse";

export class HttpFriendRequestApi {
    private static readonly path: string = '/friends/request'

    public static async getSentRequests(): Promise<UserNameResponse[]> {
        const response = await StateManager.axios.request('GET', HttpFriendRequestApi.path + "/sent");
        return response.data;
    }

    public static async getReceivedRequests(): Promise<UserNameResponse[]> {
        const response = await StateManager.axios.request('GET', HttpFriendRequestApi.path + "/received");
        return response.data;
    }

    public static async cancelRequest(userId: number) {
        await StateManager.axios.request('POST', HttpFriendRequestApi.path  + '/cancel/' + userId);
    }

    public static async declineRequest(userId: number) {
        await StateManager.axios.request('POST', HttpFriendRequestApi.path + '/decline/' + userId);
    }

    public static async sendRequest(userId: number) {
        await StateManager.axios.request('POST', HttpFriendRequestApi.path + '/send/' + userId);
    }
}