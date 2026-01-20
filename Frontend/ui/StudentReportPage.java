package Frontend.ui;

import Backend.src.database.MajorManager;
import Backend.src.database.StudentInfoManager;
import java.awt.*;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * StudentReportPage - Displays comprehensive student academic reports
 * Loads data from database - grades default to 0 if not available
 */
public class StudentReportPage extends JFrame {

    // Colors matching the mainpage theme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_PINK = new Color(236, 72, 153);
    private static final Color ACCENT_TEAL = new Color(20, 184, 166);
    private static final Color SEARCH_BG = new Color(248, 250, 252);
    private static final Color BG_GRADIENT_START = new Color(241, 245, 249);
    private static final Color BG_GRADIENT_END = new Color(226, 232, 240);
    private static final Color TABLE_HEADER_BG = new Color(241, 245, 249);
    // private static final Color TABLE_STRIPE = new Color(248, 250, 252);

    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JComboBox<String> yearFilterComboBox;
    private List<StudentReport> allStudentReports;
    private List<StudentReport> displayedReports;
    private JPanel reportsContainer;

    public StudentReportPage() {
        loadDataFromDatabase();
        initComponent();
        loadAllReports();
    }

    // ==================== DATABASE DATA LOADING ====================
    
    private void loadDataFromDatabase() {
        allStudentReports = new ArrayList<>();
        
        // Load students from database
        String[][] dbStudents = StudentInfoManager.getAllStudentsArray();
        if (dbStudents != null && dbStudents.length > 0) {
            for (String[] student : dbStudents) {
                if (student != null && student.length >= 4) {
                    String studentId = student[1] != null ? student[1] : "";
                    String studentName = student[0] != null ? student[0] : "";
                    String gender = student[2] != null ? student[2] : "";
                    String year = student[3] != null ? student[3] : "";
                    
                    // Get courses for this student from MajorManager
                    String[] courseInfo = MajorManager.getFullDepartmentMajorCourse(studentId);
                    
                    List<GradeEntry> grades = new ArrayList<>();
                    if (courseInfo != null && courseInfo.length >= 6) {
                        // courseInfo: [department, major, Course1, Course2, Course3, Course4]
                        for (int i = 2; i < 6; i++) {
                            String courseName = courseInfo[i];
                            if (courseName != null && !courseName.isEmpty()) {
                                // Default grade is 0 (no grades assigned yet)
                                grades.add(new GradeEntry(courseName, 0.0, 3, "Current Semester"));
                            }
                        }
                    }
                    
                    // If no courses found, add placeholder
                    if (grades.isEmpty()) {
                        grades.add(new GradeEntry("No courses assigned", 0.0, 0, "N/A"));
                    }
                    
                    allStudentReports.add(new StudentReport(studentId, studentName, gender, year, grades));
                }
            }
        }
        // If no students found, list will be empty (no fake data)
    }

    // ==================== UI INITIALIZATION ====================

    public void initComponent() {
        setTitle("Student Academic Reports");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1150, 850);
        setLocationRelativeTo(null);

        // Main background panel with gradient
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Main gradient background
                GradientPaint mainGradient = new GradientPaint(
                        0, 0, BG_GRADIENT_START,
                        getWidth(), getHeight(), BG_GRADIENT_END
                );
                g2d.setPaint(mainGradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles
                g2d.setColor(new Color(168, 85, 247, 15));
                g2d.fillOval(-100, -100, 300, 300);
                
                g2d.setColor(new Color(59, 130, 246, 12));
                g2d.fillOval(getWidth() - 200, -50, 350, 350);
                
                g2d.setColor(new Color(236, 72, 153, 10));
                g2d.fillOval(getWidth() - 250, getHeight() - 200, 400, 400);
                
                g2d.setColor(new Color(34, 197, 94, 8));
                g2d.fillOval(-150, getHeight() - 150, 350, 350);
            }
        };

        // Header panel
        JPanel headerPanel = createHeaderPanel();

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(15, 35, 35, 35));

        // Search and filter panel
        JPanel searchPanel = createSearchPanel();
        contentPanel.add(searchPanel, BorderLayout.NORTH);

        // Reports container with scroll
        reportsContainer = new JPanel();
        reportsContainer.setLayout(new BoxLayout(reportsContainer, BoxLayout.Y_AXIS));
        reportsContainer.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(reportsContainer);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Hide the scrollbar but keep scrolling functionality
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(backgroundPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Simple shadow
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);

                // Main card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        // Icon badge
        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, ACCENT_PINK,
                    getWidth(), getHeight(), ACCENT_PURPLE
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("R");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        iconLabel.setForeground(Color.WHITE);
        iconBadge.add(iconLabel);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Student Academic Reports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("View grades, GPA, and academic performance");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(subtitleLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        JButton backButton = createBackButton();

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Simple shadow
                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 2, 15, 15);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 12));
        panel.setBorder(new EmptyBorder(8, 15, 8, 15));

        // Search field
        JPanel searchContainer = createStyledTextField("Search by name or ID...", 200);
        
        JButton searchBtn = createGradientButton("Search", ACCENT_BLUE, new Color(37, 99, 235));
        searchBtn.addActionListener(e -> searchReports());

        // Filter label and dropdown
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        filterLabel.setForeground(TEXT_PRIMARY);

        String[] filters = {"All Students", "High GPA (3.5+)", "Good Standing (2.0+)", "Needs Improvement (<2.0)"};
        filterComboBox = createStyledComboBox(filters);
        filterComboBox.addActionListener(e -> filterReports());

        // Year filter
        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        yearLabel.setForeground(TEXT_PRIMARY);

        String[] years = {"All Years", "Year 1", "Year 2", "Year 3", "Year 4"};
        yearFilterComboBox = createStyledComboBox(years);
        yearFilterComboBox.addActionListener(e -> filterReports());

        // Action buttons
        JButton refreshBtn = createGradientButton("Refresh", ACCENT_TEAL, new Color(13, 148, 136));
        refreshBtn.addActionListener(e -> loadAllReports());

        JButton printBtn = createGradientButton("Print", ACCENT_PURPLE, new Color(139, 92, 246));
        printBtn.addActionListener(e -> printReports());

        panel.add(searchContainer);
        panel.add(searchBtn);
        panel.add(Box.createRigidArea(new Dimension(15, 0)));
        panel.add(filterLabel);
        panel.add(filterComboBox);
        panel.add(Box.createRigidArea(new Dimension(15, 0)));
        panel.add(yearLabel);
        panel.add(yearFilterComboBox);
        panel.add(Box.createRigidArea(new Dimension(15, 0)));
        panel.add(refreshBtn);
        panel.add(printBtn);

        return panel;
    }

    private JPanel createStyledTextField(String placeholder, int width) {
        JPanel container = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(SEARCH_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                g2d.setColor(new Color(203, 213, 225));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(0, 15, 0, 15));
        container.setPreferredSize(new Dimension(width, 42));

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setForeground(TEXT_PRIMARY);
        
        // Placeholder text
        searchField.setText(placeholder);
        searchField.setForeground(TEXT_SECONDARY);
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals(placeholder)) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_PRIMARY);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(placeholder);
                    searchField.setForeground(TEXT_SECONDARY);
                }
            }
        });

        container.add(searchField, BorderLayout.CENTER);
        return container;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setPreferredSize(new Dimension(150, 36));
        comboBox.setBackground(CARD_BG);
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        comboBox.setFocusable(false);
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Custom UI to hide the blue arrow box
        comboBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton();
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setBorderPainted(false);
                button.setText("▼");
                button.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                button.setForeground(TEXT_SECONDARY);
                return button;
            }
        });
        
        return comboBox;
    }

    // ==================== DATA LOADING ====================

    private void loadAllReports() {
        displayedReports = new ArrayList<>(allStudentReports);
        displayReports(displayedReports);
    }

    private void searchReports() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty() || searchTerm.equals("search by name or id...")) {
            loadAllReports();
            return;
        }
        
        displayedReports = new ArrayList<>();
        for (StudentReport report : allStudentReports) {
            if (report.studentName.toLowerCase().contains(searchTerm) ||
                report.studentID.toLowerCase().contains(searchTerm)) {
                displayedReports.add(report);
            }
        }
        displayReports(displayedReports);
    }

    private void filterReports() {
        if (allStudentReports == null) return;

        int filterIndex = filterComboBox.getSelectedIndex();
        String selectedYear = (String) yearFilterComboBox.getSelectedItem();
        displayedReports = new ArrayList<>();

        for (StudentReport report : allStudentReports) {
            double gpa = report.calculateGPA();
            boolean passesGPAFilter = false;
            boolean passesYearFilter = false;
            
            // GPA filter
            switch (filterIndex) {
                case 1: passesGPAFilter = gpa >= 3.5; break;
                case 2: passesGPAFilter = gpa >= 2.0; break;
                case 3: passesGPAFilter = gpa < 2.0; break;
                default: passesGPAFilter = true;
            }
            
            // Year filter
            if (selectedYear.equals("All Years")) {
                passesYearFilter = true;
            } else {
                passesYearFilter = report.year.equals(selectedYear);
            }
            
            if (passesGPAFilter && passesYearFilter) {
                displayedReports.add(report);
            }
        }
        displayReports(displayedReports);
    }

    private void displayReports(List<StudentReport> reports) {
        reportsContainer.removeAll();

        if (reports == null || reports.isEmpty()) {
            reportsContainer.add(createNoDataPanel());
        } else {
            reportsContainer.add(createSummaryCard(reports));
            reportsContainer.add(Box.createRigidArea(new Dimension(0, 15)));

            for (StudentReport report : reports) {
                reportsContainer.add(createStudentReportCard(report));
                reportsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        reportsContainer.revalidate();
        reportsContainer.repaint();
    }

    // ==================== UI COMPONENTS ====================

    private JPanel createNoDataPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 20, 20);
                
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(800, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel iconLabel = new JLabel("?");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 56));
        iconLabel.setForeground(new Color(203, 213, 225));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msgLabel = new JLabel("No Student Reports Found");
        msgLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        msgLabel.setForeground(TEXT_SECONDARY);
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hintLabel = new JLabel("Try a different search term or filter");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hintLabel.setForeground(new Color(148, 163, 184));
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(iconLabel);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(msgLabel);
        content.add(Box.createRigidArea(new Dimension(0, 8)));
        content.add(hintLabel);

        panel.add(content);
        return panel;
    }

    private JPanel createSummaryCard(List<StudentReport> reports) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 20, 20);

                // Card background with gradient overlay
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Top gradient accent
                GradientPaint topGradient = new GradientPaint(
                    0, 0, ACCENT_PURPLE,
                    getWidth(), 0, ACCENT_PINK
                );
                g2d.setPaint(topGradient);
                g2d.fillRoundRect(0, 0, getWidth(), 6, 20, 20);
                
                // Subtle gradient overlay
                GradientPaint overlay = new GradientPaint(
                    0, 0, new Color(99, 102, 241, 8),
                    getWidth(), getHeight(), new Color(236, 72, 153, 5)
                );
                g2d.setPaint(overlay);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setOpaque(false);
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Calculate statistics - only show total students
        int totalStudents = reports.size();

        card.add(createEnhancedStatItem("Total Students", String.valueOf(totalStudents), ACCENT_BLUE, "Students enrolled"));

        return card;
    }

    private JPanel createEnhancedStatItem(String label, String value, Color color, String subtitle) {
        JPanel item = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background with color tint only - no bar
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 15));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        item.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        item.setOpaque(false);
        item.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);

        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelLabel.setForeground(TEXT_PRIMARY);

        item.add(valueLabel);
        item.add(labelLabel);

        return item;
    }

    private JPanel createStudentReportCard(StudentReport report) {
        double gpa = report.calculateGPA();
        Color gpaColor = getGPAColor(gpa);
        
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 12, 12);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout(10, 5));
        card.setBorder(new EmptyBorder(12, 15, 12, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        // Left section: Student info + grades
        JPanel leftSection = new JPanel();
        leftSection.setLayout(new BoxLayout(leftSection, BoxLayout.Y_AXIS));
        leftSection.setOpaque(false);

        // Student name and details row
        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        nameRow.setOpaque(false);
        
        JLabel nameLabel = new JLabel(report.studentName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(TEXT_PRIMARY);
        
        nameRow.add(nameLabel);
        nameRow.add(createInfoChip("ID: " + report.studentID, ACCENT_BLUE));
        nameRow.add(createInfoChip(report.year, ACCENT_TEAL));

        // Grades row (IELTS style)
        JPanel gradesPanel = createEnhancedGradesTable(report.grades);

        leftSection.add(nameRow);
        leftSection.add(Box.createRigidArea(new Dimension(0, 8)));
        leftSection.add(gradesPanel);

        // Right section: GPA badge (compact)
        JPanel gpaBadge = createCompactGPABadge(gpa);

        card.add(leftSection, BorderLayout.CENTER);
        card.add(gpaBadge, BorderLayout.EAST);

        return card;
    }

    private JPanel createCompactGPABadge(double gpa) {
        Color gpaColor = getGPAColor(gpa);
        
        JPanel badge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(gpaColor.getRed(), gpaColor.getGreen(), gpaColor.getBlue(), 20));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        badge.setOpaque(false);
        badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
        badge.setBorder(new EmptyBorder(10, 15, 10, 15));
        badge.setPreferredSize(new Dimension(70, 60));

        JLabel gpaLabel = new JLabel(String.format("%.2f", gpa));
        gpaLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gpaLabel.setForeground(gpaColor);
        gpaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel gpaTextLabel = new JLabel("GPA");
        gpaTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        gpaTextLabel.setForeground(TEXT_SECONDARY);
        gpaTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        badge.add(gpaLabel);
        badge.add(gpaTextLabel);

        return badge;
    }

    private JPanel createStudentInfoPanel(StudentReport report) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel nameLabel = new JLabel(report.studentName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setForeground(TEXT_PRIMARY);

        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        detailsPanel.setOpaque(false);

        detailsPanel.add(createInfoChip("ID: " + report.studentID, ACCENT_BLUE));
        detailsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        detailsPanel.add(createInfoChip(report.gender, ACCENT_PURPLE));
        detailsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        detailsPanel.add(createInfoChip(report.year, ACCENT_TEAL));

        panel.add(nameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(detailsPanel);

        return panel;
    }

    private JPanel createInfoChip(String text, Color color) {
        JPanel chip = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            }
        };
        chip.setOpaque(false);
        chip.setBorder(new EmptyBorder(4, 10, 4, 10));
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(color);
        chip.add(label);
        
        return chip;
    }

    private JPanel createEnhancedGPABadge(double gpa, String classification) {
        Color gpaColor = getGPAColor(gpa);
        
        JPanel badge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background with gradient
                GradientPaint bgGradient = new GradientPaint(
                    0, 0, new Color(gpaColor.getRed(), gpaColor.getGreen(), gpaColor.getBlue(), 25),
                    getWidth(), getHeight(), new Color(gpaColor.getRed(), gpaColor.getGreen(), gpaColor.getBlue(), 15)
                );
                g2d.setPaint(bgGradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                // Border
                g2d.setColor(gpaColor);
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 14, 14);
            }
        };
        badge.setOpaque(false);
        badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
        badge.setBorder(new EmptyBorder(15, 25, 15, 25));
        badge.setPreferredSize(new Dimension(110, 90));

        JLabel gpaLabel = new JLabel(String.format("%.2f", gpa));
        gpaLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        gpaLabel.setForeground(gpaColor);
        gpaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel gpaTextLabel = new JLabel("GPA");
        gpaTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gpaTextLabel.setForeground(TEXT_SECONDARY);
        gpaTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        badge.add(gpaLabel);
        badge.add(gpaTextLabel);

        return badge;
    }

    private JPanel createEnhancedGradesTable(List<GradeEntry> grades) {
        // IELTS-style compact horizontal layout
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBorder(new EmptyBorder(5, 0, 5, 0));

        for (int i = 0; i < grades.size(); i++) {
            GradeEntry grade = grades.get(i);
            JPanel subjectCard = createIELTSStyleGradeItem(grade);
            panel.add(subjectCard);
            
            // Add separator between items (except last)
            if (i < grades.size() - 1) {
                JLabel separator = new JLabel("|");
                separator.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                separator.setForeground(new Color(200, 200, 200));
                separator.setBorder(new EmptyBorder(0, 8, 0, 8));
                panel.add(separator);
            }
        }

        return panel;
    }

    private JPanel createIELTSStyleGradeItem(GradeEntry grade) {
        Color gradeColor = getScoreColor(grade.score);
        
        JPanel item = new JPanel();
        item.setOpaque(false);
        item.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));

        // Subject abbreviation
        JLabel subjectLabel = new JLabel(getShortSubjectName(grade.subjectName) + ":");
        subjectLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        subjectLabel.setForeground(TEXT_SECONDARY);

        // Score
        JLabel scoreLabel = new JLabel(String.format("%.0f", grade.score));
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        scoreLabel.setForeground(gradeColor);

        item.add(subjectLabel);
        item.add(scoreLabel);

        return item;
    }

    private String getShortSubjectName(String fullName) {
        // Shorten common subject names
        if (fullName.length() <= 6) return fullName;
        if (fullName.equalsIgnoreCase("Mathematics")) return "Math";
        if (fullName.equalsIgnoreCase("Physics")) return "Phys";
        if (fullName.equalsIgnoreCase("Chemistry")) return "Chem";
        if (fullName.equalsIgnoreCase("Biology")) return "Bio";
        if (fullName.equalsIgnoreCase("English")) return "Eng";
        if (fullName.equalsIgnoreCase("Computer Science")) return "CS";
        return fullName.substring(0, 5) + ".";
    }

    private Color getScoreColor(double score) {
        if (score >= 90) return ACCENT_GREEN;
        if (score >= 80) return ACCENT_BLUE;
        if (score >= 70) return ACCENT_TEAL;
        if (score >= 60) return ACCENT_ORANGE;
        return ACCENT_RED;
    }

    private JPanel createFooterSummary(StudentReport report) {
        JPanel footer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(TABLE_HEADER_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        footer.setOpaque(false);
        footer.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 12));
        footer.setBorder(new EmptyBorder(5, 10, 5, 10));

        Color gpaColor = getGPAColor(report.calculateGPA());

        JLabel creditsLabel = new JLabel("Total Credits: " + report.getTotalCredits());
        creditsLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        creditsLabel.setForeground(TEXT_PRIMARY);

        JLabel statusLabel = new JLabel("Status: " + report.getGPAClassification());
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(gpaColor);

        JLabel semesterLabel = new JLabel("Semester: Fall 2025");
        semesterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        semesterLabel.setForeground(TEXT_SECONDARY);

        footer.add(creditsLabel);
        footer.add(createSeparator());
        footer.add(statusLabel);
        footer.add(createSeparator());
        footer.add(semesterLabel);

        return footer;
    }

    private JLabel createSeparator() {
        JLabel sep = new JLabel("|");
        sep.setForeground(new Color(203, 213, 225));
        return sep;
    }

    private Color getGPAColor(double gpa) {
        if (gpa >= 3.5) return ACCENT_GREEN;
        if (gpa >= 3.0) return ACCENT_BLUE;
        if (gpa >= 2.5) return ACCENT_TEAL;
        if (gpa >= 2.0) return ACCENT_ORANGE;
        return ACCENT_RED;
    }

    // ==================== BUTTONS ====================

    private JButton createGradientButton(String text, Color color1, Color color2) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient;
                if (getModel().isPressed()) {
                    gradient = new GradientPaint(0, 0, color2.darker(), 0, getHeight(), color1.darker());
                } else if (getModel().isRollover()) {
                    gradient = new GradientPaint(0, 0, color1.brighter(), 0, getHeight(), color2.brighter());
                } else {
                    gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                }
                
                // Shadow on hover
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 0, 0, 20));
                    g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 12, 12);
                }
                
                g2d.setPaint(gradient);
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
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(115, 42));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createBackButton() {
        JButton button = new JButton("< Back") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    GradientPaint gradient = new GradientPaint(0, 0, ACCENT_PINK.darker(), 0, getHeight(), ACCENT_PURPLE.darker());
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2d.setColor(Color.WHITE);
                } else if (getModel().isRollover()) {
                    GradientPaint gradient = new GradientPaint(0, 0, ACCENT_PINK, 0, getHeight(), ACCENT_PURPLE);
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2d.setColor(Color.WHITE);
                } else {
                    g2d.setColor(SEARCH_BG);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2d.setColor(new Color(203, 213, 225));
                    g2d.setStroke(new BasicStroke(1.5f));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                    g2d.setColor(TEXT_SECONDARY);
                }

                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 42));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            mainpageTeacher mainPage = new mainpageTeacher();
            mainPage.setVisible(true);
            dispose();
        });

        return button;
    }

    // ==================== PRINT FUNCTIONALITY ====================

    private void printReports() {
        if (displayedReports == null || displayedReports.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No reports to print!", 
                "Print Report", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder reportText = new StringBuilder();
        reportText.append("=".repeat(80)).append("\n");
        reportText.append("                    STUDENT ACADEMIC REPORT\n");
        reportText.append("                    Generated: ").append(java.time.LocalDate.now()).append("\n");
        reportText.append("=".repeat(80)).append("\n\n");

        for (StudentReport report : displayedReports) {
            reportText.append("Student: ").append(report.studentName).append("\n");
            reportText.append("ID: ").append(report.studentID).append(" | ");
            reportText.append("Gender: ").append(report.gender).append(" | ");
            reportText.append("Year: ").append(report.year).append("\n");
            reportText.append("-".repeat(60)).append("\n");

            reportText.append(String.format("%-25s %8s %8s %8s %10s%n", 
                "Subject", "Score", "Credits", "Grade", "Points"));
            reportText.append("-".repeat(60)).append("\n");

            for (GradeEntry grade : report.grades) {
                reportText.append(String.format("%-25s %8.1f %8d %8s %10.2f%n",
                    grade.subjectName, grade.score, grade.credits, 
                    grade.getLetterGrade(), grade.getGradePoint()));
            }

            reportText.append("-".repeat(60)).append("\n");
            reportText.append(String.format("GPA: %.2f (%s)%n", 
                report.calculateGPA(), report.getGPAClassification()));
            reportText.append(String.format("Total Credits: %d%n", report.getTotalCredits()));
            reportText.append("\n").append("=".repeat(80)).append("\n\n");
        }

        JTextArea textArea = new JTextArea(reportText.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        textArea.setEditable(false);
        textArea.setBackground(new Color(248, 250, 252));

        JScrollPane printScroll = new JScrollPane(textArea);
        printScroll.setPreferredSize(new Dimension(700, 500));

        int result = JOptionPane.showConfirmDialog(this, printScroll, 
            "Print Preview", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                textArea.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error printing: " + e.getMessage(), 
                    "Print Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== DATA CLASSES ====================

    /**
     * Grade entry for a single subject
     */
    static class GradeEntry {
        String subjectName;
        double score;
        int credits;
        String semester;

        GradeEntry(String subjectName, double score, int credits, String semester) {
            this.subjectName = subjectName != null ? subjectName : "";
            // Ensure score is never negative, default to 0
            this.score = score >= 0 ? score : 0;
            // Ensure credits is never negative, default to 0
            this.credits = credits >= 0 ? credits : 0;
            this.semester = semester != null ? semester : "";
        }

        String getLetterGrade() {
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

        double getGradePoint() {
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
    }

    /**
     * Student report containing all grades
     */
    static class StudentReport {
        String studentID;
        String studentName;
        String gender;
        String year;
        List<GradeEntry> grades;

        StudentReport(String studentID, String studentName, String gender, String year, List<GradeEntry> grades) {
            this.studentID = studentID != null ? studentID : "";
            this.studentName = studentName != null ? studentName : "";
            this.gender = gender != null ? gender : "";
            this.year = year != null ? year : "";
            this.grades = grades != null ? grades : new ArrayList<>();
        }

        double calculateGPA() {
            if (grades.isEmpty()) return 0.0;
            double totalPoints = 0;
            int totalCredits = 0;
            for (GradeEntry grade : grades) {
                totalPoints += grade.getGradePoint() * grade.credits;
                totalCredits += grade.credits;
            }
            return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
        }

        int getTotalCredits() {
            return grades.stream().mapToInt(g -> g.credits).sum();
        }

        String getGPAClassification() {
            double gpa = calculateGPA();
            if (gpa >= 3.7) return "Excellent";
            if (gpa >= 3.3) return "Very Good";
            if (gpa >= 3.0) return "Good";
            if (gpa >= 2.5) return "Satisfactory";
            if (gpa >= 2.0) return "Passing";
            return "Needs Improvement";
        }
    }

    // ==================== MAIN ====================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentReportPage().setVisible(true));
    }
}
