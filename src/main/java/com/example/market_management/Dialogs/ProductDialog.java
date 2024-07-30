package com.example.market_management.Dialogs;

import com.example.market_management.Models.Category;
import com.example.market_management.Models.Product;
import com.example.market_management.Models.Vendor;
import com.example.market_management.Services.CategorySingleton;
import com.example.market_management.Services.ProductService;
import com.example.market_management.Services.VendorSingleton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductDialog {

    @FXML
    private TextField nameField;
    @FXML
    private TextField barcodeField;
    @FXML
    private TextField unitPriceField;
    @FXML
    private TextField unitField;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private ComboBox<Vendor> vendorComboBox;
    @FXML
    private TextField inStockField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private CheckBox statusField;

    private Stage dialogStage;
    private Product product;
    private boolean saveClicked = false;
    private final ProductService productService = new ProductService();

    @FXML
    private void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(CategorySingleton.getInstance().getCategories()));
        vendorComboBox.setItems(FXCollections.observableArrayList(VendorSingleton.getInstance().getVendors()));

        categoryComboBox.setCellFactory((comboBox) -> new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        categoryComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        vendorComboBox.setCellFactory((comboBox) -> new ListCell<>() {
            @Override
            protected void updateItem(Vendor item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        vendorComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Vendor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            nameField.setText(product.getName());
            barcodeField.setText(product.getBarcode());
            unitPriceField.setText(String.valueOf(product.getUnitPrice()));
            unitField.setText(product.getUnit());
            categoryComboBox.setValue(product.getCategory());
            vendorComboBox.setValue(product.getVendor());
            inStockField.setText(String.valueOf(product.getInStock()));
            descriptionField.setText(product.getDescription());
            statusField.setSelected(product.isStatus());
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (product == null) {
                product = new Product();
            }
            product.setName(nameField.getText());
            product.setBarcode(barcodeField.getText());
            product.setUnitPrice(Double.parseDouble(unitPriceField.getText()));
            product.setUnit(unitField.getText());
            product.setCategory(categoryComboBox.getValue());
            product.setVendor(vendorComboBox.getValue());
            product.setInStock(Integer.parseInt(inStockField.getText()));
            product.setDescription(descriptionField.getText());
            product.setStatus(statusField.isSelected());

            try {
                if (product.getId() == 0) {
                    productService.addProduct(product);
                } else {
                    productService.updateProduct(product);
                }
                saveClicked = true;
                dialogStage.close();
            } catch (IOException e) {
                showAlert("Error", "Failed to save product: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().isEmpty()) {
            errorMessage += "No valid product name!\n";
        }
        if (unitPriceField.getText() == null || unitPriceField.getText().isEmpty()) {
            errorMessage += "No valid unit price!\n";
        } else {
            try {
                Double.parseDouble(unitPriceField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid unit price (must be a number)!\n";
            }
        }
        if (unitField.getText() == null || unitField.getText().isEmpty()) {
            errorMessage += "No valid unit!\n";
        }
        if (categoryComboBox.getValue() == null) {
            errorMessage += "No valid category!\n";
        }
        if (inStockField.getText() == null || inStockField.getText().isEmpty()) {
            errorMessage += "No valid in stock value!\n";
        } else {
            try {
                Integer.parseInt(inStockField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid in stock value (must be an integer)!\n";
            }
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
