import { Title } from "@solidjs/meta";
import { Navigate } from "@solidjs/router";
import { useAuth } from "src/components/AuthProvider";

export default async function Logout() {
  const auth = useAuth();
  await auth.logOutFn();

  return (
    <>
      <Title>Logout</Title>
      <div>Logged out</div>
    </>
  );
}
