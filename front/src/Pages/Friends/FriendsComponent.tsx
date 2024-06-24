import {useNavigate} from "react-router-dom";
import {Flex, Table, TableProps} from "antd";
import React, {useEffect, useState} from "react";
import {StateManager} from "../../Classes/StateManager";
import {HttpFriendApi} from "../../Classes/Http/Api/HttpFriendApi";
import FriendView from "../../Classes/Http/Response/FriendView";
import {PagesEnum} from "../PagesEnum";
import {GlobalError, globalErrorEvent} from "../ErrorModal/GlobalModalError";

export function FriendsComponent() {
    const navigate = useNavigate();

    const [rows, setRows] = useState<FriendView[]>([])

    useEffect(() => {
        update()
    }, [])

    async function update() {
        let rows = await HttpFriendApi.getFriends();
        setRows(rows);
    }

    const columns: TableProps<FriendView>['columns'] = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Room',
            dataIndex: 'roomName',
            key: 'roomName',
            render: (roomName: string | null) => {
                if (roomName == null) {
                    return (<div>Not in game</div>);
                } else {
                    return (<a><b>{roomName}</b></a>)
                }
            }
        }
    ];

    return (
        <div className='vertical' style={{alignSelf:'stretch', justifySelf:'stretch'}}>
            <h3 style={{alignSelf: 'center', margin: '5px 0px'}}>Friends</h3>
            <div style={{
                display: 'flex',
                flexDirection: 'column',
            }}>
                <Flex gap="middle" justify="center" style={{width: '100%'}} vertical>
                    <Table style={{minHeight: 500, maxHeight: 500, width: '100%', backgroundColor: "white"}}
                           locale={{emptyText: ' '}}
                           columns={columns} dataSource={rows}
                           onRow={(friendView, rowIndex) => {
                               return {
                                   onClick: event => {
                                       let roomId = friendView.roomId;
                                       if (roomId == null) {
                                           return;
                                       }
                                       console.log(friendView.roomName);
                                       if (!StateManager.trySetRoom(roomId)) {
                                           globalErrorEvent(new GlobalError( 'Room not found'))
                                       } else {
                                           navigate(PagesEnum.ROOM + roomId);
                                       }
                                   },
                               };
                           }}
                    />
                </Flex>
            </div>
        </div>
    );
}