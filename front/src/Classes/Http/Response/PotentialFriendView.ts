export default class PotentialFriendView {
    public id: number;
    public name: string;
    public requestAlreadySent: boolean
    public alreadyFriend: boolean

    constructor(id: number, name: string, requestAlreadySent: boolean, alreadyFriend: boolean) {
        this.id = id;
        this.name = name;
        this.requestAlreadySent = requestAlreadySent;
        this.alreadyFriend = requestAlreadySent;
    }
}