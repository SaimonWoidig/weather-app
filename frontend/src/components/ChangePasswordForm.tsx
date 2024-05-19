import { Match, Switch, createSignal, type Component } from "solid-js";
import { changePassword } from "src/lib/user/password";
import { useAuth } from "./AuthProvider";

type ChangePasswordFormProps = {
  authToken: string;
};

const ChangePasswordForm: Component<ChangePasswordFormProps> = (props) => {
  const auth = useAuth();

  const [password, setPassword] = createSignal("");
  const [confirmPassword, setConfirmPassword] = createSignal("");
  const [newPasswordLoading, setNewPasswordLoading] = createSignal(false);
  const [newPasswordErrorMessage, setNewPasswordErrorMessage] =
    createSignal("");

  return (
    <div class="container p-2">
      <form
        class="flex flex-col gap-2 p-2 bg-neutral w-[50rem]"
        method="post"
        onSubmit={async (e: SubmitEvent) => {
          e.preventDefault();
          if (password() !== confirmPassword()) {
            setNewPasswordErrorMessage("Passwords do not match");
            return;
          }
          setNewPasswordLoading(true);
          const result = await changePassword(props.authToken, password());
          if (result) setNewPasswordErrorMessage(result.message || "Error");
          else {
            setNewPasswordErrorMessage("");
            auth.logOutFn();
          }
          setNewPasswordLoading(false);
        }}
      >
        <label class="input input-bordered flex items-center gap-2">
          New password
          <input
            type="password"
            placeholder="Password"
            minLength={8}
            required
            onInput={(e) => setPassword(e.currentTarget.value)}
          />
        </label>
        <label class="input input-bordered flex items-center gap-2">
          Confirm new password
          <input
            type="password"
            placeholder="Password again"
            minLength={8}
            required
            onInput={(e) => setConfirmPassword(e.currentTarget.value)}
          />
        </label>
        <button class="btn btn-primary">Change password</button>
        <Switch>
          <Match when={newPasswordErrorMessage()}>
            <p class="text-error">{newPasswordErrorMessage()}</p>
          </Match>
          <Match when={newPasswordLoading()}>
            <span class="loading loading-dots loading-lg"></span>
          </Match>
        </Switch>
      </form>
    </div>
  );
};

export default ChangePasswordForm;