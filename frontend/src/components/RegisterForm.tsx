import { Show, createSignal, type Component } from "solid-js";
import { useAuth } from "./AuthProvider";
import { Navigate } from "@solidjs/router";

const RegisterForm: Component = () => {
  const auth = useAuth();
  if (auth.isLoggedInFn()) {
    return <Navigate href="/" />;
  }

  const [username, setUsername] = createSignal("");
  const [password, setPassword] = createSignal("");
  const [confirmPassword, setConfirmPassword] = createSignal("");
  const [errorMessage, setErrorMessage] = createSignal("");

  return (
    <div class="flex flex-col gap-4 items-center h-screen justify-center">
      <h1 class="text-4xl font-bold">Create account</h1>
      <form
        class="flex flex-col gap-2 p-2 bg-neutral"
        method="post"
        onSubmit={async (e: SubmitEvent) => {
          e.preventDefault();
          if (password() !== confirmPassword()) {
            setErrorMessage("Passwords do not match");
            return;
          }
          // TODO: replace with registration function and logic
          if (await auth.logInFn(username(), password()))
            setErrorMessage("User already exists");
          else setErrorMessage("");
        }}
      >
        <label for="input-username">Email:</label>
        <input
          class="input invalid:bg-error"
          type="email"
          id="input-username"
          required
          maxLength={48}
          placeholder="Email address"
          value={username()}
          onInput={(e) => setUsername(e.currentTarget.value)}
        />
        <label for="input-password">Password:</label>
        <input
          class="input invalid:bg-error"
          type="password"
          id="input-password"
          required
          minlength={8}
          placeholder="Password"
          onInput={(e) => setPassword(e.currentTarget.value)}
        />
        <label for="input-confirm-password">Confirm password:</label>
        <input
          class={"input bg-error" + (errorMessage() ? " invalid:bg-error" : "")}
          type="password"
          id="input-confirm-password"
          required
          minlength={8}
          placeholder="Password again"
          onInput={(e) => setConfirmPassword(e.currentTarget.value)}
        />
        <button type="submit" class="btn btn-primary">
          Register
        </button>
        <Show when={errorMessage()}>
          <p class="text-error">{errorMessage()}</p>
        </Show>
      </form>
    </div>
  );
};

export default RegisterForm;
