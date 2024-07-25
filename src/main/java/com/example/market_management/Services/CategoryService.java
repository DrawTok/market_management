package com.example.market_management.Services;

import com.example.market_management.Models.Category;
import com.example.market_management.Models.ApiResponse;
import com.example.market_management.Utils.Constants;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CategoryService {

    public List<Category> getAllCategories() throws IOException {
        URL url = new URL(Constants.URL + "/category/all");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            ApiResponse<List<Category>> apiResponse = getListApiResponse(conn);
            return apiResponse.getData();
        } else {
            throw new IOException("Unexpected response code: " + responseCode);
        }
    }

    public boolean addCategory(Category category) throws IOException {
        URL url = new URL(Constants.URL + "/category/create");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        Gson gson = new Gson();
        String json = gson.toJson(category);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        return responseCode == 200;
    }


    public void updateCategory(int id, Category category) throws IOException {
        URL url = new URL(Constants.URL + "/category/" + id + "/update");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        Gson gson = new Gson();
        String json = gson.toJson(category);
        conn.getOutputStream().write(json.getBytes());

        int responseCode = conn.getResponseCode();
    }

    public boolean deleteCategory(int id) throws IOException {
        URL url = new URL(Constants.URL + "/category/" + id + "/delete");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());

        int responseCode = conn.getResponseCode();
        return responseCode == 200;
    }

    private ApiResponse<List<Category>> getListApiResponse(HttpURLConnection conn) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Gson gson = new Gson();
        Type responseType = new TypeToken<ApiResponse<List<Category>>>() {}.getType();
        return gson.fromJson(response.toString(), responseType);
    }
}
