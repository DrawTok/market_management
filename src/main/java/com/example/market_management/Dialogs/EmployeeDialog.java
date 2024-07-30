package com.example.market_management.Dialogs;

import com.example.market_management.Models.Employee;
import com.example.market_management.Models.Department;
import com.example.market_management.Services.DepartmentSingleton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EmployeeDialog {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField fullNameField;
    @FXML
    private DatePicker birthdayPicker;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private ComboBox<String> departmentComboBox;

    private Stage dialogStage;
    private Employee employee;
    private boolean okClicked = false;
    private boolean isUpdate;

    @FXML
    private void initialize() {
        roleComboBox.getItems().addAll("MANAGER", "STAFF");
        DepartmentSingleton departmentSingleton = DepartmentSingleton.getInstance();
        departmentComboBox.setItems(FXCollections.observableArrayList(
                departmentSingleton.getDepartments().stream().map(Department::getName).toList()
        ));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;

        if (employee != null) {
            usernameField.setText(employee.getUsername());
            passwordField.setText(employee.getPassword());
            emailField.setText(employee.getEmail());
            fullNameField.setText(employee.getFullName());
            if (employee.getBirthday() != null) {
                birthdayPicker.setValue(employee.getBirthday());
            }
            roleComboBox.setValue(employee.getRole());
            if(employee.getDepartment() != null){
                departmentComboBox.setValue(employee.getDepartment().getName());

            }
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
        passwordField.setDisable(isUpdate);
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            employee.setUsername(usernameField.getText());
            if (!isUpdate) {
                employee.setPassword(passwordField.getText());
            }
            employee.setEmail(emailField.getText());
            employee.setFullName(fullNameField.getText());
            employee.setBirthday(birthdayPicker.getValue());
            employee.setRole(roleComboBox.getValue());
            String selectedDepartmentName = departmentComboBox.getValue();
            Department selectedDepartment = DepartmentSingleton.getInstance().getDepartments().stream()
                    .filter(department -> department.getName().equals(selectedDepartmentName))
                    .findFirst()
                    .orElse(null);

            employee.setDepartment(selectedDepartment);

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (usernameField.getText() == null || usernameField.getText().isEmpty()) {
            errorMessage += "No valid username!\n";
        }
        if (!isUpdate && (passwordField.getText() == null || passwordField.getText().isEmpty())) {
            errorMessage += "No valid password!\n";
        }
        if (emailField.getText() == null || emailField.getText().isEmpty()) {
            errorMessage += "No valid email!\n";
        }
        if (fullNameField.getText() == null || fullNameField.getText().isEmpty()) {
            errorMessage += "No valid full name!\n";
        }
        if (roleComboBox.getValue() == null) {
            errorMessage += "No valid role!\n";
        }
        if (departmentComboBox.getValue() == null) {
            errorMessage += "No valid department!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
