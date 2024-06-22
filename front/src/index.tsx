import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import {createBrowserRouter, RouterProvider} from 'react-router-dom';
import RoomPage from "./Pages/Room/RoomPage";
import {BrowserPage} from "./Pages/Browser/BrowserPage";
import {LoginPage} from "./Pages/Login/LoginPage";
import {RegisterPage} from "./Pages/Login/RegisterPage";
import {FriendsPage} from "./Pages/Friends/FriendsPage";
import GlobalModalError from "./Pages/ErrorModal/GlobalModalError";
import {PagesEnum} from "./Pages/PagesEnum";

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);


function createPage(element: JSX.Element): JSX.Element {
    return (<div>
        {element}
        <GlobalModalError/>
    </div>)
}

const router = createBrowserRouter([
    {
        path: "/",
        element: createPage(<RegisterPage/>)
    },
    {
        path: PagesEnum.Register,
        element: createPage(<RegisterPage/>)
    },
    {
        path: PagesEnum.LOGIN,
        element: createPage(<LoginPage/>)
    },
    {
        path: PagesEnum.ROOM + ":roomId",
        element: createPage(<RoomPage/>)
    },
    {
        path: PagesEnum.ROOM_LIST,
        element: createPage(<BrowserPage/>)
    }, {
        path: PagesEnum.FRIENDS,
        element: createPage(<FriendsPage/>)
    },
]);

root.render(
    <React.StrictMode>
        <RouterProvider router={router}/>
    </React.StrictMode>
);