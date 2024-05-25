import {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';

import {PagesEnum} from "../../index";
import {useAuth} from "../AuthProvider";

type ProtectedRouteProps = { children: JSX.Element | undefined };

export function ProtectedRoute({children}: ProtectedRouteProps): JSX.Element | null {
    const user = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (user === null) {
            navigate(PagesEnum.LOGIN, {replace: true});
        }
    }, [navigate, user]);

    return children ?? null;
}