import {ServerEvent} from "./ServerEvent";

export class NewDrawerEvent implements ServerEvent {
    public userId: number;

    public constructor(userId: number) {
        this.userId = userId;
    }
}