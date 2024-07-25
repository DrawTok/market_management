package com.example.market_management.Controllers;

import com.example.market_management.Dialogs.EmployeeDialog;
import com.example.market_management.Dialogs.EmployeeDetailDialog;
import com.example.market_management.Models.Employee;
import com.example.market_management.Services.EmployeeService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class EmployeeController {

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Integer> idColumn;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> roleColumn;
    @FXML
    private TableColumn<Employee, String> departmentColumn;
    @FXML
    private TableColumn<Employee, String> statusColumn;
    @FXML
    private TextField searchField;

    private final EmployeeService employeeService = new EmployeeService();
    private final ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment().getName()));
        statusColumn.setCellValueFactory(cellData -> {
            boolean status = cellData.getValue().isStatus();
            return new SimpleStringProperty(status ? "Active" : "Inactive");
        });

        loadEmployees();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterEmployees(newValue));
    }

    private void loadEmployees() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            employeeList.setAll(employees);
            employeeTable.setItems(employeeList);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load employees.");
        }
    }

    private void filterEmployees(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            employeeTable.setItems(employeeList);
        } else {
            ObservableList<Employee> filteredList = FXCollections.observableArrayList();
            for (Employee employee : employeeList) {
                if (employee.getFullName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    filteredList.add(employee);
                }
            }
            employeeTable.setItems(filteredList);
        }
    }

    @FXML
    private void handleAddEmployee() {
        Employee newEmployee = new Employee();
        boolean okClicked = showEmployeeDialog(newEmployee, false);
        if (okClicked) {
            try {
                employeeService.addEmployee(newEmployee);
                loadEmployees();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to add employee.");
            }
        }
    }

    @FXML
    private void handleEditEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            boolean okClicked = showEmployeeDialog(selectedEmployee, true);
            if (okClicked) {
                try {
                    employeeService.updateEmployee(selectedEmployee);
                    loadEmployees();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to update employee.");
                }
            }
        } else {
            showAlert("No Selection", "No Employee Selected");
        }
    }

    private boolean showEmployeeDialog(Employee employee, boolean isUpdate) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/market_management/fxml/employee_dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle(isUpdate ? "Edit Employee" : "Add Employee");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            EmployeeDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setIsUpdate(isUpdate);
            controller.setEmployee(employee);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showEmployeeDetails(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/market_management/fxml/employee_detail.fxml"));
            GridPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Employee Details");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            EmployeeDetailDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setEmployee(employee);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewEmployeeDetails() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            showEmployeeDetails(selectedEmployee);
        } else {
            showAlert("No Selection", "No Employee Selected");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
