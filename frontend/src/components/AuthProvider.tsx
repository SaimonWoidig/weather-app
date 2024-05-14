import { useNavigate } from "@solidjs/router";
import {
  createContext,
  createSignal,
  useContext,
  type ParentComponent,
  type Accessor,
} from "solid-js";

type AuthContextT = {
  user: Accessor<string | null>;
  token: Accessor<string | null>;
  logInFn: (username: string, password: string) => Promise<void>;
  logOutFn: () => Promise<void>;
};

const AuthContext = createContext<AuthContextT>();

const AuthProvider: ParentComponent = ({ children }) => {
  const navigate = useNavigate();

  const [user, setUser] = createSignal<string | null>(null);
  const [token, setToken] = createSignal<string | null>(null);

  const logInFn = async (username: string, password: string) => {
    setUser(username);
    setToken("some token");
    navigate("/");
    return;
  };

  const logOutFn = async () => {
    setUser(null);
    setToken(null);
    navigate("/login");
    return;
  };
  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        logInFn,
        logOutFn,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

const useAuth = (): AuthContextT => {
  const providedAuthContext = useContext(AuthContext);
  if (!providedAuthContext) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return providedAuthContext;
};

export { AuthProvider, useAuth };
