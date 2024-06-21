import React from "react";
import {Button} from "antd";
import {RoomBrowser} from "./RoomBrowser";
import {useNavigate} from "react-router-dom";
import {AxiosService} from "../../Classes/Http/AxiosService";
import {PagesEnum} from "../../PagesEnum";

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
                        <div className='vertical'>
                            <RoomBrowser/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}