package com.example.market_management.Dialogs;

import com.example.market_management.Models.Category;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CategoryDetailDialog {
    @FXML
    private Label nameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label departmentLabel;
    @FXML
    private Label createAtLabel;

    private Stage dialogStage;
    private Category category;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCategory(Category category) {
        this.category = category;

        nameLabel.setText(category.getName());
        descriptionLabel.setText(category.getDescription());
        statusLabel.setText(String.valueOf(category.isStatus()));
        fullNameLabel.setText(category.getCreateBy() != null ? category.getCreateBy().getFullName() : "N/A");
        roleLabel.setText(category.getCreateBy() != null ? category.getCreateBy().getRole() : "N/A");
        departmentLabel.setText(category.getCreateBy() != null && category.getCreateBy().getDepartment() != null ?
                String.valueOf(category.getCreateBy().getDepartment().getId()) : "N/A");
        createAtLabel.setText(category.getCreateAt() != null ? category.getCreateAt() : "N/A");
    }

}
