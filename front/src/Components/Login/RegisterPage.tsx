import React, {useState} from "react";
import {Button, Space} from "antd";
import {Login} from "./Login";
import {ServerRequests} from "../../Classes/ServerRequests";
import {useNavigate} from "react-router-dom";
import {PagesEnum} from "../../index";

function RegisterPage() {
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();

    return (
        <Space direction="vertical">
            <Button onClick={() => {navigate(PagesEnum.LOGIN)}}>Login</Button>
            <Login
                onClick={(login, password) => ServerRequests.register(login, password).then((id) => {
                if (id === null) {
                    setErrorMessage("cannot register")
                } else {
                    // StateManager.userId = id
                    navigate(PagesEnum.LOGIN)
                }
            })}
            />
            <p color="red">{errorMessage}</p>
        </Space>
    );
}

export {RegisterPage};
