import {createContext, PropsWithChildren, useContext} from 'react';
import {User} from "./Data/User";

const AuthContext = createContext<User | null>(null);

type AuthProviderProps = PropsWithChildren & {
    isSignedIn?: boolean;
};

// export default function AuthProvider({
//                                          children,
//                                          isSignedIn,
//                                      }: AuthProviderProps) {
//
//     const [user] = useState<User | null>(isSignedIn ? {id: 1, token: "a"} : null);
//
//     return (<AuthContext.Provider value={user}>{children}</AuthContext.Provider>);
// }

export const useAuth = () => {
    const context = useContext(AuthContext);

    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }

    return context;
};