package Backend.src.report;

import java.util.List;

public class Report {

    private String studentID;
    private String studentName;
    private String gender;
    private String year;
    private String department;
    private String major;

    private List<String> courseIDs;
    private List<String> courseNames;
    private List<Double> scores;
    private List<String> grades;

    // ===== Constructor =====
    public Report(
            String studentID,
            String studentName,
            String gender,
            String year,
            String department,
            String major,
            List<String> courseIDs,
            List<String> courseNames,
            List<Double> scores,
            List<String> grades) {

        this.studentID = studentID;
        this.studentName = studentName;
        this.gender = gender;
        this.year = year;
        this.department = department;
        this.major = major;
        this.courseIDs = courseIDs;
        this.courseNames = courseNames;
        this.scores = scores;
        this.grades = grades;
    }

    // ===== Getters =====
    public String getStudentID() { return studentID; }
    public String getStudentName() { return studentName; }
    public String getGender() { return gender; }
    public String getYear() { return year; }
    public String getDepartment() { return department; }
    public String getMajor() { return major; }

    public List<String> getCourseIDs() { return courseIDs; }
    public List<String> getCourseNames() { return courseNames; }
    public List<Double> getScores() { return scores; }
    public List<String> getGrades() { return grades; }
}
