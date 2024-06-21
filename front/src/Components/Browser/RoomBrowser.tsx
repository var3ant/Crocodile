import React, {useEffect, useState} from "react";
import {Button, Flex, Table, TableProps} from "antd";
import {HttpRoomApi} from "../../Classes/Http/HttpRoomApi";
import RoomView from "../../Classes/Http/Response/RoomView";
import {StateManager} from "../../Classes/StateManager";
import {PagesEnum} from "../../index";
import {useNavigate} from "react-router-dom";
import "../Style/Room.css";

export function RoomBrowser() {
    const [rows, setRows] = useState<RoomView[]>([])
    const navigate = useNavigate()

    const columns: TableProps<RoomView>['columns'] = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            render: (text) => <a>{text}</a>,
        },
        {
            title: 'Id',
            dataIndex: 'id',
            key: 'id',
        }
    ];

    async function update() {
        let rows = await HttpRoomApi.getRooms();
        setRows(rows);
    }

    useEffect(() => {
        update()
    }, [])

    return (
        <Flex gap="middle" justify="left" vertical>
            <Table style={{minHeight: 500, maxHeight: 500, backgroundColor: "white"}} locale={{emptyText: ' '}}
                   columns={columns} dataSource={rows}
                   onRow={(roomView, rowIndex) => {
                       return {
                           onClick: event => {
                               console.log(roomView.id + roomView.name);
                               StateManager.trySetRoom(roomView.id);
                               navigate(PagesEnum.ROOM + roomView.id);
                           },
                       };
                   }}
            />
            <Button onClick={_ => {
                update();
            }}>Update list</Button>
        </Flex>
    );
}

// private
//     getRandomInt(max
// :
//     number
// ):
//     number
//     {
//         return Math.floor(Math.random() * max);
//     }
// }