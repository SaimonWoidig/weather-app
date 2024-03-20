package cz.woidig.backend.service.weather;

import cz.woidig.backend.dto.weather.WeatherDTO;
import cz.woidig.backend.exceptions.WeatherApiException;

public interface WeatherService {
    WeatherDTO getCurrentWeather(float latitude, float longitude) throws WeatherApiException;
}
