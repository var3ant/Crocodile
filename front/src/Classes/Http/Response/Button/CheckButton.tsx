import {CheckOutlined} from "@ant-design/icons";
import {green} from "@ant-design/colors";
import {Button} from "antd";
import React from "react";

export default function CheckButton(props: Readonly<{ onClick: () => void }>) {
    return (<Button icon={<CheckOutlined style={{color: green[5]}}/>}
                    onClick={() => props.onClick()}/>)
}
