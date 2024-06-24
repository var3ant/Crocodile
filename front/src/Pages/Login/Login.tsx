import React, {KeyboardEventHandler, useState} from "react";
import {Button, Card, Input} from "antd";

export type onClickFunc = (login: string, password: string) => Promise<void>;


export function Login(props: Readonly<{ onClick: onClickFunc, buttonText: string }>) {
    let [login, setLogin] = useState("");
    let [password, setPassword] = useState("");
    let [isLoading, setLoading] = useState(false);

    const preventDefault = (e: any) => e.preventDefault();
    const onKeyUp = (e: any) => {
        if (e.key === 'Enter') {
            onClick();
        }
    }

    const onClick = () => {
        if (isLoading) {
            return;
        }
        setLoading(true);

        props.onClick(login, password).finally(() => setLoading(false));
    }


    return (
        <Card bodyStyle={{paddingBottom: '12px', paddingTop: '12px'}} onKeyUp={onKeyUp}>
            <div className='vertical' style={{gap: '10px'}}>
                <Input addonBefore='Name' placeholder="Enter name" value={login}
                       onChange={text => setLogin(text.target.value)}/>
                <Input.Password addonBefore='Password' placeholder="Enter password" value={password}
                                onChange={e => setPassword(e.target.value)}/>
                <Button loading={isLoading} style={{marginLeft: 'auto'}} type="primary"
                        onKeyDown={preventDefault}
                        onClick={onClick}
                >{props.buttonText}</Button>
            </div>
        </Card>
    );
}
