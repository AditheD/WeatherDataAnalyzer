package weatherdataanalyzer.weatherdataanalyzer;

import java.time.LocalDate;
import java.util.Objects;

public record WeatherReading(
        LocalDate date,
        double temperature,
        double humidity,
        double precipitation
) {
    /// Validates the date and weather measurements.
    ///
    /// ```java
    /// new WeatherReading(LocalDate.parse("2023-08-01"), 32.5, 65, 0);
    ///```
    ///
    /// @throws NullPointerException     if the date is null
    /// @throws IllegalArgumentException if measurements are invalid
    public WeatherReading {
        Objects.requireNonNull(date, "Date is required");

        if (!Double.isFinite(temperature) || !Double.isFinite(humidity) || !Double.isFinite(precipitation)) {
            throw new IllegalArgumentException("Measurements must be finite numbers.");
        }

        if (humidity < 0 || humidity > 100) {
            throw new IllegalArgumentException("Humidity must be between 0 and 100.");
        }

        if (precipitation < 0) {
            throw new IllegalArgumentException("Precipitation cannot be negative.");
        }
    }

    /// Checks whether precipitation is greater than zero.
    ///
    /// ```java
    /// boolean rainy = reading.isRainy();
    ///```
    ///
    /// @return true for a rainy observation
    public boolean isRainy() {
        return precipitation > 0;
    }
}