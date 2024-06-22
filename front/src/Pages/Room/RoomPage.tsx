import React, {useEffect, useState} from 'react';
import {Chat} from "./Chat/Chat";
import {Button, Flex, Space} from "antd";
import {ChooseWordDialog} from "./ChooseWordDialog";
import {DrawCanvas, PaintingSettings} from "./Drawer/DrawCanvas";
import {StateManager} from "../../Classes/StateManager";
import {MessageData} from "./Chat/ChatArea";
import {ServerEvent} from "../../Classes/Events/ServerEvent";
import {UserMessageEvent} from "../../Classes/Events/UserMessageEvent";
import {InfoMessageEvent} from "../../Classes/Events/InfoMessageEvent";
import {NewDrawerEvent} from "../../Classes/Events/NewDrawerEvent";
import {DrawEvent, Point} from "../../Classes/Events/DrawEvent";
import {ChooseWordEvent} from "../../Classes/Events/ChooseWordEvent";
import {useNavigate, useParams} from "react-router-dom";
import {RequestImageEvent} from "../../Classes/Events/RequestImageEvent";
import {ReceiveImageEvent} from "../../Classes/Events/ReceiveImageEvent";
import {DrawMenu} from "./Drawer/DrawMenu";
import {ReactionEvent} from "../../Classes/Events/ReactionEvent";
import ClearEvent from "../../Classes/Events/ClearEvent";
import ConnectionErrorEvent from "../../Classes/Events/Errors/ConnectionErrorEvent";
import {PagesEnum} from "../PagesEnum";
import {globalErrorEvent} from "../ErrorModal/GlobalModalError";

export function RoomPage() {
    const _canvas: React.RefObject<DrawCanvas> = React.createRef();
    const _chooseWordDialog: React.RefObject<ChooseWordDialog> = React.createRef();
    const _chat: React.RefObject<Chat> = React.createRef();

    const [messages, setMessages] = useState<MessageData[]>([]);
    const [wordsToChoose, setWordsToChoose] = useState<string[] | null>(null);
    const [word, setWord] = useState<string | null>("")
    const [isDrawer, setIsDrawer] = useState<boolean>(false)
    const [paintingSettings, setPaintingSettings] = useState<PaintingSettings>({size: 5, color: 'black'})

    const params = useParams();

    //connect to game
    useEffect(() => {
        const roomIdString = params.roomId;
        if (roomIdString === undefined) {
            throw new Error("roomId not defined");
        }
        const roomId = Number.parseInt(roomIdString);
        if (Number.isNaN(roomId)) {
            throw new Error("roomId not defined");
        }

        let room = StateManager.getRoom();
        if (room != null && room.getRoomId() !== roomId) {
            room.tryDisconnect();
            room = null;
        }

        if (room == null && !StateManager.trySetRoom(roomId)) {
            globalErrorEvent({redirectPath: PagesEnum.ROOM_LIST, message: 'Room not found'})
        }
    });

    const eventHandler = (event: ServerEvent) => {
        if (event instanceof UserMessageEvent) {

            setMessages([...messages, event.toMessageData()]);

        } else if (event instanceof InfoMessageEvent) {

            setMessages([...messages, new MessageData(-1, "", "Info", event.text)])

        } else if (event instanceof ReactionEvent) {

            let message = messages.find(m => m.messageId === event.messageId)
            if (message == undefined) {
                console.warn("ReactionEvent: message by messageId not found")
            } else {
                console.log("message: " + message.text + "; reaction: " + event.reaction)
                message.reaction = event.reaction;
                setMessages([...messages])
            }

        } else if (event instanceof NewDrawerEvent) {

            let isDrawer = event.userId === StateManager.getRoom()?.userId;
            setMessages([...messages, new MessageData(-1, "", "New drawer", event.userName)])
            setIsDrawer(isDrawer)
            console.log("Drawer id: " + event.userId)
            console.log("My id: " + StateManager.getRoom()?.userId)
            _canvas.current?.clear();

        } else if (event instanceof DrawEvent) {

            _canvas.current?.drawLine(event.startPoint, event.finishPoint, {color: event.color, size: event.size});

        } else if (event instanceof ChooseWordEvent) {

            setWordsToChoose(event.words)

        } else if (event instanceof RequestImageEvent) {

            let image = _canvas.current?.getImage() ?? '';
            StateManager.getRoom()?.sendImage(event.receiverId, image);

        } else if (event instanceof ReceiveImageEvent) {

            _canvas.current?.setImage(event.image);

        } else if (event instanceof ClearEvent) {

            _canvas.current?.clear();

        } else if (event instanceof ConnectionErrorEvent) {

            globalErrorEvent({redirectPath: PagesEnum.ROOM_LIST, message: 'Room not found'})

        }
    }

    StateManager.getRoom()?.subscribeEvents((event => {
        eventHandler(event)
    }));

    const chooseWord = (index: number, word: string) => {
        setWordsToChoose([])
        setWord(word)
        StateManager.getRoom()?.chooseWord(index)
        // console.log("word: " + word)
    }

    const clientNewMessage = (message: string): boolean => {
        let room = StateManager.getRoom();
        if (room === null) {
            console.assert("clientNewMessage: room === null")
            return false;
        }

        return room.sendChatMessage(message);
    }

    const clientNewDraw = (startPoint: Point, finishPoint: Point, settings: PaintingSettings) => {
        StateManager.getRoom()?.drawLine(startPoint, finishPoint, settings);
    }

    const clientClear = () => {
        if (isDrawer) {
            StateManager.getRoom()?.drawClear();
        }
    }

    const navigate = useNavigate();

    const messageWhatToDo = isDrawer ? "Draw: " + word : "Guess the word"

    return (
        <div className='vertical'>
            <Button style={{width: 'fit-content', alignSelf: 'center'}} onClick={_ => {
                let room = StateManager.getRoom();
                if (room === null) {
                    console.assert("onClickLeave: room === null")
                    return;
                }
                room.disconnect();
                navigate(PagesEnum.ROOM_LIST);
            }}>Leave</Button>
            <h1 style={{alignSelf: 'center'}}>Room: {params.roomId}</h1>
            <Flex className='room_container outer_frame'>
                <Space style={{
                    width: "100%",
                    height: "100%",
                    display: "flex",
                    justifyContent: "center"
                }}>
                    <div className='horizontal' style={{gap: '10px'}}>
                        <ChooseWordDialog ref={_chooseWordDialog}
                                          words={wordsToChoose}
                                          onClose={(index: number, word: string) => chooseWord(index, word)}
                        />
                        <div className='vertical'>
                            <DrawCanvas ref={_canvas}
                                        paintingSettings={paintingSettings}
                                        canUserPaint={isDrawer && word !== null}
                                        drawSubscriber={(startPoint: Point, finishPoint: Point, settings: PaintingSettings) =>
                                            clientNewDraw(startPoint, finishPoint, settings)}
                                        clearSubscriber={() => clientClear()}
                            />
                            <DrawMenu
                                onClear={() => _canvas.current?.clear()}
                                onChangeSettings={(settings) => setPaintingSettings(settings)}
                            />
                        </div>
                        <div className='vertical'>
                            <h3 style={{marginBottom: '10px', marginLeft: '10px'}}>{messageWhatToDo}</h3>
                            <Chat ref={_chat}
                                  isDrawer={isDrawer}
                                  messages={messages}
                                  sendNewMessage={(message: string) => clientNewMessage(message)}
                            />
                        </div>
                    </div>
                </Space>
            </Flex>
        </div>
    );
}

export default RoomPage;
