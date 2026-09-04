package com.gradebook.model;

public class Grade {
    private int studentId;
    private int subjectId;
    private double score;

    public Grade(int studentId, int subjectId, double score) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.score = score;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
