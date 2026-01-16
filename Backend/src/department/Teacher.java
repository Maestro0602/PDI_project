package Backend.src.department;

public class Teacher {
    private String teacherId;
    private String teacherName;
    private String department;
    private String major;
    private String course;

    public Teacher(String teacherId, String teacherName, String department) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.department = department;
        this.major = null;
        this.course = null;
    }

    public Teacher(String teacherId, String teacherName) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.department = null;
        this.major = null;
        this.course = null;
    }

    public Teacher(String teacherId, String teacherName, String department, String major, String course) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.department = department;
        this.major = major;
        this.course = course;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "ID: " + teacherId + " | Name: " + teacherName + " | Department: "
                + (department == null ? "Not Assigned" : department) + " | Major: "
                + (major == null ? "Not Assigned" : major) + " | Course: "
                + (course == null ? "Not Assigned" : course);
    }
}
