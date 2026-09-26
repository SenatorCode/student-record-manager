import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StudentRecordManager extends JFrame {

    private ArrayList<Student> students;

    // Input fields — declared as fields, not local variables, because
    // button-click methods (added later) will need to read from them
    private JTextField nameField;
    private JTextField studentIdField;
    private JTextField departmentField;
    private JTextField courseCodeField;
    private JTextField courseTitleField;
    private JTextField creditUnitField;

    // Dropdown instead of a text field: makes an invalid grade (e.g. "Z")
    // structurally impossible to enter, instead of relying on catching it later.
    private JComboBox<String> gradeComboBox;

    private JTextArea displayArea;

    public StudentRecordManager() {
        students = initializeSampleStudents();

        setTitle("University of Ibadan - Student Record Manager");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildInputPanel(), BorderLayout.NORTH);
        add(buildDisplayPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel buildInputPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));

        panel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        panel.add(studentIdField);

        panel.add(new JLabel("Department:"));
        departmentField = new JTextField();
        panel.add(departmentField);

        panel.add(new JLabel("Course Code:"));
        courseCodeField = new JTextField();
        panel.add(courseCodeField);

        panel.add(new JLabel("Course Title:"));
        courseTitleField = new JTextField();
        panel.add(courseTitleField);

        panel.add(new JLabel("Credit Unit:"));
        creditUnitField = new JTextField();
        panel.add(creditUnitField);

        panel.add(new JLabel("Grade:"));
        gradeComboBox = new JComboBox<>(new String[]{"A", "B", "C", "D", "E", "F"});
        panel.add(gradeComboBox);

        return panel;
    }

    private JScrollPane buildDisplayPanel() {
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        return new JScrollPane(displayArea);
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton registerButton = new JButton("Register Course");
        registerButton.addActionListener(e -> handleRegisterCourse());
        panel.add(registerButton);

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

        JButton editButton = new JButton("Edit Course");
        editButton.addActionListener(e -> handleEditCourse());
        panel.add(editButton);

        return panel;
    }

    private static ArrayList<Student> initializeSampleStudents() {
        ArrayList<Student> students = new ArrayList<>();

        // Student 1: Computer Science
        Student student1 = new Student("John Ade", "250444", "Computer Science");
        student1.registerCourse(new Course("CSC201", "Data Structures", 3, "A"));
        student1.registerCourse(new Course("CSC203", "Computer Architecture", 3, "B"));
        student1.registerCourse(new Course("MTH201", "Mathematics II", 3, "A"));
        student1.registerCourse(new Course("CSC205", "Programming II", 3, "B"));
        student1.registerCourse(new Course("STA201", "Statistics", 2, "C"));
        students.add(student1);

        // Student 2: Mathematics
        Student student2 = new Student("Amaka Obi", "250445", "Mathematics");
        student2.registerCourse(new Course("MTH301", "Real Analysis", 3, "A"));
        student2.registerCourse(new Course("MTH303", "Linear Algebra", 3, "A"));
        student2.registerCourse(new Course("STA301", "Probability Theory", 3, "B"));
        student2.registerCourse(new Course("MTH305", "Abstract Algebra", 3, "C"));
        student2.registerCourse(new Course("CSC301", "Numerical Methods", 2, "B"));
        students.add(student2);

        // Student 3: Physics
        Student student3 = new Student("Tunde Bello", "250446", "Physics");
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

    // Returns null rather than throwing when no match is found — "not found"
    // is a normal, expected outcome here (it may just mean this is a new
    // student), not an error condition. The caller decides what it means.
    private Student findStudentById(String id) {
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }

    private void handleRegisterCourse() {
        String studentId = studentIdField.getText().trim();
        String name = nameField.getText().trim();
        String department = departmentField.getText().trim();
        String courseCode = courseCodeField.getText().trim();
        String courseTitle = courseTitleField.getText().trim();
        String creditUnitText = creditUnitField.getText().trim();
        String grade = (String) gradeComboBox.getSelectedItem();

        if (studentId.isEmpty() || name.isEmpty() || department.isEmpty()
            || courseCode.isEmpty() || courseTitle.isEmpty() || creditUnitText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled in.",
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int creditUnit;
        try {
            creditUnit = Integer.parseInt(creditUnitText);
            // Integer.parseInt succeeds for "0" and negative numbers — it only
            // fails on non-numeric text. "Positive" is a separate business
            // rule, so it needs its own check here.
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

        // Existing student ID -> add to their record. Unknown ID -> treat the
        // typed name/department as a brand-new student being created on the spot.
        Student student = findStudentById(studentId);
        if (student == null) {
            student = new Student(name, studentId, department);
            students.add(student);
        }

        Course course = new Course(courseCode, courseTitle, creditUnit, grade);

        try {
            student.registerCourse(course);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Duplicate Course", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
            courseCode + " registered for " + student.getName() + ".",
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleCalculateCGPA() {
        String studentId = studentIdField.getText().trim();

        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a Student ID to calculate CGPA.",
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = findStudentById(studentId);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "No student found with ID: " + studentId,
                "Student Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double cgpa = student.calculateCGPA();
            JOptionPane.showMessageDialog(this,
                String.format("%s's CGPA is %.2f", student.getName(), cgpa),
                "CGPA Calculated", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException ex) {
            // Thrown by Student.calculateCGPA() when fewer than 5 courses are
            // registered. The model owns the rule; this just presents the failure.
            JOptionPane.showMessageDialog(this, ex.getMessage(),
            "Cannot Calculate CGPA", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDisplayProfile() {
        String studentId = studentIdField.getText().trim();

        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a Student ID to display a profile.",
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = findStudentById(studentId);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "No student found with ID: " + studentId,
                "Student Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        displayArea.setText(buildProfileText(student));
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

        // Header row uses the exact same format string as the data rows below,
        // so columns can't drift out of alignment between the two.
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
            // Same exception as the CGPA button.
            // But here it's shown inline in the profile text rather than as a dialog. 
            // The profile should still render, just showing "not calculated yet" as part of it.
            sb.append(ex.getMessage()).append("\n");
        }

        sb.append("===============================================\n");

        return sb.toString();
    }

    private void handleClearForm() {
        nameField.setText("");
        studentIdField.setText("");
        departmentField.setText("");
        courseCodeField.setText("");
        courseTitleField.setText("");
        creditUnitField.setText("");
        gradeComboBox.setSelectedIndex(0); // No setText() on JComboBox — reset to first option instead
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

    private void handleEditCourse() {
    String studentId = studentIdField.getText().trim();
    String courseCode = courseCodeField.getText().trim();
    String newTitle = courseTitleField.getText().trim();
    String creditUnitText = creditUnitField.getText().trim();
    String newGrade = (String) gradeComboBox.getSelectedItem();

    if (studentId.isEmpty() || courseCode.isEmpty() || newTitle.isEmpty() || creditUnitText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields must be filled in to edit a course.",
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

    Student student = findStudentById(studentId);
    if (student == null) {
        JOptionPane.showMessageDialog(this, "No student found with ID: " + studentId,
            "Student Not Found", JOptionPane.ERROR_MESSAGE);
        return;
    }

    boolean updated = student.editCourse(courseCode, newTitle, newCreditUnit, newGrade);
    if (!updated) {
        JOptionPane.showMessageDialog(this,
            courseCode + " is not registered for this student.",
            "Course Not Found", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JOptionPane.showMessageDialog(this,
        courseCode + " updated for " + student.getName() + ".",
        "Success", JOptionPane.INFORMATION_MESSAGE);
}
}