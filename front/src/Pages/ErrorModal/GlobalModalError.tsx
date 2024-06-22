import {Button, Modal} from "antd";
import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {createEvent, createStore} from "effector";
import {useUnit} from "effector-react";
import {PagesEnum} from "../PagesEnum";

export const globalErrorEvent = createEvent<GlobalError>();

const globalError = createStore<GlobalError>({message: "Unexpected error", redirectPath: PagesEnum.LOGIN});

export interface GlobalError {
    message: string,
    redirectPath: PagesEnum | null
}

export default function GlobalModalError() {
    const [isOpen, setOpen] = useState<boolean>(false)
    const navigate = useNavigate()

    const [errorEvent, _] = useUnit([globalError, globalErrorEvent]);
    globalError.on(globalErrorEvent, (oldState, newState) => {
        setOpen(true)
        return newState;
    });


    const onOk = () => {
        setOpen(false)
        if(errorEvent.redirectPath != null) {
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