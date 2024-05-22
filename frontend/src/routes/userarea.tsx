import { Title } from "@solidjs/meta";
import { Navigate } from "@solidjs/router";
import { VsError, VsInfo } from "solid-icons/vs";
import {
  For,
  Match,
  Show,
  Suspense,
  Switch,
  createResource,
  createSignal,
} from "solid-js";
import { useAuth } from "src/components/AuthProvider";
import WeatherCard from "src/components/WeatherCard";
import { getUserLocations, type Location } from "src/lib/location/location";
import {
  getForecastNext7Days,
  getForecastPrev7Days,
} from "src/lib/weather/weather";

export default function UserArea() {
  const auth = useAuth();

  if (!auth.isLoggedInFn()) {
    return <Navigate href="/login" />;
  }

  const [userLocations] = createResource(() =>
    getUserLocations(auth.user()?.userId!, auth.token()!)
  );

  const [location, setLocation] = createSignal<Location | undefined>();

  const [forecastNext7Days] = createResource(location, (loc) =>
    getForecastNext7Days(auth.token()!, loc.latitude, loc.longitude)
  );

  const [forecastPrev7Days] = createResource(location, (loc) =>
    getForecastPrev7Days(auth.token()!, loc.latitude, loc.longitude)
  );

  return (
    <main>
      <Title>User Area</Title>
      <h1 class="text-4xl font-bold p-2">Your personal weather data</h1>
      <div class="container">
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
                <span>
                  No locations saved. Save a location in Settings to see your
                  weather data.
                </span>
              </div>
            </Match>
            <Match when={userLocations()}>
              <select
                class="select select-bordered w-1/4 p-2"
                onChange={(e) => {
                  const selectedLocationId =
                    e.currentTarget.options[e.currentTarget.selectedIndex].id;
                  if (!selectedLocationId) return;
                  const selectedLocations = userLocations()?.filter(
                    (loc) => loc.id === selectedLocationId
                  );
                  if (selectedLocations && selectedLocations.length > 0)
                    setLocation(selectedLocations[0]);
                }}
              >
                <option disabled selected>
                  Select location
                </option>
                <For each={userLocations() || []}>
                  {(location) => (
                    <option id={location.id}>{location.name}</option>
                  )}
                </For>
              </select>
              <div class="container">
                <h2 class="text-2xl font-bold p-2">
                  Weather forecast for 7 days
                </h2>
                <Suspense
                  fallback={
                    <span class="loading loading-dots loading-lg"></span>
                  }
                >
                  <Show
                    when={forecastNext7Days()}
                    fallback={
                      <div role="alert" class="alert alert-info w-max">
                        <VsInfo />
                        <span>Select a location first</span>
                      </div>
                    }
                  >
                    <div class="flex flex-col">
                      <p>Shift+Scroll to view days</p>
                      <div class="carousel w-[48rem]">
                        <For each={forecastNext7Days()?.weather}>
                          {(day, idx) => {
                            return (
                              <div class="carousel-item">
                                <WeatherCard
                                  title={`Day +${idx() + 1}`}
                                  precipitation={day.precipitation}
                                  temperature={day.temperature}
                                  weatherType={day.weatherType}
                                  date={forecastNext7Days()?.days[idx()]}
                                />
                              </div>
                            );
                          }}
                        </For>
                      </div>
                    </div>
                  </Show>
                </Suspense>
                <h2 class="text-2xl font-bold p-2">
                  7 day historical weather data
                </h2>
                <Suspense
                  fallback={
                    <span class="loading loading-dots loading-lg"></span>
                  }
                >
                  <Show
                    when={forecastPrev7Days()}
                    fallback={
                      <div role="alert" class="alert alert-info w-max">
                        <VsInfo />
                        <span>Select a location first</span>
                      </div>
                    }
                  >
                    <div class="flex flex-col">
                      <p>Shift+Scroll to view days</p>
                      <div class="carousel w-[48rem]">
                        <For each={forecastPrev7Days()?.weather}>
                          {(day, idx) => {
                            return (
                              <div class="carousel-item">
                                <WeatherCard
                                  title={`Day -${idx() + 1}`}
                                  precipitation={day.precipitation}
                                  temperature={day.temperature}
                                  weatherType={day.weatherType}
                                  date={forecastPrev7Days()?.days[idx()]}
                                />
                              </div>
                            );
                          }}
                        </For>
                      </div>
                    </div>
                  </Show>
                </Suspense>
              </div>
            </Match>
          </Switch>
        </Show>
      </div>
    </main>
  );
}
