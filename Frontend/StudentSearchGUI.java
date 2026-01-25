package Frontend;

import Backend.src.database.MajorManager;
import Backend.src.database.StudentInfoManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class StudentSearchGUI extends JFrame {
    
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JPanel detailsPanel;
    private JScrollPane detailsScrollPane;
    
    // Modern color scheme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color BG_LIGHT = new Color(241, 245, 249);
    
    public StudentSearchGUI() {
        initializeUI();
        StudentInfoManager.createStudentInfoTable();
    }
    
    private void initializeUI() {
        setTitle("Student Search System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setResizable(true);
        
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

                // Decorative circles
                g2d.setColor(new Color(147, 197, 253, 25));
                g2d.fillOval(-80, -80, 240, 240);
                g2d.fillOval(getWidth() - 160, getHeight() - 160, 240, 240);
            }
        };
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        // Search panel
        JPanel searchPanel = createSearchPanel();
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Results panel with split view
        JSplitPane splitPane = createSplitPane();
        contentPanel.add(splitPane, BorderLayout.CENTER);
        
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

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);

                // Blue gradient accent
                GradientPaint blueGradient = new GradientPaint(
                    0, 0, new Color(59, 130, 246, 200),
                    0, getHeight(), new Color(96, 165, 250, 150)
                );
                g2d.setPaint(blueGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                // Subtle background pattern
                g2d.setColor(new Color(59, 130, 246, 8));
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
                    0, 0, ACCENT_BLUE,
                    getWidth(), getHeight(), new Color(37, 99, 235)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("🔍");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Student Search System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Find and view detailed student information");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        
        JLabel infoLabel = new JLabel("Search by Name or Student ID");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(ACCENT_BLUE);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(subtitleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 2)));
        titlePanel.add(infoLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        // Back button
        JButton backButton = createBackButton();
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel() {
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
        searchPanel.setOpaque(false);
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Search type label
        JLabel searchTypeLabel = new JLabel("Search By:");
        searchTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchTypeLabel.setForeground(TEXT_PRIMARY);

        // Search type combo box
        String[] searchTypes = {"Name", "ID"};
        searchTypeCombo = new JComboBox<>(searchTypes);
        searchTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchTypeCombo.setPreferredSize(new Dimension(140, 40));
        searchTypeCombo.setBackground(Color.WHITE);
        searchTypeCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Search field
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(400, 40));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        searchField.addActionListener(e -> performSearch());
        
        // Placeholder effect
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                searchField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_BLUE, 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                searchField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });

        // Search button
        JButton searchBtn = createActionButton("Search", ACCENT_BLUE, "");
        searchBtn.setPreferredSize(new Dimension(130, 40));
        searchBtn.addActionListener(e -> performSearch());

        // Clear button
        JButton clearBtn = createActionButton("Clear", TEXT_SECONDARY, "");
        clearBtn.setPreferredSize(new Dimension(100, 40));
        clearBtn.addActionListener(e -> clearSearch());

        searchPanel.add(searchTypeLabel);
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(clearBtn);

        return searchPanel;
    }
    
    private JSplitPane createSplitPane() {
        // Left panel - Results table
        JPanel leftPanel = createResultsPanel();
        
        // Right panel - Details view
        JPanel rightPanel = createDetailsPanel();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(650);
        splitPane.setOneTouchExpandable(true);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setOpaque(false);
        
        return splitPane;
    }
    
    private JPanel createResultsPanel() {
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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 5));
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Search Results");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        // Create table
        String[] columns = {"#", "Student Name", "Student ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resultsTable = new JTable(tableModel);
        resultsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultsTable.setRowHeight(40);
        resultsTable.setGridColor(new Color(226, 232, 240));
        resultsTable.setSelectionBackground(new Color(219, 234, 254));
        resultsTable.setSelectionForeground(TEXT_PRIMARY);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.setShowVerticalLines(true);
        resultsTable.setIntercellSpacing(new Dimension(1, 1));
        
        // Header styling
        JTableHeader header = resultsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));
        
        // Center align # column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        resultsTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        // Column widths
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        resultsTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        resultsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        // Add selection listener
        resultsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySelectedStudentDetails();
            }
        });
        
        // Add double-click listener
        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    displaySelectedStudentDetails();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 15, 0));
        
        JLabel infoLabel = new JLabel("Click on a row to view details");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(TEXT_SECONDARY);
        infoPanel.add(infoLabel);
        
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(tableContainer, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        return panel;
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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        
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
    
    private void performSearch() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            showModernDialog("Please enter a search term", "⚠️ Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Clear previous results
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
            showModernDialog(
                "No students found with name containing: " + searchName, 
                "ℹ️ No Results", 
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        // Populate table
        for (int i = 0; i < results.length; i++) {
            Object[] row = {(i + 1), results[i][0], results[i][1]};
            tableModel.addRow(row);
        }
        
        // Auto-select first row
        if (results.length > 0) {
            resultsTable.setRowSelectionInterval(0, 0);
        }
    }
    
    private void searchByID(String searchID) {
        String[] result = StudentInfoManager.getStudentInfo(searchID);
        
        if (result == null) {
            showModernDialog(
                "No student found with ID: " + searchID, 
                "ℹ️ No Results", 
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        // Add to table
        Object[] row = {1, result[0], result[1]};
        tableModel.addRow(row);
        
        // Auto-select the row
        resultsTable.setRowSelectionInterval(0, 0);
    }
    
    private void displaySelectedStudentDetails() {
        int selectedRow = resultsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            return;
        }
        
        String studentID = (String) tableModel.getValueAt(selectedRow, 2);
        String[] studentInfo = StudentInfoManager.getStudentInfo(studentID);
        
        if (studentInfo == null) {
            showModernDialog(
                "Failed to retrieve student details", 
                "❌ Error", 
                JOptionPane.ERROR_MESSAGE
            );
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
        
        basicInfoCard.add(createModernDetailRow("Name", studentInfo[0]));
        basicInfoCard.add(createModernDetailRow("Student ID", studentInfo[1]));
        basicInfoCard.add(createModernDetailRow("Gender", studentInfo[2]));
        basicInfoCard.add(createModernDetailRow("Year", studentInfo[3]));
        
        detailsPanel.add(basicInfoCard);
        detailsPanel.add(Box.createVerticalStrut(15));
        
        // Department and Major
        String[] deptMajorCourses = MajorManager.getFullDepartmentMajorCourse(studentInfo[1]);
        
        if (deptMajorCourses != null && deptMajorCourses.length >= 6) {
            // Department/Major card
            JPanel academicCard = createInfoCard("🎓 Academic Information", ACCENT_PURPLE);
            academicCard.setLayout(new GridLayout(2, 1, 0, 12));
            
            academicCard.add(createModernDetailRow("Department", deptMajorCourses[0]));
            academicCard.add(createModernDetailRow("Major", deptMajorCourses[1]));
            
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
            JPanel noDataCard = createInfoCard("⚠️ Missing Information", new Color(251, 146, 60));
            
            JLabel noDataLabel = new JLabel("Department and Major not assigned");
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
        card.setMaximumSize(new Dimension(550, 1000));
        
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
    
    private JPanel createModernDetailRow(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel labelComp = new JLabel(label + ":");
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComp.setForeground(TEXT_SECONDARY);
        labelComp.setPreferredSize(new Dimension(100, 25));
        
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
    
    private void clearSearch() {
        searchField.setText("");
        tableModel.setRowCount(0);
        clearDetailsPanel();
        searchField.requestFocus();
    }
    
    private JButton createActionButton(String text, Color normalColor, String icon) {
        JButton button = new JButton(icon + " " + text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color currentColor;
                if (getModel().isPressed()) {
                    currentColor = normalColor.darker();
                } else if (getModel().isRollover()) {
                    currentColor = new Color(
                        Math.max(normalColor.getRed() - 20, 0),
                        Math.max(normalColor.getGreen() - 20, 0),
                        Math.max(normalColor.getBlue() - 20, 0)
                    );
                } else {
                    currentColor = normalColor;
                }

                g2d.setColor(currentColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

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
        
        button.addActionListener(e -> handleBack());

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(MouseEvent evt) { button.repaint(); }
        });

        return button;
    }
    
    private void handleBack() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
            MainPageStudentGUI.main(new String[]{});
        }
    }
    
    private void showModernDialog(String message, String title, int messageType) {
        UIManager.put("OptionPane.background", CARD_BG);
        UIManager.put("Panel.background", CARD_BG);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            StudentSearchGUI frame = new StudentSearchGUI();
            frame.setVisible(true);
        });
    }
}