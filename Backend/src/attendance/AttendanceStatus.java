package Backend.src.attendance;

public class AttendanceStatus
{
    public static final int PRESENT = 1;
    public static final int LATE = 2;
    public static final int ABSENT = 3;
    public static final int EXCUSED = 4;

    public static double getScore(int status)
    {
        switch (status)
        {
            case PRESENT:
                return 1.0;
            case LATE:
                return 0.8;
            case ABSENT:
                return 0.0;
            case EXCUSED:
                return 0.5;
            default:
                return 0.0;
        }
    }

    public static String getStautsText(int status)
    {
        switch (status)
        {
            case PRESENT:
                return "Present";
            case LATE:
                return "Late";
            case ABSENT:
                return "Absent";
            case EXCUSED:
                return "Excused";
            default:
                return "Unknown";
        }
    }
    
    public static int getStatusFromText(String statusText)
    {
        switch (statusText)
        {
            case "Present":
                return PRESENT;
            case "Late":
                return LATE;
            case "Absent":
                return ABSENT;
            case "Excused":
                return EXCUSED;
            default:
                return 0;
        }
    }

    public static boolean isValid(int status)
    {
        return status >= 1 && status <= 4;
    }
}