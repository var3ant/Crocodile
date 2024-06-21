export default class FriendView {
    public id: number;
    public name: string;
    public roomId: number | null;
    public roomName: string | null;

    constructor(id: number, name: string, roomId: number | null, roomName: string | null) {
        this.id = id;
        this.name = name;
        this.roomId = roomId;
        this.roomName = roomName;
    }
}