package Frontend;

import Backend.src.database.CourseManager;
import Backend.src.database.TeacherCourseManager;
import Backend.src.database.TeacherInfoManager;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TeacherCourseAssignmentGUI extends JFrame {
    
    // Colors (matching modern theme)
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JButton searchButton;
    private JButton clearButton;
    private JButton assignButton;
    private JButton assignDepartmentButton;
    private JButton backButton;
    private JTable teacherTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> courseCombo;
    private JComboBox<String> departmentCombo;
    private JLabel statusLabel;
    
    public TeacherCourseAssignmentGUI() {
        // Initialize database tables
        TeacherInfoManager.createTeacherInfoTable();
        CourseManager.createCourseTable();
        TeacherCourseManager.createTeacherCourseTable();
        
        initializeUI();
        loadAllTeachers();
    }
    
    private void initializeUI() {
        setTitle("Teacher Course Assignment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main background panel with gradient
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
        
        // Main content area
        JPanel mainContentPanel = new JPanel(new BorderLayout(0, 15));
        mainContentPanel.setOpaque(false);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        
        // Search Panel
        JPanel searchPanel = createSearchPanel();
        mainContentPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainContentPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Action Panel
        JPanel actionPanel = createActionPanel();
        mainContentPanel.add(actionPanel, BorderLayout.SOUTH);
        
        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(mainContentPanel, BorderLayout.CENTER);
        
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

                GradientPaint purpleGradient = new GradientPaint(
                    0, 0, new Color(168, 85, 247, 200),
                    0, getHeight(), new Color(192, 132, 252, 150)
                );
                g2d.setPaint(purpleGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                g2d.setColor(new Color(168, 85, 247, 8));
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
                    0, 0, ACCENT_PURPLE,
                    getWidth(), getHeight(), new Color(147, 51, 234)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("👨‍🏫");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Teacher Course Assignment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Search and assign courses to teachers");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(subtitleLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        backButton = createBackButton();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(15, 0)) {
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
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        inputPanel.setOpaque(false);
        
        JLabel searchTypeLabel = new JLabel("Search By:");
        searchTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchTypeLabel.setForeground(TEXT_PRIMARY);
        
        String[] searchTypes = {"Teacher ID", "Department"};
        searchTypeCombo = new JComboBox<>(searchTypes);
        searchTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchTypeCombo.setPreferredSize(new Dimension(140, 40));
        searchTypeCombo.setBackground(Color.WHITE);
        searchTypeCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JLabel searchLabel = new JLabel("🔍");
        searchLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                searchField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_BLUE, 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                searchField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        searchButton = createStyledButton("Search", ACCENT_BLUE, 120, 40);
        clearButton = createStyledButton("Clear", TEXT_SECONDARY, 120, 40);
        JButton showAllButton = createStyledButton("Show All", ACCENT_GREEN, 120, 40);
        
        searchButton.addActionListener(e -> searchTeacher());
        clearButton.addActionListener(e -> clearSearch());
        showAllButton.addActionListener(e -> loadAllTeachers());
        
        searchField.addActionListener(e -> searchTeacher());
        
        inputPanel.add(searchTypeLabel);
        inputPanel.add(searchTypeCombo);
        inputPanel.add(searchLabel);
        inputPanel.add(searchField);
        inputPanel.add(searchButton);
        inputPanel.add(clearButton);
        inputPanel.add(showAllButton);
        
        searchPanel.add(inputPanel, BorderLayout.WEST);
        
        return searchPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 15)) {
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
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Teachers");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        String[] columnNames = {"#", "Teacher ID", "Department", "Major", "Course Count"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        teacherTable = new JTable(tableModel);
        teacherTable.setRowHeight(45);
        teacherTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        teacherTable.setShowGrid(true);
        teacherTable.setGridColor(new Color(226, 232, 240));
        teacherTable.setIntercellSpacing(new Dimension(1, 1));
        teacherTable.setSelectionBackground(new Color(219, 234, 254));
        teacherTable.setSelectionForeground(TEXT_PRIMARY);
        teacherTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        teacherTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        teacherTable.getTableHeader().setBackground(new Color(248, 250, 252));
        teacherTable.getTableHeader().setForeground(TEXT_PRIMARY);
        teacherTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(203, 213, 225)));
        teacherTable.getTableHeader().setPreferredSize(new Dimension(0, 50));
        
        teacherTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        teacherTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        teacherTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        teacherTable.getColumnModel().getColumn(3).setPreferredWidth(300);
        teacherTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        centerRenderer.setForeground(TEXT_PRIMARY);
        teacherTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        teacherTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
        textRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textRenderer.setForeground(TEXT_PRIMARY);
        teacherTable.getColumnModel().getColumn(1).setCellRenderer(textRenderer);
        teacherTable.getColumnModel().getColumn(2).setCellRenderer(textRenderer);
        teacherTable.getColumnModel().getColumn(3).setCellRenderer(textRenderer);
        
        JScrollPane scrollPane = new JScrollPane(teacherTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        tablePanel.add(titlePanel, BorderLayout.NORTH);
        tablePanel.add(tableContainer, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new BorderLayout()) {
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
        actionPanel.setOpaque(false);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create a main panel to hold both rows
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        
        // First row - Department Assignment
        JPanel departmentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        departmentPanel.setOpaque(false);
        
        JLabel departmentLabel = new JLabel("Assign Department:");
        departmentLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        departmentLabel.setForeground(TEXT_PRIMARY);
        
        departmentCombo = new JComboBox<>();
        departmentCombo.addItem("-- Select Department --");
        departmentCombo.addItem("GIC - General IT & Computing");
        departmentCombo.addItem("GIM - General IT & Management");
        departmentCombo.addItem("GEE - General Electrical & Engineering");
        departmentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        departmentCombo.setPreferredSize(new Dimension(380, 40));
        departmentCombo.setBackground(Color.WHITE);
        departmentCombo.setForeground(TEXT_PRIMARY);
        
        assignDepartmentButton = createStyledButton("🏢 Assign Department", ACCENT_PURPLE, 200, 45);
        assignDepartmentButton.addActionListener(e -> assignDepartment());
        
        departmentPanel.add(departmentLabel);
        departmentPanel.add(departmentCombo);
        departmentPanel.add(assignDepartmentButton);
        
        // Second row - Course Assignment
        JPanel coursePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        coursePanel.setOpaque(false);
        
        JLabel courseLabel = new JLabel("Assign Course:");
        courseLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        courseLabel.setForeground(TEXT_PRIMARY);
        
        courseCombo = new JComboBox<>();
        courseCombo.addItem("-- Select Course --");
        courseCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        courseCombo.setPreferredSize(new Dimension(380, 40));
        courseCombo.setBackground(Color.WHITE);
        courseCombo.setForeground(TEXT_PRIMARY);
        
        // Populate courses
        populateCourses();
        
        assignButton = createStyledButton("✓ Assign Course", ACCENT_GREEN, 200, 45);
        assignButton.addActionListener(e -> assignCourse());
        
        coursePanel.add(courseLabel);
        coursePanel.add(courseCombo);
        coursePanel.add(assignButton);
        
        // Add both panels to main panel
        mainPanel.add(departmentPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(coursePanel);
        
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        statusLabel.setForeground(TEXT_SECONDARY);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(statusLabel);
        
        actionPanel.add(mainPanel, BorderLayout.WEST);
        actionPanel.add(rightPanel, BorderLayout.EAST);
        
        return actionPanel;
    }
    
    private void populateCourses() {
        // Get all courses from database
        String[][] allCourses = {
            {"AI001", "Introduction to Artificial Intelligence"},
            {"AI002", "Machine Learning"},
            {"AI003", "Data Science Fundamentals"},
            {"AI004", "Deep Learning"},
            {"AU101", "Control Systems"},
            {"AU102", "Industrial Automation"},
            {"AU103", "PLC and SCADA Systems"},
            {"AU104", "Robotics Fundamentals"},
            {"CS001", "Computer and Network Security"},
            {"CS002", "Ethical Hacking Fundamentals"},
            {"CS003", "Cyber Security Management"},
            {"CS004", "Cryptography Basics"},
            {"EE101", "Electrical Circuits"},
            {"EE102", "Electrical Machines"},
            {"EE103", "Power Systems"},
            {"EE104", "Power Electronics"},
            {"EL101", "Analog Electronics"},
            {"EL102", "Digital Electronics"},
            {"EL103", "Microprocessors and Microcontrollers Systems"},
            {"EL104", "Embedded Systems"},
            {"IE101", "Operations Research"},
            {"IE102", "Quality Control and Assurance"},
            {"IE103", "Supply Chain Management"},
            {"IE104", "Work Study and Ergonomics"},
            {"ME101", "Engineering Mechanics"},
            {"ME102", "Thermodynamics"},
            {"ME103", "Machine Design"},
            {"ME104", "Fluid Mechanics"},
            {"MF101", "Manufacturing Processes"},
            {"MF102", "Computer-Aided Manufacturing"},
            {"MF103", "Production Planning and Control"},
            {"MF104", "Industrial Equipment"},
            {"SE001", "Programming Fundamentals"},
            {"SE002", "Object-Oriented Programming"},
            {"SE003", "Programming Fundamentals"},
            {"SE004", "Software Engineering Principles"}
        };
        
        for (String[] course : allCourses) {
            courseCombo.addItem(course[0] + " - " + course[1]);
        }
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

        button.addActionListener(e -> handleBack());

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(MouseEvent evt) { button.repaint(); }
        });

        return button;
    }
    
    private void loadAllTeachers() {
        tableModel.setRowCount(0);
        
        List<String[]> allTeachers = TeacherInfoManager.getAllTeachers();
        
        if (allTeachers == null || allTeachers.isEmpty()) {
            statusLabel.setText("No teachers found in database");
            statusLabel.setForeground(TEXT_SECONDARY);
            return;
        }
        
        for (int i = 0; i < allTeachers.size(); i++) {
            String[] teacher = allTeachers.get(i);
            Object[] row = {
                (i + 1),
                teacher[0], // teacherID
                teacher[1], // department
                teacher[2], // major
                teacher[3]  // course count
            };
            tableModel.addRow(row);
        }
        
        statusLabel.setText("Showing all teachers (" + allTeachers.size() + " found)");
        statusLabel.setForeground(ACCENT_GREEN);
    }
    
    private void searchTeacher() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a search term",
                "Input Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        tableModel.setRowCount(0);
        
        String searchType = (String) searchTypeCombo.getSelectedItem();
        
        if (searchType.equals("Teacher ID")) {
            searchByTeacherID(searchText);
        } else {
            searchByDepartment(searchText);
        }
    }
    
    private void searchByTeacherID(String teacherID) {
        String[] result = TeacherInfoManager.getTeacherInfo(teacherID);
        
        if (result == null) {
            statusLabel.setText("No teacher found with ID: " + teacherID);
            statusLabel.setForeground(ACCENT_ORANGE);
            return;
        }
        
        int courseCount = TeacherInfoManager.getCourseCountForTeacher(teacherID);
        
        Object[] row = {
            1,
            result[0], // teacherID
            result[1], // department
            result[2], // major
            courseCount
        };
        tableModel.addRow(row);
        
        statusLabel.setText("Teacher found: " + result[0]);
        statusLabel.setForeground(ACCENT_GREEN);
        
        teacherTable.setRowSelectionInterval(0, 0);
    }
    
    private void searchByDepartment(String department) {
        List<String[]> results = TeacherInfoManager.getTeachersByDepartment(department.toUpperCase());
        
        if (results == null || results.isEmpty()) {
            statusLabel.setText("No teachers found in department: " + department);
            statusLabel.setForeground(ACCENT_ORANGE);
            return;
        }
        
        for (int i = 0; i < results.size(); i++) {
            String[] teacher = results.get(i);
            Object[] row = {
                (i + 1),
                teacher[0], // teacherID
                teacher[1], // department
                teacher[2], // major
                teacher[3]  // course count
            };
            tableModel.addRow(row);
        }
        
        statusLabel.setText("Found " + results.size() + " teacher(s)");
        statusLabel.setForeground(ACCENT_GREEN);
        
        if (results.size() > 0) {
            teacherTable.setRowSelectionInterval(0, 0);
        }
    }
    
    private void clearSearch() {
        searchField.setText("");
        courseCombo.setSelectedIndex(0);
        departmentCombo.setSelectedIndex(0);
        loadAllTeachers();
        statusLabel.setText("");
    }
    
    private void assignDepartment() {
        int selectedRow = teacherTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a teacher from the table",
                "Selection Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (departmentCombo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a department",
                "Department Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String teacherID = (String) tableModel.getValueAt(selectedRow, 1);
        String selectedDept = (String) departmentCombo.getSelectedItem();
        String deptCode = selectedDept.substring(0, 3); // Extract GIC, GIM, or GEE
        String deptName = selectedDept.substring(6); // Extract full name
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Assign department to Teacher " + teacherID + "?\n\n" +
            "Department: " + selectedDept,
            "Confirm Department Assignment",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Get current teacher info
            String[] teacherInfo = TeacherInfoManager.getTeacherInfo(teacherID);
            String currentMajor = (teacherInfo != null) ? teacherInfo[2] : "";
            
            // Update teacher info with new department (keeping the same major)
            boolean success = TeacherInfoManager.updateTeacherInfo(teacherID, deptCode, currentMajor);
            
            if (success) {
                statusLabel.setText("✓ Department " + deptCode + " assigned to " + teacherID);
                statusLabel.setForeground(ACCENT_GREEN);
                
                JOptionPane.showMessageDialog(this,
                    "Department successfully assigned!\n\n" +
                    "Teacher: " + teacherID + "\n" +
                    "Department: " + selectedDept,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh the table
                String searchType = (String) searchTypeCombo.getSelectedItem();
                String searchText = searchField.getText().trim();
                if (!searchText.isEmpty()) {
                    searchTeacher();
                } else {
                    loadAllTeachers();
                }
            } else {
                statusLabel.setText("❌ Failed to assign department");
                statusLabel.setForeground(ACCENT_ORANGE);
            }
            
            departmentCombo.setSelectedIndex(0);
        }
    }
    
    private void assignCourse() {
        int selectedRow = teacherTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a teacher from the table",
                "Selection Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (courseCombo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a course",
                "Course Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String teacherID = (String) tableModel.getValueAt(selectedRow, 1);
        String selectedCourse = (String) courseCombo.getSelectedItem();
        String courseID = selectedCourse.substring(0, selectedCourse.indexOf(" - "));
        String courseName = selectedCourse.substring(selectedCourse.indexOf(" - ") + 3);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Assign course to Teacher " + teacherID + "?\n\n" +
            "Course: " + courseID + " - " + courseName,
            "Confirm Assignment",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = TeacherCourseManager.addTeacherCourse(teacherID, courseID);
            
            if (success) {
                statusLabel.setText("✓ Course " + courseID + " assigned to " + teacherID);
                statusLabel.setForeground(ACCENT_GREEN);
                
                JOptionPane.showMessageDialog(this,
                    "Course successfully assigned!\n\n" +
                    "Teacher: " + teacherID + "\n" +
                    "Course: " + courseID + " - " + courseName,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh the table to update course count
                String searchType = (String) searchTypeCombo.getSelectedItem();
                String searchText = searchField.getText().trim();
                if (!searchText.isEmpty()) {
                    searchTeacher();
                } else {
                    loadAllTeachers();
                }
            } else {
                statusLabel.setText("❌ Course already assigned or error occurred");
                statusLabel.setForeground(ACCENT_ORANGE);
            }
            
            courseCombo.setSelectedIndex(0);
        }
    }
    
    private void handleBack() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to go back?",
            "Confirm",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            TeacherCourseAssignmentGUI frame = new TeacherCourseAssignmentGUI();
            frame.setVisible(true);
        });
    }
}