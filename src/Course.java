public class Course {
    private String courseCode;
    private String courseTitle;
    private int creditUnit;
    private String grade;

    public Course(String courseCode, String courseTitle, int creditUnit, String grade) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.creditUnit = creditUnit;
        this.grade = grade;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    // No validation here (e.g. creditUnit > 0, grade in A-F)
    // this class just holds data.
    //  Validation happens at the GUI input boundary, where user input actually enters the system.
    
    public int getCreditUnit() {
        return creditUnit;
    }

    public void setCreditUnit(int creditUnit) {
        this.creditUnit = creditUnit;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}