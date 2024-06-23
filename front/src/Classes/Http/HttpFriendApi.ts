import {StateManager} from "../StateManager";
import FriendView from "./Response/FriendView";
import PotentialFriendView from "./Response/PotentialFriendView";

export class HttpFriendApi {
    private static readonly path: string = '/friends'

    public static async getFriends(): Promise<FriendView[]> {
        const response = await StateManager.axios.request('GET', HttpFriendApi.path);
        return response.data;
    }

    public static async getUsersNameLike(name: string): Promise<PotentialFriendView[]> {
        const params = new URLSearchParams();
        params.append("name", name)

        const response = await StateManager.axios.request('GET', HttpFriendApi.path + '/potential_by?' + params.toString());
        return response.data;
    }
}