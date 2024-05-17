import {ServerEvent} from "./ServerEvent";

export class UserMessageEvent implements ServerEvent {
    public userId: string
    public text: string

    public constructor(userId: string, text: string) {
        this.userId = userId;
        this.text = text;
    }
}