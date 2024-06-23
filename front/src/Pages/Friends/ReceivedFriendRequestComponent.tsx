import React, {useEffect, useState} from "react";
import {Card} from "antd";
import {UserNameResponse} from "../../Classes/Http/Response/UserNameResponse";
import YesNoFriendRow from "./YesNoFriendRow";
import {HttpFriendRequestApi} from "../../Classes/Http/HttpFriendRequestApi";

export default function ReceivedFriendRequestComponent() {
    const [requests, setRequests] = useState<UserNameResponse[]>([])

    useEffect(() => {
        update()
    }, [])

    async function update() {
        let rows = await HttpFriendRequestApi.getReceivedRequests();
        setRequests(rows);
    }

    return (
        <div className='vertical'>
            <h3 style={{alignSelf: 'center', margin: '5px 0px'}}>Incoming requests</h3>
            <div style={{
                display: 'flex',
                flexDirection: 'column',
                margin: '5px',
                justifyContent: 'stretch', alignContent: 'stretch',
                overflowY: 'visible',
                overflowX: 'hidden'
            }}>
                <Card style={{minHeight: '200px', alignSelf:'stretch'}}>
                    {requests.map((follower) => {
                        return (
                            <YesNoFriendRow key={follower.id} user={follower}
                                            onYesClick={() => HttpFriendRequestApi.sendRequest(follower.id).then(update)}
                                            onNoClick={() => HttpFriendRequestApi.declineRequest(follower.id).then(update)}
                            />);
                    })}
                </Card>
            </div>
        </div>
    );
}