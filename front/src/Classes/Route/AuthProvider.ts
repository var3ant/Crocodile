import {createContext, PropsWithChildren, useContext} from 'react';
import {User} from "../Data/User";

const AuthContext = createContext<User | null>(null);

type AuthProviderProps = PropsWithChildren & {
    isSignedIn?: boolean;
};

export const useAuth = () => {
    const context = useContext(AuthContext);

    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }

    return context;
};