const {
    compareExchange, wait, notify
} = Atomics;

const LOCKED = 1;
const UNLOCKED = 0;

export default class Mutex {
    lock: Int32Array
    constructor() {
        this.lock = new Int32Array();
    }

    enter() {
        while (true) {
            if (compareExchange(this.lock, 0, UNLOCKED, LOCKED) === UNLOCKED) {
                return;
            }
            wait(this.lock, 0, LOCKED);
        }
    }

    leave() {
        if (compareExchange(this.lock, 0, LOCKED, UNLOCKED) !== LOCKED) {
            // Лучше выкинуть исключение, чтобы не прозевать такой момент
            return;
        }
        notify(this.lock, 0, 1);
    }
}