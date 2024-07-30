package com.example.market_management.Dialogs;

import com.example.market_management.Models.Promotion;
import com.example.market_management.Models.Customer;
import com.example.market_management.Services.CustomerService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PromotionDialog {

    @FXML
    private TextField codeField;
    @FXML
    private ComboBox<String> customerComboBox; // Change to display names only
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    private Stage dialogStage;
    private Promotion promotion;
    private boolean saveClicked = false;
    private final CustomerService customerService = new CustomerService();
    private List<Customer> customers;

    @FXML
    private void initialize() {
        loadCustomerData();
    }

    private void loadCustomerData() {
        try {
            customers = customerService.getAllCustomers();
            customerComboBox.setItems(FXCollections.observableArrayList(customers.stream().map(Customer::getFullName).toList()));
        } catch (IOException e) {
            showAlert("Error", "Failed to load customers: " + e.getMessage());
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
        if (promotion != null) {
            codeField.setText(promotion.getCode());
            if (promotion.getCustomer() != null) {
                customerComboBox.setValue(promotion.getCustomer().getFullName());
            } else {
                customerComboBox.setValue(null); // Allow no customer
            }
            startDatePicker.setValue(promotion.getStartDate());
            endDatePicker.setValue(promotion.getEndDate());
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            promotion.setCode(codeField.getText());
            if (customerComboBox.getValue() != null && !customerComboBox.getValue().isEmpty()) {
                String selectedCustomerName = customerComboBox.getValue();
                Customer selectedCustomer = customers.stream()
                        .filter(customer -> customer.getFullName().equals(selectedCustomerName))
                        .findFirst()
                        .orElse(null);
                promotion.setCustomer(selectedCustomer);
            } else {
                promotion.setCustomer(null); // Allow no customer
            }
            promotion.setStartDate(startDatePicker.getValue());
            promotion.setEndDate(endDatePicker.getValue());

            saveClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (codeField.getText() == null || codeField.getText().length() == 0) {
            errorMessage += "No valid code!\n";
        }
        if (startDatePicker.getValue() == null) {
            errorMessage += "No valid start date!\n";
        }
        if (endDatePicker.getValue() == null) {
            errorMessage += "No valid end date!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert("Invalid Fields", errorMessage);
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
