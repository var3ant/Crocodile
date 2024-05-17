import {RoomView} from "./Response/RoomView";
import {CreateRoomData} from "../../Components/Browser/CreateRoomForm";

export class HttpRoomApi {
    private static readonly path: string = 'http://localhost:8080/room'

    public static async getRooms(): Promise<RoomView[]> {
        const requestOptions = {
            method: 'GET',
            headers: {'Content-Type': 'application/json'},
        };
        const response = await fetch(this.path, requestOptions);
        return JSON.parse(await response.text());//TODO: чё тут с типами. Что если придут не те типы.
    }

    public static async getRoom(id: string): Promise<RoomView> {
        const requestOptions = {
            method: 'GET',
            headers: {'Content-Type': 'application/json'},
        };

        const response = await fetch('http://localhost:8080/room/by_id/' + id, requestOptions);
        return JSON.parse(await response.text());
    }

    public static async createRoom(roomData: CreateRoomData): Promise<number> {
        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(roomData)
        };
        // console.log("createRoom request body: " + requestOptions.body);

        const response = await fetch('http://localhost:8080/room/create', requestOptions);
        const roomId = await response.text();
        // console.log("createRoom id:" + roomId)
        return Number.parseInt(roomId);
    }
}