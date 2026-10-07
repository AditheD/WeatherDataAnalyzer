package weatherdataanalyzer.weatherdataanalyzer;

import java.io.IOException;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/// Console entry point implemented as an interface.
///
/// ```sh
/// java -cp out weather.WeatherApp weatherdata.csv
///```
public interface WeatherApp {
    /// Loads the CSV and displays an interactive menu.
    ///
    /// ```sh
    /// java -cp out weather.WeatherApp
    ///```
    ///
    /// @param args optional CSV path; defaults to weatherdata.csv
    static void main(String[] args) {
        try {
            var path = Path.of(
                    args.length == 0 ? "weatherdata.csv" : args[0]
            );

            var data = WeatherCsv.load(path);

            System.out.println(
                    "Loaded " + data.size()
                            + " weather observations from " + path
            );

            try (var input = new Scanner(System.in)) {
                boolean running = true;

                while (running) {
                    System.out.print("""
                            
                            WEATHER DATA ANALYZER
                            =====================
                            1. Average temperature for a month
                            2. Days above a temperature threshold
                            3. Count rainy days
                            4. Display all observations and categories
                            0. Exit
                            Choose an option:
                            """);

                    if (!input.hasNextLine()) {
                        break;
                    }

                    try {
                        running = handle(
                                input.nextLine().strip(),
                                data,
                                input
                        );
                    } catch (IllegalArgumentException
                             | DateTimeException error) {
                        System.out.println(
                                "Input error: " + error.getMessage()
                        );
                    }
                }
            }

        } catch (IOException | IllegalArgumentException error) {
            System.err.println(
                    "Could not load weather data: " + error.getMessage()
            );

            System.exit(1);
        }
    }

    /// Processes a menu selection using an enhanced switch statement.
    ///
    /// ```java
    /// WeatherApp.handle("3", data, new Scanner(""));
    ///```
    ///
    /// @param choice menu selection
    /// @param data   daily observations
    /// @param input  scanner used for follow-up input
    /// @return false to exit, or true to continue
    static boolean handle(
            String choice,
            List<WeatherReading> data,
            Scanner input
    ) {
        switch (choice) {
            case "1" -> {
                System.out.print("Enter month (YYYY-MM): ");

                if (!input.hasNextLine()) {
                    return false;
                }

                var month = YearMonth.parse(
                        input.nextLine().strip()
                );

                var average = WeatherAnalysis.averageTemperature(
                        data,
                        month
                );

                System.out.println(
                        average.isPresent()
                                ? String.format(
                                Locale.US,
                                "Average for %s: %.2f C",
                                month,
                                average.getAsDouble()
                        )
                                : "No observations for " + month
                );
            }

            case "2" -> {
                System.out.print(
                        "Enter temperature threshold (C): "
                );

                if (!input.hasNextLine()) {
                    return false;
                }

                double threshold = Double.parseDouble(
                        input.nextLine().strip()
                );

                var matches = WeatherAnalysis.aboveThreshold(
                        data,
                        threshold
                );

                System.out.println(
                        "Days above threshold: " + matches.size()
                );

                matches.forEach(r ->
                        System.out.println(
                                WeatherAnalysis.format(r)
                        )
                );
            }

            case "3" -> System.out.println(
                    "Rainy days: "
                            + WeatherAnalysis.rainyDays(data)
            );

            case "4" -> data.forEach(r ->
                    System.out.println(
                            WeatherAnalysis.format(r)
                    )
            );

            case "0" -> {
                System.out.println("Goodbye!");
                return false;
            }

            default -> System.out.println(
                    "Choose 0, 1, 2, 3, or 4."
            );
        }

        return true;
    }
}
