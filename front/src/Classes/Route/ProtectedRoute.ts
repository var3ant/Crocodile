import {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';

import {useAuth} from "../AuthProvider";
import {PagesEnum} from "../../PagesEnum";

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