import "./WeatherCard.css"

import {Weather} from "../../enums/Weather.ts";
import {getIconSource} from "../../services/WeatherIconService.ts";
import type {Component} from "solid-js";
import {WiFlood, WiThermometer} from "solid-icons/wi";

type WeatherCardProps = {
    temperature: number,
    precipitation: number,
    weather: Weather
}

const WeatherCard: Component<WeatherCardProps> = (props: WeatherCardProps) => {
    return (
        <div class="weather-card">
            <div class="icon-container">
                <img src={getIconSource(props.weather)} alt="weather-icon" loading="eager"/>
            </div>
            <div class="separator"></div>
            <div class="weather-info">
                <span><WiThermometer size={24}/>{props.temperature} &deg;C</span>
                <br/>
                <span><WiFlood size={24}/>{props.precipitation} mm</span>
            </div>
        </div>
    )
}

export default WeatherCard;