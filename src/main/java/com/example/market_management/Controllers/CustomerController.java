package com.example.market_management.Controllers;

import com.example.market_management.Dialogs.CustomerDetailDialog;
import com.example.market_management.Dialogs.CustomerDialog;
import com.example.market_management.Models.Customer;
import com.example.market_management.Services.CustomerService;
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

public class CustomerController {

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> typeColumn;
    @FXML
    private TableColumn<Customer, String> statusColumn;
    @FXML
    private TextField searchField;

    private CustomerService customerService = new CustomerService();
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadCustomers();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterCustomers(newValue));
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            customerList.setAll(customers);
            customerTable.setItems(customerList);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load customers.");
        }
    }

    private void filterCustomers(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            customerTable.setItems(customerList);
        } else {
            ObservableList<Customer> filteredList = FXCollections.observableArrayList();
            for (Customer customer : customerList) {
                if (customer.getFullName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    filteredList.add(customer);
                }
            }
            customerTable.setItems(filteredList);
        }
    }

    @FXML
    private void handleAddCustomer() {
        Customer newCustomer = new Customer();
        boolean okClicked = showCustomerDialog(newCustomer, false);
        if (okClicked) {
            try {
                customerService.addCustomer(newCustomer);
                loadCustomers();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to add customer.");
            }
        }
    }

    @FXML
    private void handleEditCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            boolean okClicked = showCustomerDialog(selectedCustomer, true);
            if (okClicked) {
                try {
                    customerService.updateCustomer(selectedCustomer);
                    loadCustomers();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to update customer.");
                }
            }
        } else {
            showAlert("No Selection", "No Customer Selected");
        }
    }

    private boolean showCustomerDialog(Customer customer, boolean isUpdate) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/market_management/fxml/customer_dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Customer");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            CustomerDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setIsUpdate(isUpdate);
            controller.setCustomer(customer);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showCustomerDetails(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/market_management/fxml/customer_detail.fxml"));
            GridPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Customer Details");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CustomerDetailDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCustomer(customer);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewCustomerDetails() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            showCustomerDetails(selectedCustomer);
        } else {
            showAlert("No Selection", "No Customer Selected");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleDetailCustomer(ActionEvent actionEvent) {
        handleViewCustomerDetails();
    }
}
