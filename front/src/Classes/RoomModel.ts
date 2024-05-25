import RoomConnection from "./RoomConnection";
import {MessageData} from "../Components/Room/Chat/ChatArea";
import {ServerEvent} from "./Events/ServerEvent";
import {Point} from "./Events/DrawEvent";

class RoomModel {
    public Messages: MessageData[];
    public readonly userId: number;

    private _roomConnection: RoomConnection
    private _eventSubscriber: ((event: ServerEvent) => void) | null = null;

    constructor(userId: number, roomId: string) {
        this.Messages = [];
        this._roomConnection = new RoomConnection(this, userId, roomId);
        this._roomConnection.connect()
        this.userId = userId;
    }

    wordChosen(index: number) {
        this._roomConnection.chooseWord(index);
    }

    sendMessage(text: string): boolean {
        this._roomConnection.chat(text);
        return true;
    }

    public handleEvent(event: ServerEvent) {
        console.log("message received:" + event)
        if (this._eventSubscriber) {
            this._eventSubscriber(event);
        }
    }

    drawLine(startPoint: Point, finishPoint: Point) {
        this._roomConnection.draw(startPoint, finishPoint);
    }

    subscribeEvents(eventHandler: (event: ServerEvent) => void) {
        this._eventSubscriber = eventHandler;
    }

    leave() {
        this._roomConnection.disconnect();
    }
}

export {RoomModel}