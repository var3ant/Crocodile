import React, {useState} from "react";
import {Button, Card, Input} from "antd";

export type onClickFunc = (login: string, password: string) => void;


export function Login(props: Readonly<{ onClick: onClickFunc, buttonText: string }>) {
    let [login, setLogin] = useState("");
    let [password, setPassword] = useState("");

    return (
        <Card bodyStyle={{paddingBottom: '12px', paddingTop: '12px'}}>
            <div className='vertical' style={{gap: '10px'}}>
                <Input addonBefore='Name' placeholder="Enter name" value={login}
                       onChange={text => setLogin(text.target.value)}/>
                <Input addonBefore='Password' placeholder="Enter password" value={password}
                       onChange={text => setPassword(text.target.value)}/>
                <Button style={{marginLeft: 'auto'}} type="primary" onClick={() => {
                    props.onClick(login, password)
                }}>{props.buttonText}</Button>
            </div>
        </Card>
    );
}
