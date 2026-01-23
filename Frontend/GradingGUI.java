package Frontend;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import Backend.main.MainPageTeacher;
import Backend.src.database.GradingManagement;

public class GradingGUI extends JFrame {

    private GradingManagement dbManager;
    private Connection conn;
    
    private String currentTeacherId;
    private Map<String, String> currentTeacher;
    
    private String currentStudentId;
    private Map<String, String> currentStudent;
    
    private Map<Integer, Map<String, Object>> availableCourses;
    
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    private JTextField teacherIdField;
    private JTextArea teacherInfoArea;
    
    private JTextField studentIdField;
    private JTextArea studentInfoArea;
    
    private JTable coursesTable;
    private DefaultTableModel tableModel;
    private JTextField scoreField;
    private JLabel resultLabel;

    // Color constants - more vibrant colors
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);     // Blue
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);      // Green
    private final Color WARNING_COLOR = new Color(241, 196, 15);     // Yellow
    private final Color DANGER_COLOR = new Color(192, 57, 43);       // Red
    private final Color SECONDARY_COLOR = new Color(127, 140, 141);  // Gray
    private final Color ACCENT_COLOR = new Color(142, 68, 173);      // Purple
    private final Color INFO_COLOR = new Color(52, 152, 219);        // Light Blue
    private final Color BUTTON_TEXT_COLOR = Color.WHITE;
    
    public GradingGUI() {
        try {
            initializeDatabase();
            initializeUI();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Initialization Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initializeDatabase() throws SQLException {
        GradingManagement.createUserTable();
        dbManager = new GradingManagement();
        conn = GradingManagement.connectDB();
    }

    private void initializeUI() {
        setTitle("Grading Management System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                handleExit();
            }
        });
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createTeacherLoginPanel(), "TEACHER_LOGIN");
        mainPanel.add(createStudentInputPanel(), "STUDENT_INPUT");
        mainPanel.add(createGradingPanel(), "GRADING");

        add(mainPanel);
        cardLayout.show(mainPanel, "TEACHER_LOGIN");
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            public void paintComponent(Graphics g) {
                // Force the button to paint with our colors
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(bgColor);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw text
                g2.setColor(BUTTON_TEXT_COLOR);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
                
                // Draw border
                g2.setColor(bgColor.darker());
                g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
                g2.dispose();
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setContentAreaFilled(false); // We're painting manually
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        
        // Set preferred size for consistency
        button.setPreferredSize(new Dimension(150, 35));
        
        return button;
    }

    private JPanel createTeacherLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("Grading Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel teacherIdLabel = new JLabel("Teacher ID:");
        teacherIdLabel.setFont(new Font("Arial", Font.BOLD, 16));
        teacherIdLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(teacherIdLabel, gbc);

        teacherIdField = new JTextField(20);
        teacherIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        teacherIdField.setPreferredSize(new Dimension(250, 35));
        teacherIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        centerPanel.add(teacherIdField, gbc);

        JButton loginButton = createStyledButton("Login", SUCCESS_COLOR);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(150, 40));
        loginButton.addActionListener(e -> handleTeacherLogin());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(loginButton, gbc);

        teacherInfoArea = new JTextArea(5, 30);
        teacherInfoArea.setEditable(false);
        teacherInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        teacherInfoArea.setBackground(new Color(240, 240, 240));
        teacherInfoArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(teacherInfoArea);
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        centerPanel.add(scrollPane, gbc);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 245));
        JButton exitButton = createStyledButton("Exit to Main Page", DANGER_COLOR);
        exitButton.addActionListener(e -> handleExit());
        bottomPanel.add(exitButton);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void handleTeacherLogin() {
        currentTeacherId = teacherIdField.getText().trim();
        
        if (currentTeacherId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a Teacher ID",
                "Input Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            currentTeacher = dbManager.getTeacherInfo(conn, currentTeacherId);
            
            if (currentTeacher == null) {
                teacherInfoArea.setText("Teacher not found!");
                JOptionPane.showMessageDialog(this,
                    "Teacher not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            teacherInfoArea.setText(
                "=== TEACHER INFORMATION ===\n" +
                "Teacher ID: " + currentTeacherId + "\n" +
                "Department: " + currentTeacher.get("department") + "\n" +
                "Major: " + currentTeacher.get("major") + "\n" +
                "============================\n\n" +
                "Login successful!"
            );

            int result = JOptionPane.showConfirmDialog(this,
                "Teacher verified! Proceed to student input?",
                "Success",
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                cardLayout.show(mainPanel, "STUDENT_INPUT");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Database Error: " + e.getMessage(),
                "SQL Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JPanel createStudentInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(INFO_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("Student Verification");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setFont(new Font("Arial", Font.BOLD, 16));
        studentIdLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(studentIdLabel, gbc);

        studentIdField = new JTextField(20);
        studentIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        studentIdField.setPreferredSize(new Dimension(250, 35));
        studentIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        centerPanel.add(studentIdField, gbc);

        JButton verifyButton = createStyledButton("Verify Student", SUCCESS_COLOR);
        verifyButton.setFont(new Font("Arial", Font.BOLD, 16));
        verifyButton.setPreferredSize(new Dimension(150, 40));
        verifyButton.addActionListener(e -> handleStudentVerification());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(verifyButton, gbc);

        studentInfoArea = new JTextArea(5, 30);
        studentInfoArea.setEditable(false);
        studentInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        studentInfoArea.setBackground(new Color(240, 240, 240));
        studentInfoArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(studentInfoArea);
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        centerPanel.add(scrollPane, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(245, 245, 245));
        
        JButton backButton = createStyledButton("Back to Teacher Login", SECONDARY_COLOR);
        backButton.addActionListener(e -> {
            teacherIdField.setText("");
            teacherInfoArea.setText("");
            cardLayout.show(mainPanel, "TEACHER_LOGIN");
        });
        
        bottomPanel.add(backButton);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void handleStudentVerification() {
        currentStudentId = studentIdField.getText().trim();
        
        if (currentStudentId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a Student ID",
                "Input Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            currentStudent = dbManager.getStudentInfo(conn, currentStudentId);
            
            if (currentStudent == null) {
                studentInfoArea.setText("Student not found!");
                JOptionPane.showMessageDialog(this,
                    "Student not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            studentInfoArea.setText(
                "=== STUDENT INFORMATION ===\n" +
                "Student ID: " + currentStudentId + "\n" +
                "Department: " + currentStudent.get("department") + "\n" +
                "Major: " + currentStudent.get("major") + "\n" +
                "============================\n\n"
            );

            if (!dbManager.canTeacherGradeStudent(currentTeacher, currentStudent)) {
                studentInfoArea.append(
                    "ACCESS DENIED!\n" +
                    "Teacher's Department/Major does NOT match Student's.\n"
                );
                JOptionPane.showMessageDialog(this,
                    "Access Denied!\nTeacher's Department/Major does not match Student's.",
                    "Permission Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            studentInfoArea.append("Permission granted!\n");

            availableCourses = dbManager.getTeacherCourses(conn, currentTeacherId);
            
            if (availableCourses == null || availableCourses.isEmpty()) {
                studentInfoArea.append("\nNo courses assigned to this teacher!");
                JOptionPane.showMessageDialog(this,
                    "No courses assigned to this teacher!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int result = JOptionPane.showConfirmDialog(this,
                "Student verified! Proceed to grading?",
                "Success",
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                loadCoursesTable();
                cardLayout.show(mainPanel, "GRADING");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Database Error: " + e.getMessage(),
                "SQL Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JPanel createGradingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ACCENT_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Grade Assignment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2),
            "Available Courses",
            0, 0,
            new Font("Arial", Font.BOLD, 14)
        ));

        String[] columnNames = {"Option", "Course ID", "Course Name"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        coursesTable = new JTable(tableModel);
        coursesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        coursesTable.setRowHeight(30);
        coursesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        coursesTable.getTableHeader().setBackground(ACCENT_COLOR);
        coursesTable.getTableHeader().setForeground(Color.WHITE);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < coursesTable.getColumnCount(); i++) {
            coursesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(coursesTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2),
            "Enter Grade",
            0, 0,
            new Font("Arial", Font.BOLD, 14)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel courseLabel = new JLabel("Select course from table above");
        courseLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        courseLabel.setForeground(new Color(100, 100, 100));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        inputPanel.add(courseLabel, gbc);

        JLabel scoreLabel = new JLabel("Score (0-100):");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        inputPanel.add(scoreLabel, gbc);

        scoreField = new JTextField(10);
        scoreField.setFont(new Font("Arial", Font.PLAIN, 14));
        scoreField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        inputPanel.add(scoreField, gbc);

        JButton submitButton = createStyledButton("Submit Grade", SUCCESS_COLOR);
        submitButton.addActionListener(e -> handleGradeSubmission());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        inputPanel.add(submitButton, gbc);

        resultLabel = new JLabel("");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        inputPanel.add(resultLabel, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(245, 245, 245));
        
        JButton newStudentButton = createStyledButton("Grade Another Student", INFO_COLOR);
        newStudentButton.addActionListener(e -> {
            studentIdField.setText("");
            studentInfoArea.setText("");
            scoreField.setText("");
            resultLabel.setText("");
            cardLayout.show(mainPanel, "STUDENT_INPUT");
        });
        
        JButton finishButton = createStyledButton("Finish & Exit", DANGER_COLOR);
        finishButton.addActionListener(e -> handleExit());
        
        bottomPanel.add(newStudentButton);
        bottomPanel.add(finishButton);

        panel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(tablePanel);
        centerPanel.add(inputPanel);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadCoursesTable() {
        tableModel.setRowCount(0);
        if (availableCourses != null) {
            for (Map.Entry<Integer, Map<String, Object>> entry : availableCourses.entrySet()) {
                tableModel.addRow(new Object[]{
                    entry.getKey(),
                    entry.getValue().get("course_Id"),
                    entry.getValue().get("course_name")
                });
            }
        }
    }

    private void handleGradeSubmission() {
        int selectedRow = coursesTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a course from the table!",
                "No Course Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String scoreText = scoreField.getText().trim();
        if (scoreText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a score!",
                "Input Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        double score;
        try {
            score = Double.parseDouble(scoreText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid score format! Please enter a number.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (score < 0 || score > 100) {
            JOptionPane.showMessageDialog(this,
                "Score must be between 0 and 100!",
                "Invalid Score",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int option = (int) tableModel.getValueAt(selectedRow, 0);
            String courseId = (String) availableCourses.get(option).get("course_Id");
            String courseName = (String) availableCourses.get(option).get("course_name");

            Map<String, Object> gradeInfo = dbManager.calculateGrade(score);
            String grade = (String) gradeInfo.get("grade");

            dbManager.saveGrade(conn, currentStudentId, courseId, score, grade);

            resultLabel.setText(String.format(
                "Grade Saved! Course: %s | Score: %.2f | Grade: %s",
                courseId, score, grade
            ));
            resultLabel.setForeground(SUCCESS_COLOR);

            scoreField.setText("");
            coursesTable.clearSelection();

            JOptionPane.showMessageDialog(this,
                String.format(
                    "Grade Successfully Saved!\n\n" +
                    "Course: %s - %s\n" +
                    "Score: %.2f\n" +
                    "Grade: %s",
                    courseId, courseName, score, grade
                ),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Database Error: " + e.getMessage(),
                "SQL Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void handleExit() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            closeConnection();
            dispose();
            MainPageTeacherGUI.main(null);
        }
    }

    private void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Use a simpler look and feel that respects colors
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                
                // Override button UI to respect colors
                UIManager.put("Button.background", new Color(39, 174, 96));
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("Button.opaque", true);
                UIManager.put("Button.contentAreaFilled", true);
                UIManager.put("Button.border", BorderFactory.createLineBorder(new Color(200, 200, 200)));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            GradingGUI gui = new GradingGUI();
            gui.setVisible(true);
        });
    }
}