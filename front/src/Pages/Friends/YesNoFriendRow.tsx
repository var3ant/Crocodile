import React from "react";
import {UserNameResponse} from "../../Classes/Http/Response/UserNameResponse";
import CheckButton from "../../Classes/Http/Response/Button/CheckButton";
import CloseButton from "../../Classes/Http/Response/Button/CloseButton";

export default function YesNoFriendRow(props: Readonly<{
    user: UserNameResponse,
    onYesClick: () => Promise<void>,
    onNoClick: () => Promise<void>
}>) {

    return (
        <div className='horizontal' key={props.user.id}>
            <div style={{alignSelf: 'center', margin: '0 5px 0 0'}}>{props.user.name}</div>
                <div style={{margin: ' 0 4px 0 0'}}>
                    <CheckButton onClick={() => props.onYesClick()}/>
                </div>
                <div style={{margin: '0 4px 0 0'}}>
                    <CloseButton onClick={() => props.onNoClick()}/>
                </div>
        </div>)
}