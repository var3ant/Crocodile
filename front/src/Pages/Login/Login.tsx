import React, {KeyboardEventHandler, useState} from "react";
import {Button, Card, Input} from "antd";

export type onClickFunc = (login: string, password: string) => void;


export function Login(props: Readonly<{ onClick: onClickFunc, buttonText: string }>) {
    let [login, setLogin] = useState("");
    let [password, setPassword] = useState("");

    const preventDefault = (e: any) => e.preventDefault();
    const onKeyUp = (e: any) => {
        if (e.key === 'Enter') {
            props.onClick(login, password);
        }
    }


    return (
        <Card bodyStyle={{paddingBottom: '12px', paddingTop: '12px'}} onKeyUp={onKeyUp}>
            <div className='vertical' style={{gap: '10px'}}>
                <Input addonBefore='Name' placeholder="Enter name" value={login}
                       onChange={text => setLogin(text.target.value)}/>
                <Input.Password addonBefore='Password' placeholder="Enter password" value={password}
                                onChange={text => setPassword(text.target.value)}/>
                <Button style={{marginLeft: 'auto'}} type="primary"
                        onKeyDown={preventDefault}
                        onClick={(e) => {
                            props.onClick(login, password)
                        }}
                >{props.buttonText}</Button>
            </div>
        </Card>
    );
}
