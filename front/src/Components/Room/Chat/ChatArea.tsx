import {Card, List} from "antd";
import React, {useEffect} from "react";
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


const ContainerHeight = 582;
const ContainerWidth = 350;

export default function ChatArea(props: Readonly<{ Messages: MessageData[], isDrawer: boolean }>) {
    const listRef = React.useRef<any>(null);

    useEffect(() => {
        if (listRef.current) {
            listRef.current.scrollIntoView(false);
        }
    }, [props.Messages]);

    return (
        <div>
            <Card bodyStyle={{padding: '0px'}} style={{
                height: ContainerHeight,
                width: ContainerWidth,
                maxWidth: ContainerWidth,
                maxHeight: ContainerHeight,
                overflowY: 'visible',
                overflowX: 'hidden',
            }}>
                    <div ref={listRef} style={{padding: '15px'}}>
                        <List locale={{emptyText: ' '}}
                              dataSource={props.Messages}
                              renderItem={(item: MessageData) => (
                                  <List.Item key={item.id}>
                                      <div style={{width: "100%", display: 'flex', flexDirection: 'row'}}>
                                          <div style={{wordBreak: "break-word", textAlign: "left"}}>
                                              <b>{item.name}: </b>{item.text}
                                          </div>
                                          <div style={{marginLeft: 'auto'}}>
                                              <MessageReactionComponent messageId={item.messageId} reaction={item.reaction} isDrawer={props.isDrawer}/>
                                          </div>
                                      </div>
                                  </List.Item>
                              )}
                        >
                        </List>
                    </div>
            </Card>
        </div>
    );
}


export {MessageData}