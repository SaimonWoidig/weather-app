import { Title } from "@solidjs/meta";
import { Navigate } from "@solidjs/router";
import { useAuth } from "src/components/AuthProvider";
import ChangePasswordForm from "src/components/ChangePasswordForm";
import LocationsForm from "src/components/LocationsForm";
import NewAPITokenForm from "src/components/NewAPITokenForm";

export default function Settings() {
  const auth = useAuth();

  if (!auth.isLoggedInFn()) {
    return <Navigate href="/login" />;
  }

  return (
    <main>
      <Title>Settings</Title>
      <h1 class="text-4xl font-bold p-2">Settings</h1>
      <h2 class="text-2xl font-bold p-2">Saved locations</h2>
      <LocationsForm userId={auth.user()!.userId} authToken={auth.token()!} />
      <h2 class="text-2xl font-bold p-2">API token</h2>
      <NewAPITokenForm userId={auth.user()!.userId} authToken={auth.token()!} />
      <h2 class="text-2xl font-bold p-2">Change password</h2>
      <ChangePasswordForm
        userId={auth.user()!.userId}
        authToken={auth.token()!}
      />
    </main>
  );
}
