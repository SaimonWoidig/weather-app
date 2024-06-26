import { Navigate } from "@solidjs/router";
import { Show, createSignal, type Component } from "solid-js";
import { useAuth } from "./AuthProvider";

const RegisterForm: Component = () => {
  const auth = useAuth();
  if (auth.isLoggedInFn()) {
    return <Navigate href="/" />;
  }

  const [email, setEmail] = createSignal("");
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
          const authErr = await auth.registerFn(email(), password());
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
            id="input-username"
            required
            maxLength={48}
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
            id="input-password"
            required
            minlength={8}
            placeholder="Password"
            onInput={(e) => setPassword(e.currentTarget.value)}
          />
        </label>
        <label class="input input-bordered flex items-center gap-2">
          Confirm password
          <input
            class="grow"
            type="password"
            required
            minlength={8}
            placeholder="Password again"
            onInput={(e) => setConfirmPassword(e.currentTarget.value)}
          />
        </label>
        <button type="submit" class="btn btn-primary">
          Register
        </button>
        <Show when={errorMessage()}>
          <p class="text-error">Error: {errorMessage()}</p>
        </Show>
      </form>
    </div>
  );
};

export default RegisterForm;
