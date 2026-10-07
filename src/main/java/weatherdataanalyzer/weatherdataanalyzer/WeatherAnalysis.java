package weatherdataanalyzer.weatherdataanalyzer;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.OptionalDouble;

/// Stream-based weather calculations.
///
/// ```java
/// long rainy = WeatherAnalysis.rainyDays(data);
/// ```
public interface WeatherAnalysis {

    /// Calculates the average temperature for a specific year and month.
///
/// ```java
/// var average = WeatherAnalysis.averageTemperature(
///     data, YearMonth.of(2023, 8)
/// );
/// ```
///
/// @param data daily observations
/// @param month requested year and month
/// @return average, or empty when the month has no observations
static OptionalDouble averageTemperature(
        List<WeatherReading> data,
        YearMonth month
) {
    return data.stream()
            .filter(r -> YearMonth.from(r.date()).equals(month))
            .mapToDouble(WeatherReading::temperature)
            .average();
}

    /// Finds days with temperatures strictly above a threshold.
    ///
    /// ```java
    /// var matches = WeatherAnalysis.aboveThreshold(data, 30);
    /// ```
    ///
    /// @param data daily observations
    /// @param threshold finite temperature in Celsius
    /// @return immutable matching observations
    /// @throws IllegalArgumentException if the threshold is not finite
    static List<WeatherReading> aboveThreshold(
            List<WeatherReading> data,
            double threshold
    ) {
        if (!Double.isFinite(threshold)) {
            throw new IllegalArgumentException(
                    "Threshold must be a finite number"
            );
        }

        return data.stream()
                .filter(r -> r.temperature() > threshold)
                .toList();
    }

    /// Counts days with precipitation greater than zero.
    ///
    /// ```java
    /// long count = WeatherAnalysis.rainyDays(data);
    /// ```
    ///
    /// @param data daily observations
    /// @return number of rainy days
    static long rainyDays(List<WeatherReading> data) {
        return data.stream()
                .filter(WeatherReading::isRainy)
                .count();
    }

    /// Categorizes a temperature using pattern matching and switch guards.
    ///
    /// - **Hot:** at least 30 Celsius.
    /// - **Warm:** at least 20 but below 30 Celsius.
    /// - **Cold:** below 20 Celsius.
    ///
    /// ```java
    /// String result = WeatherAnalysis.category(32.5); // Hot
    /// ```
    ///
    /// @param temperature finite temperature in Celsius
    /// @return weather category
    /// @throws IllegalArgumentException if temperature is not finite
    static String category(double temperature) {
        if (!Double.isFinite(temperature)) {
            throw new IllegalArgumentException(
                    "Temperature must be finite"
            );
        }

        return switch (Double.valueOf(temperature)) {
            case Double t when t >= 30 -> "Hot";
            case Double t when t >= 20 -> "Warm";
            default -> "Cold";
        };
    }

    /// Formats a weather observation with its measurements and category.
    ///
    /// ```java
    /// System.out.println(WeatherAnalysis.format(reading));
    /// ```
    ///
    /// @param reading observation to format
    /// @return formatted output row
    static String format(WeatherReading reading) {
        return String.format(
                Locale.US,
                "%s | %.1f C | humidity %.0f%% | rain %.1f mm | %s",
                reading.date(),
                reading.temperature(),
                reading.humidity(),
                reading.precipitation(),
                category(reading.temperature())
        );
    }
}
