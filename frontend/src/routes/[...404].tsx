import { A } from "@solidjs/router";
import { TbError404 } from "solid-icons/tb";

export default function NotFound() {
  return (
    <main>
      <div class="h-screen flex items-center justify-center">
        <div class="card bg-neutral shadow-xl">
          <figure>
            <TbError404 size={512} />
          </figure>
          <div class="card-body">
            <h1 class="text-xl font-bold">Page not found!</h1>
            <div class="card-actions justify-end">
              <A href="/" class="btn btn-primary">
                Go back
              </A>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
