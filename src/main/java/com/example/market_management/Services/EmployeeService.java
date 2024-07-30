package com.example.market_management.Services;

import com.example.market_management.LocalDateAdapter;
import com.example.market_management.Models.Employee;
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

public class EmployeeService {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, LocalDateAdapter.SERIALIZER)
            .registerTypeAdapter(LocalDate.class, LocalDateAdapter.DESERIALIZER)
            .create();

    private ApiResponse<List<Employee>> getListApiResponse(HttpURLConnection conn) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Type responseType = new TypeToken<ApiResponse<List<Employee>>>() {}.getType();
        return gson.fromJson(response.toString(), responseType);
    }

    public List<Employee> getAllEmployees() throws IOException {
        URL url = new URL(Constants.URL + "/employee/all");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            ApiResponse<List<Employee>> apiResponse = getListApiResponse(conn);
            return apiResponse.getData();
        } else {
            throw new IOException("Unexpected response code: " + responseCode);
        }
    }

    public void addEmployee(Employee employee) throws IOException {
        URL url = new URL(Constants.URL + "/employee/signup");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());
        conn.setDoOutput(true);

        String jsonInputString = gson.toJson(employee);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to add employee, response code: " + responseCode);
        }
    }

    public void updateEmployee(Employee employee) throws IOException {
        Employee currentEmployee = new Employee(
                employee.getUsername(), employee.getEmail(), employee.getPhoneNumber(),
                employee.getAddress(), employee.getFullName(),  employee.getBirthday(),
                employee.getRole(), employee.getDepartment(), employee.isStatus());
        URL url = new URL(Constants.URL + "/employee/update/" + employee.getId());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + UserSingleton.getInstance().getUserToken());
        conn.setDoOutput(true);

        String jsonInputString = gson.toJson(currentEmployee);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to update employee, response code: " + responseCode);
        }
    }
}
