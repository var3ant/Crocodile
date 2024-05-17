import {ServerEvent} from "./ServerEvent";

export class InfoMessageEvent implements ServerEvent {
    public text: string;
    constructor(text: string) {
        this.text = text;
    }


}