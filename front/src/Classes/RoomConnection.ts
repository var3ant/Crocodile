import SockJS from "sockjs-client"
import Stomp, {Frame} from "stompjs"
import Cookies from 'universal-cookie';
import {RoomModel} from "./RoomModel";
import {MessageData} from "../Components/Room/Chat/ChatArea";
import {ServerEvent} from "./Events/ServerEvent";
import {ChooseWordEvent} from "./Events/ChooseWordEvent";
import {NewDrawerEvent} from "./Events/NewDrawerEvent";
import {InfoMessageEvent} from "./Events/InfoMessageEvent";
import {UserMessageEvent} from "./Events/UserMessageEvent";
import {DrawEvent, Point} from "./Events/DrawEvent";
import {AxiosService} from "./Http/AxiosService";
import {RequestImageEvent} from "./Events/RequestImageEvent";
import {ReceiveImageEvent} from "./Events/ReceiveImageEvent";

class RoomConnection {
    private readonly _userId: number;
    private readonly _cookies: Cookies
    private _roomId: string | null = null;
    private _roomModel: RoomModel;

    //
    constructor(roomModel: RoomModel, userId: number, roomId: string) {
        this._roomModel = roomModel;
        this._userId = userId;
        this._roomId = roomId;
        this._cookies = new Cookies();
    }

    private _client: Stomp.Client | null = null

    //region parse messages
    private parseServerMessage(body: any): ServerEvent {
        let messageType: string = body["type"];
        switch (messageType) {
            case 'CHOOSE_WORD_MESSAGE': {
                let words: string[] = body["words"];
                return new ChooseWordEvent(words);
            }

            case "NEW_DRAWER_MESSAGE": {
                let userId: number = body["userId"];
                return new NewDrawerEvent(userId);
            }

            case "INFO_MESSAGE": {
                let text: string = body["text"];
                return new InfoMessageEvent(text);
            }

            case "CHAT_MESSAGE": {
                let userId: number = body["userId"];
                let text: string = body["text"];
                return new UserMessageEvent(userId, text)
            }

            case "DRAW_MESSAGE": {
                let startPoint: Point = body["startPoint"];
                let finishPoint: Point = body["finishPoint"];
                return new DrawEvent(startPoint, finishPoint);
            }

            case "IMAGE_MESSAGE": {
                let image: string = body["image"];
                return new ReceiveImageEvent(image);
            }

            case "GET_IMAGE_MESSAGE": {
                let receiverId: number = body["receiverId"];
                return new RequestImageEvent(receiverId);
            }

            default:
                throw new Error();
        }
    }

    //endregion

    //region websocket wrapper

    public connect(): void {
        if (this._client !== null) {
            //TODO:
            console.log("client not null")
            return;
        }
        // this._cookies.set("userId", this._userId, {path: '/'})
        let sockJS = new SockJS("http://localhost:8080/game");
        this._client = Stomp.over(sockJS);
        // console.log("before connect")
        // onConnected: (frame?: Frame | undefined) => any, onError?: ((error: string | Frame) => any) | undefined
        console.log("token: " + AxiosService.getAuthToken())
        this._client.connect({Authorization: `Bearer ${AxiosService.getAuthToken()}`}, () => {
            this.joinRoom()
        }, (error: string | Frame) => console.log("connect error:" + error));
    }

    private subscribe(topic: string, onMessageReceived: ((message: Stomp.Message) => any)) {
        if (this._client === null) {
            //TODO:
            return;
        }

        this._client.subscribe(topic, onMessageReceived);
    }

    private sendMessage(topic: string, message: any) {
        if (this._client === null) {
            //TODO:
            return;
        }
        this._client.send('/app' + topic, {}, JSON.stringify(message));
    }

    private joinRoom() {
        console.log("subscribe topic after connect")
        this.subscribe("/topic/session/" + this._roomId, (m) => {
            this._roomModel.handleEvent(this.parseServerMessage(JSON.parse(m.body)));
        });

        console.log("subscribe user after connect")
        this.subscribe("/user/queue/session", (m) => {
            this._roomModel.handleEvent(this.parseServerMessage(JSON.parse(m.body)));
        });
        console.log("join after connect")
        this.sendMessage("/join/" + this._roomId, {})
    }

    //endregion

    //region client messages

    public disconnect() {
        this.sendMessage("/disconnect", {})
        this._roomId = null;
    }

    public sendImage(receiverId: number, image: string) {
        this.sendMessage("/send_image", {receiverId: receiverId, image: image});
    }

    public chat(text: string) {
        this.sendMessage("/chat", {message: text});
    }

    public draw(startPoint: Point, finishPoint: Point) {
        this.sendMessage("/draw", {startPoint: startPoint, finishPoint: finishPoint});
    }

    public chooseWord(index: number) {
        this.sendMessage("/choose_word", {index: index.toString()});
    }

    //endregion
}

export default RoomConnection;