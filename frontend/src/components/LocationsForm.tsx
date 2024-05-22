import { Navigate } from "@solidjs/router";
import { VsError, VsInfo } from "solid-icons/vs";
import {
  For,
  Match,
  Show,
  Switch,
  createResource,
  createSignal,
  type Component,
} from "solid-js";
import {
  createUserLocation,
  deleteUserLocation,
  getUserLocations,
} from "src/lib/location/location";
import { useAuth } from "./AuthProvider";

type LocationsFormProps = {
  userId: string;
  authToken: string;
};

const LocationsForm: Component<LocationsFormProps> = (props) => {
  const auth = useAuth();

  if (!auth.isLoggedInFn()) {
    return <Navigate href="/login" />;
  }

  const [userLocations, { refetch }] = createResource(() =>
    getUserLocations(auth.user()?.userId!, auth.token()!)
  );

  const [locationName, setLocationName] = createSignal("");
  const [latitude, setLatitude] = createSignal(0);
  const [longitude, setLongitude] = createSignal(0);

  return (
    <div class="flex flex-col p-2 gap-10">
      <form
        class="flex flex-col gap-2 p-2 bg-neutral w-[50rem]"
        method="post"
        onSubmit={async (e: SubmitEvent) => {
          e.preventDefault();
          await createUserLocation(
            props.userId,
            props.authToken,
            locationName(),
            latitude(),
            longitude()
          );
          refetch();
        }}
      >
        <label class="input input-bordered flex items-center gap-2">
          Name:
          <input
            class="w-full"
            type="text"
            name="name"
            placeholder="Location name"
            onChange={(e) => setLocationName(e.currentTarget.value)}
          />
        </label>
        <label class="input input-bordered flex items-center gap-2">
          Latitude:
          <input
            class="w-full"
            type="number"
            name="latitude"
            min={-90}
            max={90}
            step={0.0001}
            placeholder="Location latitude"
            required
            onChange={(e) => setLatitude(Number(e.currentTarget.value))}
          />
        </label>
        <label class="input input-bordered flex items-center gap-2">
          Longitude:
          <input
            class="w-full"
            type="number"
            name="longitude"
            min={-180}
            max={180}
            step={0.0001}
            placeholder="Location longitude"
            required
            onChange={(e) => setLongitude(Number(e.currentTarget.value))}
          />
        </label>
        <button class="btn btn-primary" type="submit">
          Create
        </button>
      </form>
      <Show when={userLocations.loading}>
        <span class="loading loading-dots loading-lg"></span>
      </Show>
      <Show when={userLocations.state === "errored"}>
        <div class="alert alert-error shadow-lg w-max">
          <VsError />
          <span>
            Error loading your locations:&nbsp;
            {userLocations.error?.message || "Unexpected error"}
          </span>
        </div>
      </Show>
      <Show when={userLocations.state === "ready"}>
        <Switch>
          <Match
            when={
              userLocations() === undefined || userLocations()!.length === 0
            }
          >
            <div role="alert" class="alert alert-info w-max">
              <VsInfo />
              <span>No locations saved yet.</span>
            </div>
          </Match>
          <Match when={userLocations()}>
            <div class="collapse bg-primary w-[50rem]">
              <input type="checkbox" />
              <div class="collapse-title text-xl p-2">
                Show/Hide saved locations
              </div>
              <div class="collapse-content bg-neutral">
                <For each={userLocations()}>
                  {(location) => (
                    <div class="flex flex-col gap-2 p-2">
                      <span>Name: {location.name}</span>
                      <span>Latitude: {location.latitude}</span>
                      <span>Longitude: {location.longitude}</span>
                      <button
                        class="btn btn-error"
                        onClick={async () => {
                          await deleteUserLocation(
                            props.userId,
                            location.id,
                            props.authToken
                          );
                          refetch();
                        }}
                      >
                        Delete
                      </button>
                    </div>
                  )}
                </For>
              </div>
            </div>
          </Match>
        </Switch>
      </Show>
    </div>
  );
};

export default LocationsForm;
