import { Title } from "@solidjs/meta";
import { VsError } from "solid-icons/vs";
import { Match, Switch, createResource } from "solid-js";
import WeatherCard from "src/components/WeatherCard";
import { getFallBackLocation } from "src/lib/location/location";
import { getCurrentWeather } from "src/lib/weather/weather";

export default function Home() {
  const [location] = createResource(() => getFallBackLocation());
  const [weather] = createResource(location, (location) =>
    getCurrentWeather(location.latitude, location.longitude)
  );

  return (
    <main>
      <Title>Home</Title>
      <h1 class="text-4xl font-bold p-2">Current weather</h1>
      <div class="flex items-center">
        <Switch>
          <Match when={location.loading}>
            <span class="loading loading-dots loading-lg"></span>
          </Match>
          <Match when={location.state === "errored"}>
            <div class="alert alert-error shadow-lg w-max">
              <VsError />
              <span class="pl-2">Failed to load weather</span>
            </div>
          </Match>
          <Match when={weather()}>
            <WeatherCard
              precipitation={weather()!.precipitation}
              temperature={weather()!.temperature}
              weatherType={weather()!.weatherType}
            />
          </Match>
        </Switch>
      </div>
    </main>
  );
}
