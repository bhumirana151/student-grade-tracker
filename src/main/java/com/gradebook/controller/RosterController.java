package com.gradebook.controller;

import com.gradebook.db.DataManager;
import com.gradebook.model.Student;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RosterController implements Initializable {

    @FXML private TextField txtRollNo;
    @FXML private TextField txtName;
    @FXML private Label lblRosterCount;
    @FXML private TableView<Student> rosterTable;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colRollNo;
    @FXML private TableColumn<Student, String> colName;

    private MainController mainController;
    private final ObservableList<Student> studentList = FXCollections.observableArrayList();

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRollNo.setCellValueFactory(new PropertyValueFactory<>("rollNo"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        rosterTable.setItems(studentList);
        loadRoster();
    }

    public void loadRoster() {
        ArrayList<Student> students = DataManager.getInstance().getStudents();
        studentList.setAll(students);
        lblRosterCount.setText("Enrolled Students (" + students.size() + ")");
    }

    @FXML
    private void handleAddStudent() {
        String roll = txtRollNo.getText().trim().toUpperCase();
        String name = txtName.getText().trim();

        if (roll.isEmpty() || name.isEmpty()) {
            showAlert("Input Error", "Please provide both Roll Number and Full Name.");
            return;
        }

        boolean success = DataManager.getInstance().addStudent(roll, name);
        if (success) {
            txtRollNo.clear();
            txtName.clear();
            loadRoster();
            showInfo("Success", "Student '" + name + "' added successfully to memory!");
        } else {
            showAlert("Duplicate Error", "A student with Roll Number '" + roll + "' already exists!");
        }
    }

    @FXML
    private void handleDeleteStudent() {
        Student selected = rosterTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a student from the table to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Student: " + selected.getName() + " (" + selected.getRollNo() + ")");
        confirm.setContentText("Are you sure? This will remove the student from memory.");

        confirm.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                DataManager.getInstance().deleteStudent(selected.getId());
                loadRoster();
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
