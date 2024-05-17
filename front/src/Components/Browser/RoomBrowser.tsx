import React, {useState} from "react";
import {Button, Flex, Table, TableProps} from "antd";
import {HttpRoomApi} from "../../Classes/Http/HttpRoomApi";
import {RoomView} from "../../Classes/Http/Response/RoomView";
import {StateManager} from "../../Classes/StateManager";
import {PagesEnum} from "../../index";
import {useNavigate} from "react-router-dom";
import "../Style/Room.css";

export function RoomBrowser(
    // onSubmit: (roomView: RoomView) => void
) {
    // constructor(props: any) {
    //     super(props);
    //     this.state = {rows: []};
    //     this.updateRooms();
    // }
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


    const updateRooms = () => {
        HttpRoomApi.getRooms().then(rows => {
            setRows(rows)
            console.log(rows)
        });
    }

    return (
        <Flex gap="middle" justify="left" vertical>
            <Table columns={columns} dataSource={rows}
                   onRow={(roomView, rowIndex) => {
                       return {
                           onClick: event => {
                               console.log(roomView.id + roomView.name);
                               StateManager.setRoom(roomView.id);
                               navigate(PagesEnum.ROOM + roomView.id);
                           }, // click row
                       };
                   }}
            />
            <Button onClick={e => {
                updateRooms();
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