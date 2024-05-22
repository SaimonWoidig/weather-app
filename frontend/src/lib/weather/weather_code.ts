import { FaSolidQuestion } from "solid-icons/fa";
import {
  WiDayCloudy,
  WiDayCloudyHigh,
  WiDayRain,
  WiDayRainMix,
  WiDayRainWind,
  WiDayShowers,
  WiDaySleet,
  WiDaySnow,
  WiDaySnowThunderstorm,
  WiDaySnowWind,
  WiDaySprinkle,
  WiDaySunny,
  WiDaySunnyOvercast,
  WiDust,
  WiFog,
  WiNightSnowThunderstorm,
  WiRain,
  WiRainMix,
  WiShowers,
  WiSleet,
  WiSnow,
  WiSnowWind,
  WiSnowflakeCold,
  WiStormShowers,
  WiThunderstorm,
} from "solid-icons/wi";
import type { JSXElement } from "solid-js";

// Mapping taken from https://open-meteo.com/en/docs
enum WeatherCode {
  Unknown = -1,
  Clear = 0,
  MainlyClear = 1,
  PartlyCloudy = 2,
  Overcast = 3,
  Fog = 45,
  DepositingRimeFog = 48,
  DrizzleLight = 51,
  DrizzleModerate = 53,
  DrizzleDense = 55,
  FreezingDrizzleLight = 56,
  FreezingDrizzleDense = 57,
  RainLight = 61,
  RainModerate = 63,
  RainHeavy = 65,
  FreezingRainLight = 66,
  FreezingRainHeavy = 67,
  SnowLight = 71,
  SnowModerate = 73,
  SnowHeavy = 75,
  SnowGrains = 77,
  RainShowersLight = 80,
  RainShowersModerate = 81,
  RainShowersDense = 82,
  SnowShowersLight = 85,
  SnowShowersHeavy = 86,
  ThunderstormSlightOrModerate = 95,
  ThunderstormHailSlight = 96,
  ThunderstormHailHeavy = 99,
}

export default WeatherCode;

const defaultIconSize: number = 128;

const numberToWeatherCode = (number: number): WeatherCode | undefined => {
  return WeatherCode[WeatherCode[number] as keyof typeof WeatherCode];
};

const weatherCodeToIcon = (code: WeatherCode): JSXElement => {
  switch (code) {
    case WeatherCode.Clear:
      return WiDaySunny({ size: defaultIconSize });
    case WeatherCode.MainlyClear:
      return WiDaySunnyOvercast({ size: defaultIconSize });
    case WeatherCode.PartlyCloudy:
      return WiDayCloudyHigh({ size: defaultIconSize });
    case WeatherCode.Overcast:
      return WiDayCloudy({ size: defaultIconSize });
    case WeatherCode.Fog:
      return WiFog({ size: defaultIconSize });
    case WeatherCode.DepositingRimeFog:
      return WiDust({ size: defaultIconSize });
    case WeatherCode.DrizzleLight:
      return WiDaySprinkle({ size: defaultIconSize });
    case WeatherCode.DrizzleModerate:
      return WiDayRainMix({ size: defaultIconSize });
    case WeatherCode.DrizzleDense:
      return WiRainMix({ size: defaultIconSize });
    case WeatherCode.FreezingDrizzleLight:
      return WiDaySleet({ size: defaultIconSize });
    case WeatherCode.FreezingDrizzleDense:
      return WiSleet({ size: defaultIconSize });
    case WeatherCode.RainLight:
      return WiDayRainWind({ size: defaultIconSize });
    case WeatherCode.RainModerate:
      return WiDayRain({ size: defaultIconSize });
    case WeatherCode.RainHeavy:
      return WiRain({ size: defaultIconSize });
    case WeatherCode.FreezingRainLight:
      return WiRainMix({ size: defaultIconSize });
    case WeatherCode.FreezingRainHeavy:
      return WiSnowflakeCold({ size: defaultIconSize });
    case WeatherCode.SnowLight:
      return WiDaySnowWind({ size: defaultIconSize });
    case WeatherCode.SnowModerate:
      return WiDaySnow({ size: defaultIconSize });
    case WeatherCode.SnowHeavy:
      return WiSnow({ size: defaultIconSize });
    case WeatherCode.SnowGrains:
      return WiSleet({ size: defaultIconSize });
    case WeatherCode.RainShowersLight:
      return WiDayShowers({ size: defaultIconSize });
    case WeatherCode.RainShowersModerate:
      return WiShowers({ size: defaultIconSize });
    case WeatherCode.RainShowersDense:
      return WiStormShowers({ size: defaultIconSize });
    case WeatherCode.SnowShowersLight:
      return WiSnow({ size: defaultIconSize });
    case WeatherCode.SnowShowersHeavy:
      return WiSnowWind({ size: defaultIconSize });
    case WeatherCode.ThunderstormSlightOrModerate:
      return WiThunderstorm({ size: defaultIconSize });
    case WeatherCode.ThunderstormHailSlight:
      return WiDaySnowThunderstorm({ size: defaultIconSize });
    case WeatherCode.ThunderstormHailHeavy:
      return WiNightSnowThunderstorm({ size: defaultIconSize });
    case WeatherCode.Unknown:
    default:
      console.warn(`Unknown weather code: '${code}'`);
      return FaSolidQuestion({ size: defaultIconSize });
  }
};

const weatherCodeToString = (code: WeatherCode): string => {
  switch (code) {
    case WeatherCode.Clear:
      return "Clear";
    case WeatherCode.MainlyClear:
      return "Mainly Clear";
    case WeatherCode.PartlyCloudy:
      return "Partly Cloudy";
    case WeatherCode.Overcast:
      return "Overcast";
    case WeatherCode.Fog:
      return "Fog";
    case WeatherCode.DepositingRimeFog:
      return "Depositing Rime Fog";
    case WeatherCode.DrizzleLight:
      return "Drizzle Light";
    case WeatherCode.DrizzleModerate:
      return "Drizzle Moderate";
    case WeatherCode.DrizzleDense:
      return "Drizzle Dense";
    case WeatherCode.FreezingDrizzleLight:
      return "Freezing Drizzle Light";
    case WeatherCode.FreezingDrizzleDense:
      return "Freezing Drizzle Dense";
    case WeatherCode.RainLight:
      return "Rain Light";
    case WeatherCode.RainModerate:
      return "Rain Moderate";
    case WeatherCode.RainHeavy:
      return "Rain Heavy";
    case WeatherCode.FreezingRainLight:
      return "Freezing Rain Light";
    case WeatherCode.FreezingRainHeavy:
      return "Freezing Rain Heavy";
    case WeatherCode.SnowLight:
      return "Snow Light";
    case WeatherCode.SnowModerate:
      return "Snow Moderate";
    case WeatherCode.SnowHeavy:
      return "Snow Heavy";
    case WeatherCode.SnowGrains:
      return "Snow Grains";
    case WeatherCode.RainShowersLight:
      return "Rain Showers Light";
    case WeatherCode.RainShowersModerate:
      return "Rain Showers Moderate";
    case WeatherCode.RainShowersDense:
      return "Rain Showers Dense";
    case WeatherCode.SnowShowersLight:
      return "Snow Showers Light";
    case WeatherCode.SnowShowersHeavy:
      return "Snow Showers Heavy";
    case WeatherCode.ThunderstormSlightOrModerate:
      return "Thunderstorm Slight Or Moderate";
    case WeatherCode.ThunderstormHailSlight:
      return "Thunderstorm Hail Slight";
    case WeatherCode.ThunderstormHailHeavy:
      return "Thunderstorm Hail Heavy";
    case WeatherCode.Unknown:
    default:
      console.warn(`Unknown weather code: '${code}'`);
      return "Unknown";
  }
};

export { numberToWeatherCode, weatherCodeToIcon, weatherCodeToString };
