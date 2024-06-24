import React from "react";
import {Space, Button, Input} from 'antd';


class SendMessageLine extends React.Component<{ canType: boolean, trySend: (text: string) => boolean }, {
    text: string
}> {
    constructor(props: { canType: boolean, trySend: (text: string) => boolean }) {
        super(props);
        this.state = {text: ""};
    }

    private onKeyUp = (e: any) => {
        if (e.key === 'Enter') {
            this.send();
        }
    }

    private send() {
        if (this.props.trySend(this.state.text)) {
            this.setState({text: ""})
        }
    }

    render() {
        const preventDefault = (e: any) => e.preventDefault();

        return (
            <Space.Compact style={{width: '100%'}} onKeyUp={this.onKeyUp}>
                <Input
                    disabled={!this.props.canType}
                    placeholder="guess word" value={this.state.text}
                    onChange={text => this.setState({text: text.target.value})}/>
                <Button type="primary" onKeyDown={preventDefault} onClick={() => this.send()}>Send</Button>
            </Space.Compact>
        );
    }
}

export default SendMessageLine;
