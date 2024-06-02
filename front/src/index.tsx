import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import {createBrowserRouter, RouterProvider} from 'react-router-dom';
import RoomPage from "./Components/Room/RoomPage";
import {BrowserPage} from "./Components/Browser/BrowserPage";
import {LoginPage} from "./Components/Login/LoginPage";
import {RegisterPage} from "./Components/Login/RegisterPage";

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);

export const PagesEnum = {
    Register: '/register',
    LOGIN: '/login',
    ROOM: '/room/',
    ROOM_LIST: '/room_list'
};

// const router = createBrowserRouter([
//     {
//         path: "/",
//         element: <RegisterPage/>
//     },
//     {
//         path: PagesEnum.Register,
//         element: <RegisterPage/>
//     },
//     {
//         path: PagesEnum.LOGIN,
//         element: (
//             <ProtectedRoute>
//                 <LoginPage/>
//             </ProtectedRoute>)
//     },
//     {
//         path: PagesEnum.ROOM + ":roomId",
//         element: (
//             <ProtectedRoute>
//                 <RoomPage/>
//             </ProtectedRoute>)
//     },
//     {
//         path: PagesEnum.ROOM_LIST,
//         element: (
//             <ProtectedRoute>
//                 <BrowserPage/>
//             </ProtectedRoute>)
//     },
// ]);

const router = createBrowserRouter([
    {
        path: "/",
        element: <RegisterPage/>
    },
    {
        path: PagesEnum.Register,
        element: <RegisterPage/>
    },
    {
        path: PagesEnum.LOGIN,
        element: <LoginPage/>
    },
    {
        path: PagesEnum.ROOM + ":roomId",
        element: <RoomPage/>
    },
    {
        path: PagesEnum.ROOM_LIST,
        element: <BrowserPage/>
    },
]);

root.render(
    <React.StrictMode>
        <RouterProvider router={router}/>
        {/*<App />*/}
    </React.StrictMode>
    // <Canvas></Canvas>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
