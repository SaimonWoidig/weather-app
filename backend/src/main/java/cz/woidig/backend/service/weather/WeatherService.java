package cz.woidig.backend.service.weather;

import cz.woidig.backend.dto.weather.WeatherDTO;
import cz.woidig.backend.dto.weather.WeatherDaysDTO;
import cz.woidig.backend.exceptions.WeatherException;

public interface WeatherService {
    WeatherDTO getCurrentWeather(float latitude, float longitude) throws WeatherException;

    WeatherDaysDTO get7DayForecast(float latitude, float longitude) throws WeatherException;

    WeatherDaysDTO get7PreviousDays(float latitude, float longitude) throws WeatherException;
}
