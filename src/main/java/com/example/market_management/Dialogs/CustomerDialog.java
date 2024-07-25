package com.example.market_management.Dialogs;

import com.example.market_management.Models.Customer;
import com.example.market_management.Services.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class CustomerDialog {

    @FXML
    private TextField nameField;
    @FXML
    private DatePicker birthdayPicker;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField pointField;
    @FXML
    private CheckBox statusCheckBox;

    private Stage dialogStage;
    private Customer customer;
    private boolean okClicked = false;
    private boolean isUpdate = false;

    private final CustomerService customerService = new CustomerService();

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            nameField.setText(customer.getFullName());
            birthdayPicker.setValue(customer.getBirthday());
            addressField.setText(customer.getAddress());
            phoneNumberField.setText(customer.getPhoneNumber());
            typeField.setText(customer.getType());

            if (isUpdate) {
                pointField.setText(String.valueOf(customer.getPoint()));
                statusCheckBox.setSelected(customer.isStatus());
                pointField.setVisible(true);
                statusCheckBox.setVisible(true);
            }
        }
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            customer.setFullName(nameField.getText());
            customer.setBirthday(birthdayPicker.getValue());
            customer.setAddress(addressField.getText());
            customer.setPhoneNumber(phoneNumberField.getText());

            if (isUpdate) {
                if(pointField.getText() == null || pointField.getText().isEmpty()){
                    customer.setPoint(0);
                }else{
                    customer.setPoint(Integer.parseInt(pointField.getText()));
                }
                if(typeField.getText() == null || typeField.getText().isEmpty()){
                    customer.setType("BRONZE");
                }else{
                    customer.setType(typeField.getText());
                }
                customer.setStatus(statusCheckBox.isSelected());
                try {
                    customerService.updateCustomer(customer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    customerService.addCustomer(customer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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

        if (nameField.getText() == null || nameField.getText().isEmpty()) {
            errorMessage += "No valid full name!\n";
        }
        if (birthdayPicker.getValue() == null) {
            errorMessage += "No valid birthday!\n";
        }

        if (phoneNumberField.getText() == null || phoneNumberField.getText().isEmpty()) {
            errorMessage += "No valid phone number!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
