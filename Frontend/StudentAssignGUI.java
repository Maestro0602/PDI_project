package Frontend;


import Backend.src.database.StudentInfoManager;
import Backend.src.database.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentAssignGUI extends JFrame {
    
    private JTextField nameField, studentIDField, genderField, yearField;
    private JTextArea resultArea;
    private JButton addBtn, updateBtn, deleteBtn, searchBtn, clearBtn;
    
    private Color primaryColor = new Color(41, 128, 185);
    private Color successColor = new Color(46, 204, 113);
    private Color warningColor = new Color(243, 156, 18);
    private Color dangerColor = new Color(231, 76, 60);
    
    public StudentAssignGUI() {
        initializeDatabase();
        initializeUI();
    }
    
    private void initializeDatabase() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                StudentInfoManager.createStudentInfoTable();
                return null;
            }
        };
        worker.execute();
    }
    
    private void initializeUI() {
        setTitle("Student Information Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Tab panel
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Add tab
        JPanel addPanel = createAddPanel();
        tabbedPane.addTab(" Add Student", addPanel);
        
        // Update tab
        JPanel updatePanel = createUpdatePanel();
        tabbedPane.addTab(" Update Student", updatePanel);
        
        // Delete tab
        JPanel deletePanel = createDeletePanel();
        tabbedPane.addTab(" Delete Student", deletePanel);
        
        // Search tab
        JPanel searchPanel = createSearchPanel();
        tabbedPane.addTab(" Search Student", searchPanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(41, 128, 185), getWidth(), 0, new Color(52, 152, 219));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(900, 100));
        
        JLabel iconLabel = new JLabel("📚");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        iconLabel.setBounds(50, 20, 60, 60);
        panel.add(iconLabel);
        
        JLabel titleLabel = new JLabel("Student Information Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(120, 30, 600, 40);
        panel.add(titleLabel);
        
        JButton backBtn = createStyledButton("← Back", dangerColor, new Color(192, 57, 43));
        backBtn.setBounds(750, 30, 120, 40);
        backBtn.addActionListener(e -> dispose());
        panel.add(backBtn);
        
        return panel;
    }
    
    private JPanel createAddPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        
        // Student Name
        JLabel nameLabel = new JLabel("Student Name");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(52, 73, 94));
        nameLabel.setBounds(50, 30, 350, 25);
        panel.add(nameLabel);
        
        nameField = createStyledTextField();
        nameField.setBounds(50, 60, 350, 40);
        panel.add(nameField);
        
        // Student ID
        JLabel idLabel = new JLabel("Student ID");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(52, 73, 94));
        idLabel.setBounds(500, 30, 350, 25);
        panel.add(idLabel);
        
        studentIDField = createStyledTextField();
        studentIDField.setBounds(500, 60, 350, 40);
        panel.add(studentIDField);
        
        // Gender
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        genderLabel.setForeground(new Color(52, 73, 94));
        genderLabel.setBounds(50, 120, 350, 25);
        panel.add(genderLabel);
        
        genderField = createStyledTextField();
        genderField.setBounds(50, 150, 350, 40);
        panel.add(genderField);
        
        // Year
        JLabel yearLabel = new JLabel("Year");
        yearLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        yearLabel.setForeground(new Color(52, 73, 94));
        yearLabel.setBounds(500, 120, 350, 25);
        panel.add(yearLabel);
        
        yearField = createStyledTextField();
        yearField.setBounds(500, 150, 350, 40);
        panel.add(yearField);
        
        // Buttons
        addBtn = createStyledButton(" Add Student", successColor, new Color(39, 174, 96));
        addBtn.setBounds(300, 220, 150, 45);
        addBtn.addActionListener(e -> handleAddStudent());
        panel.add(addBtn);
        
        clearBtn = createStyledButton("Clear", new Color(149, 165, 166), new Color(127, 140, 141));
        clearBtn.setBounds(470, 220, 100, 45);
        clearBtn.addActionListener(e -> clearAddForm());
        panel.add(clearBtn);
        
        // Result Area
        JLabel resultLabel = new JLabel("Results / Messages");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resultLabel.setForeground(new Color(52, 73, 94));
        resultLabel.setBounds(50, 290, 400, 20);
        panel.add(resultLabel);
        
        resultArea = new JTextArea();
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBounds(50, 320, 800, 180);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(scrollPane);
        
        return panel;
    }
    
    private JPanel createUpdatePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        
        // Step 1: Search by Student ID
        JLabel searchLabel = new JLabel("Step 1: Enter Student ID to Update");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchLabel.setForeground(primaryColor);
        searchLabel.setBounds(50, 30, 800, 25);
        panel.add(searchLabel);
        
        JLabel idLabel = new JLabel("Student ID");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(52, 73, 94));
        idLabel.setBounds(50, 70, 300, 25);
        panel.add(idLabel);
        
        JTextField updateIDField = createStyledTextField();
        updateIDField.setBounds(50, 100, 500, 40);
        panel.add(updateIDField);
        
        JButton searchForUpdateBtn = createStyledButton("Search", primaryColor, new Color(52, 152, 219));
        searchForUpdateBtn.setBounds(570, 100, 120, 40);
        panel.add(searchForUpdateBtn);
        
        // Step 2: Display and Update Fields (initially hidden)
        JSeparator separator = new JSeparator();
        separator.setBounds(50, 170, 800, 2);
        separator.setVisible(false);
        panel.add(separator);
        
        JLabel updateLabel = new JLabel("Step 2: Update Student Information");
        updateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        updateLabel.setForeground(primaryColor);
        updateLabel.setBounds(50, 190, 800, 25);
        updateLabel.setVisible(false);
        panel.add(updateLabel);
        
        JLabel nameUpdateLabel = new JLabel("Student Name");
        nameUpdateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameUpdateLabel.setForeground(new Color(52, 73, 94));
        nameUpdateLabel.setBounds(50, 230, 800, 25);
        nameUpdateLabel.setVisible(false);
        panel.add(nameUpdateLabel);
        
        JTextField nameUpdateField = createStyledTextField();
        nameUpdateField.setBounds(50, 260, 800, 40);
        nameUpdateField.setEnabled(false);
        nameUpdateField.setBackground(new Color(236, 240, 241));
        nameUpdateField.setVisible(false);
        panel.add(nameUpdateField);
        
        JLabel genderUpdateLabel = new JLabel("Gender");
        genderUpdateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        genderUpdateLabel.setForeground(new Color(52, 73, 94));
        genderUpdateLabel.setBounds(50, 320, 350, 25);
        genderUpdateLabel.setVisible(false);
        panel.add(genderUpdateLabel);
        
        JTextField genderUpdateField = createStyledTextField();
        genderUpdateField.setBounds(50, 350, 350, 40);
        genderUpdateField.setVisible(false);
        panel.add(genderUpdateField);
        
        JLabel yearUpdateLabel = new JLabel("Year");
        yearUpdateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        yearUpdateLabel.setForeground(new Color(52, 73, 94));
        yearUpdateLabel.setBounds(500, 320, 350, 25);
        yearUpdateLabel.setVisible(false);
        panel.add(yearUpdateLabel);
        
        JTextField yearUpdateField = createStyledTextField();
        yearUpdateField.setBounds(500, 350, 350, 40);
        yearUpdateField.setVisible(false);
        panel.add(yearUpdateField);
        
        JButton updateConfirmBtn = createStyledButton(" Update Information", successColor, new Color(39, 174, 96));
        updateConfirmBtn.setBounds(300, 420, 200, 45);
        updateConfirmBtn.setVisible(false);
        panel.add(updateConfirmBtn);
        
        JButton cancelUpdateBtn = createStyledButton("Cancel", new Color(149, 165, 166), new Color(127, 140, 141));
        cancelUpdateBtn.setBounds(520, 420, 100, 45);
        cancelUpdateBtn.setVisible(false);
        panel.add(cancelUpdateBtn);
        
        // Result Area
        JTextArea updateResultArea = new JTextArea();
        updateResultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        updateResultArea.setEditable(false);
        updateResultArea.setLineWrap(true);
        updateResultArea.setWrapStyleWord(true);
        updateResultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane updateScrollPane = new JScrollPane(updateResultArea);
        updateScrollPane.setBounds(50, 500, 800, 50);
        updateScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(updateScrollPane);
        
        // Search button action
        searchForUpdateBtn.addActionListener(e -> {
            String studentID = updateIDField.getText().trim();
            
            if (studentID.isEmpty()) {
                updateResultArea.setForeground(warningColor);
                updateResultArea.setText(" WARNING: Please enter Student ID!");
                return;
            }
            
            String[] studentInfo = StudentInfoManager.getStudentInfo(studentID);
            
            if (studentInfo != null) {
                // Show update fields
                separator.setVisible(true);
                updateLabel.setVisible(true);
                nameUpdateLabel.setVisible(true);
                nameUpdateField.setVisible(true);
                genderUpdateLabel.setVisible(true);
                genderUpdateField.setVisible(true);
                yearUpdateLabel.setVisible(true);
                yearUpdateField.setVisible(true);
                updateConfirmBtn.setVisible(true);
                cancelUpdateBtn.setVisible(true);
                
                // Fill fields
                nameUpdateField.setText(studentInfo[0]);
                genderUpdateField.setText(studentInfo[2]);
                yearUpdateField.setText(studentInfo[3]);
                
                updateResultArea.setForeground(primaryColor);
                updateResultArea.setText(" Student Found! You can now update Gender and Year below.");
            } else {
                updateResultArea.setForeground(dangerColor);
                updateResultArea.setText(" ERROR: Student ID not found!");
                
                // Hide update fields
                separator.setVisible(false);
                updateLabel.setVisible(false);
                nameUpdateLabel.setVisible(false);
                nameUpdateField.setVisible(false);
                genderUpdateLabel.setVisible(false);
                genderUpdateField.setVisible(false);
                yearUpdateLabel.setVisible(false);
                yearUpdateField.setVisible(false);
                updateConfirmBtn.setVisible(false);
                cancelUpdateBtn.setVisible(false);
            }
        });
        
        // Update confirm button action
        updateConfirmBtn.addActionListener(e -> {
            String studentID = updateIDField.getText().trim();
            String name = nameUpdateField.getText().trim();
            String gender = genderUpdateField.getText().trim();
            String year = yearUpdateField.getText().trim();
            
            if (gender.isEmpty() || year.isEmpty()) {
                updateResultArea.setForeground(warningColor);
                updateResultArea.setText(" WARNING: Gender and Year cannot be empty!");
                return;
            }
            
            if (StudentInfoManager.updateStudentInfo(studentID, name, gender, year)) {
                updateResultArea.setForeground(successColor);
                updateResultArea.setText(" SUCCESS: Student Information Updated!\n\n" +
                                       "Student ID: " + studentID + "\n" +
                                       "Name: " + name + "\n" +
                                       "Gender: " + gender + "\n" +
                                       "Year: " + year);
            } else {
                updateResultArea.setForeground(dangerColor);
                updateResultArea.setText(" ERROR: Failed to update student information!");
            }
        });
        
        // Cancel button action
        cancelUpdateBtn.addActionListener(e -> {
            updateIDField.setText("");
            nameUpdateField.setText("");
            genderUpdateField.setText("");
            yearUpdateField.setText("");
            updateResultArea.setText("");
            separator.setVisible(false);
            updateLabel.setVisible(false);
            nameUpdateLabel.setVisible(false);
            nameUpdateField.setVisible(false);
            genderUpdateLabel.setVisible(false);
            genderUpdateField.setVisible(false);
            yearUpdateLabel.setVisible(false);
            yearUpdateField.setVisible(false);
            updateConfirmBtn.setVisible(false);
            cancelUpdateBtn.setVisible(false);
        });
        
        return panel;
    }
    
    private JPanel createDeletePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Delete Student from Database");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(dangerColor);
        titleLabel.setBounds(50, 50, 800, 30);
        panel.add(titleLabel);
        
        JLabel idLabel = new JLabel("Student ID");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(52, 73, 94));
        idLabel.setBounds(50, 110, 800, 25);
        panel.add(idLabel);
        
        JTextField deleteIDField = createStyledTextField();
        deleteIDField.setBounds(50, 140, 800, 45);
        panel.add(deleteIDField);
        
        JButton deleteConfirmBtn = createStyledButton(" Delete Student", dangerColor, new Color(192, 57, 43));
        deleteConfirmBtn.setBounds(325, 210, 150, 45);
        panel.add(deleteConfirmBtn);
        
        JButton clearDeleteBtn = createStyledButton("Clear", new Color(149, 165, 166), new Color(127, 140, 141));
        clearDeleteBtn.setBounds(495, 210, 100, 45);
        panel.add(clearDeleteBtn);
        
        // Result Area
        JLabel resultLabel = new JLabel("Results / Messages");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resultLabel.setForeground(new Color(52, 73, 94));
        resultLabel.setBounds(50, 280, 800, 25);
        panel.add(resultLabel);
        
        JTextArea deleteResultArea = new JTextArea();
        deleteResultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        deleteResultArea.setEditable(false);
        deleteResultArea.setLineWrap(true);
        deleteResultArea.setWrapStyleWord(true);
        deleteResultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane deleteScrollPane = new JScrollPane(deleteResultArea);
        deleteScrollPane.setBounds(50, 310, 800, 200);
        deleteScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(deleteScrollPane);
        
        // Delete button action
        deleteConfirmBtn.addActionListener(e -> {
            String studentID = deleteIDField.getText().trim();
            
            if (studentID.isEmpty()) {
                deleteResultArea.setForeground(warningColor);
                deleteResultArea.setText(" WARNING: Please enter Student ID to delete!");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete student:\n\n" +
                "Student ID: " + studentID + "\n\n" +
                " This action cannot be undone!",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = StudentInfoManager.deleteStudent(studentID);
                
                if (success) {
                    deleteResultArea.setForeground(successColor);
                    deleteResultArea.setText(" SUCCESS: Student Deleted!\n\n" +
                                           "Student ID: " + studentID + "\n\n" +
                                           "The student has been removed from the database.");
                    deleteIDField.setText("");
                } else {
                    deleteResultArea.setForeground(dangerColor);
                    deleteResultArea.setText(" ERROR: Failed to delete student!\n\n" +
                                           "Student ID: " + studentID + " may not exist.");
                }
            }
        });
        
        // Clear button action
        clearDeleteBtn.addActionListener(e -> {
            deleteIDField.setText("");
            deleteResultArea.setText("");
        });
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Search Student Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(primaryColor);
        titleLabel.setBounds(50, 50, 800, 30);
        panel.add(titleLabel);
        
        JLabel idLabel = new JLabel("Student ID");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(52, 73, 94));
        idLabel.setBounds(50, 110, 800, 25);
        panel.add(idLabel);
        
        JTextField searchIDField = createStyledTextField();
        searchIDField.setBounds(50, 140, 650, 45);
        panel.add(searchIDField);
        
        JButton searchConfirmBtn = createStyledButton("🔍 Search", warningColor, new Color(230, 126, 34));
        searchConfirmBtn.setBounds(720, 140, 130, 45);
        panel.add(searchConfirmBtn);
        
        // Result Area
        JLabel resultLabel = new JLabel("Results");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resultLabel.setForeground(new Color(52, 73, 94));
        resultLabel.setBounds(50, 210, 800, 25);
        panel.add(resultLabel);
        
        JTextArea searchResultArea = new JTextArea();
        searchResultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchResultArea.setEditable(false);
        searchResultArea.setLineWrap(true);
        searchResultArea.setWrapStyleWord(true);
        searchResultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane searchScrollPane = new JScrollPane(searchResultArea);
        searchScrollPane.setBounds(50, 240, 800, 300);
        searchScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(searchScrollPane);
        
        // Search button action
        searchConfirmBtn.addActionListener(e -> {
            String studentID = searchIDField.getText().trim();
            
            if (studentID.isEmpty()) {
                searchResultArea.setForeground(warningColor);
                searchResultArea.setText(" WARNING: Please enter Student ID to search!");
                return;
            }
            
            String[] studentInfo = StudentInfoManager.getStudentInfo(studentID);
            
            if (studentInfo != null) {
                searchResultArea.setForeground(primaryColor);
                searchResultArea.setText(" STUDENT FOUND:\n\n" +
                                       "Name: " + studentInfo[0] + "\n" +
                                       "Student ID: " + studentInfo[1] + "\n" +
                                       "Gender: " + studentInfo[2] + "\n" +
                                       "Year: " + studentInfo[3]);
            } else {
                searchResultArea.setForeground(dangerColor);
                searchResultArea.setText(" NOT FOUND: Student ID not in database!\n\n" +
                                       "Student ID: " + studentID + " does not exist.");
            }
        });
        
        // Enter key listener
        searchIDField.addActionListener(e -> searchConfirmBtn.doClick());
        
        return panel;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(primaryColor, 2),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });
        
        return field;
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
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
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
    
    private void clearAddForm() {
        nameField.setText("");
        studentIDField.setText("");
        genderField.setText("");
        yearField.setText("");
        resultArea.setText("");
        nameField.requestFocus();
    }
    
    private void handleAddStudent() {
        String name = nameField.getText().trim();
        String studentID = studentIDField.getText().trim();
        String gender = genderField.getText().trim();
        String year = yearField.getText().trim();
        
        // Validation
        if (name.isEmpty() || studentID.isEmpty() || gender.isEmpty() || year.isEmpty()) {
            resultArea.setForeground(dangerColor);
            resultArea.setText(" ERROR: All fields are required!\n\nPlease fill in all fields before adding a student.");
            return;
        }
        
        // Check if UserID exists in users table
        if (!DatabaseManager.ConditionChecker.checkUserIDExists(studentID)) {
            resultArea.setForeground(dangerColor);
            resultArea.setText(" ERROR: UserID not found in users table!\n\n" +
                             "Student ID: " + studentID + " does not exist in the system.\n" +
                             "Please ensure the student is registered first.");
            return;
        }
        
        // Save student info
        boolean success = StudentInfoManager.saveStudentInfo(name, studentID, gender, year);
        
        if (success) {
            resultArea.setForeground(successColor);
            resultArea.setText(" SUCCESS: Student Added!\n\n" +
                             "Name: " + name + "\n" +
                             "Student ID: " + studentID + "\n" +
                             "Gender: " + gender + "\n" +
                             "Year: " + year + "\n\n" +
                             "Student information has been saved successfully.");
            clearAddForm();
        } else {
            resultArea.setForeground(dangerColor);
            resultArea.setText(" ERROR: Failed to add student!\n\n" +
                             "The student may already exist in the database.\n" +
                             "Please check and try again.");
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            StudentAssignGUI frame = new StudentAssignGUI();
            frame.setVisible(true);
        });
    }
}