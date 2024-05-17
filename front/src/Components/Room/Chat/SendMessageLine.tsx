import React from "react";
import {Space, Button, Input} from 'antd';


class SendMessageLine extends React.Component<{ canType: boolean, onClick: (text: string) => boolean }, { text: string }> {
    constructor(props: { canType: boolean, onClick: (text: string) => boolean }) {
        super(props);
        this.state = {text: ""};
    }

    render() {
        return (
            <Space.Compact style={{width: '100%'}}>
                <Input
                    disabled={!this.props.canType}
                    placeholder="guess word" value={this.state.text}
                    onChange={text => this.setState({text: text.target.value})}/>
                <Button type="primary" onClick={() => {
                    let isSuccess = this.props.onClick(this.state.text)
                    if (isSuccess) {
                        this.setState({text: ""})
                    }
                }}>Send</Button>
            </Space.Compact>
        );
    }
}

export default SendMessageLine;
