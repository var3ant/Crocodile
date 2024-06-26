import RoomConnection from "./RoomConnection";
import {MessageData} from "../Pages/Room/Chat/ChatArea";
import {ServerEvent} from "./Events/ServerEvent";
import {Point} from "./Events/DrawEvent";
import {PaintingSettings} from "../Pages/Room/Drawer/DrawCanvas";
import {ReactionType} from "./Events/UserMessageEvent";

class RoomModel {
    public Messages: MessageData[];
    public readonly userId: number;
    public readonly name: string;
    public readonly roomId: number;

    private _roomConnection: RoomConnection
    private _eventSubscriber: ((event: ServerEvent) => void) | null = null;

    constructor(userId: number, roomId: number) {
        this.Messages = [];
        this._roomConnection = new RoomConnection(this, userId, roomId);

        this.userId = userId;
        //TODO: Когда буду делать разные правила по заходу (пароль, только для друзей и т.д.),
        // буду ждать подтверждения что зашел, и в это подтверждение сервер положит имя сервера. А пока что и id'шника хватит
        this.name = roomId.toString();
        this.roomId = roomId;
    }

    public connect(): boolean {
        return this._roomConnection.connect();
    }

    public getRoomId(): number {
        return this.roomId;
    }

    public getName(): string {
        return this.name;
    }

    public handleEvent(event: ServerEvent) {
        if (this._eventSubscriber) {
            this._eventSubscriber(event);
        }
    }

    public subscribeEvents(eventHandler: (event: ServerEvent) => void) {
        this._eventSubscriber = eventHandler;
    }

    //region client messages

    public tryDisconnect() {
        this._roomConnection.trySendMessage("/disconnect", {})
        this.disconnect();
        return true;
    }

    public disconnect() {
        this._roomConnection.sendMessage("/disconnect", {})
        this._roomConnection.disconnect();
    }

    public sendImage(receiverId: number, image: string) {
        this._roomConnection.sendMessage("/send_image", {receiverId: receiverId, image: image});
    }

    public reactToMessage(messageId: string, reaction: ReactionType) {
        this._roomConnection.sendMessage("/react_to_message", {messageId: messageId, reaction: reaction});
    }

    public sendChatMessage(text: string): boolean {
        if(this.isBlank(text)) {
            return false;
        }
        this._roomConnection.sendMessage("/chat", {message: text});
        return true;
    }

    private isBlank(text: string) {
        return (!text || /^\s*$/.test(text));
    }

    public drawLine(startPoint: Point, finishPoint: Point, paintingSettings: PaintingSettings) {
        this._roomConnection.sendMessage("/draw", {
            startPoint: startPoint,
            finishPoint: finishPoint,
            size: paintingSettings.size,
            color: paintingSettings.color
        });
    }

    public chooseWord(index: number) {
        this._roomConnection.sendMessage("/choose_word", {index: index.toString()});
    }

    drawClear() {
        this._roomConnection.sendMessage("/clear", {});
    }

    //endregion
}

export {RoomModel}