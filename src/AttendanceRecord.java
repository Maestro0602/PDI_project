public class AttendanceRecord {
    private int attendanceID;
    private int studentID;
    private String attendanceDate;
    private int status;
    private double score;

    public AttendanceRecord(int studentID, String attendanceDate, int status, int score) {
        this.studentID = studentID;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.score = score;
    }

    public int getAttendanceID() {
        return attendanceID;
    }

    public void setAttendanceID(int attendanceID) {
        this.attendanceID = attendanceID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}