import {FriendsComponent} from "./FriendsComponent";
import ReceivedFriendRequestComponent from "./ReceivedFriendRequestComponent";
import SentFriendRequestComponent from "./SentFriendRequestComponent";
import SearchUserModal from "./SearchUserModal";
import {Button} from "antd";
import React, {useState} from "react";
import {AxiosService} from "../../Classes/Http/AxiosService";
import {PagesEnum} from "../PagesEnum";
import {useNavigate} from "react-router-dom";

export function FriendsPage() {
    const [isSearchOpen, setSearchOpen] = useState<boolean>(false)
    const navigate = useNavigate();

    const onAddClick = () => {
        setSearchOpen(true);
    }

    const menu = (
        <div className='horizontal' style={{alignSelf: 'center', gap: '5px'}}>
            <Button onClick={() => {
                AxiosService.setAuthToken(null)
                navigate(PagesEnum.LOGIN)
            }}>Log Out</Button>
            <Button onClick={() => {
                navigate(PagesEnum.ROOM_LIST)
            }}>Room list</Button>
        </div>
    )

    return (
        <div className='vertical'>
            <div style={{
                display: 'flex',
                flexDirection: 'column',
            }}>
                {menu}

                <h1 style={{alignSelf: 'center'}}>Friends</h1>
                <div className='outer_frame'>
                    <div className='grid_container'>
                        <div className='vertical'>
                            <div className='horizontal'>
                                <div className='vertical'
                                     style={{justifySelf: 'stretch', alignSelf: 'stretch', flex: 3}}>
                                    <FriendsComponent/>
                                    <div className='horizontal' style={{flexDirection: 'row-reverse'}}>
                                        <Button style={{margin: '5px'}} onClick={onAddClick}>Add friend</Button>
                                    </div>
                                </div>
                                <div className='vertical'
                                     style={{alignItems: 'stretch', alignSelf: 'stretch', flex: 1}}>
                                    <ReceivedFriendRequestComponent/>
                                    <SentFriendRequestComponent/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <SearchUserModal isOpen={isSearchOpen}
                             setOpen={(value: boolean) => setSearchOpen(value)}/>
        </div>
    );
}