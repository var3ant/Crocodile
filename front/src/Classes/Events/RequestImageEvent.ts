import {ServerEvent} from "./ServerEvent";

export class RequestImageEvent implements ServerEvent {
    public receiverId: number;

    constructor(receiverId: number) {
        this.receiverId = receiverId;
    }
}