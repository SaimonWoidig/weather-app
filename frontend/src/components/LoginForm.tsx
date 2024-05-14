import { Show, createSignal, type Component } from "solid-js";
import { useAuth } from "./AuthProvider";
import { Navigate } from "@solidjs/router";

const LoginForm: Component = () => {
  const auth = useAuth();
  if (auth.isLoggedInFn()) {
    return <Navigate href="/" />;
  }

  const [username, setUsername] = createSignal("admin@example.com");
  const [password, setPassword] = createSignal("password");
  const [invalidLogin, setInvalidLogin] = createSignal(false);

  return (
    <div class="flex flex-col gap-4 items-center h-screen justify-center">
      <h1 class="text-4xl font-bold">Login</h1>
      <form
        class="flex flex-col gap-2 p-2 bg-neutral"
        method="post"
        onSubmit={async (e: SubmitEvent) => {
          e.preventDefault();
          // TODO: handle errors appropriately
          if (await auth.logInFn(username(), password())) setInvalidLogin(true);
          else setInvalidLogin(false);
        }}
      >
        <label for="input-username">Email:</label>
        <input
          type="email"
          id="input-username"
          required
          placeholder="Email address"
          value={username()}
          onInput={(e) => setUsername(e.currentTarget.value)}
        />
        <label for="input-password">Password:</label>
        <input
          type="password"
          id="input-password"
          required
          placeholder="Password"
          value={password()}
          onInput={(e) => setPassword(e.currentTarget.value)}
        />
        <button type="submit" class="btn btn-primary">
          Login
        </button>
        <Show when={invalidLogin()}>
          <p class="text-error">Invalid login</p>
        </Show>
      </form>
    </div>
  );
};

export default LoginForm;
