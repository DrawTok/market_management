package com.example.market_management.Controllers;

import com.example.market_management.Dialogs.BillDetailsDialog;
import com.example.market_management.Dialogs.BillDialog;
import com.example.market_management.Models.Bill;
import com.example.market_management.Services.BillService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class BillController {

    @FXML
    private TableView<Bill> billTable;
    @FXML
    private TableColumn<Bill, Integer> idColumn;
    @FXML
    private TableColumn<Bill, String> payment;
    @FXML
    private TableColumn<Bill, String> typeColumn;
    @FXML
    private TableColumn<Bill, String> createAtColumn;
    @FXML
    private TextField searchField;

    private final BillService billService = new BillService();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        payment.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        createAtColumn.setCellValueFactory(new PropertyValueFactory<>("createAt"));
        loadBillData();
    }

    private void loadBillData() {
        try {
            billTable.setItems(FXCollections.observableArrayList(billService.getAllBills()));
        } catch (IOException e) {
            showAlert("Error", "Failed to load bills: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewBillDetails() throws IOException {
        Bill selectedBill = billTable.getSelectionModel().getSelectedItem();
        if (selectedBill != null) {

                int billId = selectedBill.getId();
                Bill billDetails = billService.getBillDetails(billId);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/market_management/fxml/bill_detail.fxml"));
                VBox root = loader.load();

                BillDetailsDialog controller = loader.getController();
                controller.setBill(billDetails);

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Bill Details");
                dialogStage.setScene(new Scene(root));
                controller.setDialogStage(dialogStage);
                dialogStage.showAndWait();


        } else {
            showAlert("No Selection", "Please select a bill in the table.");
        }
    }

    @FXML
    private void handleAddBill() throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/market_management/fxml/bill_dialog.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Bill");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(billTable.getScene().getWindow());

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            BillDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBill(new Bill());

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                Bill newBill = controller.getBill();
                billService.addBill(newBill);
                loadBillData();
            }


    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
