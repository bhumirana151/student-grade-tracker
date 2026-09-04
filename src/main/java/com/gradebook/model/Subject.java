package com.gradebook.model;

public class Subject {
    private int id;
    private String title;
    private int maxMarks;

    public Subject(int id, String title, int maxMarks) {
        this.id = id;
        this.title = title;
        this.maxMarks = maxMarks;
    }

    public Subject(String title, int maxMarks) {
        this(-1, title, maxMarks);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(int maxMarks) {
        this.maxMarks = maxMarks;
    }

    @Override
    public String toString() {
        return title + " (Max: " + maxMarks + ")";
    }
}
