import React from 'react';
import './App.css';

class App extends React.Component<{}, { isGameFound: boolean }> {
    constructor(props: any) {
        super(props);
        this.state = {isGameFound: false};
    }


    render() {
        if (this.state.isGameFound) {
            return (
                <div className="App">
                    <header className="App-header">
                        {/*<RoomPage/>*/}
                    </header>
                </div>
            );
        } else {
            return (
                <div className="App">
                    <header className="App-header">
                        {/*<LoginPage onLogin={(userId) => {}*/}
                        {/*    // ServerRequests.createRoom("roomName").then(roomId => {*/}
                        {/*    //     StateManager.room = new RoomModel(userId, roomId);*/}
                        {/*    //     this.setState({isGameFound: true})*/}
                        {/*    // })*/}
                        {/*}></LoginPage>*/}
                    </header>
                </div>
            );
        }
    }
}

export default App;
