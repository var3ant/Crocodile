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
        <Space direction="vertical" className='form_container'>
            <Button onClick={() => {navigate(PagesEnum.LOGIN)}}>Sing In</Button>
            <h1>Registration</h1>
            <Login
                buttonText='Create Account'
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
