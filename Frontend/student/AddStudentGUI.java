package Frontend.student;

import Backend.src.department.*;
import Backend.src.database.*;
import Backend.src.course.Course;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AddStudentGUI extends JDialog {

    private static final Color PRIMARY_COLOR = new Color(39, 174, 96);
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color CARD_BG = Color.WHITE;

    private JTextField studentIdField;
    private JComboBox<String> departmentCombo;
    private JComboBox<String> majorCombo;
    private JLabel studentInfoLabel;
    private JLabel currentAssignmentLabel;
    private JTable courseTable;
    private DefaultTableModel tableModel;

    public AddStudentGUI(JDialog parent) {
        super(parent, "Add Student to Department", true);
        setSize(600, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        // Display available students in console (from backend logic)
        System.out.println("\n--- Add Student to Department ---");
        System.out.println("Available Students:");
        StudentInfoManager.displayAllStudents();
        
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        // Main panel with gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, 0, getHeight(), SECONDARY_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);

        // Header
        JLabel headerLabel = new JLabel("ADD STUDENT", SwingConstants.CENTER);
        headerLabel.setBounds(50, 20, 450, 35);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        mainPanel.add(headerLabel);

        // Card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBounds(40, 70, 520, 580);
        cardPanel.setBackground(CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        int yPos = 20;

        // Student ID Section
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setBounds(20, yPos, 120, 25);
        studentIdLabel.setFont(new Font("Arial", Font.BOLD, 13));
        studentIdLabel.setForeground(TEXT_COLOR);
        cardPanel.add(studentIdLabel);

        studentIdField = new JTextField();
        studentIdField.setBounds(20, yPos + 30, 250, 35);
        studentIdField.setFont(new Font("Arial", Font.PLAIN, 13));
        cardPanel.add(studentIdField);

        JButton checkButton = new JButton("Check");
        checkButton.setBounds(280, yPos + 30, 100, 35);
        checkButton.setBackground(PRIMARY_COLOR);
        checkButton.setForeground(Color.WHITE);
        checkButton.setFont(new Font("Arial", Font.BOLD, 12));
        checkButton.setFocusPainted(false);
        checkButton.setBorderPainted(false);
        checkButton.setOpaque(true);
        checkButton.addActionListener(e -> checkStudent());
        cardPanel.add(checkButton);

        yPos += 80;

        // Student Info Display
        studentInfoLabel = new JLabel("");
        studentInfoLabel.setBounds(20, yPos, 410, 40);
        studentInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        studentInfoLabel.setForeground(TEXT_COLOR);
        cardPanel.add(studentInfoLabel);

        yPos += 50;

        // Current Assignment Display
        currentAssignmentLabel = new JLabel("");
        currentAssignmentLabel.setBounds(20, yPos, 410, 40);
        currentAssignmentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        currentAssignmentLabel.setForeground(new Color(231, 76, 60));
        cardPanel.add(currentAssignmentLabel);

        yPos += 50;

        // Department Selection
        JLabel deptLabel = new JLabel("Select Department:");
        deptLabel.setBounds(20, yPos, 150, 25);
        deptLabel.setFont(new Font("Arial", Font.BOLD, 13));
        deptLabel.setForeground(TEXT_COLOR);
        cardPanel.add(deptLabel);

        String[] departments = {
                "-- Select Department --",
                Department.GIC.getDisplayName(),
                Department.GIM.getDisplayName(),
                Department.GEE.getDisplayName()
        };

        departmentCombo = new JComboBox<>(departments);
        departmentCombo.setBounds(20, yPos + 30, 460, 35);
        departmentCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        departmentCombo.addActionListener(e -> loadMajorsForDepartment());
        cardPanel.add(departmentCombo);

        yPos += 80;

        // Major Selection
        JLabel majorLabel = new JLabel("Select Major:");
        majorLabel.setBounds(20, yPos, 150, 25);
        majorLabel.setFont(new Font("Arial", Font.BOLD, 13));
        majorLabel.setForeground(TEXT_COLOR);
        cardPanel.add(majorLabel);

        majorCombo = new JComboBox<>();
        majorCombo.setBounds(20, yPos + 30, 460, 35);
        majorCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        majorCombo.addItem("-- Select Major --");
        majorCombo.setEnabled(false);
        majorCombo.addActionListener(e -> loadCoursesForMajor());
        cardPanel.add(majorCombo);

        yPos += 80;

        // Course Table
        JLabel courseLabel = new JLabel("Courses for Selected Major:");
        courseLabel.setBounds(20, yPos, 200, 25);
        courseLabel.setFont(new Font("Arial", Font.BOLD, 13));
        courseLabel.setForeground(TEXT_COLOR);
        cardPanel.add(courseLabel);

        yPos += 30;

        String[] columnNames = {"No.", "Course Name"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setFont(new Font("Arial", Font.PLAIN, 12));
        courseTable.setRowHeight(25);
        courseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        courseTable.getTableHeader().setBackground(PRIMARY_COLOR);
        courseTable.getTableHeader().setForeground(Color.WHITE);
        courseTable.setSelectionBackground(new Color(39, 174, 96, 50));
        courseTable.setGridColor(new Color(189, 195, 199));
        
        // Set column widths
        courseTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        courseTable.getColumnModel().getColumn(1).setPreferredWidth(350);

        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBounds(20, yPos, 460, 130);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        cardPanel.add(scrollPane);

        yPos += 145;

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBounds(20, yPos, 460, 40);
        buttonPanel.setBackground(CARD_BG);

        JButton submitButton = new JButton("Add Student");
        submitButton.setPreferredSize(new Dimension(140, 35));
        submitButton.setBackground(SUCCESS_COLOR);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 13));
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setOpaque(true);
        submitButton.addActionListener(e -> addStudent());
        buttonPanel.add(submitButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(140, 35));
        cancelButton.setBackground(new Color(149, 165, 166));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 13));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setOpaque(true);
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        cardPanel.add(buttonPanel);

        mainPanel.add(cardPanel);
        add(mainPanel);
    }

    private void checkStudent() {
        String studentId = studentIdField.getText().trim();

        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Student ID.",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if UserID exists in users table (from backend logic)
        boolean userIDExists = DatabaseManager.ConditionChecker.checkUserIDExists(studentId);
        if (!userIDExists) {
            studentInfoLabel.setText("<html><b style='color: #e74c3c;'>✗ UserID not found in users table</b></html>");
            currentAssignmentLabel.setText("");
            return;
        }

        // Get student info (from backend logic)
        String[] studentInfo = StudentInfoManager.getStudentInfo(studentId);
        if (studentInfo == null) {
            studentInfoLabel.setText("<html><b style='color: #e74c3c;'>✗ Student not found</b></html>");
            currentAssignmentLabel.setText("");
            return;
        }

        studentInfoLabel.setText("<html><b style='color: #27ae60;'>✓ Student Found:</b> " + 
                                 studentInfo[1] + " (ID: " + studentInfo[0] + ")</html>");

        // Check if student already has a department assigned (from backend logic)
        String[] existingDeptMajor = MajorManager.getDepartmentMajor(studentId);
        if (existingDeptMajor != null) {
            currentAssignmentLabel.setText("<html><b>⚠ This id already have department!</b><br>" + 
                                          existingDeptMajor[0] + " - " + existingDeptMajor[1] + "</html>");
        } else {
            currentAssignmentLabel.setText("<html><b style='color: #27ae60;'>✓ No current assignment</b></html>");
        }
    }

    // Load majors when department is selected
    private void loadMajorsForDepartment() {
        String department = (String) departmentCombo.getSelectedItem();
        
        // Clear existing majors and courses
        majorCombo.removeAllItems();
        majorCombo.addItem("-- Select Major --");
        tableModel.setRowCount(0);
        
        if (department == null || department.equals("-- Select Department --")) {
            majorCombo.setEnabled(false);
            return;
        }

        majorCombo.setEnabled(true);

        // Load all majors based on department
        if (department.equals(Department.GIC.getDisplayName())) {
            majorCombo.addItem("Software Engineering");
            majorCombo.addItem("Cyber Security");
            majorCombo.addItem("Artificial Intelligence");
        } else if (department.equals(Department.GIM.getDisplayName())) {
            majorCombo.addItem("Mechanical Engineering");
            majorCombo.addItem("Manufacturing Engineering");
            majorCombo.addItem("Industrial Engineering");
        } else if (department.equals(Department.GEE.getDisplayName())) {
            majorCombo.addItem("Electrical Engineering");
            majorCombo.addItem("Electronics Engineering");
            majorCombo.addItem("Automation Engineering");
        }
    }

    // Load courses when major is selected
    private void loadCoursesForMajor() {
        String selectedMajor = (String) majorCombo.getSelectedItem();
        
        // Clear existing courses
        tableModel.setRowCount(0);
        
        if (selectedMajor == null || selectedMajor.equals("-- Select Major --")) {
            return;
        }

        // Get courses for the major using Course.getCoursesForMajor()
        String[] courses = Course.getCoursesForMajor(selectedMajor);
        
        if (courses != null) {
            for (int i = 0; i < courses.length; i++) {
                tableModel.addRow(new Object[]{(i + 1), courses[i]});
            }
        }
    }

    private void addStudent() {
        String studentId = studentIdField.getText().trim();

        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Student ID.",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if UserID exists (from backend logic)
        boolean userIDExists = DatabaseManager.ConditionChecker.checkUserIDExists(studentId);
        if (!userIDExists) {
            JOptionPane.showMessageDialog(this,
                    "UserID not found in users table.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get student info (from backend logic)
        String[] studentInfo = StudentInfoManager.getStudentInfo(studentId);
        if (studentInfo == null) {
            JOptionPane.showMessageDialog(this,
                    "Student ID not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if already assigned (from backend logic)
        String[] existingDeptMajor = MajorManager.getDepartmentMajor(studentId);
        if (existingDeptMajor != null) {
            JOptionPane.showMessageDialog(this,
                    "This id already have department!\n" +
                            "Department: " + existingDeptMajor[0] + "\n" +
                            "Major: " + existingDeptMajor[1],
                    "Already Assigned",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check department selection
        String department = (String) departmentCombo.getSelectedItem();
        if (department == null || department.equals("-- Select Department --")) {
            JOptionPane.showMessageDialog(this,
                    "Please select a department.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check major selection
        String selectedMajor = (String) majorCombo.getSelectedItem();
        if (selectedMajor == null || selectedMajor.equals("-- Select Major --")) {
            JOptionPane.showMessageDialog(this,
                    "Please select a major.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Save both department and major together (from backend logic)
        if (MajorManager.saveDepartmentMajor(studentId, department, selectedMajor)) {
            JOptionPane.showMessageDialog(this,
                    "Student assigned to " + department + " with major " + selectedMajor + " successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to assign student.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // From backend logic - getMajorForDepartment method (not used for loading, only for validation)
    // private String getMajorForDepartment(String department) {
    //     // This method is not needed anymore since we're loading all majors
    //     // But keeping it for compatibility
    //     return null;
    // }
}