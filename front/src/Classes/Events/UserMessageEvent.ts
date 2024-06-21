import {ServerEvent} from "./ServerEvent";
import {MessageData} from "../../Components/Room/Chat/ChatArea";

export enum ReactionType {
    LIKED = 'LIKED',
    DISLIKED = 'DISLIKED',
    NOT_SELECTED = 'NOT_SELECTED'
}

export function stringToReaction(reaction: string): ReactionType {
    // @ts-ignore
    return ReactionType[reaction];
}

export class UserMessageEvent implements ServerEvent {
    public userId: number
    public messageId: string
    public userName: string
    public text: string
    public reaction: ReactionType

    public constructor(userId: number, messageId: string, userName: string, text: string, reaction: ReactionType) {
        this.userId = userId;
        this.messageId = messageId;
        this.userName = userName;
        this.text = text;
        this.reaction = reaction;
    }

    public toMessageData(): MessageData {
        return new MessageData(this.userId, this.messageId, this.userName, this.text, this.reaction)
    }
}