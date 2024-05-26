import React from "react";
import {Button, Col, Row} from "antd";
import {RoomBrowser} from "./RoomBrowser";
import {CreateRoomForm} from "./CreateRoomForm";
import {useNavigate} from "react-router-dom";
import {PagesEnum} from "../../index";
import {AxiosService} from "../../Classes/Http/AxiosService";

export function BrowserPage() {
    const navigate = useNavigate();

    return (
        <div className='vertical'>
            <Button style={{width: 'fit-content', alignSelf: 'center'}} onClick={() => {
                AxiosService.setAuthToken(null)
                navigate(PagesEnum.LOGIN)
            }}>Log Out</Button>
            <div style={{
                display: 'flex',
                flexDirection: 'column',
            }}>
                <h1 style={{alignSelf: 'center'}}>Browser</h1>
                <div className='outer_frame'>
                    <div className='grid_container'>
                        <div style={{flex: 3}}>
                            <RoomBrowser/>
                        </div>
                        <div style={{flex: 1}}>
                            <CreateRoomForm/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}