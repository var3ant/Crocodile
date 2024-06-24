import SockJS from "sockjs-client"
import Stomp from "stompjs"
import Cookies from 'universal-cookie';
import {RoomModel} from "./RoomModel";
import {ServerEvent} from "./Events/ServerEvent";
import {ChooseWordEvent} from "./Events/ChooseWordEvent";
import {NewDrawerEvent} from "./Events/NewDrawerEvent";
import {InfoMessageEvent} from "./Events/InfoMessageEvent";
import {ReactionType, stringToReaction, UserMessageEvent} from "./Events/UserMessageEvent";
import {DrawEvent, Point} from "./Events/DrawEvent";
import {AxiosService} from "./Http/AxiosService";
import {RequestImageEvent} from "./Events/RequestImageEvent";
import {ReceiveImageEvent} from "./Events/ReceiveImageEvent";
import ClearEvent from "./Events/ClearEvent";
import {ReactionEvent} from "./Events/ReactionEvent";
import ConnectionErrorEvent from "./Events/Errors/ConnectionErrorEvent";
import {GlobalError, globalErrorEvent} from "../Pages/ErrorModal/GlobalModalError";
import {PagesEnum} from "../Pages/PagesEnum";
import {StateManager} from "./StateManager";
import {BACKEND_URL} from "./Http/Constants";
import {showNotAuthorizedMessage} from "./Errors/checkForAuthError";

class RoomConnection {
    private readonly _userId: number;
    private readonly _cookies: Cookies
    private _roomId: number | null = null;
    private _roomModel: RoomModel;

    //
    constructor(roomModel: RoomModel, userId: number, roomId: number) {
        this._roomModel = roomModel;
        this._userId = userId;
        this._roomId = roomId;
        this._cookies = new Cookies();
    }

    private _client: Stomp.Client | null = null

    private parseServerMessage(body: any): ServerEvent {
        let messageType: string = body["type"];
        switch (messageType) {
            case 'CHOOSE_WORD_MESSAGE': {
                let words: string[] = body["words"];
                return new ChooseWordEvent(words);
            }

            case "NEW_DRAWER_MESSAGE": {
                let userId: number = body["userId"];
                let userName: string = body["userName"];
                return new NewDrawerEvent(userId, userName);
            }

            case "INFO_MESSAGE": {
                let text: string = body["text"];
                return new InfoMessageEvent(text);
            }

            case "CHAT_MESSAGE": {
                let userId: number = body["userId"];
                let userName: string = body["userName"];
                let text: string = body["text"];
                let reaction: ReactionType = stringToReaction(body["reaction"]);
                let messageId: string = body["messageId"];
                return new UserMessageEvent(userId, messageId, userName, text, reaction)
            }

            case "REACTION_MESSAGE": {
                let messageId: string = body["messageId"];
                let reaction: ReactionType = stringToReaction(body["reaction"]);
                return new ReactionEvent(messageId, reaction)
            }

            case "DRAW_MESSAGE": {
                let startPoint: Point = body["startPoint"];
                let finishPoint: Point = body["finishPoint"];
                let color: string = body["color"];
                let size: number = body["size"];
                return new DrawEvent(startPoint, finishPoint, color, size);
            }

            case "IMAGE_MESSAGE": {
                let image: string = body["image"];
                return new ReceiveImageEvent(image);
            }

            case "GET_IMAGE_MESSAGE": {
                let receiverId: number = body["receiverId"];
                return new RequestImageEvent(receiverId);
            }

            case "CLEAR_MESSAGE": {
                return new ClearEvent();
            }

            case "CONNECTION_ERROR_MESSAGE": {
                return new ConnectionErrorEvent();
            }

            default:
                throw new Error();
        }
    }

    public connect(): boolean {
        if (this._client !== null) {
            console.error("Attempting to connect when it has already been completed")
            globalErrorEvent(new GlobalError("Attempting to connect when it has already been completed", PagesEnum.ROOM_LIST))
        }

        // this._cookies.set("userId", this._userId, {path: '/'})
        let sockJS = new SockJS(BACKEND_URL + "/game");
        this._client = Stomp.over(sockJS);
        // console.log("before connect")
        // onConnected: (frame?: Frame | undefined) => any, onError?: ((error: string | Frame) => any) | undefined
        console.log("token: " + AxiosService.getAuthToken());
        return this._client.connect({Authorization: `Bearer ${AxiosService.getAuthToken()}`}, () => {
            this.joinRoom()
            return true;
        }, (e: any) => showNotAuthorizedMessage());
    }

    private subscribe(topic: string, onMessageReceived: ((message: Stomp.Message) => any)) {
        if (this._client === null) {
            StateManager.trySetRoom(null);
            globalErrorEvent(new GlobalError('Connection lost', PagesEnum.ROOM_LIST))
            return;
        }

        this._client.subscribe(topic, onMessageReceived);
    }

    public sendMessage(topic: string, message: any) {
        if (this._client === null) {
            globalErrorEvent(new GlobalError('Connection lost', PagesEnum.ROOM_LIST))
            return;
        }
        this._client.send('/app' + topic, {}, JSON.stringify(message));
    }

    public trySendMessage(topic: string, message: any): boolean {
        if (this._client === null) {
            return false;
        }
        this._client.send('/app' + topic, {}, JSON.stringify(message));
        return true
    }

    private joinRoom() {
        let roomId = this._roomId;
        if (roomId === null) {
            throw new Error("roomId is null")
        }
        console.log("subscribe topic after connect")
        this.subscribe("/topic/session/" + roomId, (m) => {
            this._roomModel.handleEvent(this.parseServerMessage(JSON.parse(m.body)));
        });

        console.log("subscribe user after connect")
        this.subscribe("/user/queue/session", (m) => {
            this._roomModel.handleEvent(this.parseServerMessage(JSON.parse(m.body)));
        });
        console.log("join after connect")
        this.sendMessage("/join/" + this._roomId, {})
    }

    public disconnect() {
        this._roomId = null;
    }
}

export default RoomConnection;