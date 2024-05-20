package cz.woidig.backend.dto.weather;

import java.time.LocalDate;
import java.util.List;

public record WeatherDaysDTO(
        List<WeatherDTO> weather,
        List<LocalDate> days
) {
}
