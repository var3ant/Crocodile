import {RoomModel} from "./RoomModel";
import {AxiosService} from "./Http/AxiosService";

export class StateManager {
    private static room: RoomModel | null;
    public static userId: number | null;
    public static axios: AxiosService = new AxiosService();

    static trySetRoom(roomId: number): boolean {
        console.log("set roomid:" + roomId)
        let userId = StateManager.userId;
        if (userId === null) {
            console.assert("userId === null")
            return false;
        }

        StateManager.room = new RoomModel(userId, roomId);
        return true;//TODO: обработка ошибок
    }

    static getRoom(): RoomModel | null {
        return StateManager.room;
    }
}