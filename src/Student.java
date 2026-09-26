import java.util.ArrayList;

public class Student extends Person {
    private String studentId;
    private String department;
    private ArrayList<Course> courses;

    public Student(String name, String studentId, String department) {
        // Person's name field is private, so it can only be set through Person's own constructor
        // Student can't reach in and assign it directly.
        super(name);
        this.studentId = studentId;
        this.department = department;
        this.courses = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    // Returns true if this student already has a course with this code —
    // case-insensitive, since "csc201" and "CSC201" should be treated as the same course.
    public boolean hasCourse(String courseCode) {
        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(courseCode)) {
                return true;
            }
        }
        return false;
    }

    public void registerCourse(Course course) throws IllegalArgumentException {
        if (hasCourse(course.getCourseCode())) {
            throw new IllegalArgumentException(
                course.getCourseCode() + " is already registered for this student."
            );
        }
        courses.add(course);
    }

    public int getCourseCount() {
        return courses.size();
    }

    // Throws an error rather than returning a sentinel value (like -1) 
    // so the failure can't be silently ignored, and callers are forced to decide how to handle it (dialog box, inline message, etc).
    public double calculateCGPA() throws IllegalStateException {
        if (courses.size() < 5) {
            throw new IllegalStateException(
                "A student must register at least 5 courses before CGPA can be calculated."
            );
        }

        int totalGradePoints = 0;
        int totalCreditUnits = 0;

        for (Course course : courses) {
            int gradePoint = gradeToPoint(course.getGrade());
            totalGradePoints += gradePoint * course.getCreditUnit();
            totalCreditUnits += course.getCreditUnit();
        }

        return (double) totalGradePoints / totalCreditUnits;
    }

    // Lives here, not in Course, because the 5-point scale is a policy of how CGPA gets computed.
    // CGPA is not an intrinsic property of a course itself.
    private int gradeToPoint(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 5;
            case "B": return 4;
            case "C": return 3;
            case "D": return 2;
            case "E": return 1;
            case "F": return 0;
            default: throw new IllegalArgumentException("Invalid grade: " + grade);
        }
    }

    // Returns true if a matching course was found and updated, false otherwise —
    // same "null/false means not found, not an error" reasoning as findStudentById.
    public boolean editCourse(String courseCode, String newTitle, int newCreditUnit, String newGrade) {
        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(courseCode)) {
                c.setCourseTitle(newTitle);
                c.setCreditUnit(newCreditUnit);
                c.setGrade(newGrade);
                return true;
            }
        }
        return false;
    }
}