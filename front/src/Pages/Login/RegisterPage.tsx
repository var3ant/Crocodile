import React, {useState} from "react";
import {Button, Space} from "antd";
import {Login} from "./Login";
import {HttpAuthApi} from "../../Classes/Http/Api/HttpAuthApi";
import {useNavigate} from "react-router-dom";
import {Errors, errorToString} from "../../Classes/ErrorMessages";
import {PagesEnum} from "../PagesEnum";

function RegisterPage() {
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();

    const registerFunc = (id: number) => {
        // StateManager.userId = id
        navigate(PagesEnum.LOGIN)
    }

    const errorFunc = (e: any) => {
        let errorType: string = e?.response?.data;
        if (errorType == null) {
            setErrorMessage("Unknown error")
            return;
        }

        if (errorType === errorToString(Errors.ILLEGAL_NAME_MESSAGE)) {
            setErrorMessage("This login is prohibited")
        } else if (errorType === errorToString(Errors.ALREADY_EXIST_MESSAGE)) {
            setErrorMessage("This login is already taken")
        } else {
            setErrorMessage("Unknown error")
        }
    }

    return (
        <Space direction="vertical" className='form_container'>
            <Button onClick={() => {
                navigate(PagesEnum.LOGIN)
            }}>Sing In</Button>
            <h1>Registration</h1>
            <Login
                buttonText='Create Account'
                onClick={(login, password) =>
                    HttpAuthApi.register(login, password)
                        .then(registerFunc)
                        .catch(errorFunc)
                }
            />
            <b style={{color:'red'}}>{errorMessage}</b>
        </Space>
    );
}

export {RegisterPage};
