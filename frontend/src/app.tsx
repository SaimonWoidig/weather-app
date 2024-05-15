// @refresh reload
import { MetaProvider, Title } from "@solidjs/meta";
import { Router } from "@solidjs/router";
import { FileRoutes } from "@solidjs/start/router";
import { Suspense } from "solid-js";
import "./app.css";
import NavBar from "./components/NavBar";
import { AuthProvider } from "./components/AuthProvider";

export default function App() {
  return (
    <Router
      root={(props) => (
        <AuthProvider>
          <MetaProvider>
            <NavBar />
            <Title>App</Title>
            <Suspense>{props.children}</Suspense>
          </MetaProvider>
        </AuthProvider>
      )}
    >
      <FileRoutes />
    </Router>
  );
}
