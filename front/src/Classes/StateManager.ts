import {RoomModel} from "./RoomModel";
import {AxiosService} from "./Http/AxiosService";

export class StateManager {
    private static room: RoomModel | null;
    public static userId: number | null;
    public static axios: AxiosService = new AxiosService();

    static setRoom(roomId: number) {
        console.log("set roomid:" + roomId)
        let userId = StateManager.userId;
        if (userId === null) {
            console.assert("userId === null")
            return
        }

        StateManager.room = new RoomModel(userId, roomId.toString());//TODO: сделать чтобы нормально число хранилось
    }

    static getRoom(): RoomModel | null {
        return StateManager.room;
    }
}