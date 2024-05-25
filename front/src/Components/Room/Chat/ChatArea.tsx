import {Card, Space} from "antd";
import React from "react";
import ChatLine from "./ChatLine";

class MessageData {
    constructor(public id: number, public name: string, public text: string, public userMessage: boolean = true) {
    }
}

class ChatArea extends React.Component<{ Messages: MessageData[] }> {
    render() {
        return (
            <Card style={{width: 300, height: 400, justifyContent:"left"}}>
            <Space direction="vertical" style={{width: "100%", height: "100%"}}>
                {this.props.Messages.map(message => <ChatLine key={message.id} messageData={message}/>)}
            </Space>
            </Card>
        );
    }
}

export {ChatArea, MessageData};


