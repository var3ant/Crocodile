import React, {useState} from "react";
import {Input, Modal} from "antd";
import {HttpRoomApi} from "../../Classes/Http/HttpRoomApi";
import {StateManager} from "../../Classes/StateManager";
import {PagesEnum} from "../../index";
import {useNavigate} from "react-router-dom";

export default function CreateRoomModal(props: Readonly<{
    isOpen: boolean,
    setOpen: (val: boolean) => void
}>) {
    const [name, setName] = useState<string>("")
    const navigate = useNavigate();

    const okText = () => {
        console.log("create room: " + name)
        HttpRoomApi.createRoom({name: name}).then(roomId => {
            StateManager.trySetRoom(roomId);
            navigate(PagesEnum.ROOM + roomId);
        })
    }

    return (
        <Modal title={<h3 style={{textAlign: 'center'}}>Create room</h3>}
               open={props.isOpen}
               onCancel={() => props.setOpen(false)}
               onOk={okText}
               okText="Create"
               cancelText="Cancel">
            <Input addonBefore='Room name' onChange={(e) => setName(e.target.value)}/>
        </Modal>
    );
}