import {Button, Modal} from "antd";
import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {createEvent, createStore} from "effector";
import {useUnit} from "effector-react";
import {PagesEnum} from "../PagesEnum";

export class GlobalError {
    public readonly message;
    public readonly redirectPath;
    public readonly redirectBeforeMessage;
    constructor(message: string, redirectPath: PagesEnum | null = null, redirectBeforeMessage: boolean | null = null) {
        this.message = message;
        this.redirectPath = redirectPath;
        this.redirectBeforeMessage = redirectBeforeMessage;
    }
}


export const globalErrorEvent = createEvent<GlobalError>();

const globalError = createStore<GlobalError>(new GlobalError("Unexpected error", PagesEnum.LOGIN));
export default function GlobalModalError() {
    const [isOpen, setOpen] = useState<boolean>(false)
    const navigate = useNavigate()

    const [errorEvent, _] = useUnit([globalError, globalErrorEvent]);
    globalError.on(globalErrorEvent, (oldState, newState) => {
        if(errorEvent.redirectBeforeMessage && errorEvent.redirectPath != null) {
            navigate(errorEvent.redirectPath);
        }
        setOpen(true)
        return newState;
    });


    const onOk = () => {
        setOpen(false)
        if(errorEvent.redirectBeforeMessage != true && errorEvent.redirectPath != null) {
            navigate(errorEvent.redirectPath);
        }
    }

    return (
        <Modal title={<h1 style={{textAlign: 'center'}}>Error</h1>}
               open={isOpen}
               onOk={onOk}
               closable={false}
               maskClosable={false}
               footer={[<Button key='ok' onClick={() => onOk()}>OK</Button>]}
               width='400px'>
            <div className='vertical' style={{textAlign: 'center', margin: '10px 0px'}}>
                <b style={{fontSize: '16px'}}>{errorEvent.message}</b>
            </div>
        </Modal>
    );
}