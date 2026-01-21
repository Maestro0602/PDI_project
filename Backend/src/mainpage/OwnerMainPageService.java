package Backend.src.mainpage;

import Backend.src.database.CourseManager;
import Backend.src.database.EmailManager;
import Backend.src.database.StudentInfoManager;
import Backend.src.database.TeacherCourseManager;
import Backend.src.database.TeacherInfoManager;
import Backend.src.session.UserSession;

/**
 * OwnerMainPageService - Backend service class for Owner/Admin Main Page
 * Acts as an API layer between the GUI and database operations
 */
public class OwnerMainPageService {
    
    private final UserSession session;
    
    public OwnerMainPageService() {
        this.session = UserSession.getInstance();
        // Initialize course table with default courses if not already created
        CourseManager.createCourseTable();
    }
    
    /**
     * Check if the current user is logged in
     * @return true if user is logged in, false otherwise
     */
    public boolean isOwner() {
        return session.isLoggedIn();
    }
    
    /**
     * Get the current owner's name
     * @return owner name
     */
    public String getOwnerName() {
        return session.getDisplayName();
    }
    
    /**
     * Get the current owner's email
     * @return owner email
     */
    public String getOwnerEmail() {
        return session.getEmail();
    }
    
    /**
     * Get welcome message for the owner
     * @return personalized welcome message
     */
    public String getWelcomeMessage() {
        if (!session.isLoggedIn()) {
            return "Welcome to the Admin Panel!";
        }
        return "Welcome back, " + session.getDisplayName() + "!";
    }
    
    /**
     * Perform logout - clears the session
     */
    public void logout() {
        session.clearSession();
    }
    
    // ==================== STUDENT STATISTICS ====================
    
    /**
     * Get total student count
     * @return total number of students
     */
    public int getTotalStudentCount() {
        return StudentInfoManager.getTotalStudentCount();
    }
    
    /**
     * Get student statistics by department and gender
     * @param department the department code (GIC, GIM, GEE)
     * @return int array [total, male, female] or null
     */
    public int[] getStudentsByDepartmentAndGender(String department) {
        return StudentInfoManager.getStudentsByDepartmentAndGender(department);
    }
    
    /**
     * Get all students as array for table display
     * @return 2D array of student data
     */
    public String[][] getAllStudentsArray() {
        return StudentInfoManager.getAllStudentsArray();
    }
    
    /**
     * Search students by name
     * @param name search term
     * @return 2D array of matching students
     */
    public String[][] searchStudentByName(String name) {
        return StudentInfoManager.searchStudentByName(name);
    }
    
    // ==================== TEACHER MANAGEMENT ====================
    
    /**
     * Get teacher info by ID
     * @param teacherId the teacher ID
     * @return String array [teacherID, department, major, courseCount] or null
     */
    public String[] getTeacherInfo(String teacherId) {
        return TeacherInfoManager.getTeacherInfo(teacherId);
    }
    
    /**
     * Check if teacher exists
     * @param teacherId the teacher ID
     * @return true if teacher exists
     */
    public boolean teacherExists(String teacherId) {
        return TeacherInfoManager.teacherExists(teacherId);
    }
    
    /**
     * Get all courses available
     * @return array of course names
     */
    public String[] getAllCourses() {
        return CourseManager.getAllCoursesArray();
    }
    
    /**
     * Get total number of courses
     * @return count of all courses
     */
    public int getCourseCount() {
        String[] courses = getAllCourses();
        return courses != null ? courses.length : 0;
    }
    
    /**
     * Assign a course to a teacher
     * @param teacherId the teacher ID
     * @param courseId the course ID
     * @return true if assignment successful
     */
    public boolean assignCourseToTeacher(String teacherId, String courseId) {
        return TeacherCourseManager.addTeacherCourse(teacherId, courseId);
    }
    
    /**
     * Remove a course from a teacher
     * @param assignmentId the assignment ID
     * @return true if removal successful
     */
    public boolean removeCourseFromTeacher(int assignmentId) {
        return TeacherCourseManager.deleteTeacherCourse(assignmentId);
    }
    
    // ==================== EMAIL ACCOUNT MANAGEMENT ====================
    
    /**
     * Create student email account
     * @param studentId the student ID
     * @param email the email address
     * @return true if creation successful
     */
    public boolean createStudentEmail(String studentId, String email) {
        return EmailManager.createStudentEmail(studentId, email);
    }
    
    /**
     * Create teacher email account
     * @param teacherId the teacher ID
     * @param email the email address
     * @return true if creation successful
     */
    public boolean createTeacherEmail(String teacherId, String email) {
        return EmailManager.createTeacherEmail(teacherId, email);
    }
    
    /**
     * Create owner email account
     * @param ownerId the owner ID
     * @param email the email address
     * @return true if creation successful
     */
    public boolean createOwnerEmail(String ownerId, String email) {
        return EmailManager.createOwnerEmail(ownerId, email);
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Get full department name from code
     * @param deptCode department code (GIC, GIM, GEE)
     * @return full department name
     */
    public String getDepartmentFullName(String deptCode) {
        switch (deptCode) {
            case "GIC": return "GIC (IT & Computing)";
            case "GIM": return "GIM (IT & Management)";
            case "GEE": return "GEE (Electrical & Engineering)";
            default: return deptCode;
        }
    }
    
    /**
     * Get all department codes
     * @return array of department codes
     */
    public String[] getAllDepartments() {
        return new String[]{"GIC", "GIM", "GEE"};
    }
    
    /**
     * Validate ID format based on type
     * @param id the ID to validate
     * @param type 0 for student, 1 for teacher, 2 for owner
     * @return true if ID format is valid
     */
    public boolean validateIdFormat(String id, int type) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        switch (type) {
            case 0: return id.startsWith("P2024");
            case 1: return id.startsWith("T2024");
            case 2: return id.startsWith("O2024");
            default: return false;
        }
    }
    
    /**
     * Validate email format
     * @param email the email to validate
     * @return true if email format is valid
     */
    public boolean validateEmailFormat(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    public static void main(String[] args) {
        
    }
}
