import {ServerEvent} from "./ServerEvent";

export class NewDrawerEvent implements ServerEvent {
    public userId: string;

    public constructor(userId: string) {
        this.userId = userId;
    }
}