package Backend.src.mainpage;

import Backend.src.database.TeacherInfoManager;
import Backend.src.database.TeacherCourseManager;
import Backend.src.session.UserSession;

/**
 * TeacherMainPageService - Backend service class for Teacher Main Page
 * Acts as an API layer between the GUI and database operations
 */
public class TeacherMainPageService {
    
    private UserSession session;
    
    public TeacherMainPageService() {
        this.session = UserSession.getInstance();
    }
    
    /**
     * Check if the current logged-in user is a teacher
     * @return true if user is a teacher, false otherwise
     */
    public boolean isTeacher() {
        return session.isLoggedIn() && session.isTeacher();
    }
    
    /**
     * Check if the current logged-in user is an owner/admin
     * @return true if user is an owner, false otherwise
     */
    public boolean isOwner() {
        return session.isLoggedIn() && session.isOwner();
    }
    
    /**
     * Get the current teacher's ID
     * @return teacher ID or null if not logged in
     */
    public String getTeacherId() {
        return session.getUserId();
    }
    
    /**
     * Get the current teacher's name
     * @return teacher name
     */
    public String getTeacherName() {
        return session.getDisplayName();
    }
    
    /**
     * Get the current teacher's email
     * @return teacher email
     */
    public String getTeacherEmail() {
        return session.getEmail();
    }
    
    /**
     * Get the current teacher's department
     * @return department name or "Not Assigned" if not set
     */
    public String getTeacherDepartment() {
        String dept = session.getDepartment();
        return dept != null ? dept : "Not Assigned";
    }
    
    /**
     * Get the current teacher's major
     * @return major name or "Not Assigned" if not set
     */
    public String getTeacherMajor() {
        String major = session.getMajor();
        return major != null ? major : "Not Assigned";
    }
    
    /**
     * Get the current teacher's course count
     * @return number of courses assigned to the teacher
     */
    public int getTeacherCourseCount() {
        return session.getCourseCount();
    }
    
    /**
     * Get teacher info summary for display
     * @return formatted string with department, major, and course count
     */
    public String getTeacherInfoSummary() {
        return session.getTeacherInfoSummary();
    }
    
    /**
     * Refresh teacher information from database
     * Call this when teacher info might have changed
     */
    public void refreshTeacherInfo() {
        session.refreshTeacherInfo();
    }
    
    /**
     * Get detailed teacher info from database
     * @return String array [teacherID, department, major, courseCount] or null
     */
    public String[] getTeacherInfoFromDB() {
        String teacherId = session.getUserId();
        if (teacherId != null) {
            return TeacherInfoManager.getTeacherInfo(teacherId);
        }
        return null;
    }
    
    /**
     * Check if teacher exists in the database
     * @return true if teacher record exists
     */
    public boolean teacherExistsInDB() {
        String teacherId = session.getUserId();
        if (teacherId != null) {
            return TeacherInfoManager.teacherExists(teacherId);
        }
        return false;
    }
    
    /**
     * Perform logout - clears the session
     */
    public void logout() {
        session.clearSession();
    }
    
    /**
     * Get welcome message based on user role
     * @return personalized welcome message
     */
    public String getWelcomeMessage() {
        if (!session.isLoggedIn()) {
            return "Welcome to the Teacher Portal!";
        }
        
        String name = session.getDisplayName();
        String dept = getTeacherDepartment();
        
        if (!"Not Assigned".equals(dept)) {
            return "Welcome back, " + name + " (" + dept + ")!";
        }
        return "Welcome back, " + name + "!";
    }
    
    /**
     * Get subtitle with teacher details
     * @return subtitle with department and course info
     */
    public String getSubtitleInfo() {
        if (!session.isLoggedIn()) {
            return "Please log in to access your dashboard";
        }
        
        String dept = getTeacherDepartment();
        String major = getTeacherMajor();
        int courses = getTeacherCourseCount();
        
        StringBuilder sb = new StringBuilder();
        sb.append("Department: ").append(dept);
        if (!"Not Assigned".equals(major)) {
            sb.append(" • Major: ").append(major);
        }
        if (courses > 0) {
            sb.append(" • ").append(courses).append(" Course(s)");
        }
        return sb.toString();
    }
}
