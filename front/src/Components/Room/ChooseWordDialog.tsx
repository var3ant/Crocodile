import * as React from 'react';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import DialogTitle from '@mui/material/DialogTitle';
import Dialog from '@mui/material/Dialog';
import {RoomModel} from "../../Classes/RoomModel";

export interface ChooseWordDialogProps {
    words: string[] | null;
    onClose: (index: number, value: string) => void;
    open: boolean
}

export class ChooseWordDialog extends React.Component<ChooseWordDialogProps, {}> {//TODONOW: тут надо подписаться на обновления wordsToChoose

    constructor(props: ChooseWordDialogProps) {
        super(props);
    }

    render() {
        let isOpen = this.props.words != null && this.props.words.length !== 0;
        if(isOpen) {
            return (
                <Dialog open={isOpen}>
                    <DialogTitle>Choose word</DialogTitle>
                    <List sx={{ pt: 0 }}>
                        {this.props.words?.map((word: string, index: number) => (
                            <ListItem disableGutters key={word}>
                                <ListItemButton onClick={() => this.props.onClose(index, word)}>
                                    <ListItemText primary={word} />
                                </ListItemButton>
                            </ListItem>
                        ))}
                    </List>
                </Dialog>
            );
        } else {//TODO: сократить это, чтобы без ифа и копипасты
            return (
                <Dialog open={isOpen}>
                    <DialogTitle>Choose word</DialogTitle>
                </Dialog>
            );
        }

    }
}

// export default function ChooseWordDialogDemo() {
//     const [open, setOpen] = React.useState(false);
//     const [selectedValue, setSelectedValue] = React.useState(words[1]);
//
//     const handleClickOpen = () => {
//         setOpen(true);
//     };
//
//     const handleClose = (value: string) => {
//         setOpen(false);
//         setSelectedValue(value);
//     };
//
//     return (
//         <div>
//             <Typography variant="subtitle1" component="div">
//                 Selected: {selectedValue}
//             </Typography>
//             <br />
//             <Button variant="outlined" onClick={handleClickOpen}>
//                 Open simple dialog
//             </Button>
//             <ChooseWordDialog
//                 selectedValue={selectedValue}
//                 open={open}
//                 onClose={handleClose}
//             />
//         </div>
//     );
// }
