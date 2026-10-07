module weatherdataanalyzer.weatherdataanalyzer {
    requires javafx.controls;
    requires javafx.fxml;


    opens weatherdataanalyzer.weatherdataanalyzer to javafx.fxml;
    exports weatherdataanalyzer.weatherdataanalyzer;
}