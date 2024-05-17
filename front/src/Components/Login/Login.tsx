import React from "react";
import {Button, Input, Space} from "antd";

class Login extends React.Component<{ onClick: (text: string) => void }, { text: string }> {
    constructor(props: { onClick: (text: string) => boolean }) {
        super(props);
        this.state = {text: ""};
    }

    render() {
        return (
            <Space.Compact style={{width: '100%'}}>
                <Input placeholder="Enter name" value={this.state.text}
                       onChange={text => this.setState({text: text.target.value})}/>
                <Button type="primary" onClick={() => {
                    this.props.onClick(this.state.text)
                }}>Send</Button>
            </Space.Compact>
        );
    }
}

export {Login};
