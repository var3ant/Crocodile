import React from "react";
import {Space} from "antd";
import ChatArea, {MessageData} from "./ChatArea";
import SendMessageLine from "./SendMessageLine";

type ChatProps = {
    messages: MessageData[],
    sendNewMessage: (message: string) => boolean,
    isDrawer: boolean
};

class Chat extends React.Component<ChatProps, {}> {
    constructor(props: ChatProps) {
        super(props);
        this.state = {
            messages: []
        }
    }

    render() {
        return (
            <Space direction="vertical">
                <ChatArea Messages={this.props.messages} isDrawer={this.props.isDrawer}/>
                <SendMessageLine canType={!this.props.isDrawer} trySend={text => this.props.sendNewMessage(text)}/>
            </Space>
        );
    }
}

export {Chat};
