package attendance;

import java.time.LocalDate;

public class AttendanceRecord {
    private String studentId;
    private String studentName;
    private LocalDate attendanceDate;
    private int status;
    private double score;
    
    /**
     * Constructor for creating new attendance record
     */
    public AttendanceRecord(String studentId, LocalDate attendanceDate, int status, double score) {
        this.studentId = studentId;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.score = score;
    }
    
    /**
     * Constructor for retrieving attendance record from database
     */
    public AttendanceRecord(String studentId, String studentName, LocalDate attendanceDate, int status, double score) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.score = score;
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