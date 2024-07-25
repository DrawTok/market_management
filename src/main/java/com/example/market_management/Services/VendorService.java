package com.example.market_management.Services;

import com.example.market_management.Models.ApiResponse;
import com.example.market_management.Models.Vendor;
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
import java.util.Scanner;

public class VendorService {

    public boolean addVendor(Vendor vendor) throws IOException {
        URL url = new URL(Constants.URL + "/vendor/create");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        Gson gson = new Gson();
        String json = gson.toJson(vendor);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        return responseCode == 200;
    }

    public boolean updateVendor(Vendor vendor) throws IOException {
        URL url = new URL(Constants.URL + "/vendor/" + vendor.getId()+"/update");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        Gson gson = new Gson();
        String json = gson.toJson(vendor);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        return responseCode == 200;
    }

    public boolean deleteVendor(int id) throws IOException {
        URL url = new URL(Constants.URL + "/vendor/"+id+"/delete");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());

        int responseCode = conn.getResponseCode();
        return responseCode == 200;
    }

    public List<Vendor> getAllVendors() throws IOException {
        URL url = new URL(Constants.URL + "/vendor/all");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            ApiResponse<List<Vendor>> apiResponse = getApiResponse(conn);

            return apiResponse.getData();
        } else {
            throw new IOException("Unexpected response code: " + responseCode);
        }
    }

    private ApiResponse<List<Vendor>> getApiResponse(HttpURLConnection conn) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Gson gson = new Gson();
        Type responseType = new TypeToken<ApiResponse<List<Vendor>>>() {}.getType();
        return gson.fromJson(response.toString(), responseType);
    }
}
