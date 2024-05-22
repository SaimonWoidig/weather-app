import { Navigate } from "@solidjs/router";
import { Show, createSignal, type Component } from "solid-js";
import { useAuth } from "./AuthProvider";

const LoginForm: Component = () => {
  const auth = useAuth();
  if (auth.isLoggedInFn()) {
    return <Navigate href="/" />;
  }

  const [email, setEmail] = createSignal("");
  const [password, setPassword] = createSignal("");
  const [errorMessage, setErrorMessage] = createSignal("");

  return (
    <div class="flex flex-col gap-4 items-center h-screen justify-center">
      <h1 class="text-4xl font-bold">Login</h1>
      <form
        class="flex flex-col gap-2 p-2 bg-neutral"
        method="post"
        onSubmit={async (e: SubmitEvent) => {
          e.preventDefault();
          const authErr = await auth.logInFn(email(), password());
          if (authErr) {
            setErrorMessage(authErr.message);
            return;
          }
          setErrorMessage("");
        }}
      >
        <label class="input input-bordered flex items-center gap-2">
          Email
          <input
            class="grow"
            type="email"
            required
            placeholder="john.doe@example.com"
            value={email()}
            onInput={(e) => setEmail(e.currentTarget.value)}
          />
        </label>
        <label class="input input-bordered flex items-center gap-2">
          Password
          <input
            class="grow"
            type="password"
            required
            placeholder="Password"
            value={password()}
            onInput={(e) => setPassword(e.currentTarget.value)}
          />
        </label>
        <button type="submit" class="btn btn-primary">
          Login
        </button>
        <Show when={errorMessage()}>
          <p class="text-error">Error: {errorMessage()}</p>
        </Show>
      </form>
    </div>
  );
};

export default LoginForm;
