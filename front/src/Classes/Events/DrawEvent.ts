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
    public color: string;
    public size: number;

    constructor(startPoint: Point, finishPoint: Point, color: string, size: number) {
        this.startPoint = startPoint;
        this.finishPoint = finishPoint;
        this.color = color;
        this.size = size;
    }
}

export {DrawEvent, Point};