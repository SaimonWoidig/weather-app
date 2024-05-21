import type { Component } from "solid-js";
import type WeatherCode from "src/lib/weather/weather_code";
import {
  weatherCodeToIcon,
  weatherCodeToString,
} from "src/lib/weather/weather_code";

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
          <li class="font-bold">Temperature: {temperature} °C</li>
          <li class="font-bold">Precipitation: {precipitation} mm</li>
          <li class="font-bold">Report:</li>
          <p>
            We can expect the weather to be{" "}
            <strong>{weatherCodeToString(weatherType)}</strong> with the
            temperature being <strong>{temperature} °C</strong> and the
            precipitation being <strong>{precipitation} mm</strong>.
          </p>
        </ul>
      </div>
    </div>
  );
};

export default WeatherCard;
