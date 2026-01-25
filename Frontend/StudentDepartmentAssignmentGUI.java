package Frontend;

import Backend.src.database.MajorManager;
import Backend.src.database.StudentInfoManager;
import Backend.src.department.Department;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class StudentDepartmentAssignmentGUI extends JFrame {
    
    // Colors (matching modern theme)
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JButton searchButton;
    private JButton clearButton;
    private JButton assignButton;
    private JButton backButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> departmentCombo;
    private JLabel statusLabel;
    private JPanel detailsPanel;
    private JScrollPane detailsScrollPane;
    

    public static final Color ACCENT_RED = new Color(239, 68, 68);
    public StudentDepartmentAssignmentGUI() {
        // Initialize database tables
        StudentInfoManager.createStudentInfoTable();
        MajorManager.createDepartmentMajorTable();
        
        initializeUI();
        loadAllStudents();
    }
    
    private void initializeUI() {
        setTitle("Student Department Assignment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
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
        
        // Split pane for table and details
        JSplitPane splitPane = createSplitPane();
        mainContentPanel.add(splitPane, BorderLayout.CENTER);
        
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

                // Card background with shadow
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);

                // Main card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);

                // Purple gradient accent on the left
                GradientPaint purpleGradient = new GradientPaint(
                    0, 0, new Color(168, 85, 247, 200),
                    0, getHeight(), new Color(192, 132, 252, 150)
                );
                g2d.setPaint(purpleGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                // Subtle purple background pattern
                g2d.setColor(new Color(168, 85, 247, 8));
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // Icon and title container
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

        JLabel iconLabel = new JLabel("🎓");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Student Department Assignment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Search and assign students to departments");
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

                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 12));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 3, 20, 20);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Search input
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        inputPanel.setOpaque(false);
        
        // Search type label
        JLabel searchTypeLabel = new JLabel("Search By:");
        searchTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchTypeLabel.setForeground(TEXT_PRIMARY);
        
        // Search type combo box
        String[] searchTypes = {"Name", "ID"};
        searchTypeCombo = new JComboBox<>(searchTypes);
        searchTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchTypeCombo.setPreferredSize(new Dimension(120, 40));
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
        
        // Add focus listener for border effect
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
        
        searchButton.addActionListener(e -> searchStudent());
        clearButton.addActionListener(e -> clearSearch());
        showAllButton.addActionListener(e -> loadAllStudents());
        
        // Add Enter key listener to search field
        searchField.addActionListener(e -> searchStudent());
        
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
    
    private JSplitPane createSplitPane() {
        // Left panel - Results table
        JPanel leftPanel = createTablePanel();
        
        // Right panel - Details view
        JPanel rightPanel = createDetailsPanel();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(650);
        splitPane.setOneTouchExpandable(true);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setOpaque(false);
        
        return splitPane;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 12));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 3, 20, 20);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 5));
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Students");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        // Table
        String[] columnNames = {"#", "Student Name", "Student ID", "Gender", "Year"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(45);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentTable.setShowGrid(true);
        studentTable.setGridColor(new Color(226, 232, 240));
        studentTable.setIntercellSpacing(new Dimension(1, 1));
        studentTable.setSelectionBackground(new Color(219, 234, 254));
        studentTable.setSelectionForeground(TEXT_PRIMARY);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Table header styling
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentTable.getTableHeader().setBackground(new Color(248, 250, 252));
        studentTable.getTableHeader().setForeground(TEXT_PRIMARY);
        studentTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(203, 213, 225)));
        studentTable.getTableHeader().setPreferredSize(new Dimension(0, 50));
        
        // Set column widths
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        // Center align # column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        centerRenderer.setForeground(TEXT_PRIMARY);
        studentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        studentTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        studentTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        // Custom renderer for other columns
        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
        textRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textRenderer.setForeground(TEXT_PRIMARY);
        studentTable.getColumnModel().getColumn(1).setCellRenderer(textRenderer);
        studentTable.getColumnModel().getColumn(2).setCellRenderer(textRenderer);
        
        // Add selection listener
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySelectedStudentDetails();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
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
    
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 16, 16);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 16, 16);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 20, 0));
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Student Details");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        
        // Initial message
        JLabel emptyLabel = new JLabel("Select a student to view details");
        emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        emptyLabel.setForeground(TEXT_SECONDARY);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(Box.createVerticalGlue());
        detailsPanel.add(emptyLabel);
        detailsPanel.add(Box.createVerticalGlue());
        
        detailsScrollPane = new JScrollPane(detailsPanel);
        detailsScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        detailsScrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(detailsScrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 12));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 3, 20, 20);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        actionPanel.setOpaque(false);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left side - Department selection
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        
        JLabel deptLabel = new JLabel("Select Department:");
        deptLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deptLabel.setForeground(TEXT_PRIMARY);
        
        departmentCombo = new JComboBox<>(new String[]{
            "-- Select Department --",
            Department.GIC.getDisplayName() + " - General IT & Computing",
            Department.GIM.getDisplayName() + " - General IT & Management",
            Department.GEE.getDisplayName() + " - General Electrical & Engineering"
        });
        departmentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        departmentCombo.setPreferredSize(new Dimension(380, 40));
        departmentCombo.setBackground(Color.WHITE);
        departmentCombo.setForeground(TEXT_PRIMARY);
        
        assignButton = createStyledButton("✓ Assign to Department", ACCENT_GREEN, 200, 45);
        assignButton.addActionListener(e -> assignDepartment());
        
        leftPanel.add(deptLabel);
        leftPanel.add(departmentCombo);
        leftPanel.add(assignButton);
        
        // Right side - Status label
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        statusLabel.setForeground(TEXT_SECONDARY);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(statusLabel);
        
        actionPanel.add(leftPanel, BorderLayout.WEST);
        actionPanel.add(rightPanel, BorderLayout.EAST);
        
        return actionPanel;
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
    
    private void loadAllStudents() {
        tableModel.setRowCount(0);
        clearDetailsPanel();
        
        // Use searchStudentByName with empty string to get all students
        String[][] allStudents = StudentInfoManager.searchStudentByName("");
        
        if (allStudents == null || allStudents.length == 0) {
            statusLabel.setText("No students found in database");
            statusLabel.setForeground(TEXT_SECONDARY);
            return;
        }
        
        // Populate table with all students
        // searchStudentByName returns: [studentName, studentID, gender, year]
        for (int i = 0; i < allStudents.length; i++) {
            Object[] row = {
                (i + 1),
                allStudents[i][0], // studentName
                allStudents[i][1], // studentID
                allStudents[i][2], // gender
                allStudents[i][3]  // year
            };
            tableModel.addRow(row);
        }
        
        statusLabel.setText("Showing all students (" + allStudents.length + " found)");
        statusLabel.setForeground(ACCENT_GREEN);
    }
    
    private void searchStudent() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a search term",
                "Input Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        tableModel.setRowCount(0);
        clearDetailsPanel();
        
        String searchType = (String) searchTypeCombo.getSelectedItem();
        
        if (searchType.equals("Name")) {
            searchByName(searchText);
        } else {
            searchByID(searchText);
        }
    }
    
    private void searchByName(String searchName) {
        String[][] results = StudentInfoManager.searchStudentByName(searchName.toLowerCase());
        
        if (results == null || results.length == 0) {
            statusLabel.setText("No student found with name containing: " + searchName);
            statusLabel.setForeground(ACCENT_ORANGE);
            return;
        }
        
        // Populate table - results format: [studentName, studentID, gender, year]
        for (int i = 0; i < results.length; i++) {
            Object[] row = {
                (i + 1),
                results[i][0], // studentName
                results[i][1], // studentID
                results[i][2], // gender
                results[i][3]  // year
            };
            tableModel.addRow(row);
        }
        
        statusLabel.setText("Found " + results.length + " student(s)");
        statusLabel.setForeground(ACCENT_GREEN);
        
        // Auto-select first row
        if (results.length > 0) {
            studentTable.setRowSelectionInterval(0, 0);
        }
    }
    
    private void searchByID(String searchID) {
        String[] result = StudentInfoManager.getStudentInfo(searchID);
        
        if (result == null) {
            statusLabel.setText("No student found with ID: " + searchID);
            statusLabel.setForeground(ACCENT_ORANGE);
            return;
        }
        
        // Add to table - result format: [Name, StudentID, Gender, Year]
        Object[] row = {
            1,
            result[0], // Name
            result[1], // StudentID
            result[2], // Gender
            result[3]  // Year
        };
        tableModel.addRow(row);
        
        statusLabel.setText("Student found: " + result[0]);
        statusLabel.setForeground(ACCENT_GREEN);
        
        // Auto-select the row
        studentTable.setRowSelectionInterval(0, 0);
    }
    
    private void clearSearch() {
        searchField.setText("");
        departmentCombo.setSelectedIndex(0);
        loadAllStudents();
        statusLabel.setText("");
    }
    
    private void displaySelectedStudentDetails() {
        int selectedRow = studentTable.getSelectedRow();
        
        if (selectedRow == -1) {
            return;
        }
        
        String studentID = (String) tableModel.getValueAt(selectedRow, 2);
        String[] studentInfo = StudentInfoManager.getStudentInfo(studentID);
        
        if (studentInfo == null) {
            clearDetailsPanel();
            return;
        }
        
        displayStudentDetails(studentInfo);
    }
    
    private void displayStudentDetails(String[] studentInfo) {
        detailsPanel.removeAll();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        
        detailsPanel.add(Box.createVerticalStrut(15));
        
        // Student basic info card
        JPanel basicInfoCard = createInfoCard("👤 Basic Information", ACCENT_BLUE);
        basicInfoCard.setLayout(new GridLayout(4, 1, 0, 12));
        
        basicInfoCard.add(createDetailRow("Name", studentInfo[0]));
        basicInfoCard.add(createDetailRow("Student ID", studentInfo[1]));
        basicInfoCard.add(createDetailRow("Gender", studentInfo[2]));
        basicInfoCard.add(createDetailRow("Year", studentInfo[3]));
        
        detailsPanel.add(basicInfoCard);
        detailsPanel.add(Box.createVerticalStrut(15));
        
        // Department and Major
        String[] deptMajorCourses = MajorManager.getFullDepartmentMajorCourse(studentInfo[1]);
        
        if (deptMajorCourses != null && deptMajorCourses.length >= 6) {
            // Department/Major card
            JPanel academicCard = createInfoCard("🎓 Academic Information", ACCENT_PURPLE);
            academicCard.setLayout(new GridLayout(2, 1, 0, 12));
            
            academicCard.add(createDetailRow("Department", deptMajorCourses[0]));
            academicCard.add(createDetailRow("Major", deptMajorCourses[1]));
            
            detailsPanel.add(academicCard);
            detailsPanel.add(Box.createVerticalStrut(15));
            
            // Courses card
            JPanel coursesCard = createInfoCard("📚 Enrolled Courses", ACCENT_GREEN);
            coursesCard.setLayout(new BoxLayout(coursesCard, BoxLayout.Y_AXIS));
            
            for (int i = 2; i < 6; i++) {
                JPanel courseRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                courseRow.setOpaque(false);
                
                JLabel courseLabel = new JLabel((i - 1) + ". " + deptMajorCourses[i]);
                courseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                courseLabel.setForeground(TEXT_PRIMARY);
                
                courseRow.add(courseLabel);
                coursesCard.add(courseRow);
                
                if (i < 5) {
                    coursesCard.add(Box.createVerticalStrut(10));
                }
            }
            
            detailsPanel.add(coursesCard);
        } else {
            // No data card
            JPanel noDataCard = createInfoCard("⚠️ Department Assignment", ACCENT_ORANGE);
            
            JLabel noDataLabel = new JLabel("Department and Major not assigned yet");
            noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            noDataLabel.setForeground(TEXT_SECONDARY);
            
            noDataCard.add(noDataLabel);
            
            detailsPanel.add(noDataCard);
        }
        
        detailsPanel.add(Box.createVerticalGlue());
        
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }
    
    private JPanel createInfoCard(String title, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(new Color(248, 250, 252));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Left accent border
                g2d.setColor(accentColor);
                g2d.fillRoundRect(0, 0, 4, getHeight(), 12, 12);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setMaximumSize(new Dimension(450, 1000));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 12, 0));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createDetailRow(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel labelComp = new JLabel(label + ":");
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComp.setForeground(TEXT_SECONDARY);
        labelComp.setPreferredSize(new Dimension(90, 25));
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueComp.setForeground(TEXT_PRIMARY);
        
        panel.add(labelComp, BorderLayout.WEST);
        panel.add(valueComp, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void clearDetailsPanel() {
        detailsPanel.removeAll();
        
        JLabel emptyLabel = new JLabel("Select a student to view details");
        emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        emptyLabel.setForeground(TEXT_SECONDARY);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        detailsPanel.add(Box.createVerticalGlue());
        detailsPanel.add(emptyLabel);
        detailsPanel.add(Box.createVerticalGlue());
        
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }
    
    private void assignDepartment() {
        int selectedRow = studentTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a student from the table",
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
        
        String studentName = (String) tableModel.getValueAt(selectedRow, 1);
        String studentID = (String) tableModel.getValueAt(selectedRow, 2);
        String selectedDept = (String) departmentCombo.getSelectedItem();
        String department = selectedDept.substring(0, 3); // Extract GIC, GIM, or GEE
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Assign " + studentName + " (" + studentID + ") to " + department + "?\n\n" +
            "This will open the major selection dialog.",
            "Confirm Assignment",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Get major selection based on department
            String selectedMajor = null;
            
            switch (department) {
                case "GIC":
                    selectedMajor = Backend.src.major.major.getGICMajor();
                    break;
                case "GIM":
                    selectedMajor = Backend.src.major.major.getGIMMajor();
                    break;
                case "GEE":
                    selectedMajor = Backend.src.major.major.getGEEMajor();
                    break;
            }
            
            if (selectedMajor == null) {
                statusLabel.setText("❌ Major selection cancelled");
                statusLabel.setForeground(ACCENT_ORANGE);
                return;
            }
            
            // Save to database using MajorManager
            boolean success = MajorManager.saveDepartmentMajor(studentID, department, selectedMajor);
            
            if (success) {
                statusLabel.setText("✓ " + studentName + " assigned to " + department + " - " + selectedMajor);
                statusLabel.setForeground(ACCENT_GREEN);
                
                JOptionPane.showMessageDialog(this,
                    "Student successfully assigned!\n\n" +
                    "Department: " + department + "\n" +
                    "Major: " + selectedMajor,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh details to show updated information
                displaySelectedStudentDetails();
            } else {
                statusLabel.setText("❌ Failed to assign department");
                statusLabel.setForeground(ACCENT_RED);
                
                JOptionPane.showMessageDialog(this,
                    "Failed to assign student to department.\n" +
                    "Please check if the student is already assigned.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
            // Reset selection
            departmentCombo.setSelectedIndex(0);
        }
    }
    
    private void handleBack() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to go back?",
            "Confirm",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
            new MainpageTeacher().setVisible(true);
        });
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            StudentDepartmentAssignmentGUI frame = new StudentDepartmentAssignmentGUI();
            frame.setVisible(true);
        });
    }
}