package com.example.market_management.Controllers;

import com.example.market_management.Dialogs.VendorDetailDialog;
import com.example.market_management.Dialogs.VendorDialog;
import com.example.market_management.Models.Category;
import com.example.market_management.Models.Vendor;
import com.example.market_management.Services.VendorService;
import com.example.market_management.Services.VendorSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class VendorController {

    @FXML
    private TableView<Vendor> vendorTable;
    @FXML
    private TableColumn<Vendor, Integer> idColumn;
    @FXML
    private TableColumn<Vendor, String> nameColumn;
    @FXML
    private TableColumn<Vendor, String> phoneColumn;
    @FXML
    private TableColumn<Vendor, String> emailColumn;
    @FXML
    private TableColumn<Vendor, String> addressColumn;
    @FXML
    private TextField searchField;

    private final VendorService vendorService = new VendorService();
    private final ObservableList<Vendor> vendorList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        loadVendors();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterVendors(newValue));
    }

    private void loadVendors() {
        try {
            List<Vendor> vendors = vendorService.getAllVendors();
            vendorList.setAll(vendors);
            VendorSingleton.getInstance().setVendors(vendorList);
            vendorTable.setItems(vendorList);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load vendors.");
        }
    }

    private void filterVendors(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            vendorTable.setItems(vendorList);
        } else {
            ObservableList<Vendor> filteredList = FXCollections.observableArrayList();
            for (Vendor vendor : vendorList) {
                if (vendor.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    filteredList.add(vendor);
                }
            }
            vendorTable.setItems(filteredList);
        }
    }
    @FXML
    private void handleAddVendor() {
        Vendor newVendor = new Vendor();
        boolean okClicked = showVendorDialog(newVendor);
        if (okClicked) {
            try {
                vendorService.addVendor(newVendor);
                loadVendors();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to add vendor.");
            }
        }
    }

    @FXML
    private void handleEditVendor() {
        Vendor selectedVendor = vendorTable.getSelectionModel().getSelectedItem();
        if (selectedVendor != null) {
            boolean okClicked = showVendorDialog(selectedVendor);
            if (okClicked) {
                try {
                    vendorService.updateVendor(selectedVendor);
                    loadVendors();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to update vendor.");
                }
            }
        } else {
            showAlert("No Selection", "No Vendor Selected");
        }
    }

    @FXML
    private void handleDeleteVendor() {
        Vendor selectedVendor = vendorTable.getSelectionModel().getSelectedItem();
        if (selectedVendor != null) {
            try {
                vendorService.deleteVendor(selectedVendor.getId());
                loadVendors();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete vendor.");
            }
        } else {
            showAlert("No Selection", "No Vendor Selected");
        }
    }

    @FXML
    private void handleDetailVendor() {
        Vendor selectedVendor = vendorTable.getSelectionModel().getSelectedItem();
        if (selectedVendor != null) {
            showDetailDialog(selectedVendor);
        } else {
            showAlert("No Selection", "No Vendor Selected");
        }
    }

    private boolean showVendorDialog(Vendor vendor) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/market_management/fxml/vendor_dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Vendor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            VendorDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setVendor(vendor);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showDetailDialog(Vendor vendor) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/market_management/fxml/vendor_detail.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Vendor Details");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            VendorDetailDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setVendor(vendor);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
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
