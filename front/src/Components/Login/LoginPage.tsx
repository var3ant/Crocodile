import React, {useState} from "react";
import {Button, Space} from "antd";
import {Login} from "./Login";
import {StateManager} from "../../Classes/StateManager";
import {useNavigate} from "react-router-dom";
import {Errors, errorToString} from "../../Classes/ErrorMessages";
import {PagesEnum} from "../../PagesEnum";
import {HttpAuthApi} from "../../Classes/Http/HttpAuthApi";

function LoginPage() {
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();

    const loginFunc = (id: number) => {
        StateManager.userId = id
        navigate(PagesEnum.ROOM_LIST)
    }

    const errorFunc = (e: any) => {
        let errorType = e?.response?.data;
        if(errorType == null) {
            setErrorMessage("Unknown error")
            return;
        }

        if (errorType === errorToString(Errors.INVALID_USER_AUTH_DATA_MESSAGE)) {
            setErrorMessage("Wrong login or password")
        } else {
            setErrorMessage("Unknown error")
        }
    }

    return (
        <Space direction="vertical" className='form_container'>
            <Button onClick={() => {
                navigate(PagesEnum.Register)
            }}>Create New Account</Button>
            <h1>Authorization</h1>
            <Login
                buttonText='Log In'
                onClick={(login, password) =>
                    HttpAuthApi.login(login, password)
                        .then(loginFunc)
                        .catch(errorFunc)
            }
            />
            <b style={{color:'red'}}>{errorMessage}</b>
        </Space>
    );
}

export {LoginPage};
