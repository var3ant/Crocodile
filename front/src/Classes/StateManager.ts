import {RoomModel} from "./RoomModel";
import {AxiosService} from "./Http/AxiosService";

export class StateManager {
    private static room: RoomModel | null;
    private static _userId: number | null;
    public static readonly axios: AxiosService = new AxiosService();

    public static get userId(): number | null {
        return StateManager._userId;
    }
    public static set userId(value: number | null) {
        StateManager._userId = value;
    }

    static trySetRoom(roomId: number | null): boolean {
        console.log("set roomid:" + roomId)

        if(roomId == null) {
            StateManager.room?.tryDisconnect();
            StateManager.room = null;
            return true;
        }

        let userId = StateManager.userId;
        if (userId === null) {
            console.assert("userId === null")
            return false;
        }

        StateManager.room = new RoomModel(userId, roomId);
        
        return StateManager.room.connect();
    }

    static getRoom(): RoomModel | null {
        return StateManager.room;
    }
}