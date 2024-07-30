package com.example.market_management.Dialogs;

import com.example.market_management.Models.Bill;
import com.example.market_management.Models.CartItem;
import com.example.market_management.Models.Customer;
import com.example.market_management.Services.CustomerService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BillDialog {

    @FXML
    private TextField typeField;
    @FXML
    private TextField pointField;
    @FXML
    private TextArea cartArea;

    private Stage dialogStage;
    private Bill bill;
    private boolean saveClicked = false;


    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
        if (bill != null) {
            typeField.setText(bill.getType());
            pointField.setText(String.valueOf(bill.getPoint()));
//            cartArea.setText(bill.getCart().stream()
//                    .map(CartItem::toString)
//                    .collect(Collectors.joining("\n")));
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (bill == null) {
                bill = new Bill();
            }
            bill.setType(typeField.getText());
            bill.setPoint(Integer.parseInt(pointField.getText()));
            bill.setCart(parseCart(cartArea.getText()));

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

        if (typeField.getText() == null || typeField.getText().isEmpty()) {
            errorMessage += "No valid type!\n";
        }
        if (pointField.getText() == null || pointField.getText().isEmpty()) {
            errorMessage += "No valid points!\n";
        }
        // Additional validation for cart input

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert("Invalid Fields", errorMessage);
            return false;
        }
    }

    private List<CartItem> parseCart(String cartText) {
        return Stream.of(cartText.split("\n"))
                .map(line -> {

                    String[] parts = line.split(", ");
                    int productId = Integer.parseInt(parts[0].split(":")[1]);
                    int qty = Integer.parseInt(parts[1].split(":")[1]);
                    return new CartItem(productId, qty);
                })
                .collect(Collectors.toList());
    }

    public Bill getBill() {
        return bill;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
