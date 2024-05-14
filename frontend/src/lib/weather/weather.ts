import type { JSXElement } from "solid-js";
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
import WeatherCode from "./weather_code";

const defaultIconSize: number = 128;

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

const numberToWeatherCode = (number: number): WeatherCode | undefined => {
  return WeatherCode[WeatherCode[number] as keyof typeof WeatherCode];
};

export { weatherCodeToIcon, numberToWeatherCode };
