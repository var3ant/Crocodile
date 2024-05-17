import React, {useState} from 'react';
import {Chat} from "./Chat/Chat";
import {Button, Flex, Space} from "antd";
import {ChooseWordDialog} from "./ChooseWordDialog";
import {DrawCanvas} from "./Drawer/DrawCanvas";
import {StateManager} from "../../Classes/StateManager";
import {MessageData} from "./Chat/ChatArea";
import {ServerEvent} from "../../Classes/Events/ServerEvent";
import {UserMessageEvent} from "../../Classes/Events/UserMessageEvent";
import {InfoMessageEvent} from "../../Classes/Events/InfoMessageEvent";
import {NewDrawerEvent} from "../../Classes/Events/NewDrawerEvent";
import {DrawEvent, Point} from "../../Classes/Events/DrawEvent";
import {ChooseWordEvent} from "../../Classes/Events/ChooseWordEvent";
import {useNavigate} from "react-router-dom";
import {PagesEnum} from "../../index";

export function RoomPage() {
    const _canvas: React.RefObject<DrawCanvas> = React.createRef();
    const _chooseWordDialog: React.RefObject<ChooseWordDialog> = React.createRef();
    const _chat: React.RefObject<Chat> = React.createRef();

    // constructor(props: any) {
    //     StateManager.getRoom()?.subscribeEvents((event => {
    //         this.eventHandler(event)
    //     }));
    // }
    const [messages, setMessages] = useState<MessageData[]>([]);
    const [wordsToChoose, setWordsToChoose] = useState<string[] | null>(null);
    const [word, setWord] = useState<string | null>(null)
    const [isDrawer, setIsDrawer] = useState<boolean>(false)

    const eventHandler = (event: ServerEvent) => {
        if (event instanceof UserMessageEvent) {

            setMessages([...messages, new MessageData(event.userId, event.userId, event.text)])

        } else if (event instanceof InfoMessageEvent) {

            setMessages([...messages, new MessageData("Info", "Info", event.text, false)])

        } else if (event instanceof NewDrawerEvent) {

            let isDrawer = event.userId === StateManager.getRoom()?.userId;
            setMessages([...messages, new MessageData("New drawer", "New drawer", event.userId, false)])
            setIsDrawer(isDrawer)
            _canvas.current?.clear();

        } else if (event instanceof DrawEvent) {

            _canvas.current?.drawLine(event.startPoint, event.finishPoint);

        } else if (event instanceof ChooseWordEvent) {

            setWordsToChoose(event.words)

        }
    }

    StateManager.getRoom()?.subscribeEvents((event => {
        eventHandler(event)
    }));

    const chooseWord = (index: number, word: string) => {
        setWordsToChoose([])
        setWord(word)
        StateManager.getRoom()?.wordChosen(index)
        // console.log("word: " + word)
    }

    const clientNewMessage = (message: string): boolean => {
        let room = StateManager.getRoom();
        if (room === null) {
            console.assert("clientNewMessage: room === null")
            return false;
        }

        return room.sendMessage(message);
    }

    const clientNewDraw = (startPoint: Point, finishPoint: Point) => {
        StateManager.getRoom()?.drawLine(startPoint, finishPoint);
    }

    const navigate = useNavigate();

    return (
        <Flex>
            <Button onClick={e => {
                let room = StateManager.getRoom();
                if (room === null) {
                    console.assert("onClickLeave: room === null")
                    return;
                }
                room.leave();
                navigate(PagesEnum.ROOM_LIST);
            }}>Leave</Button>
            <Space>
                <ChooseWordDialog ref={_chooseWordDialog}
                                  words={wordsToChoose}
                                  onClose={(index: number, word: string) => chooseWord(index, word)}
                                  open={wordsToChoose !== null}/>
                <DrawCanvas ref={_canvas}
                            canUserPaint={isDrawer && word !== null}
                            drawSubscriber={(startPoint: Point, finishPoint: Point) =>
                                clientNewDraw(startPoint, finishPoint)}/>
                <Chat ref={_chat}
                      canType={!isDrawer}
                      messages={messages}
                      sendNewMessage={(message: string) => clientNewMessage(message)}
                />
            </Space>
        </Flex>
    );
}

export default RoomPage;
