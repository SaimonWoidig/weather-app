import type {Weather} from "../enums/Weather.ts";

type TWeather = {
    temperature: number,
    precipitation: number,
    weather: Weather
}

export type {TWeather};