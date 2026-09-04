package com.gradebook.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradebookRow {
    private final Student student;
    private final Map<Integer, Double> scores = new HashMap<>(); // subjectId -> score
    private final DoubleProperty totalPercentage = new SimpleDoubleProperty(0.0);
    private final StringProperty letterGrade = new SimpleStringProperty("N/A");

    public GradebookRow(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public String getRollNo() {
        return student.getRollNo();
    }

    public String getStudentName() {
        return student.getName();
    }

    public Double getScore(int subjectId) {
        return scores.getOrDefault(subjectId, 0.0);
    }

    public void setScore(int subjectId, double score) {
        scores.put(subjectId, score);
    }

    public Map<Integer, Double> getScores() {
        return scores;
    }

    public double getTotalPercentage() {
        return totalPercentage.get();
    }

    public DoubleProperty totalPercentageProperty() {
        return totalPercentage;
    }

    public String getLetterGrade() {
        return letterGrade.get();
    }

    public StringProperty letterGradeProperty() {
        return letterGrade;
    }

    public void recalculate(List<Subject> subjects) {
        if (subjects == null || subjects.isEmpty()) {
            totalPercentage.set(0.0);
            letterGrade.set("N/A");
            return;
        }

        double earned = 0.0;
        double possible = 0.0;

        for (Subject s : subjects) {
            double score = scores.getOrDefault(s.getId(), 0.0);
            earned += score;
            possible += s.getMaxMarks();
        }

        if (possible <= 0) {
            totalPercentage.set(0.0);
            letterGrade.set("N/A");
            return;
        }

        double pct = (earned / possible) * 100.0;
        totalPercentage.set(Math.round(pct * 100.0) / 100.0);

        if (pct >= 90.0) {
            letterGrade.set("A+");
        } else if (pct >= 80.0) {
            letterGrade.set("A");
        } else if (pct >= 70.0) {
            letterGrade.set("B");
        } else if (pct >= 60.0) {
            letterGrade.set("C");
        } else if (pct >= 50.0) {
            letterGrade.set("D");
        } else {
            letterGrade.set("F");
        }
    }
}
