import type { Component } from "solid-js";
import { weatherCodeToIcon } from "src/lib/weather/weather";
import type WeatherCode from "src/lib/weather/weather_code";

type WeatherCardProps = {
  temperature: number;
  precipitation: number;
  weatherType: WeatherCode;
};

const WeatherCard: Component<WeatherCardProps> = (props) => {
  const { temperature, precipitation, weatherType } = props;
  const weatherIcon = weatherCodeToIcon(weatherType);
  return (
    <div class="flex items-center justify-center">
      <div class="card lg:card-side bg-neutral shadow-xl p-20">
        <figure>{weatherIcon}</figure>
        <div class="card-body">
          <h2 class="card-title">Current weather status:</h2>
          <ul>
            <li>Temperature: {temperature} °C</li>
            <li>Precipitation: {precipitation} mm</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default WeatherCard;
