package com.example.market_management.Services;

import com.example.market_management.Models.Department;
import com.example.market_management.Models.ApiResponse;
import com.example.market_management.Utils.Constants;

import com.google.gson.*;
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

public class DepartmentService {

    public List<Department> getAllDepartments() throws IOException {
        URL url = new URL(Constants.URL + "/department/all");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            ApiResponse<List<Department>> apiResponse = getListApiResponse(conn);

            return apiResponse.getData();
        } else {
            throw new IOException("Unexpected response code: " + responseCode);
        }
    }

    public Department addDepartment(Department newDepartment) throws IOException {
        URL url = new URL(Constants.URL + "/department/create");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        Gson gson = new Gson();
        String jsonInputString = gson.toJson(newDepartment);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                response.append(responseLine.trim());
            }
            Type responseType = new TypeToken<ApiResponse<Department>>() {}.getType();
            ApiResponse<Department> apiResponse = gson.fromJson(response.toString(), responseType);

            return apiResponse.getData();
        } else {
            throw new IOException("Unexpected response code: " + responseCode);
        }
    }

    public Department updateDepartment(int departmentId, Department updatedDepartment) throws IOException {
        URL url = new URL(Constants.URL + "/department/" + departmentId + "/update");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        Gson gson = new Gson();
        String jsonInputString = gson.toJson(updatedDepartment);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                response.append(responseLine.trim());
            }
            Type responseType = new TypeToken<ApiResponse<Department>>() {}.getType();
            ApiResponse<Department> apiResponse = gson.fromJson(response.toString(), responseType);

            return apiResponse.getData();
        } else {
            throw new IOException("Unexpected response code: " + responseCode);
        }
    }

    public boolean deleteDepartment(int id) throws IOException {
        URL url = new URL(Constants.URL + "/department/" + id + "/delete");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            return getSuccessFromResponse(conn);
        } else {
            throw new IOException("Unexpected response code: " + responseCode);
        }
    }




    private boolean getSuccessFromResponse(HttpURLConnection conn) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
        return jsonObject.get("success").getAsBoolean();
    }


    private ApiResponse<List<Department>> getListApiResponse(HttpURLConnection conn) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Gson gson = new Gson();
        Type responseType = new TypeToken<ApiResponse<List<Department>>>() {}.getType();
        return gson.fromJson(response.toString(), responseType);
    }
}
