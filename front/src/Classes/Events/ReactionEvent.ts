import {ServerEvent} from "./ServerEvent";
import {ReactionType} from "./UserMessageEvent";

export class ReactionEvent implements ServerEvent {
    public messageId: string
    public reaction: ReactionType

    public constructor(messageId: string, reaction: ReactionType) {
        this.messageId = messageId;
        this.reaction = reaction;
    }
}