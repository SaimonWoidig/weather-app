import type { Component } from "solid-js";
import { weatherCodeToIcon } from "src/lib/weather/weather";
import type WeatherCode from "src/lib/weather/weather_code";

type WeatherCardProps = {
  title?: string;
  temperature: number;
  precipitation: number;
  weatherType: WeatherCode;
};

const WeatherCard: Component<WeatherCardProps> = (props) => {
  const { temperature, precipitation, weatherType } = props;
  const weatherIcon = weatherCodeToIcon(weatherType);
  return (
    <div class="card lg:card-side bg-neutral shadow-xl p-20 w-[48rem]">
      <figure>{weatherIcon}</figure>
      <div class="card-body">
        {props.title && <h2 class="card-title">{props.title}</h2>}
        <ul>
          <li>Temperature: {temperature} °C</li>
          <li>Precipitation: {precipitation} mm</li>
        </ul>
      </div>
    </div>
  );
};

export default WeatherCard;
