package com.gradebook.controller;

import com.gradebook.db.DataManager;
import com.gradebook.model.GradebookRow;
import com.gradebook.model.Subject;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.util.*;

public class GradebookController implements Initializable {

    @FXML private Label lblTotalEnrolled;
    @FXML private Label lblClassAverage;
    @FXML private Label lblHighestScore;
    @FXML private Label lblHighestStudent;
    @FXML private Label lblLowestScore;
    @FXML private Label lblLowestStudent;

    @FXML private TextField txtSearch;
    @FXML private TableView<GradebookRow> gradebookTable;

    private final ObservableList<GradebookRow> rowData = FXCollections.observableArrayList();
    private FilteredList<GradebookRow> filteredData;
    private ArrayList<Subject> currentSubjects = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filteredData = new FilteredList<>(rowData, p -> true);
        if (txtSearch != null) {
            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(row -> {
                    if (newValue == null || newValue.trim().isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase().trim();
                    if (row.getStudentName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return row.getRollNo().toLowerCase().contains(lowerCaseFilter);
                });
            });
        }

        refreshGradebook();
    }

    public void refreshGradebook() {
        DataManager dataManager = DataManager.getInstance();
        currentSubjects = dataManager.getSubjects();
        ArrayList<GradebookRow> rows = dataManager.getGradebookRows();

        rowData.setAll(rows);
        buildColumns();
        gradebookTable.setItems(filteredData);
        updateKpis();
    }

    @SuppressWarnings("unchecked")
    private void buildColumns() {
        gradebookTable.getColumns().clear();

        // 1. Roll No Column
        TableColumn<GradebookRow, String> colRollNo = new TableColumn<>("Roll No");
        colRollNo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRollNo()));
        colRollNo.setMinWidth(110);
        colRollNo.setEditable(false);

        // 2. Student Name Column
        TableColumn<GradebookRow, String> colName = new TableColumn<>("Student Name");
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudentName()));
        colName.setMinWidth(170);
        colName.setEditable(false);

        gradebookTable.getColumns().addAll(colRollNo, colName);

        // Calculate subject averages across all students using ArrayList
        Map<Integer, Double> subjectAvgMap = new HashMap<>();
        if (!rowData.isEmpty()) {
            for (Subject subject : currentSubjects) {
                double sum = 0.0;
                for (GradebookRow row : rowData) {
                    sum += row.getScore(subject.getId());
                }
                subjectAvgMap.put(subject.getId(), sum / rowData.size());
            }
        }

        // 3. Dynamic Subject Columns
        for (Subject subject : currentSubjects) {
            double avg = subjectAvgMap.getOrDefault(subject.getId(), 0.0);
            String headerText = String.format("%s\n(Max: %d | Avg: %.1f)", subject.getTitle(), subject.getMaxMarks(), avg);

            TableColumn<GradebookRow, Double> colSub = new TableColumn<>(headerText);
            colSub.setMinWidth(150);
            colSub.setEditable(true);

            colSub.setCellValueFactory(cellData -> 
                new SimpleObjectProperty<>(cellData.getValue().getScore(subject.getId()))
            );

            colSub.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter() {
                @Override
                public Double fromString(String value) {
                    try {
                        return super.fromString(value);
                    } catch (Exception e) {
                        return 0.0;
                    }
                }
            }));

            colSub.setOnEditCommit(event -> {
                GradebookRow row = event.getRowValue();
                Double newScore = event.getNewValue();

                if (newScore == null) newScore = 0.0;
                if (newScore < 0) newScore = 0.0;
                if (newScore > subject.getMaxMarks()) {
                    newScore = (double) subject.getMaxMarks();
                    showAlert("Marks Capped", "Marks entered exceed maximum allowed (" + subject.getMaxMarks() + "). Capped to max marks.");
                }

                // Update in-memory data manager
                DataManager.getInstance().updateGrade(row.getStudent().getId(), subject.getId(), newScore);
                refreshGradebook();
            });

            gradebookTable.getColumns().add(colSub);
        }

        // 4. Fixed Total % Column
        TableColumn<GradebookRow, String> colTotalPct = new TableColumn<>("Total %");
        colTotalPct.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.format("%.2f%%", cellData.getValue().getTotalPercentage()))
        );
        colTotalPct.setMinWidth(90);
        colTotalPct.setEditable(false);

        // 5. Fixed Letter Grade Column with 4-Tier Color Coding
        TableColumn<GradebookRow, String> colGrade = new TableColumn<>("Grade");
        colGrade.setCellValueFactory(cellData -> cellData.getValue().letterGradeProperty());
        colGrade.setMinWidth(80);
        colGrade.setEditable(false);
        colGrade.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    getStyleClass().removeAll("grade-a", "grade-b", "grade-c", "grade-d-f");
                } else {
                    setText(item);
                    getStyleClass().removeAll("grade-a", "grade-b", "grade-c", "grade-d-f");
                    if (item.equals("A+") || item.equals("A")) {
                        getStyleClass().add("grade-a");
                    } else if (item.equals("B")) {
                        getStyleClass().add("grade-b");
                    } else if (item.equals("C")) {
                        getStyleClass().add("grade-c");
                    } else if (item.equals("D") || item.equals("F")) {
                        getStyleClass().add("grade-d-f");
                    }
                }
            }
        });

        gradebookTable.getColumns().addAll(colTotalPct, colGrade);
    }

    private void updateKpis() {
        int studentCount = rowData.size();
        lblTotalEnrolled.setText(String.valueOf(studentCount));

        if (studentCount == 0 || currentSubjects.isEmpty()) {
            lblClassAverage.setText("0.0%");
            lblHighestScore.setText("N/A");
            lblHighestStudent.setText("No student data");
            lblLowestScore.setText("N/A");
            lblLowestStudent.setText("No student data");
            return;
        }

        // Satisfies requirement: Calculate average, highest, and lowest scores using ArrayLists
        ArrayList<GradebookRow> studentRowList = new ArrayList<>(rowData);
        ArrayList<Double> percentageList = new ArrayList<>();
        for (GradebookRow row : studentRowList) {
            percentageList.add(row.getTotalPercentage());
        }

        double avg = calculateClassAverage(percentageList);
        lblClassAverage.setText(String.format("%.1f%%", avg));

        GradebookRow highestRow = findHighestScorer(studentRowList);
        if (highestRow != null) {
            lblHighestScore.setText(String.format("%.1f%%", highestRow.getTotalPercentage()));
            lblHighestStudent.setText(highestRow.getStudentName());
        }

        GradebookRow lowestRow = findLowestScorer(studentRowList);
        if (lowestRow != null) {
            lblLowestScore.setText(String.format("%.1f%%", lowestRow.getTotalPercentage()));
            lblLowestStudent.setText(lowestRow.getStudentName());
        }
    }

    // Satisfies requirement: Calculate average, highest, and lowest scores using ArrayLists
    private double calculateClassAverage(ArrayList<Double> percentageScores) {
        if (percentageScores == null || percentageScores.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Double score : percentageScores) {
            sum += score;
        }
        return sum / percentageScores.size();
    }

    // Satisfies requirement: Calculate average, highest, and lowest scores using ArrayLists
    private GradebookRow findHighestScorer(ArrayList<GradebookRow> studentRows) {
        if (studentRows == null || studentRows.isEmpty()) {
            return null;
        }
        GradebookRow highest = studentRows.get(0);
        for (int i = 1; i < studentRows.size(); i++) {
            if (studentRows.get(i).getTotalPercentage() > highest.getTotalPercentage()) {
                highest = studentRows.get(i);
            }
        }
        return highest;
    }

    // Satisfies requirement: Calculate average, highest, and lowest scores using ArrayLists
    private GradebookRow findLowestScorer(ArrayList<GradebookRow> studentRows) {
        if (studentRows == null || studentRows.isEmpty()) {
            return null;
        }
        GradebookRow lowest = studentRows.get(0);
        for (int i = 1; i < studentRows.size(); i++) {
            if (studentRows.get(i).getTotalPercentage() < lowest.getTotalPercentage()) {
                lowest = studentRows.get(i);
            }
        }
        return lowest;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
