import React, {useEffect, useState} from "react";
import {Card} from "antd";
import {HttpFriendApi} from "../../Classes/Http/HttpFriendApi";
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
                            <YesNoFriendRow key={follower.id} user={follower}
                                            onYesClick={() => HttpFriendApi.sendRequest(follower.id).then(update)}
                                            onNoClick={() => HttpFriendApi.declineRequest(follower.id).then(update)}
                            />);
                    })}
                </Card>
            </div>
        </div>
    );
}