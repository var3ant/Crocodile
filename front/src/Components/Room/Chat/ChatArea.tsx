import {Card, List} from "antd";
import React from "react";
import MessageReactionComponent from "./MessageReactionComponent";
import {ReactionType} from "../../../Classes/Events/UserMessageEvent";

class MessageData {
    constructor(
        public id: number,
        public messageId: string,
        public name: string,
        public text: string,
        public reaction: ReactionType | null = null) {
    }
}


const ContainerHeight = 600;
const ContainerWidth = 350;

class ChatArea extends React.Component<{ Messages: MessageData[], isDrawer: boolean }> {
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
                    //   dataSource={[new MessageData(1, "name1", "asdasdasd", true)]}
                      renderItem={(item: MessageData) => (
                          <List.Item key={item.id}>
                              <div style={{width: "100%", display: 'flex', flexDirection: 'row'}}>
                                  <div style={{wordBreak: "break-word", textAlign: "left"}}>
                                      <b>{item.name}: </b>{item.text}
                                  </div>
                                  <div style={{marginLeft: 'auto'}}>
                                      <MessageReactionComponent messageId={item.messageId} reaction={item.reaction} isDrawer={this.props.isDrawer}/>
                                  </div>
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