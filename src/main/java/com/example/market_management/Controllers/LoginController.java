package com.example.market_management.Controllers;

import com.example.market_management.Models.ApiResponse;
import com.example.market_management.Models.Data;
import com.example.market_management.Services.AuthService;
import com.example.market_management.Services.SharedService;
import com.example.market_management.Services.UserSingleton;
import com.example.market_management.Utils.Constants;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    private final AuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    @FXML
    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            ApiResponse<Data> response = authService.authenticateUser(username, password);

            if (response.isSuccess()) {
                System.out.println("Data: "+response.getData());
                SharedService.getInstance().setPreference(Constants.TOKEN, response.getData().getToken());
                UserSingleton.getInstance().setUserToken(response.getData().getToken());

                Stage stage = (Stage) usernameField.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/market_management/fxml/dashboard.fxml"));
                Parent dashboardPane = loader.load();
                Scene scene = new Scene(dashboardPane, 900, 600);
                stage.setScene(scene);
                stage.setTitle("Dashboard");
            } else {
                System.out.println("Login failed: " + response.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
