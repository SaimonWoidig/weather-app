import WeatherForm from "@components/WeatherForm/WeatherForm.tsx";
import WeatherCard from "@components/WeatherCard/WeatherCard.tsx";
import {Weather} from "../../enums/Weather.ts";
import {createStore} from "solid-js/store";
import type {WeatherData} from "../../types/WeatherData.ts";
import type {Component} from "solid-js";

const WeatherContainer: Component = () => {
    const [
        weatherData,
        setWeatherData
    ] = createStore<WeatherData>(
        {temperature: 0, precipitation: 0, weather: Weather.UNKNOWN}
    );

    return (
        <div class="weather-container">
            <WeatherForm
                defaultLocation={{latitude: 0, longitude: 0}}
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