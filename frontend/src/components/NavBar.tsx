import { Show, type Component } from "solid-js";
import { useAuth } from "./AuthProvider";
import { A } from "@solidjs/router";

const NavBar: Component = () => {
  const auth = useAuth();
  const user = () => auth.user();

  return (
    <div class="navbar bg-primary">
      <div class="flex-1">
        <a class="btn btn-ghost text-xl" href="/">
          Home
        </a>
        <Show when={!auth.isLoggedInFn()}>
          <A class="btn btn-ghost text-xl" href="/login">
            Login
          </A>
          <A class="btn btn-ghost text-xl" href="/register">
            Register
          </A>
        </Show>
      </div>
      <Show when={auth.isLoggedInFn()}>
        <div class="flex-none gap-2">
          <p>{user()}</p>
          <div class="dropdown dropdown-end">
            <div
              tabindex="0"
              role="button"
              class="btn btn-ghost btn-circle avatar"
            >
              <div class="w-16 rounded-full">
                <img
                  alt="User Avatar"
                  src={`https://identicon.rmhdev.net/${user()}/circle.png`}
                />
              </div>
            </div>
            <ul
              tabindex="0"
              class="mt-3 z-[1] p-2 shadow menu menu-sm dropdown-content bg-neutral rounded-box w-52"
            >
              <li>
                <A class="btn btn-ghost text-xl" href="/userarea">
                  User Area
                </A>
              </li>
              <li>
                <A class="btn btn-ghost text-xl" href="/settings">
                  Settings
                </A>
              </li>
              <li>
                <A class="btn btn-ghost text-xl" href="/logout">
                  Logout
                </A>
              </li>
            </ul>
          </div>
        </div>
      </Show>
    </div>
  );
};

export default NavBar;
