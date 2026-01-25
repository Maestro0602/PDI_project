package Frontend;

import Backend.src.database.GradingManagement;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

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
    private JPanel teacherInfoPanel;
    
    private JTextField studentIdField;
    private JPanel studentInfoPanel;
    
    private JTable coursesTable;
    private DefaultTableModel tableModel;
    private JTextField scoreField;
    private JLabel statusLabel;

    // Modern color scheme matching MainpageTeacher
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_PINK = new Color(236, 72, 153);
    
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
        setSize(1000, 720);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createTeacherLoginPanel(), "TEACHER_LOGIN");
        mainPanel.add(createStudentInputPanel(), "STUDENT_INPUT");
        mainPanel.add(createGradingPanel(), "GRADING");

        add(mainPanel);
        cardLayout.show(mainPanel, "TEACHER_LOGIN");
    }

    private JPanel createTeacherLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(241, 245, 249),
                    getWidth(), getHeight(), new Color(226, 232, 240)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(147, 197, 253, 25));
                g2d.fillOval(-80, -80, 240, 240);
                g2d.fillOval(getWidth() - 160, getHeight() - 160, 240, 240);
            }
        };

        // Header with back button
        JPanel headerPanel = createStyledHeaderWithBackButton("Teacher Login", "Verify your identity to continue", "👨‍🏫", ACCENT_BLUE);
        
        // Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JPanel loginCard = createLoginCard();
        contentPanel.add(loginCard);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createLoginCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 12));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 3, 20, 20);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setLayout(new BorderLayout(0, 20));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        card.setPreferredSize(new Dimension(500, 350));

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        JLabel idLabel = new JLabel("Teacher ID");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(TEXT_PRIMARY);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        teacherIdField = new JTextField();
        teacherIdField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        teacherIdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        teacherIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        teacherIdField.setAlignmentX(Component.LEFT_ALIGNMENT);

        teacherIdField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                teacherIdField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_BLUE, 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                teacherIdField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        teacherInfoPanel = createInfoPanel();
        teacherInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton loginButton = createStyledButton("Login & Continue", ACCENT_BLUE, 400, 45);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.addActionListener(e -> handleTeacherLogin());

        teacherIdField.addActionListener(e -> loginButton.doClick());

        formPanel.add(idLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(teacherIdField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(teacherInfoPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        formPanel.add(loginButton);

        card.add(formPanel, BorderLayout.CENTER);

        return card;
    }

    private void handleTeacherLogin() {
        currentTeacherId = teacherIdField.getText().trim();
        
        if (currentTeacherId.isEmpty()) {
            showErrorDialog("Please enter a Teacher ID");
            return;
        }

        try {
            currentTeacher = dbManager.getTeacherInfo(conn, currentTeacherId);
            
            if (currentTeacher == null) {
                updateInfoPanel(teacherInfoPanel, "❌ Teacher not found!", ACCENT_RED);
                showErrorDialog("Teacher not found!");
                return;
            }

            String infoText = String.format(
                "<html><b>✓ Verified</b><br>" +
                "Department: %s<br>" +
                "Major: %s</html>",
                currentTeacher.get("department"),
                currentTeacher.get("major")
            );
            updateInfoPanel(teacherInfoPanel, infoText, ACCENT_GREEN);

            int result = JOptionPane.showConfirmDialog(this,
                "Teacher verified successfully!\nProceed to student input?",
                "Success",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
            
            if (result == JOptionPane.YES_OPTION) {
                cardLayout.show(mainPanel, "STUDENT_INPUT");
            }
        } catch (SQLException e) {
            showErrorDialog("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private JPanel createStudentInputPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(241, 245, 249),
                    getWidth(), getHeight(), new Color(226, 232, 240)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(147, 197, 253, 25));
                g2d.fillOval(-80, -80, 240, 240);
                g2d.fillOval(getWidth() - 160, getHeight() - 160, 240, 240);
            }
        };

        JPanel headerPanel = createStyledHeaderWithBackToTeacherButton("Student Verification", "Verify student eligibility for grading", "🎓", ACCENT_PURPLE);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JPanel studentCard = createStudentCard();
        contentPanel.add(studentCard);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel createStyledHeaderWithBackToTeacherButton(String title, String subtitle, String icon, Color accentColor) {
        JPanel headerPanel = createStyledHeader(title, subtitle, icon, accentColor);
        
        // Add back button to the right side
        JButton backButton = new JButton("← Back to Teacher Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(71, 85, 105));
                } else if (getModel().isRollover()) {
                    g2d.setColor(TEXT_SECONDARY);
                } else {
                    g2d.setColor(new Color(100, 116, 139));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };

        backButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backButton.setForeground(Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(220, 45));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { backButton.repaint(); }
            @Override
            public void mouseExited(MouseEvent evt) { backButton.repaint(); }
        });
        
        backButton.addActionListener(e -> {
            teacherIdField.setText("");
            updateInfoPanel(teacherInfoPanel, "", TEXT_SECONDARY);
            cardLayout.show(mainPanel, "TEACHER_LOGIN");
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createStudentCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 12));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 3, 20, 20);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setLayout(new BorderLayout(0, 20));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        card.setPreferredSize(new Dimension(550, 400));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        JLabel idLabel = new JLabel("Student ID");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(TEXT_PRIMARY);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        studentIdField = new JTextField();
        studentIdField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        studentIdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        studentIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        studentIdField.setAlignmentX(Component.LEFT_ALIGNMENT);

        studentIdField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                studentIdField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_PURPLE, 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                studentIdField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        studentInfoPanel = createInfoPanel();
        studentInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton verifyButton = createStyledButton("Verify & Proceed", ACCENT_PURPLE, 450, 45);
        verifyButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        verifyButton.addActionListener(e -> handleStudentVerification());

        studentIdField.addActionListener(e -> verifyButton.doClick());

        formPanel.add(idLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(studentIdField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(studentInfoPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        formPanel.add(verifyButton);

        card.add(formPanel, BorderLayout.CENTER);

        return card;
    }

    private void handleStudentVerification() {
        currentStudentId = studentIdField.getText().trim();
        
        if (currentStudentId.isEmpty()) {
            showErrorDialog("Please enter a Student ID");
            return;
        }

        try {
            currentStudent = dbManager.getStudentInfo(conn, currentStudentId);
            
            if (currentStudent == null) {
                updateInfoPanel(studentInfoPanel, "❌ Student not found!", ACCENT_RED);
                showErrorDialog("Student not found!");
                return;
            }

            String infoText = String.format(
                "<html><b>Student Found</b><br>" +
                "Department: %s<br>" +
                "Major: %s<br></html>",
                currentStudent.get("department"),
                currentStudent.get("major")
            );
            updateInfoPanel(studentInfoPanel, infoText, ACCENT_BLUE);

            if (!dbManager.canTeacherGradeStudent(currentTeacher, currentStudent)) {
                updateInfoPanel(studentInfoPanel, 
                    "<html><b>❌ Access Denied</b><br>Department/Major mismatch!</html>", 
                    ACCENT_RED);
                showErrorDialog("Access Denied!\nTeacher's Department/Major does not match Student's.");
                return;
            }

            updateInfoPanel(studentInfoPanel, 
                infoText + "<br><span style='color: #22c55e;'><b>✓ Permission Granted</b></span>", 
                ACCENT_GREEN);

            availableCourses = dbManager.getTeacherCourses(conn, currentTeacherId);
            
            if (availableCourses == null || availableCourses.isEmpty()) {
                showErrorDialog("No courses assigned to this teacher!");
                return;
            }

            int result = JOptionPane.showConfirmDialog(this,
                "Student verified successfully!\nProceed to grading?",
                "Success",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
            
            if (result == JOptionPane.YES_OPTION) {
                loadCoursesTable();
                cardLayout.show(mainPanel, "GRADING");
            }
        } catch (SQLException e) {
            showErrorDialog("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private JPanel createGradingPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(241, 245, 249),
                    getWidth(), getHeight(), new Color(226, 232, 240)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(147, 197, 253, 25));
                g2d.fillOval(-80, -80, 240, 240);
                g2d.fillOval(getWidth() - 160, getHeight() - 160, 240, 240);
            }
        };

        JPanel headerPanel = createStyledHeader("Assign Grades", "Select course and enter student scores", "📊", ACCENT_GREEN);
        
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 30, 30));

        JPanel tableCard = createTableCard();
        JPanel inputCard = createInputCard();
        
        contentPanel.add(tableCard, BorderLayout.CENTER);
        contentPanel.add(inputCard, BorderLayout.SOUTH);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTableCard() {
        JPanel card = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 12));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 3, 20, 20);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Available Courses");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);

        String[] columnNames = {"#", "Course ID", "Course Name"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        coursesTable = new JTable(tableModel);
        coursesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        coursesTable.setRowHeight(50);
        coursesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        coursesTable.getTableHeader().setBackground(new Color(248, 250, 252));
        coursesTable.getTableHeader().setForeground(TEXT_PRIMARY);
        coursesTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(203, 213, 225)));
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursesTable.setSelectionBackground(new Color(219, 234, 254));
        coursesTable.setSelectionForeground(TEXT_PRIMARY);
        coursesTable.setShowGrid(true);
        coursesTable.setGridColor(new Color(226, 232, 240));

        coursesTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        coursesTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        coursesTable.getColumnModel().getColumn(2).setPreferredWidth(400);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        coursesTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(coursesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        scrollPane.setBackground(Color.WHITE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private JPanel createInputCard() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 12));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 3, 20, 20);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        contentPanel.setOpaque(false);

        JLabel scoreLabel = new JLabel("Score (0-100):");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        scoreLabel.setForeground(TEXT_PRIMARY);

        scoreField = new JTextField(15);
        scoreField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        scoreField.setPreferredSize(new Dimension(200, 45));
        scoreField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        scoreField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                scoreField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_GREEN, 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                scoreField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        JButton submitButton = createStyledButton("✓ Submit Grade", ACCENT_GREEN, 180, 45);
        submitButton.addActionListener(e -> handleGradeSubmission());

        JButton newStudentButton = createStyledButton("Grade Another Student", ACCENT_PURPLE, 200, 45);
        newStudentButton.addActionListener(e -> {
            studentIdField.setText("");
            updateInfoPanel(studentInfoPanel, "", TEXT_SECONDARY);
            scoreField.setText("");
            statusLabel.setText("");
            cardLayout.show(mainPanel, "STUDENT_INPUT");
        });

        JButton finishButton = createStyledButton("Finish & Exit", ACCENT_RED, 150, 45);
        finishButton.addActionListener(e -> handleExit());

        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(TEXT_SECONDARY);

        contentPanel.add(scoreLabel);
        contentPanel.add(scoreField);
        contentPanel.add(submitButton);
        contentPanel.add(newStudentButton);
        contentPanel.add(finishButton);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(statusLabel);

        card.add(contentPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
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
            showErrorDialog("Please select a course from the table!");
            return;
        }

        String scoreText = scoreField.getText().trim();
        if (scoreText.isEmpty()) {
            showErrorDialog("Please enter a score!");
            return;
        }

        double score;
        try {
            score = Double.parseDouble(scoreText);
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid score format! Please enter a number.");
            return;
        }

        if (score < 0 || score > 100) {
            showErrorDialog("Score must be between 0 and 100!");
            return;
        }

        try {
            int option = (int) tableModel.getValueAt(selectedRow, 0);
            String courseId = (String) availableCourses.get(option).get("course_Id");
            String courseName = (String) availableCourses.get(option).get("course_name");

            Map<String, Object> gradeInfo = dbManager.calculateGrade(score);
            String grade = (String) gradeInfo.get("grade");

            dbManager.saveGrade(conn, currentStudentId, courseId, score, grade);

            statusLabel.setText(String.format("✓ Saved: %s | Score: %.2f | Grade: %s", courseId, score, grade));
            statusLabel.setForeground(ACCENT_GREEN);

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
            showErrorDialog("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private JPanel createStyledHeader(String title, String subtitle, String icon, Color accentColor) {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);

                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 200),
                    0, getHeight(), new Color(
                        Math.min(accentColor.getRed() + 50, 255),
                        Math.min(accentColor.getGreen() + 50, 255),
                        Math.min(accentColor.getBlue() + 50, 255), 150
                    )
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 8));
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, accentColor,
                    getWidth(), getHeight(), accentColor.darker()
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(subtitleLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        headerPanel.add(titleContainer, BorderLayout.WEST);

        return headerPanel;
    }
    
    private JPanel createStyledHeaderWithBackButton(String title, String subtitle, String icon, Color accentColor) {
        JPanel headerPanel = createStyledHeader(title, subtitle, icon, accentColor);
        
        // Add back button to the right side
        JButton backButton = createBackToMainButton();
        backButton.addActionListener(e -> handleExit());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(248, 250, 252));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(400, 120));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel label = new JLabel("<html><i>Information will appear here...</i></html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_SECONDARY);
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private void updateInfoPanel(JPanel panel, String text, Color color) {
        panel.removeAll();
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(color);
        panel.add(label, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    private JButton createStyledButton(String text, Color color, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(color);
                } else {
                    g2d.setColor(color.darker().darker());
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(width, height));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(MouseEvent evt) { button.repaint(); }
        });

        return button;
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private JButton createBackToMainButton() {
        JButton button = new JButton("← Back to Main") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background color based on state
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(185, 28, 28)); // darker red
                } else if (getModel().isRollover()) {
                    g2d.setColor(ACCENT_RED);
                } else {
                    g2d.setColor(new Color(127, 29, 29)); // dark maroon like image
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // White text
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(MouseEvent evt) { button.repaint(); }
        });

        return button;
    }

    private void handleExit() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            closeConnection();
            dispose();
            MainpageTeacher.main(null);
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
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            GradingGUI gui = new GradingGUI();
            gui.setVisible(true);
        });
    }
}