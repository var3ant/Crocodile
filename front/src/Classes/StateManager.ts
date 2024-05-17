import {RoomModel} from "./RoomModel";

export class StateManager {
    private static room: RoomModel | null;
    public static userId: string | null;

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