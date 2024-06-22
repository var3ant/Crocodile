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
            <div>{props.user.name}</div>
            <div>
                <CheckButton onClick={() => props.onYesClick()}/>
                <CloseButton onClick={() => props.onNoClick()}/>
            </div>
        </div>)
}