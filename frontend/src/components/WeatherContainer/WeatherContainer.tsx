import {type Component, onMount} from "solid-js";
import {createStore} from "solid-js/store";

import WeatherForm from "@components/WeatherForm/WeatherForm.tsx";
import WeatherCard from "@components/WeatherCard/WeatherCard.tsx";

import type {TWeather} from "@lib/types/weather.ts";
import {Weather} from "@lib/enums/Weather.ts";
import type {TGeo} from "@lib/types/geo.ts";

const WeatherContainer: Component = () => {
    const [
        weatherData,
        setWeatherData
    ] = createStore<TWeather>(
        {temperature: 0, precipitation: 0, weather: Weather.UNKNOWN}
    );

    const [defaultGeoData, setDefaultGeoData] = createStore<TGeo>({latitude: 0, longitude: 0});

    onMount(async () => {
        let res = await fetch(`/api/geo`, {method: "GET"})
        if (!res.ok) {
            console.error("Failed to fetch geo data");
            return;
        }
        const geoData: TGeo = await res.json();
        setDefaultGeoData(geoData);

        res = await fetch(
            `/api/weather?latitude=${geoData.latitude}&longitude=${geoData.longitude}&type=current`,
            {method: "GET"}
        );
        if (!res.ok) {
            console.error("Failed to fetch weather data");
            return;
        }
        const weatherData: TWeather = await res.json();
        setWeatherData(weatherData);
    })

    return (
        <div class="weather-container">
            <WeatherForm
                defaultLocation={defaultGeoData}
                setWeatherData={setWeatherData}
            />
            <WeatherCard
                temperature={weatherData.temperature}
                precipitation={weatherData.precipitation}
                weather={weatherData.weather}
            />
        </div>
    )
}

export default WeatherContainer;