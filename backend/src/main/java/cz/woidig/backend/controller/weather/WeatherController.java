package cz.woidig.backend.controller.weather;

import cz.woidig.backend.dto.weather.WeatherDTO;
import cz.woidig.backend.exceptions.WeatherException;
import cz.woidig.backend.service.weather.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/weather")
public class WeatherController {
    WeatherService weatherService;

    @GetMapping("/current")
    public WeatherDTO getCurrentWeather(@RequestParam float latitude, @RequestParam float longitude) throws WeatherException {
        return weatherService.getCurrentWeather(latitude, longitude);
    }
}
