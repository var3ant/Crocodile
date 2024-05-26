import React, {useState} from "react";
import {Button, Space} from "antd";
import {Login} from "./Login";
import {ServerRequests} from "../../Classes/ServerRequests";
import {StateManager} from "../../Classes/StateManager";
import {useNavigate} from "react-router-dom";
import {PagesEnum} from "../../index";

function LoginPage() {
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();

    return (
        <Space direction="vertical" className='form_container'>
            <Button onClick={() => {navigate(PagesEnum.Register)}}>Create New Account</Button>
            <h1>Authorization</h1>
            <Login
                buttonText='Log In'
                onClick={(login, password) => ServerRequests.login(login, password).then((id) => {
                if (id === null) {
                    setErrorMessage("cannot login")
                } else {
                    StateManager.userId = id
                    navigate(PagesEnum.ROOM_LIST)
                }
            })}
            />
            <p color="red">{errorMessage}</p>
        </Space>
    );
}

export {LoginPage};
