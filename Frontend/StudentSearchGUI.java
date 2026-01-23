package Frontend;

import Backend.src.database.StudentInfoManager;
import Backend.src.database.MajorManager;
import Backend.main.Main;
import Backend.main.MainPageStudent;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;

public class StudentSearchGUI extends JFrame {
    
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JPanel detailsPanel;
    private JScrollPane detailsScrollPane;
    
    private Color primaryColor = new Color(41, 128, 185);
    private Color accentColor = new Color(46, 204, 113);
    private Color dangerColor = new Color(231, 76, 60);
    private Color universityBlue = new Color(0, 51, 102);
    private Color universityGold = new Color(218, 165, 32);
    
    public StudentSearchGUI() {
        initializeUI();
        StudentInfoManager.createStudentInfoTable();
    }
    
    private void initializeUI() {
        setTitle("Student Search System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Top panel for search
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Center panel with split view
        JSplitPane splitPane = createSplitPane();
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, universityBlue, getWidth(), 0, new Color(0, 76, 153));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(1200, 150));
        
        // Title
        JLabel titleLabel = new JLabel("STUDENT SEARCH SYSTEM");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(universityGold);
        titleLabel.setBounds(0, 15, 1200, 40);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Find students by name or ID");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setBounds(0, 55, 1200, 20);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(subtitleLabel);
        
        // Search type label
        JLabel searchTypeLabel = new JLabel("Search By:");
        searchTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchTypeLabel.setForeground(Color.WHITE);
        searchTypeLabel.setBounds(250, 95, 100, 30);
        panel.add(searchTypeLabel);
        
        // Search type combo box
        String[] searchTypes = {"Name", "ID"};
        searchTypeCombo = new JComboBox<>(searchTypes);
        searchTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchTypeCombo.setBounds(350, 95, 120, 35);
        searchTypeCombo.setBackground(Color.WHITE);
        panel.add(searchTypeCombo);
        
        // Search field
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBounds(480, 95, 300, 35);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchField.addActionListener(e -> performSearch());
        panel.add(searchField);
        
        // Search button
        JButton searchBtn = createStyledButton("Search", accentColor, new Color(39, 174, 96));
        searchBtn.setBounds(790, 95, 120, 35);
        searchBtn.addActionListener(e -> performSearch());
        panel.add(searchBtn);
        
        // Back button
        JButton backBtn = createStyledButton("← Back", dangerColor, new Color(192, 57, 43));
        backBtn.setBounds(1050, 15, 120, 35);
        backBtn.addActionListener(e -> handleBack());
        panel.add(backBtn);
        
        return panel;
    }
    
    private JSplitPane createSplitPane() {
        // Left panel - Results table
        JPanel leftPanel = createResultsPanel();
        
        // Right panel - Details view
        JPanel rightPanel = createDetailsPanel();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(600);
        splitPane.setOneTouchExpandable(true);
        
        return splitPane;
    }
    
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 5),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                "SEARCH RESULTS",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                universityBlue
            )
        ));
        
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
        resultsTable.setRowHeight(35);
        resultsTable.setGridColor(new Color(189, 195, 199));
        resultsTable.setSelectionBackground(new Color(52, 152, 219));
        resultsTable.setSelectionForeground(Color.WHITE);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Header styling
        JTableHeader header = resultsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(universityBlue);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Center align # column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        resultsTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        // Column widths
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        resultsTable.getColumnModel().getColumn(1).setPreferredWidth(250);
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
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Info label
        JLabel infoLabel = new JLabel("Click on a row to view details");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(127, 140, 141));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(infoLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 5, 10, 10),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                "STUDENT DETAILS",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                universityBlue
            )
        ));
        
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        
        // Initial message
        JLabel emptyLabel = new JLabel("Select a student to view details");
        emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        emptyLabel.setForeground(new Color(127, 140, 141));
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(Box.createVerticalGlue());
        detailsPanel.add(emptyLabel);
        detailsPanel.add(Box.createVerticalGlue());
        
        detailsScrollPane = new JScrollPane(detailsPanel);
        detailsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(detailsScrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void performSearch() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a search term", 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, 
                "No students found with name containing: " + searchName, 
                "No Results", 
                JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, 
                "No student found with ID: " + searchID, 
                "No Results", 
                JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, 
                "Failed to retrieve student details", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        displayStudentDetails(studentInfo);
    }
    
    private void displayStudentDetails(String[] studentInfo) {
        detailsPanel.removeAll();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        
        detailsPanel.add(Box.createVerticalStrut(20));
        
        // Student info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(4, 1, 10, 10));
        infoPanel.setMaximumSize(new Dimension(500, 150));
        infoPanel.setBackground(new Color(236, 240, 241));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        infoPanel.add(createDetailLabel("Name:", studentInfo[0]));
        infoPanel.add(createDetailLabel("ID:", studentInfo[1]));
        infoPanel.add(createDetailLabel("Gender:", studentInfo[2]));
        infoPanel.add(createDetailLabel("Year:", studentInfo[3]));
        
        detailsPanel.add(infoPanel);
        detailsPanel.add(Box.createVerticalStrut(20));
        
        // Department and Major
        String[] deptMajorCourses = MajorManager.getFullDepartmentMajorCourse(studentInfo[1]);
        
        if (deptMajorCourses != null && deptMajorCourses.length >= 6) {
            // Department/Major panel
            JPanel deptPanel = new JPanel();
            deptPanel.setLayout(new GridLayout(2, 1, 10, 10));
            deptPanel.setMaximumSize(new Dimension(500, 80));
            deptPanel.setBackground(new Color(236, 240, 241));
            deptPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(universityGold, 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));
            
            deptPanel.add(createDetailLabel("Department:", deptMajorCourses[0]));
            deptPanel.add(createDetailLabel("Major:", deptMajorCourses[1]));
            
            detailsPanel.add(deptPanel);
            detailsPanel.add(Box.createVerticalStrut(20));
            
            // Courses panel
            JPanel coursesPanel = new JPanel();
            coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
            coursesPanel.setMaximumSize(new Dimension(500, 200));
            coursesPanel.setBackground(Color.WHITE);
            coursesPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    "ENROLLED COURSES",
                    0, 0,
                    new Font("Segoe UI", Font.BOLD, 14),
                    universityBlue
                ),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            
            for (int i = 2; i < 6; i++) {
                JLabel courseLabel = new JLabel((i - 1) + ". " + deptMajorCourses[i]);
                courseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                courseLabel.setForeground(new Color(44, 62, 80));
                courseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                coursesPanel.add(courseLabel);
                if (i < 5) {
                    coursesPanel.add(Box.createVerticalStrut(8));
                }
            }
            
            detailsPanel.add(coursesPanel);
        } else {
            JPanel noDataPanel = new JPanel();
            noDataPanel.setMaximumSize(new Dimension(500, 80));
            noDataPanel.setBackground(new Color(255, 243, 224));
            noDataPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 165, 0), 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));
            
            JLabel noDataLabel = new JLabel("⚠ Department and Major not assigned");
            noDataLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            noDataLabel.setForeground(new Color(255, 140, 0));
            noDataPanel.add(noDataLabel);
            
            detailsPanel.add(noDataPanel);
        }
        
        detailsPanel.add(Box.createVerticalGlue());
        
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }
    
    private JPanel createDetailLabel(String label, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(new Color(236, 240, 241));
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComp.setForeground(new Color(52, 73, 94));
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueComp.setForeground(new Color(44, 62, 80));
        
        panel.add(labelComp);
        panel.add(valueComp);
        
        return panel;
    }
    
    private void clearDetailsPanel() {
        detailsPanel.removeAll();
        
        JLabel emptyLabel = new JLabel("Select a student to view details");
        emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        emptyLabel.setForeground(new Color(127, 140, 141));
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        detailsPanel.add(Box.createVerticalGlue());
        detailsPanel.add(emptyLabel);
        detailsPanel.add(Box.createVerticalGlue());
        
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }
    
    private JButton createStyledButton(String text, Color normalColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(hoverColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(normalColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
            MainPageTeacherGUI.main(new String[]{});
        }
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
