package Backend.src.grading;

import Backend.src.database.GradingManagement;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class GradingGUI extends JFrame {

    private final GradingManagement db = new GradingManagement();
    private Connection conn;

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    private JTextField teacherIdField;
    private JTextField studentIdField;
    private JComboBox<String> courseBox;
    private JTextField scoreField;

    private String teacherId;
    private String studentId;
    private Map<Integer, Map<String, Object>> courses;

    public GradingGUI() {
        setTitle("Grading Management System");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            GradingManagement.createUserTable();
            conn = GradingManagement.connectDB();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Error");
            System.exit(1);
        }

        mainPanel.add(teacherPanel(), "teacher");
        mainPanel.add(studentPanel(), "student");
        mainPanel.add(coursePanel(), "course");

        add(mainPanel);
        cardLayout.show(mainPanel, "teacher");
    }

    /* ================= TEACHER PANEL ================= */
    private JPanel teacherPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        teacherIdField = new JTextField();

        JButton nextBtn = new JButton("Next");
        JButton exitBtn = new JButton("Exit");

        nextBtn.addActionListener(e -> {
            try {
                validateTeacher();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        exitBtn.addActionListener(e -> System.exit(0));

        panel.setBorder(BorderFactory.createTitledBorder("Teacher Login"));
        panel.add(new JLabel("Teacher ID:"));
        panel.add(teacherIdField);
        panel.add(nextBtn);
        panel.add(exitBtn);

        return panel;
    }

    private void validateTeacher() throws SQLException {
        teacherId = teacherIdField.getText().trim();

        if (teacherId.isEmpty()) {
            alert("Enter Teacher ID");
            return;
        }

        Map<String, String> teacher = db.getTeacherInfo(conn, teacherId);
        if (teacher == null) {
            alert("Teacher not found!");
            return;
        }

        cardLayout.show(mainPanel, "student");
    }

    /* ================= STUDENT PANEL ================= */
    private JPanel studentPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        studentIdField = new JTextField();
        JButton nextBtn = new JButton("Next");
        JButton backBtn = new JButton("Back");

        nextBtn.addActionListener(e -> {
            try {
                validateStudent();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "teacher"));

        panel.setBorder(BorderFactory.createTitledBorder("Student Selection"));
        panel.add(new JLabel("Student ID:"));
        panel.add(studentIdField);
        panel.add(nextBtn);
        panel.add(backBtn);

        return panel;
    }

    private void validateStudent() throws SQLException {
        studentId = studentIdField.getText().trim();

        Map<String, String> student = db.getStudentInfo(conn, studentId);
        if (student == null) {
            alert("Student not found!");
            return;
        }

        Map<String, String> teacher = db.getTeacherInfo(conn, teacherId);

        if (!db.canTeacherGradeStudent(teacher, student)) {
            alert("Access denied!");
            cardLayout.show(mainPanel, "teacher");
            return;
        }

        courses = db.getTeacherCourses(conn, teacherId);
        courseBox.removeAllItems();

        for (Map<String, Object> c : courses.values()) {
            courseBox.addItem(c.get("course_Id") + " - " + c.get("course_name"));
        }

        cardLayout.show(mainPanel, "course");
    }

    /* ================= COURSE PANEL ================= */
    private JPanel coursePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        courseBox = new JComboBox<>();
        scoreField = new JTextField();

        JButton saveBtn = new JButton("Save Grade");
        JButton finishBtn = new JButton("Finish");

        saveBtn.addActionListener(e -> saveGrade());
        finishBtn.addActionListener(e -> cardLayout.show(mainPanel, "teacher"));

        panel.setBorder(BorderFactory.createTitledBorder("Grading"));
        panel.add(new JLabel("Course:"));
        panel.add(courseBox);
        panel.add(new JLabel("Score (0-100):"));
        panel.add(scoreField);
        panel.add(saveBtn);
        panel.add(finishBtn);

        return panel;
    }

    private void saveGrade() {
        try {
            double score = Double.parseDouble(scoreField.getText());
            if (score < 0 || score > 100) {
                alert("Invalid score!");
                return;
            }

            String selected = (String) courseBox.getSelectedItem();
            String courseId = selected.split(" - ")[0];

            Map<String, Object> gradeInfo = db.calculateGrade(score);
            String grade = (String) gradeInfo.get("grade");

            db.saveGrade(conn, studentId, courseId, score, grade);

            JOptionPane.showMessageDialog(this,
                    "Grade Saved!\nCourse: " + courseId +
                            "\nScore: " + score +
                            "\nGrade: " + grade);

            scoreField.setText("");

        } catch (Exception e) {
            alert("Invalid input!");
        }
    }

    private void alert(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GradingGUI().setVisible(true));
    }
}
