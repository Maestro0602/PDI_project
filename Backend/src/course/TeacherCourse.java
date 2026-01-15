package Backend.src.course;

public class TeacherCourse {
    private int id;
    private String teacherID;
    private String courseId;

    public TeacherCourse(int id, String teacherID, String courseId) {
        this.id = id;
        this.teacherID = teacherID;
        this.courseId = courseId;
    }

    public TeacherCourse(String teacherID, String courseId) {
        this.teacherID = teacherID;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Teacher ID: " + teacherID + " | Course ID: " + courseId;
    }
}
