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
  date?: string;
};

const WeatherCard: Component<WeatherCardProps> = (props) => {
  const weatherIcon = () => weatherCodeToIcon(props.weatherType);
  return (
    <div class="card lg:card-side bg-neutral shadow-xl p-20 w-[48rem]">
      <figure>{weatherIcon()}</figure>
      <div class="card-body">
        {props.title && <h2 class="card-title">{props.title}</h2>}
        <ul>
          <li class="font-bold">Temperature: {props.temperature || 0} °C</li>
          <li class="font-bold">
            Precipitation: {props.precipitation || 0} mm
          </li>
          <li class="font-bold">Report:</li>
          {props.date && <li class="font-bold">Date: {props.date}</li>}
          <p>
            We can expect the weather to be{" "}
            <strong>{weatherCodeToString(props.weatherType)}</strong> with the
            temperature being <strong>{props.temperature} °C</strong> and the
            precipitation being <strong>{props.precipitation} mm</strong>.
          </p>
        </ul>
      </div>
    </div>
  );
};

export default WeatherCard;
