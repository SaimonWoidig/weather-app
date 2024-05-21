import { VsCheck, VsError } from "solid-icons/vs";
import { Match, Switch, createSignal, type Component } from "solid-js";
import { resetAPIToken } from "src/lib/user/password";

type NewAPITokenFormProps = {
  userId: string;
  authToken: string;
};

const NewAPITokenForm: Component<NewAPITokenFormProps> = (props) => {
  const [newAPIToken, setNewAPIToken] = createSignal("");
  const [newAPITokenLoading, setNewAPITokenLoading] = createSignal(false);
  const [newAPITokenErrorMessage, setNewAPITokenErrorMessage] =
    createSignal("");
  return (
    <div class="container p-2">
      <div class="flex flex-col gap-2 p-2 bg-neutral w-[50rem]">
        <button
          class="btn btn-primary"
          onClick={async () => {
            setNewAPITokenLoading(true);
            const result = await resetAPIToken(props.userId, props.authToken);
            if ("error" in result) {
              const errMessage = result.error?.message;
              setNewAPITokenLoading(false);
              setNewAPITokenErrorMessage(errMessage || "Unexpected error");
              return;
            }
            setNewAPITokenLoading(false);
            setNewAPIToken(result.token);
          }}
        >
          Set a new token
        </button>
        <Switch>
          <Match when={newAPITokenLoading()}>
            <span class="loading loading-dots loading-lg"></span>
          </Match>
          <Match when={newAPITokenErrorMessage()}>
            <div class="alert alert-error shadow-lg w-max">
              <VsError />
              <span>
                Error generating new API token:&nbsp;
                {newAPITokenErrorMessage() || "Unexpected error"}
              </span>
            </div>
          </Match>
          <Match when={newAPIToken()}>
            <div class="alert alert-success shadow-lg w-max">
              <VsCheck />
              <span>Token: </span>
              <code class="bg-neutral text-neutral-content rounded-md p-[0.15rem]">
                {newAPIToken()}
              </code>
              <span>UserID: </span>
              <code class="bg-neutral text-neutral-content rounded-md p-[0.15rem]">
                {props.userId}
              </code>
            </div>
          </Match>
        </Switch>
      </div>
    </div>
  );
};

export default NewAPITokenForm;
