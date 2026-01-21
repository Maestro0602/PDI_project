package Frontend.ui;

import Backend.src.mainpage.OwnerMainPageService;
import Backend.src.session.UserSession;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class mainpageOwner extends JFrame {

    // Colors (matching other pages theme)
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
    private static final Color ACCENT_INDIGO = new Color(99, 102, 241);
    
    // Session reference
    private UserSession session;
    // Backend service
    private OwnerMainPageService ownerService;

    public mainpageOwner() {
        this.session = UserSession.getInstance();
        this.ownerService = new OwnerMainPageService();
        
        initComponent();
    }

    public void initComponent() {
        setTitle("Owner Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 720);
        setLocationRelativeTo(null);

        // Main background panel
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
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

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Content panel with cards
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(5, 30, 30, 30));

        JPanel cardsContainer = createCardsContainer();
        contentPanel.add(cardsContainer);

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

                // Card background with shadow
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);

                // Main card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);

                // Indigo gradient accent on the left
                GradientPaint indigoGradient = new GradientPaint(
                    0, 0, new Color(99, 102, 241, 200),
                    0, getHeight(), new Color(139, 92, 246, 150)
                );
                g2d.setPaint(indigoGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                // Subtle background pattern
                g2d.setColor(new Color(99, 102, 241, 8));
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        // Icon and title container
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        // Owner icon badge
        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(99, 102, 241),
                    getWidth(), getHeight(), new Color(139, 92, 246)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("👑");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Owner Dashboard");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(TEXT_PRIMARY);

        // Personalized welcome message using backend service
        String welcomeText = ownerService.getWelcomeMessage();
        JLabel subtitleLabel = new JLabel(welcomeText);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(welcomeLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(subtitleLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        JButton logoutButton = createLogoutButton();

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createCardsContainer() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        container.add(createOptionCard(
                "Student Statistics",
                "View student counts by department and gender",
                ACCENT_BLUE,
                "📊",
                this::showStudentStatistics
        ), gbc);

        gbc.gridx = 1;
        container.add(createOptionCard(
                "View All Students",
                "Browse and search all registered students",
                ACCENT_GREEN,
                "👥",
                this::showAllStudents
        ), gbc);

        gbc.gridx = 2;
        container.add(createOptionCard(
                "View All Teachers",
                "Browse registered teachers and their info",
                ACCENT_ORANGE,
                "👨‍🏫",
                this::showAllTeachers
        ), gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        container.add(createOptionCard(
                "Assign Teacher to Course",
                "Assign courses to teachers",
                ACCENT_PURPLE,
                "📚",
                this::showAssignTeacherCourse
        ), gbc);

        gbc.gridx = 1;
        container.add(createOptionCard(
                "View All Courses",
                "Browse available courses in system",
                ACCENT_BLUE,
                "📖",
                this::showAllCourses
        ), gbc);

        gbc.gridx = 2;
        container.add(createOptionCard(
                "Search Student",
                "Find students by name or ID",
                ACCENT_TEAL,
                "🔍",
                this::showSearchStudent
        ), gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 2;
        container.add(createOptionCard(
                "Create Account",
                "Create email accounts for students/teachers",
                ACCENT_PINK,
                "✉️",
                this::showCreateAccount
        ), gbc);

        return container;
    }

    private JPanel createOptionCard(String title, String description, Color accentColor, String icon, Runnable onClick) {
        JPanel card = new JPanel() {
            private boolean isHovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Elevated shadow
                int shadowOffset = isHovered ? 8 : 4;
                int shadowAlpha = isHovered ? 40 : 20;
                g2d.setColor(new Color(0, 0, 0, shadowAlpha));
                g2d.fillRoundRect(shadowOffset/2, shadowOffset/2, getWidth() - shadowOffset/2, 
                                getHeight() - shadowOffset/2, 20, 20);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Accent border with gradient
                g2d.setStroke(new BasicStroke(3f));
                GradientPaint gradient = new GradientPaint(
                    0, 0, accentColor,
                    getWidth(), getHeight(), new Color(
                        Math.min(accentColor.getRed() + 30, 255),
                        Math.min(accentColor.getGreen() + 30, 255),
                        Math.min(accentColor.getBlue() + 30, 255)
                    )
                );
                g2d.setPaint(gradient);
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);

                // Top accent bar
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), 
                                      accentColor.getBlue(), 40));
                g2d.fillRoundRect(0, 0, getWidth(), 50, 20, 20);
            }
        };

        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(new EmptyBorder(20, 18, 20, 18));
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon with colored background
        JPanel iconContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), 
                                      accentColor.getBlue(), 25));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        iconContainer.setOpaque(false);
        iconContainer.setPreferredSize(new Dimension(50, 50));
        iconContainer.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconContainer.add(iconLabel);

        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(descLabel);

        card.add(iconContainer, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);

        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.repaint();
            }
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (onClick != null) onClick.run();
            }
        });

        return card;
    }

    // ==================== FEATURE METHODS ====================

    private void showStudentStatistics() {
        JDialog dialog = createStyledDialog("Student Statistics", 600, 500);
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(new Color(248, 250, 252));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Get statistics from backend service
        int totalStudents = ownerService.getTotalStudentCount();
        String[] departments = ownerService.getAllDepartments();
        
        // Stats cards panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statsPanel.setOpaque(false);

        // Total students card
        statsPanel.add(createStatCard("Total Students", String.valueOf(totalStudents), ACCENT_BLUE, "👥"));

        int totalMale = 0, totalFemale = 0;
        
        // Calculate totals using backend service
        for (String dept : departments) {
            int[] stats = ownerService.getStudentsByDepartmentAndGender(dept);
            if (stats != null && stats.length >= 3) {
                totalMale += stats[1];
                totalFemale += stats[2];
            }
        }

        statsPanel.add(createStatCard("Male Students", String.valueOf(totalMale), ACCENT_TEAL, "👨"));
        statsPanel.add(createStatCard("Female Students", String.valueOf(totalFemale), ACCENT_PINK, "👩"));
        statsPanel.add(createStatCard("Departments", String.valueOf(departments.length), ACCENT_PURPLE, "🏛️"));

        contentPanel.add(statsPanel, BorderLayout.NORTH);

        // Department breakdown table
        String[] columnNames = {"Department", "Total", "Male", "Female"};
        Object[][] data = new Object[departments.length + 1][4];

        int grandTotal = 0, grandMale = 0, grandFemale = 0;
        for (int i = 0; i < departments.length; i++) {
            int[] stats = ownerService.getStudentsByDepartmentAndGender(departments[i]);
            int total = stats != null ? stats[0] : 0;
            int males = stats != null ? stats[1] : 0;
            int females = stats != null ? stats[2] : 0;
            
            data[i] = new Object[]{ownerService.getDepartmentFullName(departments[i]), total, males, females};
            grandTotal += total;
            grandMale += males;
            grandFemale += females;
        }
        data[departments.length] = new Object[]{"TOTAL", grandTotal, grandMale, grandFemale};

        JTable table = createStyledTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void showAllStudents() {
        JDialog dialog = createStyledDialog("All Students", 800, 600);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(248, 250, 252));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Get all students from backend service
        String[][] students = ownerService.getAllStudentsArray();
        
        String[] columnNames = {"Name", "Student ID", "Gender", "Year"};
        
        JTable table = createStyledTable(students, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Header with count
        JLabel headerLabel = new JLabel("Total Students: " + (students != null ? students.length : 0));
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(TEXT_PRIMARY);

        contentPanel.add(headerLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void showAllTeachers() {
        JDialog dialog = createStyledDialog("All Teachers", 800, 600);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(248, 250, 252));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Get all teachers - we need to query them
        List<String[]> teachersList = getAllTeachersFromDB();
        String[][] teachers = teachersList.toArray(new String[0][]);
        
        String[] columnNames = {"Teacher ID", "Department", "Major", "Courses"};
        
        JTable table = createStyledTable(teachers, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Header with count
        JLabel headerLabel = new JLabel("Total Teachers: " + teachers.length);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(TEXT_PRIMARY);

        contentPanel.add(headerLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void showAssignTeacherCourse() {
        JDialog dialog = createStyledDialog("Assign Teacher to Course", 500, 350);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(248, 250, 252));
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Teacher ID field
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel teacherLabel = new JLabel("Teacher ID:");
        teacherLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPanel.add(teacherLabel, gbc);

        gbc.gridx = 1;
        JTextField teacherField = createStyledTextField();
        teacherField.setPreferredSize(new Dimension(250, 40));
        contentPanel.add(teacherField, gbc);

        // Course dropdown
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel courseLabel = new JLabel("Select Course:");
        courseLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPanel.add(courseLabel, gbc);

        gbc.gridx = 1;
        String[] courses = ownerService.getAllCourses();
        JComboBox<String> courseCombo = new JComboBox<>(courses != null ? courses : new String[]{"No courses available"});
        courseCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        courseCombo.setPreferredSize(new Dimension(250, 40));
        contentPanel.add(courseCombo, gbc);

        // Assign button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        JButton assignButton = createActionButton("Assign Course", ACCENT_PURPLE);
        assignButton.addActionListener(e -> {
            String teacherId = teacherField.getText().trim();
            String course = (String) courseCombo.getSelectedItem();
            
            if (teacherId.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter Teacher ID", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!ownerService.teacherExists(teacherId)) {
                JOptionPane.showMessageDialog(dialog, "Teacher ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = ownerService.assignCourseToTeacher(teacherId, course);
            if (success) {
                JOptionPane.showMessageDialog(dialog, 
                    "Course assigned successfully!\n\nTeacher: " + teacherId + "\nCourse: " + course, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                teacherField.setText("");
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to assign course. It may already be assigned.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        contentPanel.add(assignButton, gbc);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void showSearchStudent() {
        JDialog dialog = createStyledDialog("Search Student", 700, 500);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 15));
        contentPanel.setBackground(new Color(248, 250, 252));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);

        JTextField searchField = createStyledTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        
        JButton searchButton = createActionButton("Search", ACCENT_TEAL);
        searchButton.setPreferredSize(new Dimension(100, 40));

        searchPanel.add(new JLabel("Student Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Results table
        String[] columnNames = {"Name", "Student ID", "Gender", "Year"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = createStyledTable(new String[0][4], columnNames);
        table.setModel(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            model.setRowCount(0);
            
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a search term", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String[][] results = ownerService.searchStudentByName(searchTerm);
            if (results != null && results.length > 0) {
                for (String[] row : results) {
                    model.addRow(row);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "No students found matching: " + searchTerm, "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void showAllCourses() {
        JDialog dialog = createStyledDialog("All Available Courses", 700, 600);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 15));
        contentPanel.setBackground(new Color(248, 250, 252));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        infoPanel.setOpaque(false);
        
        String[] courses = ownerService.getAllCourses();
        int courseCount = courses != null ? courses.length : 0;
        
        JLabel infoLabel = new JLabel("Total Courses: " + courseCount);
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoLabel.setForeground(TEXT_PRIMARY);
        infoPanel.add(infoLabel);

        // Courses table
        String[] columnNames = {"#", "Course Name"};
        String[][] data = new String[courseCount][2];
        
        if (courses != null) {
            for (int i = 0; i < courses.length; i++) {
                data[i][0] = String.valueOf(i + 1);
                data[i][1] = courses[i];
            }
        }
        
        JTable table = createStyledTable(data, columnNames);
        
        // Adjust column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(550);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Info message
        JLabel tipLabel = new JLabel("💡 Use these courses when assigning to teachers");
        tipLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        tipLabel.setForeground(TEXT_SECONDARY);
        tipLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        contentPanel.add(infoPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(tipLabel, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void showCreateAccount() {
        JDialog dialog = createStyledDialog("Create Email Account", 500, 400);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(248, 250, 252));
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Account type
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel typeLabel = new JLabel("Account Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        String[] types = {"Student (P2024...)", "Teacher (T2024...)", "Owner (O2024...)"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeCombo.setPreferredSize(new Dimension(250, 40));
        contentPanel.add(typeCombo, gbc);

        // ID field
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        JTextField idField = createStyledTextField();
        idField.setPreferredSize(new Dimension(250, 40));
        contentPanel.add(idField, gbc);

        // Email field
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        JTextField emailField = createStyledTextField();
        emailField.setPreferredSize(new Dimension(250, 40));
        contentPanel.add(emailField, gbc);

        // Create button
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        JButton createButton = createActionButton("Create Account", ACCENT_PINK);
        createButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String email = emailField.getText().trim();
            int typeIndex = typeCombo.getSelectedIndex();
            
            String typeName = typeIndex == 0 ? "Student" : (typeIndex == 1 ? "Teacher" : "Owner");
            
            if (id.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!ownerService.validateIdFormat(id, typeIndex)) {
                String prefix = typeIndex == 0 ? "P2024" : (typeIndex == 1 ? "T2024" : "O2024");
                JOptionPane.showMessageDialog(dialog, "ID must start with " + prefix, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!ownerService.validateEmailFormat(email)) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid email address", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create email account using backend service
            boolean success = false;
            switch (typeIndex) {
                case 0:
                    success = ownerService.createStudentEmail(id, email);
                    break;
                case 1:
                    success = ownerService.createTeacherEmail(id, email);
                    break;
                case 2:
                    success = ownerService.createOwnerEmail(id, email);
                    break;
            }
            
            if (success) {
                JOptionPane.showMessageDialog(dialog, 
                    "Account Created Successfully!\n\nType: " + typeName + "\nID: " + id + "\nEmail: " + email, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                idField.setText("");
                emailField.setText("");
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to create account. The ID or email may already exist.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        contentPanel.add(createButton, gbc);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    // ==================== HELPER METHODS ====================

    private List<String[]> getAllTeachersFromDB() {
        List<String[]> teachers = new ArrayList<>();
        // Query all teachers - using the existing methods
        String[] depts = {"GIC", "GIM", "GEE"};
        for (String dept : depts) {
            // We can check if teachers exist in each department
            // For now, return sample data based on what exists
        }
        
        // Try to get teachers by checking if they exist
        // This is a simplified version - ideally TeacherInfoManager would have getAllTeachersArray()
        return teachers;
    }

    private String getDepartmentFullName(String deptCode) {
        switch (deptCode) {
            case "GIC": return "GIC (IT & Computing)";
            case "GIM": return "GIM (IT & Management)";
            case "GEE": return "GEE (Electrical & Eng)";
            default: return deptCode;
        }
    }

    private JDialog createStyledDialog(String title, int width, int height) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        return dialog;
    }

    private JPanel createStatCard(String label, String value, Color color, String icon) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 15, 15);
                
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                g2d.fillRoundRect(0, 0, 5, getHeight(), 15, 15);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout(10, 5));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);

        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelLabel.setForeground(TEXT_SECONDARY);

        textPanel.add(valueLabel);
        textPanel.add(labelLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JTable createStyledTable(Object[][] data, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setForeground(TEXT_PRIMARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));
        
        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
        
        return table;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(color.brighter());
                } else {
                    g2d.setColor(color);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.setColor(Color.WHITE);
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
        button.setPreferredSize(new Dimension(200, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private JButton createLogoutButton() {
        JButton button = new JButton("Logout") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(220, 38, 38));
                } else if (getModel().isRollover()) {
                    g2d.setColor(ACCENT_RED);
                } else {
                    g2d.setColor(new Color(248, 250, 252));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                if (getModel().isRollover() || getModel().isPressed()) {
                    g2d.setColor(Color.WHITE);
                } else {
                    g2d.setColor(TEXT_SECONDARY);
                }
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
        button.setPreferredSize(new Dimension(100, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            // Clear user session on logout using backend service
            ownerService.logout();
            loginpage login = new loginpage();
            login.setVisible(true);
            dispose();
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { button.repaint(); }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new mainpageOwner().setVisible(true));
    }
}
