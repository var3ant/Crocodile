import {useNavigate} from "react-router-dom";
import {FriendsComponent} from "./FriendsComponent";
import ReceivedFriendRequestComponent from "./ReceivedFriendRequestComponent";
import SentFriendRequestComponent from "./SentFriendRequestComponent";
import SearchUserModal from "./SearchUserModal";
import {Button} from "antd";
import React, {useState} from "react";

export function FriendsPage() {
    const [isSearchOpen, setSearchOpen] = useState<boolean>(false)

    const onAddClick = () => {
        setSearchOpen(true);
    }

    return (
        <div className='vertical'>
            <div style={{
                display: 'flex',
                flexDirection: 'column',
            }}>
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
                                     style={{alignItems: 'stretch', justifyItems: 'stretch', flex: 1}}>
                                    <ReceivedFriendRequestComponent/>
                                    <SentFriendRequestComponent/>
                                </div>
                                <SearchUserModal isOpen={isSearchOpen}
                                                 setOpen={(value: boolean) => setSearchOpen(value)}/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}