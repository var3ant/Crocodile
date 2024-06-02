import Card from "antd/es/card";
import * as React from "react";
import {Point} from "../../../Classes/Events/DrawEvent";

const width = 600
const height = 600

export interface PaintingSettings {
    color: string,
    size: number
}

export class DrawCanvas extends React.Component<{
    canUserPaint: boolean,
    drawSubscriber: (startPoint: Point, finishPoint: Point, settings: PaintingSettings) => void,
    clearSubscriber: () => void,
    paintingSettings: PaintingSettings
}, {}> {
    private readonly _canvasRef: React.RefObject<any>;
    private _isPainting: boolean;
    private _ctx: any;
    private _startPoint: Point | null = null;

    constructor(props: any) {
        super(props);
        this._isPainting = false
        this._canvasRef = React.createRef();
        // console.log("ctr can paint: " + this.props.canUserPaint)
    }

    public clear() {
        this.internalClear();
        this.props.clearSubscriber();
    }

    private internalClear() {
        this._ctx.strokeStyle = '#ffffff';
        this._ctx.clearRect(0, 0, this._canvasRef.current.width, this._canvasRef.current.height);
    }

    public getImage(): string {
        return this._canvasRef.current.toDataURL("image/png");
    }

    public setImage(imageSource: string) {
        let image = new Image();
        image.onload = () => {
            this._ctx.drawImage(image, 0, 0)
        };
        image.src = imageSource;
        // image.src = 'https://www.tutorialspoint.com/images/logo.png';
        // this._ctx.drawImage(image, 0, 0)

    }

    public drawLine(startPoint: Point, finishPoint: Point, paintingSettings: PaintingSettings) {
        let ctx = this._ctx;

        ctx.beginPath(); // begin
        ctx.lineWidth = paintingSettings.size;
        ctx.lineCap = 'round';
        ctx.strokeStyle = paintingSettings.color;
        ctx.moveTo(startPoint.x, startPoint.y); // from
        ctx.lineTo(finishPoint.x, finishPoint.y); // to
        ctx.stroke(); // draw it!
    }

    componentDidMount() {
        this._ctx = this._canvasRef.current.getContext('2d');
        this.internalClear();
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
        this.props.drawSubscriber(this._startPoint, newPoint, this.props.paintingSettings);
        this.drawLine(this._startPoint, newPoint, this.props.paintingSettings);
        this.setPosition(newPoint);
    }

    private setPosition(newPoint: Point) {
        this._startPoint = newPoint;
    }
}

const canvasStyle = {
    // border: "2px solid black"
}
