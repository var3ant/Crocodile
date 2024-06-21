import {StateManager} from "../StateManager";
import {UserNameResponse} from "./Response/UserNameResponse";
import FriendView from "./Response/FriendView";
import PotentialFriendView from "./Response/PotentialFriendView";

export class HttpFriendApi {
    private static readonly path: string = '/friends'

    public static async getFriends(): Promise<FriendView[]> {
        const response = await StateManager.axios.request('GET', HttpFriendApi.path);
        return response.data;
    }

    public static async getSentRequests(): Promise<UserNameResponse[]> {
        const response = await StateManager.axios.request('GET', HttpFriendApi.path + "/sent_requests");
        return response.data;
    }

    public static async getReceivedRequests(): Promise<UserNameResponse[]> {
        const response = await StateManager.axios.request('GET', HttpFriendApi.path + "/received_requests");
        return response.data;
    }

    public static async cancelRequest(userId: number) {
        await StateManager.axios.request('POST', HttpFriendApi.path  + '/cancel_request/' + userId);
    }

    public static async declineRequest(userId: number) {
        await StateManager.axios.request('POST', HttpFriendApi.path + '/decline_request/' + userId);
    }

    public static async sendRequest(userId: number) {
        await StateManager.axios.request('POST', HttpFriendApi.path + '/send_request/' + userId);
    }

    public static async getUsersNameLike(name: string): Promise<PotentialFriendView[]> {
        const params = new URLSearchParams();
        params.append("name", name)

        const response = await StateManager.axios.request('GET', HttpFriendApi.path + '/potential_by?' + params.toString());
        return response.data;
    }
}