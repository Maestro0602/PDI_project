import java.time.LocalDate;

public class AttendanceRecord
{
    private int attedanceId;
    private int studentId;
    private LocalDate attendanceDate;
    private int status;
    private double score;

    public AttendanceRecord(int studentId, LocalDate attendanceDate, int status, double score)
    {
        this.studentId = studentId;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.score = score;
    }

    public int getAttendanceId()
    {
        return attedanceId;
    }

    public void setAttendanceId(int attedanceId)
    {
        this.attedanceId = attedanceId;
    }

    public int getStudentId()
    {
        return studentId;
    }

    public void setStudentId(int studentId)
    {
        this.studentId = studentId;
    }

    public LocalDate getAttendaceDate()
    {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate)
    {
        this.attendanceDate = attendanceDate;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public double getScore()
    {
        return score;
    }

    public void setScore(double score)
    {
        this.score = score;
    }
}