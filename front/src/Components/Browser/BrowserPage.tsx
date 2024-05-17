import React from "react";
import {Col, Row} from "antd";
import {RoomBrowser} from "./RoomBrowser";
import {CreateRoomForm} from "./CreateRoomForm";
import {useNavigate} from "react-router-dom";

export function BrowserPage() {
    const navigate = useNavigate();

    // const onRoomChoose = (roomView: RoomView) => {
    //
    // }

    return (
        <>
            <Row justify="center" align="middle">
                <Col flex={3}>
                    <RoomBrowser/>
                </Col>
                <Col flex={1}>
                    <CreateRoomForm/>
                </Col>
            </Row>
        </>
    );
}