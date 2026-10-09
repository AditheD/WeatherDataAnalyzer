package weatherdataanalyzer.weatherdataanalyzer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

/// Reads weather observations from a four-column CSV file.
///
/// ```java
/// var data
public interface WeatherCsv {

    /// Loads, validates, and sorts observations by date.
    ///
    /// Blank data lines are ignored. Duplicate dates are rejected.
    ///
    /// ```java
    /// var data = WeatherCsv.load(Path.of("weatherdata.csv"));
    ///```
    ///
    /// @param path UTF-8 CSV file
    /// @return immutable observations sorted by date
    /// @throws IOException              if the file cannot be read
    /// @throws IllegalArgumentException if the header or data is invalid
    static List<WeatherReading> load(Path path) throws IOException {
        var lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        if (lines.isEmpty() || !lines.get(0)
                .replace("\uFEFF", "")
                .replace(" ", "")
                .strip()
                .equals("Date,Temperature,Humidity,Precipitation")) {
            throw new IllegalArgumentException("Expected header: Date,Temperature,Humidity,Precipitation");
        }

        var dates = new HashSet<LocalDate>();

        return IntStream.range(1, lines.size()).filter(i -> !lines.get(i).isBlank()).mapToObj(i -> {
                    var reading = parse(lines.get(i), i + 1);

                    if (!dates.add(reading.date())) {
                        throw new IllegalArgumentException("Duplicate date at line " + (i + 1));
                    }

                    return reading;
                })
                .sorted(Comparator.comparing(WeatherReading::date))
                .toList();
    }

    /// Parses one CSV observation.
    ///
    /// ```java
    /// var reading = WeatherCsv.parse("2023-08-01,32.5,65,0.0", 2);
    ///```
    ///
    /// @param line       four unquoted comma-separated values
    /// @param lineNumber original one-based CSV line number
    /// @return validated weather observation
    /// @throws IllegalArgumentException if the row is malformed
    static WeatherReading parse(String line, int lineNumber) {
        try {
            var fields = line.split(",", -1);

            if (fields.length != 4) {
                throw new IllegalArgumentException("Expected exactly four columns");
            }

            return new WeatherReading(
                    LocalDate.parse(fields[0].strip()),
                    Double.parseDouble(fields[1].strip()),
                    Double.parseDouble(fields[2].strip()),
                    Double.parseDouble(fields[3].strip())
            );

        } catch (RuntimeException error) {
            throw new IllegalArgumentException("Invalid CSV line " + lineNumber + ": " + error.getMessage(), error);
        }
    }
}
