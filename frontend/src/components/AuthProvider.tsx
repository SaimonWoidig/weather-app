import { cookieStorage, makePersisted } from "@solid-primitives/storage";
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
  logInFn: (username: string, password: string) => Promise<Error | null>;
  logOutFn: () => Promise<void>;
  isLoggedInFn: () => boolean;
};

const AuthContext = createContext<AuthContextT>();

const AuthProvider: ParentComponent = (props) => {
  const navigate = useNavigate();

  const [user, setUser] = makePersisted(createSignal<string | null>(null), {
    name: "userInfo",
    storage: cookieStorage,
  });
  const [token, setToken] = makePersisted(createSignal<string | null>(null), {
    name: "userToken",
    storage: cookieStorage,
  });

  const logInFn = async (username: string, password: string) => {
    if (username !== "admin@example.com" || password !== "password") {
      return new Error("Invalid login");
    }
    setUser(username);
    setToken("some token");
    navigate("/");
    return null;
  };

  const logOutFn = async () => {
    setUser(null);
    setToken(null);
    navigate("/login");
    return;
  };

  const isLoggedInFn = () => !!user() && !!token();

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        logInFn,
        logOutFn,
        isLoggedInFn,
      }}
    >
      {props.children}
    </AuthContext.Provider>
  );
};

const useAuth = (): AuthContextT => {
  const providedAuthContext = useContext(AuthContext);
  if (providedAuthContext === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return providedAuthContext;
};

export { AuthProvider, useAuth };
