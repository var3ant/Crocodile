import RoomView from "./Response/RoomView";
import {CreateRoomData} from "../../Components/Browser/CreateRoomForm";
import {StateManager} from "../StateManager";

export class HttpRoomApi {
    private static readonly path: string = '/room'

    public static async getRooms(): Promise<RoomView[]> {
        const response = await StateManager.axios.request('GET', HttpRoomApi.path, null);
        return response.data;
    }

    public static async getRoom(id: string): Promise<RoomView> {
        const response = await StateManager.axios.request('GET', HttpRoomApi.path + "/by_id/" + id, null);
        return response.data;
    }

    public static async createRoom(roomData: CreateRoomData): Promise<number> {
        const response = await StateManager.axios.request('POST', HttpRoomApi.path + "/create", roomData);
        console.log(response.data)
        console.log(Number.parseInt(response.data))
        return Number.parseInt(response.data);
    }
}