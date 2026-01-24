package Backend.src.session;

import Backend.src.database.TeacherInfoManager;

/**
 * UserSession - Singleton class to manage logged-in user session
 * Stores user information across the application without modifying backend database logic
 */
public class UserSession {
    
    private static UserSession instance;
    
    // User session data
    private String username;
    private String email;
    private String userId;
    private String userType; // STUDENT, TEACHER, or OWNER
    private boolean isLoggedIn;
    
    // Teacher-specific data
    private String department;
    private String major;
    private int courseCount;
    
    // Private constructor for singleton
    private UserSession() {
        this.isLoggedIn = false;
        this.department = null;
        this.major = null;
        this.courseCount = 0;
    }
    
    /**
     * Get the singleton instance
     */
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    /**
     * Create a new session after successful login
     */
    public void createSession(String username, String email, String userId, String userType) {
        this.username = username;
        this.email = email;
        this.userId = userId;
        this.userType = userType;
        this.isLoggedIn = true;
        
        // Load teacher-specific info if user is a teacher
        if ("TEACHER".equals(userType) && userId != null) {
            loadTeacherInfo(userId);
        }
    }
    
    /**
     * Load teacher information from the database
     */
    private void loadTeacherInfo(String teacherId) {
        String[] teacherInfo = TeacherInfoManager.getTeacherInfo(teacherId);
        if (teacherInfo != null) {
            this.department = teacherInfo[1];
            this.major = teacherInfo[2];
            try {
                this.courseCount = Integer.parseInt(teacherInfo[3]);
            } catch (NumberFormatException e) {
                this.courseCount = 0;
            }
        }
    }
    
    /**
     * Refresh teacher info from database
     */
    public void refreshTeacherInfo() {
        if ("TEACHER".equals(userType) && userId != null) {
            loadTeacherInfo(userId);
        }
    }
    
    /**
     * Clear the session (logout)
     */
    public void clearSession() {
        this.username = null;
        this.email = null;
        this.userId = null;
        this.userType = null;
        this.isLoggedIn = false;
        this.department = null;
        this.major = null;
        this.courseCount = 0;
    }
    
    // Getters
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    
    /**
     * Check if current user is a student
     */
    public boolean isStudent() {
        return "STUDENT".equals(userType);
    }
    
    /**
     * Check if current user is a teacher
     */
    public boolean isTeacher() {
        return "TEACHER".equals(userType);
    }
    
    /**
     * Check if current user is an owner/admin
     */
    public boolean isOwner() {
        return "OWNER".equals(userType);
    }
    
    /**
     * Get teacher's department
     */
    public String getDepartment() {
        return department;
    }
    
    /**
     * Get teacher's major
     */
    public String getMajor() {
        return major;
    }
    
    /**
     * Get teacher's course count
     */
    public int getCourseCount() {
        return courseCount;
    }
    
    /**
     * Get display name for the logged-in user
     */
    public String getDisplayName() {
        return username != null ? username : "User";
    }
    
    /**
     * Get teacher info summary for display
     */
    public String getTeacherInfoSummary() {
        if (!isTeacher()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Department: ").append(department != null ? department : "Not Assigned");
        sb.append(" | Major: ").append(major != null ? major : "Not Assigned");
        sb.append(" | Courses: ").append(courseCount);
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "UserSession{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userId='" + userId + '\'' +
                ", userType='" + userType + '\'' +
                ", department='" + department + '\'' +
                ", major='" + major + '\'' +
                ", courseCount=" + courseCount +
                ", isLoggedIn=" + isLoggedIn +
                '}';
    }
}
