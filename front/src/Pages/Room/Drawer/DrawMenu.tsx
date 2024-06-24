import React, {useState} from "react";
import ColorButton from "./ColorButton";
import {ColorPicker, Slider} from "antd";
import {PaintingSettings} from "./DrawCanvas";
import "../../Style/Drawer.css"

export type onChangePaintingSettingsFunc = (paintingSettings: PaintingSettings) => void;


export function DrawMenu(props: Readonly<{ onChangeSettings: onChangePaintingSettingsFunc, onClear: () => void }>) {
    const colorsRow1: string[] = ['black', 'white', 'red', 'green', 'blue', 'deepskyblue', 'yellow', 'brown']
    const colorsRow2: string[] = ['gray', 'darkslategray', 'cyan', 'darkcyan', 'darkgray', 'orange', 'magenta', 'pink']
    const [settings, setSettings] = useState<PaintingSettings>({size: 5, color: 'black'})

    const updateSettings = (newSettings: PaintingSettings) => {
        props.onChangeSettings(newSettings)
        setSettings(newSettings)
    }

    const CreateButtonsRow = (colors: string[]) => {
        return colors.map(
            (color: string) =>
                <ColorButton key={color}
                             onClick={(color: string) => {
                                 updateSettings({size: settings.size, color: color});
                             }}
                             color={color}
                />)
    }

    return (
        <div className="horizontal" style={{alignSelf: 'center'}}>
            <div className="vertical">
                <div className="horizontal">
                    {CreateButtonsRow(colorsRow1)}
                    <ColorPicker value={settings.color}
                                 onChange={(_, hex) => {
                                     updateSettings({size: settings.size, color: hex})
                                 }}
                                 size='middle' style={{margin: '3px', alignSelf: "center", justifySelf: 'center'}}
                    />
                </div>
                <div className="horizontal">
                    {CreateButtonsRow(colorsRow2)}
                    <img className='boxButton' src='/Assets/bin.png' onClick={props.onClear}/>
                </div>
            </div>
            <div style={{width: '200px'}}>
                <Slider min={1} max={50} value={settings.size}
                        onChange={(newSize) => {
                            updateSettings({size: newSize, color: settings.color});
                        }}
                />
            </div>
            {/*<input type="range" min={1} max={50} defaultValue={10} onChange={}></input>*/}
        </div>
    )
}