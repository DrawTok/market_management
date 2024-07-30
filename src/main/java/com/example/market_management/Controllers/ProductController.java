package com.example.market_management.Controllers;

import com.example.market_management.Dialogs.ProductDetailDialog;
import com.example.market_management.Dialogs.ProductDialog;
import com.example.market_management.Models.Product;
import com.example.market_management.Services.ProductService;
import com.example.market_management.Models.ApiResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductController {
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Double> unitPriceColumn;
    @FXML
    private TableColumn<Product, String> unitColumn;
    @FXML
    private TableColumn<Product, Integer> inStockColumn;
    @FXML
    private TextField searchField;

    private final ProductService productService = new ProductService();

    @FXML
    private void initialize() throws IOException {
        ObservableList<Product> productObservableList = FXCollections.observableArrayList(productService.getAllProducts());
        FilteredList<Product> filteredData = new FilteredList<>(productObservableList, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(product.getId()).contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Product> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(productTable.comparatorProperty());

        productTable.setItems(sortedData);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        inStockColumn.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    }

    @FXML
    private void handleNewProduct() {
        showProductFormDialog(null);
    }

    @FXML
    private void handleEditProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            showProductFormDialog(selectedProduct);
        } else {
            showAlert("No Selection", "Please select a product to edit.");
        }
    }

    @FXML
    private void handleDeleteProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                ApiResponse<Object> response = productService.deleteProduct(selectedProduct.getId());
                productTable.getItems().remove(selectedProduct);

            } catch (IOException e) {
                showAlert("Error", "Failed to delete product: " + e.getMessage());
            }
        } else {
            showAlert("No Selection", "Please select a product to delete.");
        }
    }

    @FXML
    private void handleViewProduct() {
        Product productSelected = productTable.getSelectionModel().getSelectedItem();
        if (productSelected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/market_management/fxml/product_detail.fxml"));
                VBox page = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Product Details");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(productTable.getScene().getWindow());
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);

                ProductDetailDialog controller = loader.getController();
                controller.setProduct(productSelected);

                dialogStage.showAndWait();
            } catch (IOException e) {
                showAlert("Error", "Failed to load product details dialog: " + e.getMessage());
            }
        } else {
            showAlert("No Selection", "No Employee Selected");
        }

    }

    private void showProductFormDialog(Product product) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/market_management/fxml/product_dialog.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(product == null ? "Add Product" : "Edit Product");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(productTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ProductDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setProduct(product);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                refreshProductTable();
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to load product form dialog: " + e.getMessage());
        }
    }

    private void refreshProductTable() throws IOException {
        ObservableList<Product> productObservableList = FXCollections.observableArrayList(productService.getAllProducts());
        productTable.setItems(productObservableList);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
