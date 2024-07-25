package com.example.market_management.Dialogs;

import com.example.market_management.Models.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CustomerDetailDialog {

    @FXML
    private Label fullNameLabel;
    @FXML
    private Label birthdayLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label pointLabel;
    @FXML
    private Label statusLabel;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCustomer(Customer customer) {
        if (customer != null) {
            fullNameLabel.setText(customer.getFullName());
            birthdayLabel.setText(customer.getBirthday().toString());
            addressLabel.setText(customer.getAddress());
            phoneNumberLabel.setText(customer.getPhoneNumber());
            typeLabel.setText(customer.getType());
            pointLabel.setText(String.valueOf(customer.getPoint()));
            statusLabel.setText(customer.isStatus() ? "Active" : "Inactive");
        }
    }
}
