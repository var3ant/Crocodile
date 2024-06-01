import {Card, List} from "antd";
import React from "react";
import VirtualList from 'rc-virtual-list';

class MessageData {
    constructor(public id: number, public name: string, public text: string, public userMessage: boolean = true) {
    }
}


const ContainerHeight = 600;
const ContainerWidth = 350;

class ChatArea extends React.Component<{ Messages: MessageData[] }> {
    render() {
        return (
            <Card style={{
                height: ContainerHeight,
                width: ContainerWidth,
                maxWidth: ContainerWidth,
                maxHeight: ContainerHeight,
                overflowY: 'visible',
                overflowX: 'hidden'
            }}>
                <List locale={{emptyText: ' '}}
                    dataSource={this.props.Messages}
                    renderItem={(item: MessageData) => (
                        <List.Item key={item.id}>
                            <div style={{width: "100%"}}>
                                <div style={{wordBreak: "break-word", textAlign: "left"}}>
                                    <b>{item.name}: </b>{item.text}</div>
                            </div>
                        </List.Item>
                    )}
                >
                </List>
            </Card>
        );
    }
}


export {ChatArea, MessageData}