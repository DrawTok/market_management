package com.example.market_management.Services;

import com.example.market_management.LocalDateAdapter;
import com.example.market_management.Models.ApiResponse;
import com.example.market_management.Models.Promotion;
import com.example.market_management.Utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

public class PromotionService {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, LocalDateAdapter.SERIALIZER)
            .registerTypeAdapter(LocalDate.class, LocalDateAdapter.DESERIALIZER)
            .create();

    private ApiResponse<List<Promotion>> getListApiResponse(HttpURLConnection conn) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Type responseType = new TypeToken<ApiResponse<List<Promotion>>>() {}.getType();
        return gson.fromJson(response.toString(), responseType);
    }

    public List<Promotion> getAllPromotions() throws IOException {
        URL url = new URL(Constants.URL + "/promotion/all");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            ApiResponse<List<Promotion>> apiResponse = getListApiResponse(conn);
            return apiResponse.getData();
        } else {
            throw new IOException("Unexpected response code: " + responseCode);
        }
    }

    public void addPromotion(Promotion promotion) throws IOException {
        URL url = new URL(Constants.URL + "/promotion/create");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());
        conn.setDoOutput(true);

        String jsonInputString = gson.toJson(promotion);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to add promotion, response code: " + responseCode);
        }
    }

    public void updatePromotion(Promotion promotion) throws IOException {
        URL url = new URL(Constants.URL + "/promotion/" + promotion.getId() + "/update");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());
        conn.setDoOutput(true);

        String jsonInputString = gson.toJson(promotion);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to update promotion, response code: " + responseCode);
        }
    }

    public ApiResponse<Object> deletePromotion(int id) throws IOException {
        URL url = new URL(Constants.URL + "/promotion/" + id + "/delete");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to delete promotion, response code: " + responseCode);
        }

        Type responseType = new TypeToken<ApiResponse<Object>>() {}.getType();
        return getApiResponse(conn, responseType);
    }

    private <T> ApiResponse<T> getApiResponse(HttpURLConnection conn, Type responseType) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return gson.fromJson(response.toString(), responseType);
    }
}
