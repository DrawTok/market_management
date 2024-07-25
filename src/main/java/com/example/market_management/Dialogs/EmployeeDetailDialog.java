package com.example.market_management.Dialogs;

import com.example.market_management.Models.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EmployeeDetailDialog {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label birthdayLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label departmentLabel;
    @FXML
    private Label statusLabel;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEmployee(Employee employee) {
        if (employee != null) {
            usernameLabel.setText(employee.getUsername());
            emailLabel.setText(employee.getEmail());
            fullNameLabel.setText(employee.getFullName());
            if (employee.getBirthday() != null) {
                birthdayLabel.setText(employee.getBirthday().toString());
            }
            roleLabel.setText(employee.getRole());
            departmentLabel.setText(employee.getDepartment().getName());
            statusLabel.setText(employee.isStatus() ? "Active" : "Inactive");
        }
    }
}
