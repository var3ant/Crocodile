import React, {useEffect, useState} from "react";
import {Button, Card, Flex} from "antd";
import {HttpFriendApi} from "../../Classes/Http/HttpFriendApi";
import {CheckOutlined, CloseOutlined} from '@ant-design/icons';
import {green, red} from "@ant-design/colors";
import {UserNameResponse} from "../../Classes/Http/Response/UserNameResponse";
import YesNoFriendRow from "./YesNoFriendRow";

export default function ReceivedFriendRequestComponent() {
    const [requests, setRequests] = useState<UserNameResponse[]>([])

    useEffect(() => {
        update()
    }, [])

    async function update() {
        let rows = await HttpFriendApi.getReceivedRequests();
        setRequests(rows);
    }

    return (
        <div className='vertical'>
            <h3 style={{alignSelf: 'center', margin: '5px 0px'}}>Incoming requests</h3>
            <div style={{
                display: 'flex',
                flexDirection: 'column',
                margin: '5px',
                justifyContent: 'stretch', alignContent: 'stretch'
            }}>
                <Card style={{minHeight: '200px'}}>
                    {requests.map((follower) => {
                        return (
                            <YesNoFriendRow user={follower}
                                            onYesClick={() => HttpFriendApi.sendRequest(follower.id).then(update)}
                                            onNoClick={() => HttpFriendApi.declineRequest(follower.id).then(update)}
                            />);
                    })}
                </Card>
            </div>
        </div>
    );
}