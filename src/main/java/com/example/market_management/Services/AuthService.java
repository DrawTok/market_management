package com.example.market_management.Services;

import com.example.market_management.Models.ApiResponse;
import com.example.market_management.Models.Data;
import com.example.market_management.Utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AuthService {
    public ApiResponse<Data> authenticateUser(String username, String password) throws IOException {
        URL url = new URL(Constants.URL + "/employee/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        conn.disconnect();
        Gson gson = new Gson();

        Type responseType = new TypeToken<ApiResponse<Data>>() {}.getType();
        return gson.fromJson(response.toString(), responseType);
    }

    public void logout(){
        SharedService.getInstance().removePreference(Constants.TOKEN);
        UserSingleton.getInstance().clearUserToken();
    }
}