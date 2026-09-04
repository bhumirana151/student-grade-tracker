package com.gradebook.controller;

import com.gradebook.db.DataManager;
import com.gradebook.model.Subject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SubjectController implements Initializable {

    @FXML private TextField txtTitle;
    @FXML private TextField txtMaxMarks;
    @FXML private Label lblSubjectCount;
    @FXML private TableView<Subject> subjectTable;
    @FXML private TableColumn<Subject, Integer> colId;
    @FXML private TableColumn<Subject, String> colTitle;
    @FXML private TableColumn<Subject, Integer> colMaxMarks;

    private MainController mainController;
    private final ObservableList<Subject> subjectList = FXCollections.observableArrayList();

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colMaxMarks.setCellValueFactory(new PropertyValueFactory<>("maxMarks"));

        subjectTable.setItems(subjectList);
        loadSubjects();
    }

    public void loadSubjects() {
        ArrayList<Subject> subjects = DataManager.getInstance().getSubjects();
        subjectList.setAll(subjects);
        lblSubjectCount.setText("Curriculum Subjects (" + subjects.size() + ")");
    }

    @FXML
    private void handleAddSubject() {
        String title = txtTitle.getText().trim();
        String maxMarksStr = txtMaxMarks.getText().trim();

        if (title.isEmpty() || maxMarksStr.isEmpty()) {
            showAlert("Input Error", "Please provide both Subject Title and Max Marks.");
            return;
        }

        int maxMarks;
        try {
            maxMarks = Integer.parseInt(maxMarksStr);
            if (maxMarks <= 0) {
                showAlert("Input Error", "Max marks must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Max marks must be a valid integer.");
            return;
        }

        boolean success = DataManager.getInstance().addSubject(title, maxMarks);
        if (success) {
            txtTitle.clear();
            txtMaxMarks.clear();
            loadSubjects();
            showInfo("Success", "Subject '" + title + "' added successfully to memory!");
        }
    }

    @FXML
    private void handleDeleteSubject() {
        Subject selected = subjectTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a subject from the table to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Subject: " + selected.getTitle() + " (Max: " + selected.getMaxMarks() + ")");
        confirm.setContentText("Are you sure? This will remove the subject from memory.");

        confirm.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                DataManager.getInstance().deleteSubject(selected.getId());
                loadSubjects();
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
