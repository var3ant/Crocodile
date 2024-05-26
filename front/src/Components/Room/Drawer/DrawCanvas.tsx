import Card from "antd/es/card";
import * as React from "react";
import {Point} from "../../../Classes/Events/DrawEvent";

const width = 600
const height = 600

export class DrawCanvas extends React.Component<{
    canUserPaint: boolean,
    drawSubscriber: (startPoint: Point, finishPoint: Point) => void
}, {}> {
    private readonly _canvasRef: React.RefObject<any>;
    private _isPainting: boolean
    private _ctx: any
    private _startPoint: Point | null = null
    private readonly drawSubscriber: (startPoint: Point, finishPoint: Point) => void;

    constructor(props: any) {
        super(props);
        this._isPainting = false
        this._canvasRef = React.createRef();
        this.drawSubscriber = props.drawSubscriber;
        // console.log("ctr can paint: " + this.props.canUserPaint)
    }

    public clear() {
        this._ctx.strokeStyle = '#ffffff';
        this._ctx.clearRect(0, 0, this._canvasRef.current.width, this._canvasRef.current.height);
    }

    componentDidMount() {
        this._ctx = this._canvasRef.current.getContext('2d');
        this.clear()
    }

    render() {
        return (
            <Card>
                <canvas
                    ref={this._canvasRef}
                    width={width}
                    height={height}
                    onMouseDown={e => this.mouseDown(e)}
                    onMouseUp={e => this.mouseUp(e)}
                    onMouseMove={e => this.mouseMove(e)}
                    style={canvasStyle}
                />
            </Card>
        )
    }

    private mouseDown(e: React.MouseEvent<HTMLElement>) {
        this._isPainting = true;
        this.setPosition(this.calculatePosition(e));
    }

    private calculatePosition(e: React.MouseEvent<HTMLElement>): Point {
        let rect = this._canvasRef.current.getBoundingClientRect();
        return new Point(e.clientX - rect.left, e.clientY - rect.top);
    }

    private mouseUp(_: React.MouseEvent<HTMLElement>) {
        this._isPainting = false;
    }

    private mouseMove(e: React.MouseEvent<HTMLElement>) {
        // console.log("move can paint: " + this.props.canUserPaint)
        if (!this.props.canUserPaint) {
            return;
        }
        if (!this._isPainting) {
            return;
        }
        if (e.buttons !== 1) return;
        if (this._startPoint === null) {
            throw new Error();
        }

        let newPoint = this.calculatePosition(e);
        this.drawSubscriber(this._startPoint, newPoint);
        this.drawLine(this._startPoint, newPoint);
        this.setPosition(newPoint);
    }

    private setPosition(newPoint: Point) {
        this._startPoint = newPoint;
    }

    public drawLine(startPoint: Point, finishPoint: Point) {
        let ctx = this._ctx;

        ctx.beginPath(); // begin
        ctx.lineWidth = 5;
        ctx.lineCap = 'round';
        ctx.strokeStyle = '#c0392b';
        ctx.moveTo(startPoint.x, startPoint.y); // from
        ctx.lineTo(finishPoint.x, finishPoint.y); // to
        ctx.stroke(); // draw it!
    }
}

const canvasStyle = {
    // border: "2px solid black"
}