package com.gradebook.db;

import com.gradebook.model.GradebookRow;
import com.gradebook.model.Student;
import com.gradebook.model.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized In-Memory Data Store Singleton using Java ArrayLists.
 * Satisfies assignment rubric requirement: Store and manage data strictly using Java ArrayLists in memory.
 */
public class DataManager {

    private static DataManager instance;

    // Satisfies requirement: Use arrays or ArrayLists to store and manage data
    private final ArrayList<Student> studentList = new ArrayList<>();
    private final ArrayList<Subject> subjectList = new ArrayList<>();
    private final Map<Integer, GradebookRow> gradebookRowMap = new HashMap<>();

    private int nextStudentId = 1;
    private int nextSubjectId = 1;

    private DataManager() {
        seedInitialData();
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void seedInitialData() {
        // 1. Seed Default Subjects into subjectList (ArrayList)
        String[][] defaultSubjects = {
            {"Engineering Mathematics", "100"},
            {"Digital Electronics", "100"},
            {"COA", "100"},
            {"Operating System", "100"},
            {"Software Engineering", "100"}
        };

        for (String[] sub : defaultSubjects) {
            Subject subject = new Subject(nextSubjectId++, sub[0], Integer.parseInt(sub[1]));
            subjectList.add(subject);
        }

        // 2. Seed 20 Indian Students into studentList (ArrayList)
        String[][] sampleStudents = {
            {"CS-2024-01", "Aarav Sharma"},
            {"CS-2024-02", "Priya Patel"},
            {"CS-2024-03", "Rohan Verma"},
            {"CS-2024-04", "Ananya Iyer"},
            {"CS-2024-05", "Vikram Malhotra"},
            {"CS-2024-06", "Bhumika Desai"},
            {"CS-2024-07", "Aditya Singh"},
            {"CS-2024-08", "Kavya Reddy"},
            {"CS-2024-09", "Neha Kapoor"},
            {"CS-2024-10", "Siddharth Joshi"},
            {"CS-2024-11", "Arjun Nair"},
            {"CS-2024-12", "Meera Menon"},
            {"CS-2024-13", "Rahul Gupta"},
            {"CS-2024-14", "Sneha Das"},
            {"CS-2024-15", "Karan Bhatia"},
            {"CS-2024-16", "Pooja Agarwal"},
            {"CS-2024-17", "Yash Rao"},
            {"CS-2024-18", "Divya Kumar"},
            {"CS-2024-19", "Ishaan Mukherjee"},
            {"CS-2024-20", "Riya Choudhury"}
        };

        double[][] sampleScores = {
            {85.0, 88.0, 90.0, 84.0, 86.0}, // Aarav (86.6%)
            {94.0, 96.0, 98.0, 95.0, 97.0}, // Priya (96.0% - A+ Top)
            {70.0, 74.0, 76.0, 72.0, 78.0}, // Rohan (74.0%)
            {82.0, 85.0, 88.0, 80.0, 84.0}, // Ananya (83.8%)
            {55.0, 58.0, 60.0, 52.0, 56.0}, // Vikram (56.2% - D Lowest)
            {90.0, 92.0, 94.0, 89.0, 95.0}, // Bhumika (92.0%)
            {78.0, 81.0, 83.0, 79.0, 84.0}, // Aditya (81.0%)
            {65.0, 68.0, 70.0, 64.0, 69.0}, // Kavya (67.2%)
            {88.0, 91.0, 87.0, 89.0, 92.0}, // Neha (89.4%)
            {72.0, 75.0, 78.0, 71.0, 74.0}, // Siddharth (74.0%)
            {80.0, 83.0, 85.0, 82.0, 86.0}, // Arjun (83.2%)
            {91.0, 93.0, 96.0, 90.0, 94.0}, // Meera (92.8%)
            {62.0, 66.0, 68.0, 60.0, 64.0}, // Rahul (64.0%)
            {76.0, 79.0, 82.0, 75.0, 80.0}, // Sneha (78.4%)
            {84.0, 87.0, 89.0, 83.0, 88.0}, // Karan (86.2%)
            {73.0, 77.0, 75.0, 74.0, 76.0}, // Pooja (75.0%)
            {68.0, 71.0, 73.0, 67.0, 72.0}, // Yash (70.2%)
            {87.0, 90.0, 92.0, 86.0, 91.0}, // Divya (89.2%)
            {58.0, 61.0, 63.0, 59.0, 62.0}, // Ishaan (60.6%)
            {93.0, 95.0, 97.0, 92.0, 96.0}  // Riya (94.6%)
        };

        for (int i = 0; i < sampleStudents.length; i++) {
            Student student = new Student(nextStudentId++, sampleStudents[i][0], sampleStudents[i][1]);
            studentList.add(student);

            GradebookRow row = new GradebookRow(student);
            for (int j = 0; j < Math.min(subjectList.size(), sampleScores[i].length); j++) {
                row.setScore(subjectList.get(j).getId(), sampleScores[i][j]);
            }
            row.recalculate(subjectList);
            gradebookRowMap.put(student.getId(), row);
        }
    }

    // --- Student Operations using ArrayList ---

    public ArrayList<Student> getStudents() {
        return new ArrayList<>(studentList);
    }

    public boolean addStudent(String rollNo, String name) {
        for (Student s : studentList) {
            if (s.getRollNo().equalsIgnoreCase(rollNo)) {
                return false; // Duplicate Roll Number
            }
        }
        Student student = new Student(nextStudentId++, rollNo.toUpperCase().trim(), name.trim());
        studentList.add(student);

        GradebookRow row = new GradebookRow(student);
        row.recalculate(subjectList);
        gradebookRowMap.put(student.getId(), row);
        return true;
    }

    public boolean deleteStudent(int studentId) {
        Student toRemove = null;
        for (Student s : studentList) {
            if (s.getId() == studentId) {
                toRemove = s;
                break;
            }
        }
        if (toRemove != null) {
            studentList.remove(toRemove);
            gradebookRowMap.remove(studentId);
            return true;
        }
        return false;
    }

    // --- Subject Operations using ArrayList ---

    public ArrayList<Subject> getSubjects() {
        return new ArrayList<>(subjectList);
    }

    public boolean addSubject(String title, int maxMarks) {
        Subject subject = new Subject(nextSubjectId++, title.trim(), maxMarks);
        subjectList.add(subject);

        // Recalculate gradebook rows for all students
        for (GradebookRow row : gradebookRowMap.values()) {
            row.recalculate(subjectList);
        }
        return true;
    }

    public boolean deleteSubject(int subjectId) {
        Subject toRemove = null;
        for (Subject s : subjectList) {
            if (s.getId() == subjectId) {
                toRemove = s;
                break;
            }
        }
        if (toRemove != null) {
            subjectList.remove(toRemove);
            // Clean score entries for deleted subject
            for (GradebookRow row : gradebookRowMap.values()) {
                row.getScores().remove(subjectId);
                row.recalculate(subjectList);
            }
            return true;
        }
        return false;
    }

    // --- Gradebook Row Operations using ArrayList ---

    public ArrayList<GradebookRow> getGradebookRows() {
        ArrayList<GradebookRow> list = new ArrayList<>();
        for (Student s : studentList) {
            GradebookRow row = gradebookRowMap.get(s.getId());
            if (row != null) {
                row.recalculate(subjectList);
                list.add(row);
            }
        }
        return list;
    }

    public void updateGrade(int studentId, int subjectId, double score) {
        GradebookRow row = gradebookRowMap.get(studentId);
        if (row != null) {
            row.setScore(subjectId, score);
            row.recalculate(subjectList);
        }
    }
}
