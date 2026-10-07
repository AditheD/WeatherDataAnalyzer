# Weather Data Analyzer

A Java 23 console application that loads daily weather data from CSV
and analyzes temperature and precipitation.

## Features

- Calculate the average temperature for a specific year and month.
- List days with temperatures strictly above a given threshold.
- Count rainy days.
- Display all observations with weather categories.
- Validate CSV data and user input.
- Report months with no observations.
- Reject duplicate dates.

## Requirements

- JDK 23.
- IntelliJ IDEA or a terminal.
- No external dependencies.
- No preview flags required.

## Project Structure

```text
WeatherDataAnalyzer/
├── weatherdata.csv
├── README.md
├── .gitignore
└── src/
    └── weather/
        ├── WeatherReading.java
        ├── WeatherCsv.java
        ├── WeatherAnalysis.java
        └── WeatherApp.java
```

## Code Organization

The project uses records and interfaces without explicit class declarations.

- WeatherReading: immutable record representing one daily observation.
- WeatherCsv: reads, validates, and sorts CSV observations.
- WeatherAnalysis: calculates results using lambdas and streams.
- WeatherApp: handles console input and menu actions.

Records are implicitly classes in Java's type system, but the source
follows the assignment's record-and-interface design.

## Modern Java Features

| Feature | Version | Project Usage |
| --- | --- | --- |
| Lambdas and streams | Java 8 | Filtering, averaging, counting, and output |
| Enhanced switch | Java 14 | Menu actions with arrow syntax |
| Text blocks | Java 15 | Multiline menu |
| Records | Java 16 | Immutable weather observations |
| Stream.toList() | Java 16 | Immutable result lists |
| Pattern matching for switch | Java 21 | Temperature categories with guards |
| Markdown Javadoc | Java 23 | Triple-slash comments and fenced code examples |

Markdown documentation comments were introduced in Java 23.
Java 18 introduced Javadoc code snippets.

References:

- https://openjdk.org/jeps/467
- https://openjdk.org/jeps/413

## CSV Format

```csv
Date,Temperature,Humidity,Precipitation
2023-08-01,32.5,65,0.0
2023-08-02,35.0,60,0.2
```

### Validation Rules

- Dates use YYYY-MM-DD.
- Temperature is measured in Celsius.
- Humidity is a percentage between 0 and 100.
- Precipitation is measured in millimeters and cannot be negative.
- All numeric measurements must be finite.
- Each date can appear only once.
- Blank data lines are ignored.
- Observations are sorted by date.
- Invalid rows produce an error containing their line number.

The parser supports the assignment's unquoted numeric CSV format.
Quoted fields and embedded commas are not supported.

## Running in IntelliJ

1. Open the project.
2. Select JDK 23 as the Project SDK.
3. Set the language level to Java 23.
4. Ensure src is marked as a Sources Root.
5. Open WeatherApp.java.
6. Run WeatherApp.main.
7. Use the project folder as the working directory.

## Running from a Terminal

Run these commands from the project folder:

```sh
mkdir -p out
javac --release 23 -d out src/weather/*.java
java -cp out weather.WeatherApp
```

To use a different CSV:

```sh
java -cp out weather.WeatherApp /path/to/weatherdata.csv
```

## Menu Options

1. Average temperature: enter a month such as 2023-08.
2. Days above threshold: enter a Celsius value such as 30.
3. Rainy-day count.
4. Display all observations.
0. Exit.

A rainy day has precipitation greater than zero.

The threshold comparison is strictly greater than the entered value.

## Weather Categories

| Temperature | Category |
| --- | --- |
| At least 30 C | Hot |
| At least 20 C but below 30 C | Warm |
| Below 20 C | Cold |

These thresholds are application-specific.

## Expected Sample Results

| Operation | Expected Result |
| --- | --- |
| Average for 2023-08 | 30.20 C |
| Temperatures above 30 C | August 1 and August 2 |
| Rainy-day count | 4 |
| Average for 2024-01 | No observations |
| Category for 30 C | Hot |
| Category for 20 C | Warm |
| Category for 19.9 C | Cold |

Additional manual checks:

- Enter an invalid month: an input error appears and the menu continues.
- Enter NaN as a threshold: an input error appears.
- Add a duplicate date: CSV loading fails.
- Add an invalid measurement: the error identifies the CSV line.

## Generate Javadoc

Use the JDK 23 Javadoc tool:

```sh
javadoc -d docs -sourcepath src -subpackages weather
```

Open docs/index.html to view the generated documentation.

All declared methods and the record constructor have Markdown
documentation with code examples.

## Efficiency

Loading and analysis use linear passes over the observations.
Sorting takes O(n log n).

Observations are stored in memory, making the application suitable
for small and medium daily weather files.

## Submission

Upload all source files, weatherdata.csv, .gitignore, and README.md
to GitHub, then submit the repository URL.

This project implements the required console application.
The optional GUI and CSS extra credit are not included.