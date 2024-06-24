import {CloseOutlined} from "@ant-design/icons";
import {red} from "@ant-design/colors";
import {Button} from "antd";
import React from "react";

export default function CloseButton(props: Readonly<{ onClick: () => void }>) {
    return (<Button icon={<CloseOutlined style={{color: red[5]}}/>}
                    onClick={props.onClick}/>)
}
