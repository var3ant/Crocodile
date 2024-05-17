import React, {useState} from "react";
import {Space} from "antd";
import {Login} from "./Login";
import {ServerRequests} from "../../Classes/ServerRequests";
import {StateManager} from "../../Classes/StateManager";
import {useNavigate} from "react-router-dom";
import {PagesEnum} from "../../index";

function LoginPage() {
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();

    return (
        <Space direction="vertical">
            <Login onClick={(text) => ServerRequests.login(text).then((id) => {
                if (id === null) {
                    setErrorMessage("cannot login")
                } else {
                    // this.props.onLogin(id);
                    StateManager.userId = id
                    navigate(PagesEnum.ROOM_LIST)
                }
            })}/>
            <p color="red">{errorMessage}</p>
        </Space>
    );
}

export {LoginPage};
