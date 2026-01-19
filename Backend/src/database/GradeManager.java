package Backend.src.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GradeManager - Handles all grade-related database operations
 * Includes methods for storing, retrieving, and calculating student grades and GPA
 */
public class GradeManager {

    /**
     * Create studentGrades table if it doesn't exist
     */
    public static void createGradesTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS studentGrades (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "studentID VARCHAR(50) NOT NULL, " +
                        "subjectName VARCHAR(100) NOT NULL, " +
                        "score DECIMAL(5,2) NOT NULL, " +
                        "credits INT DEFAULT 3, " +
                        "semester VARCHAR(20) DEFAULT 'Fall 2025', " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                        "UNIQUE KEY unique_student_subject (studentID, subjectName, semester))";

                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.out.println("Error creating studentGrades table: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /**
     * Save or update a student's grade
     */
    public static boolean saveGrade(String studentID, String subjectName, double score, int credits, String semester) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "INSERT INTO studentGrades (studentID, subjectName, score, credits, semester) " +
                        "VALUES (?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE score = ?, credits = ?, updated_at = CURRENT_TIMESTAMP";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, studentID);
                pstmt.setString(2, subjectName);
                pstmt.setDouble(3, score);
                pstmt.setInt(4, credits);
                pstmt.setString(5, semester);
                pstmt.setDouble(6, score);
                pstmt.setInt(7, credits);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error saving grade: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    /**
     * Get all grades for a specific student
     * Returns: List of [subjectName, score, credits, letterGrade, gradePoint]
     */
    public static List<String[]> getStudentGrades(String studentID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String[]> grades = new ArrayList<>();

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT subjectName, score, credits, semester FROM studentGrades " +
                        "WHERE studentID = ? ORDER BY semester DESC, subjectName";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, studentID);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    double score = rs.getDouble("score");
                    int credits = rs.getInt("credits");
                    String letterGrade = calculateLetterGrade(score);
                    double gradePoint = calculateGradePoint(score);

                    grades.add(new String[]{
                            rs.getString("subjectName"),
                            String.format("%.2f", score),
                            String.valueOf(credits),
                            letterGrade,
                            String.format("%.2f", gradePoint),
                            rs.getString("semester")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting student grades: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return grades;
    }

    /**
     * Calculate GPA for a specific student
     * Returns: [GPA, totalCredits, totalGradePoints]
     */
    public static double[] calculateStudentGPA(String studentID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT score, credits FROM studentGrades WHERE studentID = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, studentID);
                rs = pstmt.executeQuery();

                double totalGradePoints = 0;
                int totalCredits = 0;

                while (rs.next()) {
                    double score = rs.getDouble("score");
                    int credits = rs.getInt("credits");
                    double gradePoint = calculateGradePoint(score);

                    totalGradePoints += gradePoint * credits;
                    totalCredits += credits;
                }

                if (totalCredits > 0) {
                    double gpa = totalGradePoints / totalCredits;
                    return new double[]{gpa, totalCredits, totalGradePoints};
                }
            }
        } catch (SQLException e) {
            System.out.println("Error calculating GPA: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return new double[]{0.0, 0, 0.0};
    }

    /**
     * Get GPA by semester for a student
     * Returns: List of [semester, GPA, totalCredits]
     */
    public static List<String[]> getGPABySemester(String studentID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String[]> semesterGPAs = new ArrayList<>();

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT semester, SUM(credits) as totalCredits, " +
                        "AVG(CASE " +
                        "  WHEN score >= 90 THEN 4.0 " +
                        "  WHEN score >= 85 THEN 3.7 " +
                        "  WHEN score >= 80 THEN 3.3 " +
                        "  WHEN score >= 77 THEN 3.0 " +
                        "  WHEN score >= 73 THEN 2.7 " +
                        "  WHEN score >= 70 THEN 2.3 " +
                        "  WHEN score >= 67 THEN 2.0 " +
                        "  WHEN score >= 63 THEN 1.7 " +
                        "  WHEN score >= 60 THEN 1.3 " +
                        "  WHEN score >= 57 THEN 1.0 " +
                        "  ELSE 0.0 END) as avgGPA " +
                        "FROM studentGrades WHERE studentID = ? " +
                        "GROUP BY semester ORDER BY semester DESC";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, studentID);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    semesterGPAs.add(new String[]{
                            rs.getString("semester"),
                            String.format("%.2f", rs.getDouble("avgGPA")),
                            String.valueOf(rs.getInt("totalCredits"))
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting semester GPAs: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return semesterGPAs;
    }

    /**
     * Get all students with their grades for report generation
     * Returns: List of StudentReport objects
     */
    public static List<StudentReport> getAllStudentReports() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<StudentReport> reports = new ArrayList<>();

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                // Get all students
                String sql = "SELECT DISTINCT s.studentID, s.studentName, s.gender, s.year " +
                        "FROM studentInfo s ORDER BY s.studentName";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String studentID = rs.getString("studentID");
                    String studentName = rs.getString("studentName");
                    String gender = rs.getString("gender");
                    String year = rs.getString("year");

                    // Calculate GPA for this student
                    double[] gpaData = calculateStudentGPA(studentID);
                    List<String[]> grades = getStudentGrades(studentID);

                    StudentReport report = new StudentReport(
                            studentID, studentName, gender, year,
                            gpaData[0], (int) gpaData[1], grades
                    );
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting all student reports: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        return reports;
    }

    /**
     * Search students by ID or name for reports
     */
    public static List<StudentReport> searchStudentReports(String searchTerm) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<StudentReport> reports = new ArrayList<>();

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                String sql = "SELECT DISTINCT s.studentID, s.studentName, s.gender, s.year " +
                        "FROM studentInfo s " +
                        "WHERE s.studentID LIKE ? OR s.studentName LIKE ? " +
                        "ORDER BY s.studentName";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + searchTerm + "%");
                pstmt.setString(2, "%" + searchTerm + "%");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    String studentID = rs.getString("studentID");
                    String studentName = rs.getString("studentName");
                    String gender = rs.getString("gender");
                    String year = rs.getString("year");

                    double[] gpaData = calculateStudentGPA(studentID);
                    List<String[]> grades = getStudentGrades(studentID);

                    StudentReport report = new StudentReport(
                            studentID, studentName, gender, year,
                            gpaData[0], (int) gpaData[1], grades
                    );
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching student reports: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return reports;
    }

    /**
     * Insert sample grades data for testing
     */
    public static void insertSampleGrades() {
        // Sample data - only insert if table is empty
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.connectDB();
            if (conn != null) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM studentGrades");
                if (rs.next() && rs.getInt("count") == 0) {
                    // Get existing students
                    rs = stmt.executeQuery("SELECT studentID FROM studentInfo LIMIT 10");
                    String[] subjects = {"Mathematics", "Physics", "Chemistry", "Biology", "English", "Computer Science"};
                    int[] credits = {4, 3, 3, 3, 3, 4};

                    while (rs.next()) {
                        String studentID = rs.getString("studentID");
                        for (int i = 0; i < subjects.length; i++) {
                            double score = 60 + Math.random() * 40; // Random score between 60-100
                            saveGrade(studentID, subjects[i], score, credits[i], "Fall 2025");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting sample grades: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    /**
     * Calculate letter grade from score
     */
    public static String calculateLetterGrade(double score) {
        if (score >= 90) return "A";
        if (score >= 85) return "A-";
        if (score >= 80) return "B+";
        if (score >= 77) return "B";
        if (score >= 73) return "B-";
        if (score >= 70) return "C+";
        if (score >= 67) return "C";
        if (score >= 63) return "C-";
        if (score >= 60) return "D+";
        if (score >= 57) return "D";
        return "F";
    }

    /**
     * Calculate grade point from score (4.0 scale)
     */
    public static double calculateGradePoint(double score) {
        if (score >= 90) return 4.0;
        if (score >= 85) return 3.7;
        if (score >= 80) return 3.3;
        if (score >= 77) return 3.0;
        if (score >= 73) return 2.7;
        if (score >= 70) return 2.3;
        if (score >= 67) return 2.0;
        if (score >= 63) return 1.7;
        if (score >= 60) return 1.3;
        if (score >= 57) return 1.0;
        return 0.0;
    }

    /**
     * Get GPA classification
     */
    public static String getGPAClassification(double gpa) {
        if (gpa >= 3.7) return "Excellent (Dean's List)";
        if (gpa >= 3.3) return "Very Good";
        if (gpa >= 3.0) return "Good";
        if (gpa >= 2.5) return "Satisfactory";
        if (gpa >= 2.0) return "Passing";
        return "Academic Warning";
    }

    /**
     * Close database resources safely
     */
    private static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }

    /**
     * Inner class to hold student report data
     */
    public static class StudentReport {
        private String studentID;
        private String studentName;
        private String gender;
        private String year;
        private double gpa;
        private int totalCredits;
        private List<String[]> grades; // [subjectName, score, credits, letterGrade, gradePoint, semester]

        public StudentReport(String studentID, String studentName, String gender, String year,
                             double gpa, int totalCredits, List<String[]> grades) {
            this.studentID = studentID;
            this.studentName = studentName;
            this.gender = gender;
            this.year = year;
            this.gpa = gpa;
            this.totalCredits = totalCredits;
            this.grades = grades;
        }

        // Getters
        public String getStudentID() { return studentID; }
        public String getStudentName() { return studentName; }
        public String getGender() { return gender; }
        public String getYear() { return year; }
        public double getGpa() { return gpa; }
        public int getTotalCredits() { return totalCredits; }
        public List<String[]> getGrades() { return grades; }
        public String getGPAClassification() { return GradeManager.getGPAClassification(gpa); }
    }
}
