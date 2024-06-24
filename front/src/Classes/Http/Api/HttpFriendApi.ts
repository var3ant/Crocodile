import {StateManager} from "../../StateManager";
import FriendView from "../Response/FriendView";
import PotentialFriendView from "../Response/PotentialFriendView";
import checkForAuthError from "../../Errors/checkForAuthError";

export class HttpFriendApi {
    private static readonly path: string = '/friends'

    public static async getFriends(): Promise<FriendView[]> {
        const response = await StateManager.axios
            .request('GET', HttpFriendApi.path).catch((e) => checkForAuthError(e));
        return response.data;
    }

    public static async getUsersNameLike(name: string): Promise<PotentialFriendView[]> {
        const params = new URLSearchParams();
        params.append("name", name)

        const response = await StateManager.axios
            .request('GET', HttpFriendApi.path + '/potential_by?' + params.toString())
            .catch((e) => checkForAuthError(e));
        return response.data;
    }
}