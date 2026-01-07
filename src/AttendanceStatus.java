public class AttendanceStatus {
    public static final int PRESENT = 1;
    public static final int LATE = 2;
    public static final int ABSENT = 3;
    public static final int EXCUSE = 4;

    public static double calculateScore(int status) {
        switch(status) {
            case PRESENT : return 1.0;
            case LATE : return 0.8;
            case ABSENT : return 0.0;
            case EXCUSE : return 0.5;
            default : return 0.0;
        }
    }

    public static String getStatusText(int status) {
        switch(status) {
            case PRESENT : return "Present";
            case LATE : return "Late";
            case ABSENT : return "Absent";
            case EXCUSE : return "Excuse";
            default : return "Unkown";
        }
    }

    public static boolean isValidStatus(int status) {
        return status >= 1 && status <= 4;
    }
}