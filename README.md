# 🎓 Student Grade Tracker - Academic Evaluation Suite

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.2-blue.svg)](https://openjfx.io/)
[![Build](https://img.shields.io/badge/Maven-3.8%2B-brightgreen.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A sleek, self-contained **JavaFX Desktop Application** designed for university faculty and academic evaluators to seamlessly manage student rosters, configure curriculum subjects, record marks in real-time, and compute class analytics. 

Built with a clean, minimalist Light Theme inspired by modern academic platforms (such as Canvas and Google Classroom), featuring 100% in-memory data management powered strictly by Java `ArrayList`s for rubric compliance.

---

## ✨ Key Features

- 📊 **Student Summary Report & Dynamic Matrix**:
  - Dynamically renders columns for all curriculum subjects at runtime.
  - Computes and displays subject-level class averages in each column header (e.g., `Engineering Mathematics (Max: 100 | Avg: 84.5)`).
  - Inline score editing: Double-click any mark cell to edit scores directly with automatic recalculation of student totals and grades.

- 📈 **Rubric-Compliant KPI Analytics Cards**:
  - **👥 Total Enrolled**: Live count of active students.
  - **📊 Class Average (%)**: Overall percentage across all enrolled students.
  - **🏆 Highest Score**: Displays the top percentage along with the student's name (e.g., `96.0% - Priya Patel`).
  - **🚩 Lowest Score**: Displays the lowest percentage along with the student's name (e.g., `56.2% - Vikram Malhotra`).

- 🔍 **Interactive Live Search Filter**:
  - Instant client-side filtering by student name or roll number as the user types in the search bar.

- 🎨 **4-Tier Grade Color Coding & Clean Grid**:
  - **Distinction (`A+` / `A`)**: Bold Green (`#16A34A`)
  - **First Class (`B`)**: Blue (`#2563EB`)
  - **Second Class (`C`)**: Amber / Orange (`#D97706`)
  - **Needs Attention (`D` / `F`)**: Red (`#DC2626`)
  - Spreadsheet zebra striping with soft sky-blue row hover highlights (`#E0F2FE`) and hidden empty grid lines.

- 👨‍🎓 **Manage Student Roster**:
  - Easily enroll new students with full name and roll number.
  - Automatic Roll Number uppercase conversion (e.g. `cs-2024-21` -> `CS-2024-21`).
  - Delete students with automatic cascade cleanup.

- 📚 **Manage Curriculum Subjects**:
  - Add or remove subjects with custom title and max possible marks.
  - Dynamic gradebook column creation and removal.

- 🏛️ **Institutional UI & Branding**:
  - Institutional gold-accented header badge (`Apex Institute of Technology`).
  - Faculty profile card (`Dr. Bhumika, Assistant Professor, CSE`).
  - Academic session info panel (`2025–2026 | Semester IV | CSE Section A`).
  - Footer status bar (`System Status: Ready (In-Memory Mode)`).

---

## 🛠️ Technology Stack

| Technology | Purpose |
| :--- | :--- |
| **Java 17 / JDK 21** | Core Programming Language |
| **JavaFX 21** | Graphical User Interface (FXML + CSS) |
| **Apache Maven** | Dependency Management & Build Automation |
| **Java `ArrayList`** | In-Memory Data Management (`DataManager.java`) |

---

## 📁 Project Structure

```
studentgradetracker/
├── pom.xml                                   # Maven dependencies & build plugins
├── run.bat                                   # Windows 1-click execution script
├── README.md                                 # Project documentation
├── .gitignore                                # Git ignore rules
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       gradebook/
        │           ├── App.java              # Main JavaFX Application launcher
        │           ├── AppMain.java          # Wrapper entry point (module fix)
        │           ├── db/
        │           │   └── DataManager.java  # Singleton in-memory ArrayList data store
        │           ├── model/
        │           │   ├── Student.java      # Student data model
        │           │   ├── Subject.java      # Subject data model
        │           │   ├── Grade.java        # Grade mapping entity
        │           │   └── GradebookRow.java # Dynamic TableView row math model
        │           └── controller/
        │               ├── MainController.java      # Sidebar navigation controller
        │               ├── GradebookController.java # Summary report & matrix controller
        │               ├── RosterController.java    # Student roster controller
        │               └── SubjectController.java   # Subject management controller
        └── resources/
            ├── css/
            │   └── styles.css                # Minimalist Academic Light CSS
            └── fxml/
                ├── main.fxml                 # Master layout (Sidebar + Header + Footer)
                ├── gradebook.fxml            # Student summary report view
                ├── roster.fxml               # Manage roster view
                └── subject.fxml              # Manage subjects view
```

---

## 🚀 Getting Started

### Prerequisites
- **Java Development Kit (JDK 17 or higher)** installed and set in system `PATH`.
- **Apache Maven** installed (or use your IDE's bundled Maven).

### Installation & Run

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/student-grade-tracker.git
   cd student-grade-tracker
   ```

2. **Compile the Code**:
   ```bash
   mvn clean compile
   ```

3. **Launch the Desktop Application**:
   ```bash
   mvn clean javafx:run
   ```
   *Or double-click [`run.bat`](file:///c:/Users/bhumi/OneDrive/Desktop/studentgradetracker/run.bat) on Windows.*

---

## 📄 Pre-populated Seed Roster (20 Students)

The application initializes in memory with 20 pre-configured Indian students across 5 core subjects:
- **Core Subjects**: *Engineering Mathematics*, *Digital Electronics*, *COA*, *Operating System*, *Software Engineering*.
- **Enrolled Roster**: `CS-2024-01` (Aarav Sharma) through `CS-2024-20` (Riya Choudhury).

---

## 📜 License

This project is open-source under the [MIT License](LICENSE).
