package Frontend.ui;

import Backend.src.database.CourseManager;
import Backend.src.database.StudentInfoManager;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ManageStudentsPage extends JFrame {

    // Colors matching the theme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_TEAL = new Color(20, 184, 166);
    private static final Color SEARCH_BG = new Color(248, 250, 252);

    // Filter components
    private JTextField searchField;
    private JComboBox<String> classCombo;
    private JComboBox<String> majorCombo;
    private JComboBox<String> departmentCombo;
    private JPanel studentsPanel;

    // Data from database
    private List<StudentInfo> allStudents = new ArrayList<>();
    private List<String> classes = new ArrayList<>();
    private List<String> majors = new ArrayList<>();
    private List<String> departments = new ArrayList<>();

    public ManageStudentsPage() {
        loadDataFromDatabase();
        initComponent();
    }

    private void loadDataFromDatabase() {
        // Load departments from database
        departments.add("All Departments");
        String[] dbDepartments = CourseManager.getAllDepartments();
        if (dbDepartments != null && dbDepartments.length > 0) {
            for (String dept : dbDepartments) {
                if (dept != null && !dept.isEmpty()) {
                    departments.add(dept);
                }
            }
        }

        // Load majors from database
        majors.add("All Majors");
        String[] dbMajors = CourseManager.getAllMajors();
        if (dbMajors != null && dbMajors.length > 0) {
            for (String major : dbMajors) {
                if (major != null && !major.isEmpty()) {
                    majors.add(major);
                }
            }
        }

        // Load courses/classes from database
        classes.add("All Classes");
        String[] dbCourses = CourseManager.getAllCoursesArray();
        if (dbCourses != null && dbCourses.length > 0) {
            for (String course : dbCourses) {
                if (course != null && !course.isEmpty()) {
                    classes.add(course);
                }
            }
        }

        // Load students from database
        String[][] dbStudents = StudentInfoManager.getAllStudentsWithDepartment();
        if (dbStudents != null && dbStudents.length > 0) {
            for (String[] student : dbStudents) {
                if (student != null && student.length >= 5) {
                    String name = student[0] != null ? student[0] : "";
                    String id = student[1] != null ? student[1] : "";
                    String major = student[2] != null ? student[2] : "Not Assigned";
                    String dept = student[3] != null ? student[3] : "Not Assigned";
                    String course = student[4] != null ? student[4] : "Not Assigned";
                    allStudents.add(new StudentInfo(name, id, major, dept, course));
                }
            }
        }
    }

    public void initComponent() {
        setTitle("Manage Students");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
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

        JPanel headerPanel = createHeaderPanel();
        JPanel contentPanel = createContentPanel();

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

                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);

                GradientPaint orangeGradient = new GradientPaint(
                    0, 0, new Color(249, 115, 22, 200),
                    0, getHeight(), new Color(251, 146, 60, 150)
                );
                g2d.setPaint(orangeGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                g2d.setColor(new Color(249, 115, 22, 8));
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, ACCENT_ORANGE,
                    getWidth(), getHeight(), new Color(234, 88, 12)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("👥");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Manage Students");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("View and manage student information");
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

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(15, 30, 30, 30));

        // Filter panel
        JPanel filterPanel = createFilterPanel();
        contentPanel.add(filterPanel, BorderLayout.NORTH);

        // Students list
        JPanel studentsContainer = createStudentsContainer();
        contentPanel.add(studentsContainer, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createFilterPanel() {
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
        searchBtn.addActionListener(e -> applyFilters());

        // Class filter label and dropdown
        JLabel classLabel = new JLabel("Class:");
        classLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        classLabel.setForeground(TEXT_PRIMARY);

        classCombo = createStyledComboBox(classes.toArray(new String[0]));
        classCombo.addActionListener(e -> applyFilters());

        // Major filter
        JLabel majorLabel = new JLabel("Major:");
        majorLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        majorLabel.setForeground(TEXT_PRIMARY);

        majorCombo = createStyledComboBox(majors.toArray(new String[0]));
        majorCombo.addActionListener(e -> applyFilters());

        // Department filter
        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        deptLabel.setForeground(TEXT_PRIMARY);

        departmentCombo = createStyledComboBox(departments.toArray(new String[0]));
        departmentCombo.addActionListener(e -> applyFilters());

        // Action buttons
        JButton refreshBtn = createGradientButton("Refresh", ACCENT_TEAL, new Color(13, 148, 136));
        refreshBtn.addActionListener(e -> {
            clearFilters();
            applyFilters();
        });

        JButton clearBtn = createGradientButton("Clear", ACCENT_RED, new Color(220, 38, 38));
        clearBtn.addActionListener(e -> clearFilters());

        panel.add(searchContainer);
        panel.add(searchBtn);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(classLabel);
        panel.add(classCombo);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(majorLabel);
        panel.add(majorCombo);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(deptLabel);
        panel.add(departmentCombo);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(refreshBtn);
        panel.add(clearBtn);

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
        button.setPreferredSize(new Dimension(90, 42));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void clearFilters() {
        searchField.setText("Search by name or ID...");
        searchField.setForeground(TEXT_SECONDARY);
        classCombo.setSelectedIndex(0);
        majorCombo.setSelectedIndex(0);
        departmentCombo.setSelectedIndex(0);
        applyFilters();
    }

    private void applyFilters() {
        String searchText = searchField.getText();
        if (searchText.equals("Search by name or ID...")) {
            searchText = "";
        }
        searchText = searchText.toLowerCase();

        String selectedClass = (String) classCombo.getSelectedItem();
        String selectedMajor = (String) majorCombo.getSelectedItem();
        String selectedDept = (String) departmentCombo.getSelectedItem();

        final String finalSearchText = searchText;
        
        List<StudentInfo> filteredStudents = allStudents.stream()
            .filter(s -> {
                // Search filter
                if (!finalSearchText.isEmpty()) {
                    boolean matchesSearch = s.name.toLowerCase().contains(finalSearchText) ||
                                          s.studentId.toLowerCase().contains(finalSearchText);
                    if (!matchesSearch) return false;
                }
                
                // Class filter
                if (!selectedClass.equals("All Classes") && !s.className.equals(selectedClass)) {
                    return false;
                }
                
                // Major filter
                if (!selectedMajor.equals("All Majors") && !s.major.equals(selectedMajor)) {
                    return false;
                }
                
                // Department filter
                if (!selectedDept.equals("All Departments") && !s.department.equals(selectedDept)) {
                    return false;
                }
                
                return true;
            })
            .collect(Collectors.toList());

        updateStudentsList(filteredStudents);
    }

    private JPanel createStudentsContainer() {
        JPanel container = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 2, 15, 15);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header row
        JPanel headerRow = createTableHeader();
        container.add(headerRow, BorderLayout.NORTH);

        // Students list
        studentsPanel = new JPanel();
        studentsPanel.setLayout(new BoxLayout(studentsPanel, BoxLayout.Y_AXIS));
        studentsPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(studentsPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        container.add(scrollPane, BorderLayout.CENTER);

        // Initial load
        updateStudentsList(allStudents);

        return container;
    }

    private JPanel createTableHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(10, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);

        String[] columns = {"Student", "ID", "Major", "Department", "Class", "Actions"};
        double[] weights = {0.2, 0.1, 0.2, 0.15, 0.25, 0.1};

        for (int i = 0; i < columns.length; i++) {
            gbc.gridx = i;
            gbc.weightx = weights[i];
            
            JLabel label = new JLabel(columns[i]);
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            label.setForeground(TEXT_SECONDARY);
            header.add(label, gbc);
        }

        return header;
    }

    private void updateStudentsList(List<StudentInfo> students) {
        studentsPanel.removeAll();
        studentsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        if (students.isEmpty()) {
            JLabel emptyLabel = new JLabel("No students found matching the criteria");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyLabel.setForeground(TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            studentsPanel.add(Box.createVerticalGlue());
            studentsPanel.add(emptyLabel);
            studentsPanel.add(Box.createVerticalGlue());
        } else {
            for (StudentInfo student : students) {
                studentsPanel.add(createStudentRow(student));
                studentsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        studentsPanel.revalidate();
        studentsPanel.repaint();
    }

    private JPanel createStudentRow(StudentInfo student) {
        JPanel row = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(248, 250, 252));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(12, 15, 12, 15));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.anchor = GridBagConstraints.WEST;

        double[] weights = {0.2, 0.1, 0.2, 0.15, 0.25, 0.1};

        // Student name with avatar
        gbc.gridx = 0;
        gbc.weightx = weights[0];
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        namePanel.setOpaque(false);
        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        JLabel nameLabel = new JLabel(student.name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(TEXT_PRIMARY);
        namePanel.add(avatarLabel);
        namePanel.add(nameLabel);
        row.add(namePanel, gbc);

        // Student ID
        gbc.gridx = 1;
        gbc.weightx = weights[1];
        JLabel idLabel = new JLabel(student.studentId);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setForeground(TEXT_SECONDARY);
        row.add(idLabel, gbc);

        // Major
        gbc.gridx = 2;
        gbc.weightx = weights[2];
        JLabel majorLabel = createBadge(student.major, ACCENT_BLUE);
        row.add(majorLabel, gbc);

        // Department
        gbc.gridx = 3;
        gbc.weightx = weights[3];
        JLabel deptLabel = new JLabel(student.department);
        deptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        deptLabel.setForeground(TEXT_SECONDARY);
        row.add(deptLabel, gbc);

        // Class
        gbc.gridx = 4;
        gbc.weightx = weights[4];
        JLabel classLabel = new JLabel(student.className);
        classLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        classLabel.setForeground(TEXT_PRIMARY);
        row.add(classLabel, gbc);

        // Actions
        gbc.gridx = 5;
        gbc.weightx = weights[5];
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        actionsPanel.setOpaque(false);
        
        JButton viewBtn = createIconButton("👁", "View Details", ACCENT_BLUE);
        JButton editBtn = createIconButton("✏", "Edit", ACCENT_ORANGE);
        
        viewBtn.addActionListener(e -> showStudentDetails(student));
        editBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Edit student: " + student.name, "Edit", JOptionPane.INFORMATION_MESSAGE));
        
        actionsPanel.add(viewBtn);
        actionsPanel.add(editBtn);
        row.add(actionsPanel, gbc);

        return row;
    }

    private JLabel createBadge(String text, Color color) {
        JLabel badge = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        badge.setForeground(color);
        badge.setBorder(new EmptyBorder(4, 8, 4, 8));
        return badge;
    }

    private JButton createIconButton(String icon, String tooltip, Color color) {
        JButton button = new JButton(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isRollover()) {
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                } else {
                    g2d.setColor(new Color(248, 250, 252));
                }
                g2d.fillOval(0, 0, getWidth(), getHeight());

                g2d.setColor(color);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };

        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(30, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);

        return button;
    }

    private void showStudentDetails(StudentInfo student) {
        JDialog dialog = new JDialog(this, "Student Details", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        contentPanel.setBackground(Color.WHITE);

        // Avatar
        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Name
        JLabel nameLabel = new JLabel(student.name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setForeground(TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(avatarLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Details
        addDetailRow(contentPanel, "Student ID", student.studentId);
        addDetailRow(contentPanel, "Major", student.major);
        addDetailRow(contentPanel, "Department", student.department);
        addDetailRow(contentPanel, "Current Class", student.className);

        // Close button
        JButton closeBtn = new JButton("Close");
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeBtn.addActionListener(e -> dialog.dispose());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(closeBtn);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelComp = new JLabel(label + ":");
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelComp.setForeground(TEXT_SECONDARY);
        labelComp.setPreferredSize(new Dimension(120, 25));

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        valueComp.setForeground(TEXT_PRIMARY);

        row.add(labelComp, BorderLayout.WEST);
        row.add(valueComp, BorderLayout.CENTER);

        panel.add(row);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private JButton createBackButton() {
        JButton button = new JButton("← Back") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(226, 232, 240));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(241, 245, 249));
                } else {
                    g2d.setColor(Color.WHITE);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.setColor(TEXT_PRIMARY);
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
            mainpageTeacher teacherPage = new mainpageTeacher();
            teacherPage.setVisible(true);
            dispose();
        });

        return button;
    }

    // Inner class for student data
    private static class StudentInfo {
        String name;
        String studentId;
        String major;
        String department;
        String className;

        StudentInfo(String name, String studentId, String major, String department, String className) {
            this.name = name;
            this.studentId = studentId;
            this.major = major;
            this.department = department;
            this.className = className;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageStudentsPage().setVisible(true));
    }
}
