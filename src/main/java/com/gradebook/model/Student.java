package com.gradebook.model;

public class Student {
    private int id;
    private String rollNo;
    private String name;

    public Student(int id, String rollNo, String name) {
        this.id = id;
        this.rollNo = rollNo;
        this.name = name;
    }

    public Student(String rollNo, String name) {
        this(-1, rollNo, name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " (" + rollNo + ")";
    }
}
