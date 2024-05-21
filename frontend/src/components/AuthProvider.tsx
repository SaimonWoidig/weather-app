import { cookieStorage, makePersisted } from "@solid-primitives/storage";
import { useNavigate } from "@solidjs/router";
import {
  createContext,
  createSignal,
  useContext,
  type Accessor,
  type ParentComponent,
} from "solid-js";
import { login } from "src/lib/auth/auth";
import type { UserData } from "src/lib/types";

type AuthContextT = {
  user: Accessor<UserData | null>;
  token: Accessor<string | null>;
  logInFn: (email: string, password: string) => Promise<Error | null>;
  logOutFn: () => Promise<void>;
  isLoggedInFn: () => boolean;
};

const AuthContext = createContext<AuthContextT>();

const AuthProvider: ParentComponent = (props) => {
  const navigate = useNavigate();

  const [user, setUser] = makePersisted(createSignal<UserData | null>(null), {
    name: "userInfo",
    storage: cookieStorage,
  });
  const [token, setToken] = makePersisted(createSignal<string | null>(null), {
    name: "userToken",
    storage: cookieStorage,
  });

  const logInFn = async (email: string, password: string) => {
    try {
      const loginResponse = await login(email, password);
      setUser({ email: loginResponse.email, userId: loginResponse.userId });
      setToken(loginResponse.jwtToken);
      navigate("/");
      return null;
    } catch (err) {
      if (err instanceof Error) {
        return err;
      }
      return new Error("Unexpected error");
    }
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
