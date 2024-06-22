import "../../Style/Drawer.css"

export type onColorClickFunc = (color: string) => void;

export default function ColorButton(props: Readonly<{ onClick: onColorClickFunc, color: string }>) {

    const boxStyle = {
        backgroundColor: props.color
    }

    return (
        <div className='boxButton' style={boxStyle} onClick={() => props.onClick(props.color)}>
        </div>
    )
}