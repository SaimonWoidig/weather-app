import type { Component } from "solid-js";

const NavBar: Component = () => {
  const user = {
    email: "user.xyz@gmail.com",
  };
  return (
    <div class="navbar bg-primary">
      <div class="flex-1">
        <a class="btn btn-ghost text-xl">Home</a>
      </div>
      <div class="flex-none gap-2">
        <p>Hello User X</p>
        <div class="dropdown dropdown-end">
          <div
            tabindex="0"
            role="button"
            class="btn btn-ghost btn-circle avatar"
          >
            <div class="w-16 rounded-full">
              <img
                alt="User Avatar"
                src={`https://identicon.rmhdev.net/${user.email}/circle.png`}
              />
            </div>
          </div>
          <ul
            tabindex="0"
            class="mt-3 z-[1] p-2 shadow menu menu-sm dropdown-content bg-neutral rounded-box w-52"
          >
            <li>
              <a>Logout</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default NavBar;
