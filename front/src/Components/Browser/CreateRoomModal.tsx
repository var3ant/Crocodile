import React, {useState} from "react";
import {Input, Modal} from "antd";
import {HttpRoomApi} from "../../Classes/Http/HttpRoomApi";
import {StateManager} from "../../Classes/StateManager";
import {useNavigate} from "react-router-dom";
import {Errors, errorToString} from "../../Classes/ErrorMessages";
import {PagesEnum} from "../../PagesEnum";

export default function CreateRoomModal(props: Readonly<{
    isOpen: boolean,
    setOpen: (val: boolean) => void
}>) {
    const [name, setName] = useState<string>("")
    const [errorMessage, setErrorMessage] = useState<string>("")
    const navigate = useNavigate();

    const okText = async (): Promise<boolean> => {
        console.log("create room: " + name)
        let result = HttpRoomApi.createRoom({name: name}).then(roomId => {
            if (!StateManager.trySetRoom(roomId)) {
                setErrorMessage("roomId is null. Unknown error")
                return false;
            }
            navigate(PagesEnum.ROOM + roomId);
            return true;

        }).catch(e => {
            let errorType = e?.response?.data;
            if (errorType == null) {
                setErrorMessage("Unknown error")
            } else if (errorType === errorToString(Errors.ALREADY_EXIST_MESSAGE)) {
                setErrorMessage("Room with this name already exist")
            }
            return false;
        });

        return await result;
    }

    return (
        <Modal title={<h3 style={{textAlign: 'center'}}>Create room</h3>}
               open={props.isOpen}
               onCancel={() => props.setOpen(false)}
               onOk={okText}
               okText="Create"
               cancelText="Cancel">
            <Input addonBefore='Room name' onChange={(e) => setName(e.target.value)}/>
            <div className='vertical' style={{textAlign:'center', margin: '10px 0px'}}>
                <b style={{color: 'red'}}>{errorMessage}</b>
            </div>
        </Modal>
    );
}