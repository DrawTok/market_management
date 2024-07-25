package com.example.market_management.Services;

import com.example.market_management.LocalDateAdapter;
import com.example.market_management.Models.Customer;
import com.example.market_management.Models.ApiResponse;
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

public class CustomerService {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, LocalDateAdapter.SERIALIZER)
            .registerTypeAdapter(LocalDate.class, LocalDateAdapter.DESERIALIZER)
            .create();

    private ApiResponse<List<Customer>> getListApiResponse(HttpURLConnection conn) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Type responseType = new TypeToken<ApiResponse<List<Customer>>>() {}.getType();
        return gson.fromJson(response.toString(), responseType);
    }

    public List<Customer> getAllCustomers() throws IOException {
        URL url = new URL(Constants.URL + "/customer/all");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            ApiResponse<List<Customer>> apiResponse = getListApiResponse(conn);
            return apiResponse.getData();
        } else {
            throw new IOException("Unexpected response code: " + responseCode);
        }
    }

    public void addCustomer(Customer customer) throws IOException {
        URL url = new URL(Constants.URL + "/customer/create");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        String jsonInputString = gson.toJson(customer);

        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Unexpected response code: " + responseCode);
        }
    }

    public void updateCustomer(Customer customer) throws IOException {
        URL url = new URL(Constants.URL + "/customer/" + customer.getId() + "/update");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        String jsonInputString = gson.toJson(customer);

        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Unexpected response code: " + responseCode);
        }
    }
}
