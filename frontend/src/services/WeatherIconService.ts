import type {ImageMetadata} from "astro";
import {Weather} from "../enums/Weather.ts";

// All images are taken from https://github.com/Makin-Things/weather-icons
import clearSky from "../images/icons/weather/clear-sky.svg";
import mainlyClear from "../images/icons/weather/mainly-clear.svg";
import partlyCloudy from "../images/icons/weather/partly-cloudy.svg";
import overcast from "../images/icons/weather/overcast.svg";
import fog from "../images/icons/weather/fog.svg";
import drizzle from "../images/icons/weather/drizzle.svg";
import rain from "../images/icons/weather/rain.svg";
import rainShowers from "../images/icons/weather/rain-showers.svg";
import freezingDrizzleRain from "../images/icons/weather/freezing-drizzle-rain.svg";
import slightSnow from "../images/icons/weather/slight-snow.svg";
import moderateHeavySnow from "../images/icons/weather/moderate-heavy-snow.svg";
import snowGrains from "../images/icons/weather/snow-grains.svg";
import thunderstorm from "../images/icons/weather/thunderstorm.svg";
import unknownWeather from "../images/icons/weather/unknown-weather.svg";


export function weatherIcon(weather: Weather): ImageMetadata {
    switch (weather) {
        case Weather.CLEAR_SKY:
            return clearSky;
        case Weather.MAINLY_CLEAR:
            return mainlyClear;
        case Weather.PARTLY_CLOUDY:
            return partlyCloudy;
        case Weather.OVERCAST:
            return overcast;
        case Weather.FOG:
        case Weather.DEPOSITING_RIME_FOG:
            return fog;
        case Weather.LIGHT_DRIZZLE:
        case Weather.MODERATE_DRIZZLE:
        case Weather.DENSE_DRIZZLE:
            return drizzle;
        case Weather.SLIGHT_RAIN:
        case Weather.MODERATE_RAIN:
        case Weather.HEAVY_RAIN:
            return rain;
        case Weather.SLIGHT_RAIN_SHOWERS:
        case Weather.MODERATE_RAIN_SHOWERS:
        case Weather.VIOLENT_RAIN_SHOWERS:
            return rainShowers;
        case Weather.LIGHT_FREEZING_DRIZZLE:
        case Weather.DENSE_FREEZING_DRIZZLE:
        case Weather.LIGHT_FREEZING_RAIN:
        case Weather.HEAVY_FREEZING_RAIN:
            return freezingDrizzleRain;
        case Weather.SLIGHT_SNOW:
            return slightSnow;
        case Weather.MODERATE_SNOW:
        case Weather.HEAVY_SNOW:
            return moderateHeavySnow;
        case Weather.SNOW_GRAINS:
            return snowGrains;
        case Weather.SLIGHT_SNOW_SHOWERS:
        case Weather.HEAVY_SNOW_SHOWERS:
            return snowGrains;
        case Weather.THUNDERSTORM:
        case Weather.SLIGHT_HAIL_THUNDERSTORM:
        case Weather.HEAVY_HAIL_THUNDERSTORM:
            return thunderstorm;
        case Weather.UNKNOWN:
        default:
            return unknownWeather;
    }
}