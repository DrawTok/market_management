package com.example.market_management.Controllers;

import com.example.market_management.Dialogs.CategoryDetailDialog;
import com.example.market_management.Dialogs.CategoryDialog;
import com.example.market_management.Models.Category;
import com.example.market_management.Models.User;
import com.example.market_management.Services.CategoryService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CategoryController {
    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, Integer> idColumn;
    @FXML
    private TableColumn<Category, String> nameColumn;
    @FXML
    private TableColumn<Category, String> descriptionColumn;
    @FXML
    private TableColumn<Category, String> createByColumn;
    @FXML
    private TableColumn<Category, String> createAtColumn;
    @FXML
    private TextField searchField;

    private final CategoryService categoryService = new CategoryService();
    private final ObservableList<Category> categoryList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        createByColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getCreateBy();
            return user == null ? new ReadOnlyStringWrapper("") : new ReadOnlyStringWrapper(user.getFullName());
        });
        createAtColumn.setCellValueFactory(new PropertyValueFactory<>("createAt"));

        loadCategories();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterCategories(newValue));
    }

    private void loadCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            categoryList.setAll(categories);
            categoryTable.setItems(categoryList);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load categories.");
        }
    }

    private void filterCategories(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            categoryTable.setItems(categoryList);
        } else {
            ObservableList<Category> filteredList = FXCollections.observableArrayList();
            for (Category category : categoryList) {
                if (category.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    filteredList.add(category);
                }
            }
            categoryTable.setItems(filteredList);
        }
    }

    @FXML
    private void handleAddCategory() {
        Category newCategory = new Category();
        boolean okClicked = showEditDialog(newCategory);
        if (okClicked) {
            try {
                categoryService.addCategory(newCategory);
                loadCategories();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to add category.");
            }
        }
    }

    @FXML
    private void handleEditCategory() {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            boolean okClicked = showEditDialog(selectedCategory);
            if (okClicked) {
                try {
                    categoryService.updateCategory(selectedCategory.getId(), selectedCategory);
                    loadCategories();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to update category.");
                }
            }
        } else {
            showAlert("No Category Selected", "Please select a category to edit.");
        }
    }

    @FXML
    private void handleDeleteCategory() {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            try {
                boolean deleted = categoryService.deleteCategory(selectedCategory.getId());
                if (deleted) {
                    loadCategories();
                } else {
                    showAlert("Delete Failed", "Failed to delete category.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete category.");
            }
        } else {
            showAlert("No Category Selected", "Please select a category to delete.");
        }
    }

    @FXML
    private void handleDetailCategory() {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            showDetailDialog(selectedCategory);
        } else {
            showAlert("No Category Selected", "Please select a category to view details.");
        }
    }

    private void showDetailDialog(Category category) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/market_management/fxml/category_detail.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Category Details");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(categoryTable.getScene().getWindow());
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            CategoryDetailDialog controller = loader.getController();
            controller.setCategory(category);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to show category details.");
        }
    }


    private boolean showEditDialog(Category category) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/market_management/fxml/category_dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Category");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(categoryTable.getScene().getWindow());
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            CategoryDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCategory(category);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
