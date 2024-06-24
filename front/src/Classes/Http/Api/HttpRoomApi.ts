import RoomView from "../Response/RoomView";
import {StateManager} from "../../StateManager";
import CreateRoomData from "../Request/CreateRoomData";
import checkForAuthError from "../../Errors/checkForAuthError";

export class HttpRoomApi {
    private static readonly path: string = '/room'

    public static async getRooms(): Promise<RoomView[]> {
        const response = await StateManager.axios.request('GET', HttpRoomApi.path, null)
            .catch((e) => checkForAuthError(e));
        return response.data;
    }

    public static async createRoom(roomData: CreateRoomData): Promise<number> {
        const response = await StateManager.axios.request('POST', HttpRoomApi.path + "/create", roomData)
            .catch((e) => checkForAuthError(e));
        return Number.parseInt(response.data);
    }
}