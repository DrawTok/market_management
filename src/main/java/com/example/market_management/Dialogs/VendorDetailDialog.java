package com.example.market_management.Dialogs;

import com.example.market_management.Models.Vendor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class VendorDetailDialog {
    @FXML
    private Label nameLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label statusLabel;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setVendor(Vendor vendor) {
        nameLabel.setText(vendor.getName());
        phoneNumberLabel.setText(vendor.getPhoneNumber());
        emailLabel.setText(vendor.getEmail());
        addressLabel.setText(vendor.getAddress());
        descriptionLabel.setText(vendor.getDescription());
        statusLabel.setText(vendor.isStatus() ? "Active" : "Inactive");
    }
}
