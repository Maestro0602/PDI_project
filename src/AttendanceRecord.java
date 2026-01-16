import java.time.LocalDate;

public class AttendanceRecord {
    private int attendanceId;
    private int studentId;
    private String studentName;
    private LocalDate attendanceDate;
    private int status;
    private double score;
    
    public AttendanceRecord(int studentId, LocalDate attendanceDate, int status, double score) {
        this.studentId = studentId;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.score = score;
    }
    
    public AttendanceRecord(int attendanceId, int studentId, String studentName, LocalDate attendanceDate, int status, double score) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.score = score;
    }
    
    public int getAttendanceId() {
        return attendanceId;
    }
    
    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }
    
    public void setAttendanceDate(LocalDate attendanceDate) {
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
    
    public void setScore(double score) {
        this.score = score;
    }
}
