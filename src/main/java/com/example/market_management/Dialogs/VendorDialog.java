package com.example.market_management.Dialogs;

import com.example.market_management.Models.Vendor;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VendorDialog {
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField descriptionField;
    @FXML
    private CheckBox activeCheckBox;

    private Stage dialogStage;
    private Vendor vendor;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;

        nameField.setText(vendor.getName());
        phoneNumberField.setText(vendor.getPhoneNumber());
        emailField.setText(vendor.getEmail());
        addressField.setText(vendor.getAddress());
        descriptionField.setText(vendor.getDescription());
    }

    public void getVendor() {
        vendor.setName(nameField.getText());
        vendor.setPhoneNumber(phoneNumberField.getText());
        vendor.setEmail(emailField.getText());
        vendor.setAddress(addressField.getText());
        vendor.setDescription(descriptionField.getText());
        vendor.setStatus(activeCheckBox.isSelected());
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            getVendor();
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().isEmpty()) {
            errorMessage += "No valid name!\n";
        }
        if (phoneNumberField.getText() == null || phoneNumberField.getText().isEmpty()) {
            errorMessage += "No valid phone number!\n";
        }
        if (emailField.getText() == null || emailField.getText().isEmpty()) {
            errorMessage += "No valid email!\n";
        }
        if (addressField.getText() == null || addressField.getText().isEmpty()) {
            errorMessage += "No valid address!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid input: " + errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
