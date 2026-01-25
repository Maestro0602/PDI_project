package Backend.src.department;

public class Student {
    private String studentId;
    private String studentName;
    private String department;
    private String major;

    public Student(String studentId, String studentName, String department,String major) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.department = department;
        this.major = major;
    }

    public Student(String studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.department = null;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Override
    public String toString() {
        return "ID: " + studentId + " | Name: " + studentName + " | Department: "
                + (department == null ? "Not Assigned" : department) + " | Major: "
                + (major == null ? "Not Assigned" : major);
    }
}
