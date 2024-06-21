import React, {useState} from "react";
import PotentialFriendView from "../../Classes/Http/Response/PotentialFriendView";
import {HttpFriendApi} from "../../Classes/Http/HttpFriendApi";
import {Card, Input, Modal} from "antd";
import CloseButton from "../../Classes/Http/Response/Button/CloseButton";
import CheckButton from "../../Classes/Http/Response/Button/CheckButton";
import {green} from "@ant-design/colors";
import {UserOutlined} from "@ant-design/icons";

const {Search} = Input;

export default function SearchUserModal(props: Readonly<{ isOpen: boolean, setOpen: (val: boolean) => void }>) {
    const [users, setUsers] = useState<PotentialFriendView[]>([])
    const [name, setName] = useState<string>("")
    const update = () => {
        HttpFriendApi.getUsersNameLike(name).then(rows => {
            setUsers(rows)
        });
    }

    return (
        <div className='vertical'>
            <Modal title={<h3 style={{textAlign: 'center'}}>Add friend</h3>}
                   open={props.isOpen}
                   onCancel={() => props.setOpen(false)}
                   footer={null}
            >
                <div style={{alignContent: 'center'}}>
                    <div style={{
                        display: 'flex',
                        flexDirection: 'column',
                        width: '460px',
                        margin: '5px'
                    }}>
                        <Search placeholder="input search text"
                                onSearch={update}
                                onChange={(e) => setName(e.target.value)}
                        />
                        <Card style={{minHeight: '200px'}}>
                            {users.map((user) => {
                                if (user.requestAlreadySent) {
                                    return (
                                        <div className='horizontal' key={user.id}>
                                            <div>{user.name}</div>
                                            <CloseButton onClick={() => HttpFriendApi.cancelRequest(user.id).then(update)}/>
                                        </div>);
                                } else if (user.alreadyFriend) {
                                    return (
                                        <div className='horizontal' key={user.id}>
                                            <div>{user.name}</div>
                                            <UserOutlined style={{color: green[5]}}/>
                                        </div>)
                                } else {
                                    return (
                                        <div className='horizontal' key={user.id}>
                                            <div>{user.name}</div>
                                            <CheckButton onClick={() => HttpFriendApi.sendRequest(user.id).then(update)}/>
                                        </div>);
                                }
                            })}
                        </Card>
                    </div>
                </div>
            </Modal>
        </div>
    );
}