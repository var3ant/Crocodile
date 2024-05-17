import {ServerEvent} from "./ServerEvent";

class Point {
    public x: number;
    public y: number;

    constructor(x: number, y:number) {
        this.x = x;
        this.y = y;
    }
}

class DrawEvent implements ServerEvent {
    public startPoint: Point;
    public finishPoint: Point;

    constructor(startPoint: Point, finishPoint: Point) {
        this.startPoint = startPoint;
        this.finishPoint = finishPoint;
    }
}

export {DrawEvent, Point};