import * as React from 'react';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import DialogTitle from '@mui/material/DialogTitle';
import Dialog from '@mui/material/Dialog';

export interface ChooseWordDialogProps {
    words: string[] | null;
    onClose: (index: number, value: string) => void;
}

export class ChooseWordDialog extends React.Component<ChooseWordDialogProps, {}> {//TODONOW: тут надо подписаться на обновления wordsToChoose
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