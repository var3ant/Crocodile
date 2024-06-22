import {ServerEvent} from "./ServerEvent";

export class NewDrawerEvent implements ServerEvent {
    public readonly userId: number;
    public readonly userName: string;

    public constructor(userId: number, userName: string) {
        this.userId = userId;
        this.userName = userName;
    }
}