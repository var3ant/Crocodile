import {ServerEvent} from "./ServerEvent";

export class ReceiveImageEvent implements ServerEvent {
    public image: string;

    constructor(image: string) {
        this.image = image;
    }
}