import React from "react";
import {Space} from "antd";
import {RoomModel} from "../../../Classes/RoomModel";
import {ChatArea, MessageData} from "./ChatArea";
import SendMessageLine from "./SendMessageLine";

type ChatProps = {
    messages: MessageData[],
    sendNewMessage: (message: string) => boolean,
    canType: boolean
};

class Chat extends React.Component<ChatProps, {}> {
    // public state: ChatState;

    constructor(props: ChatProps) {
        super(props);
        this.state = {
            messages: []
        }
    }

    render() {
        // let messages = this.state.Messages;
        return (
            <Space direction="vertical">
                <ChatArea Messages={this.props.messages}/>
                <SendMessageLine canType={this.props.canType} onClick={text => this.props.sendNewMessage(text)}/>
            </Space>
        );
    }
}

export {Chat};
