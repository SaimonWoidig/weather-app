import { Navigate } from "@solidjs/router";
import { useAuth } from "src/components/AuthProvider";

export default async function Logout() {
  const auth = useAuth();

  if (!auth.isLoggedInFn()) {
    return <Navigate href="/login" />;
  }

  await auth.logOutFn();

  return (
    <>
      <div>Logged out</div>
    </>
  );
}
