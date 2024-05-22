"use server";

import { serverEnv } from "../env/server/env";
import WeatherCode from "./weather_code";

export type Forecast = {
  weatherType: WeatherCode;
  temperature: number;
  precipitation: number;
};

type DaysForecast = {
  weather: Forecast[];
  days: string[];
};

type GetForecastResponse = {
  weatherCode: WeatherCode;
  temperature: number;
  precipitation: number;
};

type Get7DayForecastResponse = {
  weather: GetForecastResponse[];
  days: string[];
};

export const getCurrentWeather = async (
  latitude: number,
  longitude: number
): Promise<Forecast> => {
  const apiUrl = new URL(
    `/weather/current?latitude=${latitude}&longitude=${longitude}`,
    serverEnv.BACKEND_API_URL
  );
  console.log("fetching weather", apiUrl);
  try {
    const response = await fetch(apiUrl, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      const data: GetForecastResponse = await response.json();
      return {
        weatherType: data.weatherCode,
        temperature: data.temperature,
        precipitation: data.precipitation,
      };
    }

    throw new Error("Unexpected error");
  } catch (err) {
    console.error("Failed to get weather", err);
    throw new Error("Unexpected error");
  }
};

export const getForecastNext7Days = async (
  token: string,
  latitude: number,
  longitude: number
): Promise<DaysForecast> => {
  const apiUrl = new URL(
    `/weather/forecast?latitude=${latitude}&longitude=${longitude}`,
    serverEnv.BACKEND_API_URL
  );
  try {
    const response = await fetch(apiUrl, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (response.ok) {
      const data: Get7DayForecastResponse = await response.json();
      const weather: Forecast[] = data.weather.map((w) => ({
        weatherType: w.weatherCode,
        temperature: w.temperature,
        precipitation: w.precipitation,
      }));
      return {
        weather: weather,
        days: data.days,
      };
    }

    throw new Error("Unexpected error");
  } catch (err) {
    console.error("Failed to get 7 day forecast", err);
    throw new Error("Unexpected error");
  }
};

type Get7PrevDayForecastResponse = {
  weather: GetForecastResponse[];
  days: string[];
};

export const getForecastPrev7Days = async (
  token: string,
  latitude: number,
  longitude: number
): Promise<DaysForecast> => {
  const apiUrl = new URL(
    `/weather/previous?latitude=${latitude}&longitude=${longitude}`,
    serverEnv.BACKEND_API_URL
  );
  try {
    const response = await fetch(apiUrl, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (response.ok) {
      const data: Get7PrevDayForecastResponse = await response.json();
      const weather: Forecast[] = data.weather.map((w) => ({
        weatherType: w.weatherCode,
        temperature: w.temperature,
        precipitation: w.precipitation,
      }));
      return {
        weather: weather,
        days: data.days,
      };
    }

    throw new Error("Unexpected error");
  } catch (err) {
    console.error("Failed to previous 7 day weather", err);
    throw new Error("Unexpected error");
  }
};
