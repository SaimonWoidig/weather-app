import { VsError } from "solid-icons/vs";
import {
  ErrorBoundary,
  Match,
  Switch,
  createSignal,
  onMount,
  type Component,
} from "solid-js";
import { getFallbackCoordinate } from "src/lib/location/location";
import { getCurrentWeather } from "src/lib/weather/weather";
import WeatherCode from "src/lib/weather/weather_code";
import WeatherCard from "./WeatherCard";

const CurrentWeatherForm: Component = () => {
  const [latitude, setLatitude] = createSignal(0);
  const [longitude, setLongitude] = createSignal(0);

  const [temperature, setTemperature] = createSignal(0);
  const [precipitation, setPrecipitation] = createSignal(0);
  const [weatherType, setWeatherType] = createSignal<WeatherCode>(
    WeatherCode.Unknown
  );

  const [weatherLoading, setWeatherLoading] = createSignal(true);

  onMount(async () => {
    const fallbackCoordinate = await getFallbackCoordinate();
    setLatitude(fallbackCoordinate.latitude);
    setLongitude(fallbackCoordinate.longitude);

    const weather = await getCurrentWeather(
      fallbackCoordinate.latitude,
      fallbackCoordinate.longitude
    );
    setTemperature(weather.temperature);
    setPrecipitation(weather.precipitation);
    setWeatherType(weather.weatherType);

    setWeatherLoading(false);
  });

  return (
    <div class="flex flex-col gap-2">
      <form
        class="flex flex-col gap-2 p-2 bg-neutral w-[50rem]"
        method="post"
        onSubmit={async (e) => {
          e.preventDefault();

          setWeatherLoading(true);

          const weather = await getCurrentWeather(latitude()!, longitude()!);
          setTemperature(weather.temperature);
          setPrecipitation(weather.precipitation);
          setWeatherType(weather.weatherType);

          setWeatherLoading(false);
        }}
      >
        <label class="input input-bordered flex items-center gap-2">
          Latitude:
          <input
            class="grow"
            type="number"
            min={-90}
            max={90}
            step={0.0001}
            placeholder="Location latitude"
            value={latitude()}
            required
            onInput={(e) => setLatitude(Number(e.currentTarget.value))}
          />
        </label>
        <label class="input input-bordered flex items-center gap-2">
          Longitude:
          <input
            class="grow"
            type="number"
            min={-180}
            max={180}
            step={0.0001}
            placeholder="Location longitude"
            value={longitude()}
            required
            onInput={(e) => setLongitude(Number(e.currentTarget.value))}
          />
        </label>
        <button class="btn btn-primary" type="submit">
          Show weather
        </button>
      </form>
      <Switch fallback={<span class="loading loading-dots loading-lg"></span>}>
        <Match when={!weatherLoading()}>
          <ErrorBoundary
            fallback={(error) => (
              <div class="alert alert-error shadow-lg w-max">
                <VsError />
                <span class="pl-2">
                  Failed to load weather: {error?.message || "Unexpected error"}
                </span>
              </div>
            )}
          >
            <WeatherCard
              temperature={temperature()}
              precipitation={precipitation()}
              weatherType={weatherType()}
            />
          </ErrorBoundary>
        </Match>
      </Switch>
    </div>
  );
};

export default CurrentWeatherForm;
