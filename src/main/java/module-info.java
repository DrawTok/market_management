module com.example.market_management.market_management {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.market_management.market_management to javafx.fxml;
    exports com.example.market_management.market_management;
}