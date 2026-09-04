package com.gradebook.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private Button btnDashboard;
    @FXML private Button btnRoster;
    @FXML private Button btnSubjects;
    @FXML private StackPane contentArea;

    private Parent dashboardView;
    private Parent rosterView;
    private Parent subjectView;

    private GradebookController gradebookController;
    private RosterController rosterController;
    private SubjectController subjectController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadViews();
        showDashboard();
    }

    private void loadViews() {
        try {
            FXMLLoader gLoader = new FXMLLoader(getClass().getResource("/fxml/gradebook.fxml"));
            dashboardView = gLoader.load();
            gradebookController = gLoader.getController();

            FXMLLoader rLoader = new FXMLLoader(getClass().getResource("/fxml/roster.fxml"));
            rosterView = rLoader.load();
            rosterController = rLoader.getController();
            rosterController.setMainController(this);

            FXMLLoader sLoader = new FXMLLoader(getClass().getResource("/fxml/subject.fxml"));
            subjectView = sLoader.load();
            subjectController = sLoader.getController();
            subjectController.setMainController(this);

        } catch (IOException e) {
            System.err.println("Error loading FXML views: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void showDashboard() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(dashboardView);
        updateActiveButton(btnDashboard);
        if (gradebookController != null) {
            gradebookController.refreshGradebook();
        }
    }

    @FXML
    public void showRoster() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(rosterView);
        updateActiveButton(btnRoster);
        if (rosterController != null) {
            rosterController.loadRoster();
        }
    }

    @FXML
    public void showSubjects() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(subjectView);
        updateActiveButton(btnSubjects);
        if (subjectController != null) {
            subjectController.loadSubjects();
        }
    }

    private void updateActiveButton(Button activeButton) {
        btnDashboard.getStyleClass().remove("active");
        btnRoster.getStyleClass().remove("active");
        btnSubjects.getStyleClass().remove("active");

        if (!activeButton.getStyleClass().contains("active")) {
            activeButton.getStyleClass().add("active");
        }
    }
}
