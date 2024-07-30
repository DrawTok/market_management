package com.example.market_management.Dialogs;

import com.example.market_management.Models.Bill;
import com.example.market_management.Models.CartItem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class BillDetailsDialog {

    @FXML
    private TextField employeeField;
    @FXML
    private TextField customerField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField paymentMethodField;
    @FXML
    private TextField totalField;
    @FXML
    private TableView<CartItem> cartTable;
    @FXML
    private TableColumn<CartItem, String> productNameColumn;
    @FXML
    private TableColumn<CartItem, Double> unitPriceColumn;
    @FXML
    private TableColumn<CartItem, Integer> qtyColumn;

    private Stage dialogStage;

    @FXML
    private void initialize() {
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setBill(Bill bill) {
        if (bill != null) {
            employeeField.setText(bill.getEmployee().getFullName());
            customerField.setText(bill.getCustomer().getFullName());
            typeField.setText(bill.getType());
            paymentMethodField.setText(bill.getPaymentMethod());
            totalField.setText(String.valueOf(bill.getTotal()));

            List<CartItem> cartItems = bill.getCart();
            cartTable.setItems(FXCollections.observableArrayList(cartItems));
        }
    }

    @FXML
    private void handleClose() {
        dialogStage.close();
    }
}
