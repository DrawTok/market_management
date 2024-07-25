module com.example.market_management.market_management {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.net.http;
    requires com.google.gson;
    requires java.prefs;
    requires com.fasterxml.jackson.annotation;

    opens com.example.market_management.Controllers to javafx.fxml;
    opens com.example.market_management.Dialogs to javafx.fxml;
    opens com.example.market_management.Models to com.google.gson, javafx.base;
    opens com.example.market_management.Services to com.google.gson;
    exports com.example.market_management;

}