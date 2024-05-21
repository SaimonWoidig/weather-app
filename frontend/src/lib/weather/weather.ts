import WeatherCode from "./weather_code";

type Forecast = {
  weatherType: WeatherCode;
  temperature: number;
  precipitation: number;
};

export const getForecastNext7Days = async (
  token: string,
  latitude: number,
  longitude: number
): Promise<Forecast[]> => {
  "use server";

  if (!token) {
    throw new Error("No token provided");
  }

  // simulate long running call
  await new Promise<void>((res) => setTimeout(() => res(), 3000));

  // TODO: connect to API
  return [
    {
      weatherType: WeatherCode.Fog,
      temperature: 10,
      precipitation: 0,
    },
    {
      weatherType: WeatherCode.Overcast,
      temperature: 13,
      precipitation: 1,
    },
    {
      weatherType: WeatherCode.Clear,
      temperature: 22,
      precipitation: 0,
    },
    {
      weatherType: WeatherCode.RainLight,
      temperature: 17,
      precipitation: 3,
    },
    {
      weatherType: WeatherCode.RainHeavy,
      temperature: 15,
      precipitation: 5,
    },
    {
      weatherType: WeatherCode.Overcast,
      temperature: 18,
      precipitation: 0,
    },
    {
      weatherType: WeatherCode.PartlyCloudy,
      temperature: 19,
      precipitation: 0,
    },
  ];
};

export const getForecastPrev7Days = async (
  token: string,
  latitude: number,
  longitude: number
): Promise<Forecast[]> => {
  "use server";
  if (!token) {
    throw new Error("No token provided");
  }

  // simulate long running call
  await new Promise<void>((res) => setTimeout(() => res(), 2000));

  // TODO: connect to API
  return [
    {
      weatherType: WeatherCode.SnowLight,
      temperature: 0,
      precipitation: 2,
    },
    {
      weatherType: WeatherCode.SnowHeavy,
      temperature: -5,
      precipitation: 5,
    },
    {
      weatherType: WeatherCode.SnowHeavy,
      temperature: -12,
      precipitation: 8,
    },
    {
      weatherType: WeatherCode.SnowLight,
      temperature: -5,
      precipitation: 6,
    },
    {
      weatherType: WeatherCode.SnowGrains,
      temperature: 0,
      precipitation: 2,
    },
    {
      weatherType: WeatherCode.Overcast,
      temperature: 4,
      precipitation: 0.5,
    },
    {
      weatherType: WeatherCode.PartlyCloudy,
      temperature: 6,
      precipitation: 0,
    },
  ];
};
