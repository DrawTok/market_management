package com.example.market_management;

import com.example.market_management.Services.SharedService;
import com.example.market_management.Services.UserSingleton;
import com.example.market_management.Utils.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        UserSingleton userSingleton = UserSingleton.getInstance();
        String token = SharedService.getInstance().getPreference(Constants.TOKEN);
        userSingleton.setUserToken(token);
        int width = 400, height = 300;
        FXMLLoader fxmlLoader;
        if (token != null) {
            fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/dashboard.fxml"));
            stage.setTitle("Dashboard");
            width = 900;
            height = 600;
        } else {
            fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/login.fxml"));
            stage.setTitle("Login");
        }

        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}