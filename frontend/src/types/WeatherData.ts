import type {Weather} from "../enums/Weather.ts";

type WeatherData = {
    temperature: number,
    precipitation: number,
    weather: Weather
}

export type {WeatherData};