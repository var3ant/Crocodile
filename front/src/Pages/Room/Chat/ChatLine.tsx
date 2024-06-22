import React from "react";
import {MessageData} from "./ChatArea";

class ChatLine extends React.Component<{ messageData: MessageData }> {
    render() {
        return (
            <p style={{float:"left"}}>{this.props.messageData.name}: {this.props.messageData.text}</p>
        );
    }
}

export default ChatLine;
