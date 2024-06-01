import React, {useState} from "react";
import {Button, Card, Col, Input, InputNumber, Row} from "antd";
import {HttpRoomApi} from "../../Classes/Http/HttpRoomApi";
import {StateManager} from "../../Classes/StateManager";
import {PagesEnum} from "../../index";
import {useNavigate} from "react-router-dom";

export interface CreateRoomData {
    name: string,
    password: string,
    maxPlayers: number
}

export function CreateRoomForm(
    // {onSubmit}: { onSubmit: (roomData: CreateRoomData) => void }
) {
    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [maxPlayers, setMaxPlayers] = useState(0);
    const navigate = useNavigate();

    const createRoomData = (): CreateRoomData => {
        return {name: name, password: password, maxPlayers: maxPlayers};
    }

    const rowStyle = {width: "100%", justifyContent: 'center'}

    return (
        <Card>
            <Row style={rowStyle}>
                <Col span={5}>
                    <label>Name:</label>
                </Col>
                <Col span={5}>
                    <Input
                        value={name}
                        onChange={e => setName(e.target.value)}
                    />
                </Col>
            </Row>
            <Row style={rowStyle}>
                <Col span={5}>
                    <label>Password:</label>
                </Col>
                <Col span={5}>
                    <Input
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                    />
                </Col>
            </Row>
            <Row style={rowStyle}>
                <Col span={5}>
                    <label>Max players:</label>
                </Col>
                <Col span={5}>
                    <InputNumber
                        value={maxPlayers}
                        onChange={e => setMaxPlayers(e ?? 0)}
                    />
                </Col>
            </Row>
                <Button
                    type="primary" onClick={e => {
                    let roomData = createRoomData();
                    console.log(roomData)
                    HttpRoomApi.createRoom(roomData).then(roomId => {
                        StateManager.trySetRoom(roomId);
                        navigate(PagesEnum.ROOM + roomId);
                    })
                }}>
                    Create
                </Button>
        </Card>
    );
}