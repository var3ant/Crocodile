import React, {useState} from "react";
import { Card} from "antd";
import {useNavigate} from "react-router-dom";

export function CreateRoomForm(
    // {onSubmit}: { onSubmit: (roomData: CreateRoomData) => void }
) {
    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [maxPlayers, setMaxPlayers] = useState(0);
    const navigate = useNavigate();

    // const createRoomData = (): CreateRoomData => {
    //     return {name: name, password: password, maxPlayers: maxPlayers};
    // }

    const rowStyle = {padding: '5px 0px', alignContent: 'center'}
    const labelStyle = {width: '80px'}
    const inputStyle = {width: '250px'}

    return (
        <Card style={{width: '100%', height: '100%', alignContent: 'center', justifyContent: 'center'}}>
            {/*<div className='vertical' style={{width:'70%'}}>*/}
            {/*    <div className='horizontal' style={rowStyle}>*/}
            {/*        <label style={labelStyle}>Name:</label>*/}
            {/*        <Input style={inputStyle}*/}
            {/*            value={name}*/}
            {/*            onChange={e => setName(e.target.value)}*/}
            {/*        />*/}
            {/*    </div>*/}
            {/*    <div className='horizontal' style={rowStyle}>*/}
            {/*        <label style={labelStyle}>Password:</label>*/}
            {/*        <Input style={inputStyle}*/}
            {/*            value={password}*/}
            {/*            onChange={e => setPassword(e.target.value)}*/}
            {/*        />*/}
            {/*    </div>*/}
            {/*    <div className='horizontal' style={rowStyle}>*/}
            {/*        <label style={labelStyle}>Max players:</label>*/}
            {/*        <InputNumber style={inputStyle}*/}
            {/*            value={maxPlayers}*/}
            {/*            onChange={e => setMaxPlayers(e ?? 0)}*/}
            {/*        />*/}
            {/*    </div>*/}
            {/*    <div className='horizontal' style={{width: "100%", justifyContent: 'center'}}>*/}
            {/*        <Button*/}
            {/*            type="primary" onClick={e => {*/}
            {/*            let roomData = createRoomData();*/}
            {/*            console.log(roomData)*/}
            {/*            HttpRoomApi.createRoom(roomData).then(roomId => {*/}
            {/*                StateManager.trySetRoom(roomId);*/}
            {/*                navigate(PagesEnum.ROOM + roomId);*/}
            {/*            })*/}
            {/*        }}>*/}
            {/*            Create*/}
            {/*        </Button>*/}
            {/*    </div>*/}
            {/*</div>*/}
        </Card>
    );
}