import {ServerEvent} from "./ServerEvent";

export class ChooseWordEvent implements ServerEvent {
    public words: string[];

    constructor(words: string[]) {
        this.words = words;
    }
}