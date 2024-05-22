import {
  ErrorBoundary,
  Suspense,
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
  });

  return (
    <div class="flex flex-col gap-2">
      <form
        class="flex flex-col gap-2 p-2 bg-neutral w-[50rem]"
        method="post"
        onSubmit={async (e) => {
          e.preventDefault();
          const weather = await getCurrentWeather(latitude()!, longitude()!);
          setTemperature(weather.temperature);
          setPrecipitation(weather.precipitation);
          setWeatherType(weather.weatherType);
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
            value={latitude()}
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
            value={longitude()}
            onInput={(e) => setLongitude(Number(e.currentTarget.value))}
          />
        </label>
        <button class="btn btn-primary" type="submit">
          Show weather
        </button>
      </form>
      <ErrorBoundary fallback={(error) => <div>{error.message}</div>}>
        <Suspense
          fallback={<span class="loading loading-dots loading-lg"></span>}
        >
          <WeatherCard
            temperature={temperature()}
            precipitation={precipitation()}
            weatherType={weatherType()}
          />
        </Suspense>
      </ErrorBoundary>
    </div>
  );
};

export default CurrentWeatherForm;
