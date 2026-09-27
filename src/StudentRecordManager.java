import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentRecordManager extends JFrame {

    private ArrayList<Student> students;

    // The single source of truth for "who are we currently looking at."
    // null means "no one selected yet / creating a new student."
    private Student currentStudent;

    private JComboBox<Student> studentSelector;

    private JTextField nameField;
    private JTextField studentIdField;
    private JTextField departmentField;
    private JTextField courseCodeField;
    private JTextField courseTitleField;
    private JTextField creditUnitField;
    private JComboBox<String> gradeComboBox;

    private DefaultTableModel courseTableModel;
    private JTable courseTable;

    private JTextArea displayArea;

    public StudentRecordManager() {
        students = initializeSampleStudents();

        setTitle("University of Ibadan - Student Record Manager");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildInputPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        refreshStudentSelector(); // populates the dropdown with the seeded students
        setVisible(true);
    }

    private JPanel buildInputPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(5, 5));

        JPanel selectorPanel = new JPanel(new BorderLayout(5, 5));
        selectorPanel.add(new JLabel("Select Student:"), BorderLayout.WEST);

        studentSelector = new JComboBox<>();
        // Custom renderer only changes how items are DISPLAYED, not what's
        // stored — the null item still becomes null when selected.
        studentSelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                String text = (value == null) ? "-- New Student --" : value.toString();
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            }
        });
        studentSelector.addActionListener(e -> onStudentSelectionChanged());
        selectorPanel.add(studentSelector, BorderLayout.CENTER);
        wrapper.add(selectorPanel, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridLayout(7, 2, 5, 5));

        fieldsPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        fieldsPanel.add(nameField);

        fieldsPanel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        fieldsPanel.add(studentIdField);

        fieldsPanel.add(new JLabel("Department:"));
        departmentField = new JTextField();
        fieldsPanel.add(departmentField);

        fieldsPanel.add(new JLabel("Course Code:"));
        courseCodeField = new JTextField();
        fieldsPanel.add(courseCodeField);

        fieldsPanel.add(new JLabel("Course Title:"));
        courseTitleField = new JTextField();
        fieldsPanel.add(courseTitleField);

        fieldsPanel.add(new JLabel("Credit Unit:"));
        creditUnitField = new JTextField();
        fieldsPanel.add(creditUnitField);

        fieldsPanel.add(new JLabel("Grade:"));
        gradeComboBox = new JComboBox<>(new String[]{"A", "B", "C", "D", "E", "F"});
        fieldsPanel.add(gradeComboBox);

        wrapper.add(fieldsPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private JSplitPane buildCenterPanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.add(new JLabel("Registered Courses (click a row to edit):"), BorderLayout.NORTH);

        String[] columns = {"Course Code", "Course Title", "Credit Unit", "Grade"};
        courseTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Read-only: edits must go through the form + Edit Course
                // button, where validation actually happens.
                return false;
            }
        };
        courseTable = new JTable(courseTableModel);
        courseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onCourseRowSelected();
            }
        });
        tablePanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);

        JPanel displayPanel = new JPanel(new BorderLayout(5, 5));
        displayPanel.add(new JLabel("Student Profile:"), BorderLayout.NORTH);
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        displayPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, displayPanel);
        splitPane.setResizeWeight(0.4); // table gets ~40% of the width, profile the rest
        return splitPane;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton registerButton = new JButton("Register Course");
        registerButton.addActionListener(e -> handleRegisterCourse());
        panel.add(registerButton);

        JButton editButton = new JButton("Edit Course");
        editButton.addActionListener(e -> handleEditCourse());
        panel.add(editButton);

        JButton cgpaButton = new JButton("Calculate CGPA");
        cgpaButton.addActionListener(e -> handleCalculateCGPA());
        panel.add(cgpaButton);

        JButton displayButton = new JButton("Display Profile");
        displayButton.addActionListener(e -> handleDisplayProfile());
        panel.add(displayButton);

        JButton clearButton = new JButton("Clear Form");
        clearButton.addActionListener(e -> handleClearForm());
        panel.add(clearButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> handleExit());
        panel.add(exitButton);

        return panel;
    }

    private static ArrayList<Student> initializeSampleStudents() {
        ArrayList<Student> students = new ArrayList<>();

        Student student1 = new Student("John Ade", "UI/CSC/2026/001", "Computer Science");
        student1.registerCourse(new Course("CSC201", "Data Structures", 3, "A"));
        student1.registerCourse(new Course("CSC203", "Computer Architecture", 3, "B"));
        student1.registerCourse(new Course("MTH201", "Mathematics II", 3, "A"));
        student1.registerCourse(new Course("CSC205", "Programming II", 3, "B"));
        student1.registerCourse(new Course("STA201", "Statistics", 2, "C"));
        students.add(student1);

        Student student2 = new Student("Amaka Obi", "UI/MTH/2026/002", "Mathematics");
        student2.registerCourse(new Course("MTH301", "Real Analysis", 3, "A"));
        student2.registerCourse(new Course("MTH303", "Linear Algebra", 3, "A"));
        student2.registerCourse(new Course("STA301", "Probability Theory", 3, "B"));
        student2.registerCourse(new Course("MTH305", "Abstract Algebra", 3, "C"));
        student2.registerCourse(new Course("CSC301", "Numerical Methods", 2, "B"));
        students.add(student2);

        Student student3 = new Student("Tunde Bello", "UI/PHY/2026/003", "Physics");
        student3.registerCourse(new Course("PHY201", "Classical Mechanics", 3, "B"));
        student3.registerCourse(new Course("PHY203", "Thermodynamics", 3, "A"));
        student3.registerCourse(new Course("MTH201", "Mathematics II", 3, "B"));
        student3.registerCourse(new Course("PHY205", "Electromagnetism", 3, "C"));
        student3.registerCourse(new Course("CSC101", "Intro to Programming", 2, "A"));
        students.add(student3);

        return students;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRecordManager());
    }

    private Student findStudentById(String id) {
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }

    // Rebuilds the dropdown's contents from the students list. Called on
    // startup and any time a new student is added, so the dropdown always
    // reflects what's actually in `students`.
    private void refreshStudentSelector() {
        studentSelector.removeAllItems();
        studentSelector.addItem(null); // "-- New Student --"
        for (Student s : students) {
            studentSelector.addItem(s);
        }
    }

    // Fires whenever the dropdown selection changes — this is the single
    // place responsible for keeping currentStudent, the identity fields,
    // and the course table all in sync with each other.
    private void onStudentSelectionChanged() {
        currentStudent = (Student) studentSelector.getSelectedItem();
        boolean isNewStudent = (currentStudent == null);

        nameField.setEditable(isNewStudent);
        studentIdField.setEditable(isNewStudent);
        departmentField.setEditable(isNewStudent);

        if (isNewStudent) {
            nameField.setText("");
            studentIdField.setText("");
            departmentField.setText("");
        } else {
            nameField.setText(currentStudent.getName());
            studentIdField.setText(currentStudent.getStudentId());
            departmentField.setText(currentStudent.getDepartment());
        }

        refreshCourseTable();
        clearCourseEntryFields();
    }

    private void refreshCourseTable() {
        courseTableModel.setRowCount(0); // clear existing rows
        if (currentStudent == null) return;

        for (Course c : currentStudent.getCourses()) {
            courseTableModel.addRow(new Object[]{
                c.getCourseCode(), c.getCourseTitle(), c.getCreditUnit(), c.getGrade()
            });
        }
    }

    // Pre-fills the course entry fields from whichever row was clicked, so
    // "Edit Course" has something to work with without retyping the code.
    private void onCourseRowSelected() {
        int row = courseTable.getSelectedRow();
        if (row == -1 || currentStudent == null) return;

        String code = (String) courseTableModel.getValueAt(row, 0);
        Course match = null;
        for (Course c : currentStudent.getCourses()) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {
                match = c;
                break;
            }
        }
        if (match == null) return;

        courseCodeField.setText(match.getCourseCode());
        courseTitleField.setText(match.getCourseTitle());
        creditUnitField.setText(String.valueOf(match.getCreditUnit()));
        gradeComboBox.setSelectedItem(match.getGrade());
    }

    private void clearCourseEntryFields() {
        courseCodeField.setText("");
        courseTitleField.setText("");
        creditUnitField.setText("");
        gradeComboBox.setSelectedIndex(0);
    }

    private void handleRegisterCourse() {
        String courseCode = courseCodeField.getText().trim();
        String courseTitle = courseTitleField.getText().trim();
        String creditUnitText = creditUnitField.getText().trim();
        String grade = (String) gradeComboBox.getSelectedItem();

        if (currentStudent == null) {
            currentStudent = createNewStudentFromForm();
            if (currentStudent == null) {
                return; // validation failed; createNewStudentFromForm already showed a dialog
            }
        }

        if (courseCode.isEmpty() || courseTitle.isEmpty() || creditUnitText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All course fields must be filled in.",
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int creditUnit;
        try {
            creditUnit = Integer.parseInt(creditUnitText);
            if (creditUnit <= 0) {
                JOptionPane.showMessageDialog(this, "Credit unit must be a positive number.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credit unit must be a whole number.",
                "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Course course = new Course(courseCode, courseTitle, creditUnit, grade);
        try {
            currentStudent.registerCourse(course);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Duplicate Course", JOptionPane.WARNING_MESSAGE);
            return;
        }

        refreshCourseTable();
        clearCourseEntryFields();
        JOptionPane.showMessageDialog(this,
            courseCode + " registered for " + currentStudent.getName() + ".",
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Builds a new Student from the identity fields and adds them to both
    // `students` and the dropdown. Returns null (having already shown a
    // dialog) if the fields are invalid — same "null means don't proceed"
    // convention as findStudentById.
    private Student createNewStudentFromForm() {
        String name = nameField.getText().trim();
        String studentId = studentIdField.getText().trim();
        String department = departmentField.getText().trim();

        if (name.isEmpty() || studentId.isEmpty() || department.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the new student's name, ID, and department first.",
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (findStudentById(studentId) != null) {
            JOptionPane.showMessageDialog(this,
                "A student with this ID already exists. Select them from the dropdown instead.",
                "Duplicate Student", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Student newStudent = new Student(name, studentId, department);
        students.add(newStudent);
        refreshStudentSelector();
        studentSelector.setSelectedItem(newStudent);
        return newStudent;
    }

    private void handleEditCourse() {
        if (currentStudent == null) {
            JOptionPane.showMessageDialog(this, "Select a student first.",
                "No Student Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courseCode = courseCodeField.getText().trim();
        String newTitle = courseTitleField.getText().trim();
        String creditUnitText = creditUnitField.getText().trim();
        String newGrade = (String) gradeComboBox.getSelectedItem();

        if (courseCode.isEmpty() || newTitle.isEmpty() || creditUnitText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Click a course row, or fill in all fields, to edit a course.",
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int newCreditUnit;
        try {
            newCreditUnit = Integer.parseInt(creditUnitText);
            if (newCreditUnit <= 0) {
                JOptionPane.showMessageDialog(this, "Credit unit must be a positive number.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credit unit must be a whole number.",
                "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean updated = currentStudent.editCourse(courseCode, newTitle, newCreditUnit, newGrade);
        if (!updated) {
            JOptionPane.showMessageDialog(this, courseCode + " is not registered for this student.",
                "Course Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        refreshCourseTable();
        clearCourseEntryFields();
        JOptionPane.showMessageDialog(this,
            courseCode + " updated for " + currentStudent.getName() + ".",
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleCalculateCGPA() {
        if (currentStudent == null) {
            JOptionPane.showMessageDialog(this, "Select a student first.",
                "No Student Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double cgpa = currentStudent.calculateCGPA();
            JOptionPane.showMessageDialog(this,
                String.format("%s's CGPA is %.2f", currentStudent.getName(), cgpa),
                "CGPA Calculated", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Cannot Calculate CGPA", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDisplayProfile() {
        if (currentStudent == null) {
            JOptionPane.showMessageDialog(this, "Select a student first.",
                "No Student Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        displayArea.setText(buildProfileText(currentStudent));
    }

    private String buildProfileText(Student student) {
        StringBuilder sb = new StringBuilder();

        sb.append("===============================================\n");
        sb.append("UNIVERSITY OF IBADAN - FACULTY OF COMPUTING\n");
        sb.append("STUDENT PROFILE\n");
        sb.append("===============================================\n");
        sb.append(String.format("Student ID : %s%n", student.getStudentId()));
        sb.append(String.format("Name       : %s%n", student.getName()));
        sb.append(String.format("Department : %s%n", student.getDepartment()));
        sb.append(String.format("Courses Registered: %d%n", student.getCourseCount()));
        sb.append("---------------------------------------------------------------\n");
        sb.append(String.format("%-12s %-25s %-5s %-5s%n", "Course Code", "Course Title", "CU", "Grade"));
        sb.append("---------------------------------------------------------------\n");

        int totalCreditUnits = 0;
        for (Course course : student.getCourses()) {
            sb.append(String.format("%-12s %-25s %-5d %-5s%n",
                course.getCourseCode(),
                course.getCourseTitle(),
                course.getCreditUnit(),
                course.getGrade()));
            totalCreditUnits += course.getCreditUnit();
        }

        sb.append("---------------------------------------------------------------\n");
        sb.append(String.format("Total Credit Units : %d%n", totalCreditUnits));

        try {
            double cgpa = student.calculateCGPA();
            sb.append(String.format("CGPA : %.2f%n", cgpa));
        } catch (IllegalStateException ex) {
            sb.append(ex.getMessage()).append("\n");
        }

        sb.append("===============================================\n");
        return sb.toString();
    }

    private void handleClearForm() {
        // Resetting the selector to "-- New Student --" (index 0) triggers
        // onStudentSelectionChanged(), which clears every field and the
        // table for us — no need to duplicate that logic here.
        studentSelector.setSelectedIndex(0);
    }

    private void handleExit() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}