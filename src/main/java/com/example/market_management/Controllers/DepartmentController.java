package com.example.market_management.Controllers;

import com.example.market_management.Dialogs.DepartmentDialog;
import com.example.market_management.Models.Department;
import com.example.market_management.Services.DepartmentService;
import com.example.market_management.Services.DepartmentSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.IOException;
import java.util.List;

public class DepartmentController {
    @FXML
    private TableView<Department> departmentTable;
    @FXML
    private TableColumn<Department, Integer> idColumn;
    @FXML
    private TableColumn<Department, String> nameColumn;
    @FXML
    private TableColumn<Department, String> descriptionColumn;
    @FXML
    private TableColumn<Department, String> createAtColumn;
    @FXML
    private TextField searchField;

    private DepartmentService departmentService = new DepartmentService();
    private ObservableList<Department> departmentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        createAtColumn.setCellValueFactory(new PropertyValueFactory<>("createAt"));

        loadDepartments();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterDepartments(newValue));
    }

    private void loadDepartments() {
        try {
            List<Department> departments = departmentService.getAllDepartments();
            departmentList.setAll(departments);
            DepartmentSingleton.getInstance().setDepartments(departments);
            departmentTable.setItems(departmentList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterDepartments(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            departmentTable.setItems(departmentList);
        } else {
            ObservableList<Department> filteredList = FXCollections.observableArrayList();
            for (Department department : departmentList) {
                if (department.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    filteredList.add(department);
                }
            }
            departmentTable.setItems(filteredList);
        }
    }

    @FXML
    private void handleAddDepartment() {
        Department newDepartment = new Department();
        boolean okClicked = showEditDialog(newDepartment);
        if (okClicked) {
            try{
                departmentService.addDepartment(newDepartment);
                loadDepartments();
            }catch (IOException e){
                e.printStackTrace();
                showAlert("Error", "Failed to add department.");
            }
        }
    }

    @FXML
    private void handleEditDepartment() {
        Department selectedDepartment = departmentTable.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            boolean okClicked = showEditDialog(selectedDepartment);
            if (okClicked) {
                try {
                    departmentService.updateDepartment(selectedDepartment.getId(), selectedDepartment);
                    loadDepartments();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to update department.");
                }
            }
        } else {
            showAlert("No Department Selected", "Please select a department to edit.");
        }
    }

    @FXML
    private void handleDeleteDepartment() {
        Department selectedDepartment = departmentTable.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            try {
                boolean deleted = departmentService.deleteDepartment(selectedDepartment.getId());
                if (deleted) {
                    loadDepartments();
                } else {
                    showAlert("Delete Failed", "Failed to delete department.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete department.");
            }
        } else {
            showAlert("No Department Selected", "Please select a department to delete.");
        }
    }

    private boolean showEditDialog(Department department) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/market_management/fxml/department_dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Department");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(departmentTable.getScene().getWindow());
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            DepartmentDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setDepartment(department);

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
