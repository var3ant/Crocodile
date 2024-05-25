import React, {useState} from "react";
import {Button, Input, Space} from "antd";

export type onClickFunc = (login: string, password: string) => void;


export function Login(props: Readonly<{onClick: onClickFunc}>) {
    let [login, setLogin] = useState("");
    let [password, setPassword] = useState("");

    return (
        <Space.Compact style={{width: '100%'}}>
            <Input placeholder="Enter name" value={login}
                   onChange={text => setLogin(text.target.value)}/>
            <Input placeholder="Enter password" value={password}
                   onChange={text => setPassword(text.target.value)}/>
            <Button type="primary" onClick={() => {
                props.onClick(login, password)
            }}>Send</Button>
        </Space.Compact>
    );
}
