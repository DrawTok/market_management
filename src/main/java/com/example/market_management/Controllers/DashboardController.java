package com.example.market_management.Controllers;

import com.example.market_management.Services.SharedService;
import com.example.market_management.Services.UserSingleton;
import com.example.market_management.Utils.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private BorderPane contentArea;

    @FXML
    private Pane paneHome;

    @FXML
    private Pane paneDepartment;
    @FXML
    private Pane paneCategory;
    @FXML
    private Pane paneProduct;
    @FXML
    private Pane panePromotion;
    @FXML
    private Pane paneEmployee;
    @FXML
    private Pane paneCustomer;
    @FXML
    private Pane paneVendor;

    @FXML
    private Pane paneLogout;

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Pane newLoadedPane = loader.load();
            contentArea.setCenter(newLoadedPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearSelection() {
        paneHome.setStyle("-fx-background-color: transparent;");
        paneDepartment.setStyle("-fx-background-color: transparent;");
        paneCategory.setStyle("-fx-background-color: transparent;");
        paneProduct.setStyle("-fx-background-color: transparent;");
        panePromotion.setStyle("-fx-background-color: transparent;");
        paneEmployee.setStyle("-fx-background-color: transparent;");
        paneCustomer.setStyle("-fx-background-color: transparent;");
        paneVendor.setStyle("-fx-background-color: transparent;");
        paneLogout.setStyle("-fx-background-color: transparent;");
    }

    @FXML
    private void showHome() {
        clearSelection();
        paneHome.setStyle("-fx-background-color: white;");
        loadView("/com/example/market_management/fxml/home.fxml");
    }

    @FXML
    private void showDepartment() {
        clearSelection();
        paneDepartment.setStyle("-fx-background-color: white;");
        loadView("/com/example/market_management/fxml/department.fxml");
    }

    @FXML
    private void showCategory() {
        clearSelection();
        paneCategory.setStyle("-fx-background-color: white;");
        loadView("/com/example/market_management/fxml/category.fxml");
    }

    @FXML
    private void showProduct() {
        clearSelection();
        paneProduct.setStyle("-fx-background-color: white;");
        loadView("/com/example/market_management/fxml/product.fxml");
    }

    @FXML
    private void showPromotion() {
        clearSelection();
        panePromotion.setStyle("-fx-background-color: white;");
        loadView("/com/example/market_management/fxml/promotion.fxml");
    }

    @FXML
    private void showEmployee() {
        clearSelection();
        paneEmployee.setStyle("-fx-background-color: white;");
        loadView("/com/example/market_management/fxml/employee.fxml");
    }

    @FXML
    private void showCustomer() {
        clearSelection();
        paneCustomer.setStyle("-fx-background-color: white;");
        loadView("/com/example/market_management/fxml/customer.fxml");
    }

    @FXML
    private void showVendor() {
        clearSelection();
        paneVendor.setStyle("-fx-background-color: white;");
        loadView("/com/example/market_management/fxml/vendor.fxml");
    }

    @FXML
    private void logout() {
        // Logic to handle logout
        clearSelection();
        paneLogout.setStyle("-fx-background-color: white;");
        UserSingleton.getInstance().clearUserToken();
        SharedService.getInstance().removePreference(Constants.TOKEN);
        try {
            Stage stage = (Stage) contentArea.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/market_management/fxml/login.fxml"));
            Parent loginPane = loader.load();
            Scene scene = new Scene(loginPane);
            stage.setScene(scene);
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
