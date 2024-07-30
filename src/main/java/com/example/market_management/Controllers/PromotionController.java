package com.example.market_management.Controllers;

import com.example.market_management.Dialogs.PromotionDialog;
import com.example.market_management.Models.Promotion;
import com.example.market_management.Services.PromotionService;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PromotionController {

    @FXML
    private TableView<Promotion> promotionTable;
    @FXML
    private TableColumn<Promotion, String> codeColumn;
    @FXML
    private TableColumn<Promotion, String> startDateColumn;
    @FXML
    private TableColumn<Promotion, String> endDateColumn;
    @FXML
    private TextField searchField;

    private final ObservableList<Promotion> promotionData = FXCollections.observableArrayList();
    private final PromotionService promotionService = new PromotionService();


    @FXML
    private void initialize() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterPromotionList(newValue));

        try {
            loadPromotions();
        } catch (IOException e) {
            showAlert("Error", "Failed to load promotions: " + e.getMessage());
        }
    }

    private void loadPromotions() throws IOException {
        List<Promotion> promotions = promotionService.getAllPromotions();
        promotionTable.getItems().setAll(promotions);
    }

    @FXML
    private void handleAddPromotion() {
        Promotion newPromotion = new Promotion();
        boolean saveClicked = showPromotionDialog(newPromotion);
        if (saveClicked) {
            try {
                promotionService.addPromotion(newPromotion);
                loadPromotions();
            } catch (IOException e) {
                showAlert("Error", "Failed to add promotion: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEditPromotion() {
        Promotion selectedPromotion = promotionTable.getSelectionModel().getSelectedItem();
        if (selectedPromotion != null) {
            boolean saveClicked = showPromotionDialog(selectedPromotion);
            if (saveClicked) {
                try {
                    promotionService.updatePromotion(selectedPromotion);
                    loadPromotions();
                } catch (IOException e) {
                    showAlert("Error", "Failed to update promotion: " + e.getMessage());
                }
            }
        } else {
            showAlert("No Selection", "No promotion selected.");
        }
    }

    @FXML
    private void handleDeletePromotion() {
        Promotion selectedPromotion = promotionTable.getSelectionModel().getSelectedItem();
        if (selectedPromotion != null) {
            try {
                promotionService.deletePromotion(selectedPromotion.getId());
                loadPromotions();
            } catch (IOException e) {
                showAlert("Error", "Failed to delete promotion: " + e.getMessage());
            }
        } else {
            showAlert("No Selection", "No promotion selected.");
        }
    }

    private void filterPromotionList(String query) {
        if (query == null || query.isEmpty()) {
            promotionTable.setItems(promotionData);
        } else {
            ObservableList<Promotion> filteredList = FXCollections.observableArrayList();
            for (Promotion promotion : promotionData) {
                if (promotion.getCode().toLowerCase().contains(query.toLowerCase()) ||
                        promotion.getCustomer().getFullName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(promotion);
                }
            }
            promotionTable.setItems(filteredList);
        }
    }

    @FXML
    private void handleViewPromotion() {
        Promotion selectedPromotion = promotionTable.getSelectionModel().getSelectedItem();
        if (selectedPromotion != null) {
            showPromotionDialog(selectedPromotion);
        } else {
            showAlert("No Selection", "No promotion selected.");
        }
    }

    private boolean showPromotionDialog(Promotion promotion) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/market_management/fxml/promotion_dialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Promotion Details");
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            PromotionDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPromotion(promotion);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            showAlert("Error", "Failed to load promotion dialog: " + e.getMessage());
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
