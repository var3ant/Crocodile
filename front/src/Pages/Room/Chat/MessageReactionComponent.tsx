import React from "react";
import {DislikeTwoTone, LikeTwoTone} from "@ant-design/icons";
import {gray, green, red} from "@ant-design/colors";
import {ReactionType} from "../../../Classes/Events/UserMessageEvent";
import {StateManager} from "../../../Classes/StateManager";

const greenColor = {primary: green[5], secondary: green[4]};
const redColor = {primary: red[5], secondary: red[4]};
const grayColor = {primary: gray[1], secondary: gray[0]};
const buttonStyle = {fontSize: '25px'};

interface TwoColors {
    primary: string,
    secondary: string
}

function getStyles(reaction: ReactionType): [TwoColors, TwoColors] {
    console.log(typeof reaction)

    switch (reaction) {
        case ReactionType.LIKED:
            return [greenColor, grayColor]
        case ReactionType.DISLIKED:
            return [grayColor, redColor]
        case ReactionType.NOT_SELECTED:
            return [grayColor, grayColor]
        default:
            throw Error("invalid ReactionType: " + reaction);
    }
}

export default function MessageReactionComponent(props: Readonly<{ messageId: string, reaction: ReactionType | null, isDrawer: boolean }>) {

    if (props.reaction == null) {
        return (<div></div>);
    }

    const onLikeClick = () => {
        if (!props.isDrawer) {
            return;
        }

        let newReaction = props.reaction !== ReactionType.LIKED ? ReactionType.LIKED : ReactionType.NOT_SELECTED;
        StateManager.getRoom()?.reactToMessage(props.messageId, newReaction);
    }

    const onDislikeClick = () => {
        if (!props.isDrawer) {
            return;
        }

        let newReaction = props.reaction !== ReactionType.DISLIKED ? ReactionType.DISLIKED : ReactionType.NOT_SELECTED;
        StateManager.getRoom()?.reactToMessage(props.messageId, newReaction);
    }

    let styles = getStyles(props.reaction);
    console.log(styles)
    const [likedColor, dislikedColor] = styles;
    return (<div className='horizontal'>
        <LikeTwoTone style={buttonStyle} twoToneColor={[likedColor.primary, likedColor.secondary]}
                     onClick={onLikeClick}/>
        <DislikeTwoTone style={buttonStyle} twoToneColor={[dislikedColor.primary, dislikedColor.secondary]}
                        onClick={onDislikeClick}/>
    </div>);
}