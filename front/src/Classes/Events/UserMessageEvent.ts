import {ServerEvent} from "./ServerEvent";

export class UserMessageEvent implements ServerEvent {
    public userId: number
    public text: string

    public constructor(userId: number, text: string) {
        this.userId = userId;
        this.text = text;
    }
}