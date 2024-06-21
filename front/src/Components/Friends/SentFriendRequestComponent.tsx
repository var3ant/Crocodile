import React, {useEffect, useState} from "react";
import {Card} from "antd";
import {HttpFriendApi} from "../../Classes/Http/HttpFriendApi";
import {UserNameResponse} from "../../Classes/Http/Response/UserNameResponse";
import CloseButton from "../../Classes/Http/Response/Button/CloseButton";

export default function SentFriendRequestComponent() {
    const [requests, setRequests] = useState<UserNameResponse[]>([])

    useEffect(() => {
        update()
    }, [])

    async function update() {
        let rows = await HttpFriendApi.getSentRequests();
        setRequests(rows);
    }

    return (
        <div className='vertical'>
            <h3 style={{alignSelf: 'center', margin:'5px 0px'}}>Outcoming requests</h3>
            <div style={{
                display: 'flex',
                flexDirection: 'column',
                margin: '5px',
            }}>
                <Card style={{minHeight: '200px'}}>
                    {requests.map((following) => {
                        return (
                            <div className='horizontal' key={following.id}>
                                <div>{following.name}</div>
                                <CloseButton
                                        onClick={() => {
                                            HttpFriendApi.cancelRequest(following.id).then(update)
                                        }}/>
                            </div>)
                    })}
                </Card>
            </div>
        </div>
    );
}