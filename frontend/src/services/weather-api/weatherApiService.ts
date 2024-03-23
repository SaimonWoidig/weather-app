import {fetchAs} from "../../utils/FetchUtils.ts";
import type {Weather} from "../../enums/Weather.ts";

const apiUrl: string = import.meta.env.API_URL;

interface CurrentWeatherApiResponse {
    temperature: number;
    precipitation: number;
    weatherCode: number;
}

export interface CurrentWeather {
    temperature: number;
    precipitation: number;
    weather: Weather;
}

export async function getCurrentWeather(latitude: number, longitude: number): Promise<CurrentWeather> {
    if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
        throw new Error('Latitude and longitude must be between -90 and 90 and -180 and 180 respectively');
    }

    const url: string = `${apiUrl}/weather/current?latitude=${latitude}&longitude=${longitude}`;
    const response = await fetchAs<CurrentWeatherApiResponse>(url);

    if (!response) {
        throw new Error('Failed to fetch weather data');
    }

    return {temperature: response.temperature, precipitation: response.precipitation, weather: response.weatherCode};
}