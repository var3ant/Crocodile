import React, {useEffect, useState} from "react";
import {Button, Flex, Table, TableProps} from "antd";
import {HttpRoomApi} from "../../Classes/Http/Api/HttpRoomApi";
import RoomView from "../../Classes/Http/Response/RoomView";
import {StateManager} from "../../Classes/StateManager";
import {useNavigate} from "react-router-dom";
import "../Style/Room.css";
import CreateRoomModal from "./CreateRoomModal";
import {PagesEnum} from "../PagesEnum";
import {GlobalError, globalErrorEvent} from "../ErrorModal/GlobalModalError";

export function RoomBrowser() {
    const [rows, setRows] = useState<RoomView[]>([])
    const [isCreateRoomOpen, setCreateRoomOpen] = useState<boolean>(false)
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
                               if (!StateManager.trySetRoom(roomView.id)) {
                                   globalErrorEvent(new GlobalError('Room not found', PagesEnum.ROOM_LIST))
                                   return;
                               }
                               navigate(PagesEnum.ROOM + roomView.id);
                           },
                       };
                   }}
            />
            <div style={{display: 'flex', flexDirection: 'row-reverse'}}>
                <Button style={{margin: '4px 4px 4px 2px'}} onClick={_ =>
                    setCreateRoomOpen(true)
                }>Create room</Button>
                <Button style={{margin: '4px 4px 4px 2px'}} onClick={_ =>
                    update()
                }>Update list</Button>
            </div>
            <CreateRoomModal isOpen={isCreateRoomOpen}
                             setOpen={(value: boolean) => setCreateRoomOpen(value)}/>
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