package Frontend.teacher;

import Backend.src.department.*;
import Backend.src.database.*;
import Backend.src.course.Course;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateTeacherGUI extends JDialog {

    private static final Color PRIMARY_COLOR = new Color(243, 156, 18);
    private static final Color SECONDARY_COLOR = new Color(230, 126, 34);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color CARD_BG = Color.WHITE;

    private JTextField teacherIdField;
    private JComboBox<String> departmentCombo;
    private JComboBox<String> majorCombo;
    private JComboBox<String> courseCombo;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JLabel teacherInfoLabel;
    private JLabel currentInfoLabel;
    private List<String> selectedCourses = new ArrayList<>();

    public UpdateTeacherGUI(JDialog parent) {
        super(parent, "Update Teacher Information", true);
        setSize(650, 800);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        System.out.println("\n--- Update Teacher Information ---");
        DatabaseManager.displayTemporaryTeacherUsers();
        
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
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

        JLabel headerLabel = new JLabel("UPDATE TEACHER", SwingConstants.CENTER);
        headerLabel.setBounds(50, 20, 550, 35);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        mainPanel.add(headerLabel);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBounds(40, 70, 570, 680);
        cardPanel.setBackground(CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        int yPos = 20;

        JLabel teacherIdLabel = new JLabel("Teacher ID:");
        teacherIdLabel.setBounds(20, yPos, 150, 25);
        teacherIdLabel.setFont(new Font("Arial", Font.BOLD, 13));
        teacherIdLabel.setForeground(TEXT_COLOR);
        cardPanel.add(teacherIdLabel);

        teacherIdField = new JTextField();
        teacherIdField.setBounds(20, yPos + 30, 350, 35);
        teacherIdField.setFont(new Font("Arial", Font.PLAIN, 13));
        cardPanel.add(teacherIdField);

        JButton checkButton = new JButton("Check");
        checkButton.setBounds(380, yPos + 30, 100, 35);
        checkButton.setBackground(PRIMARY_COLOR);
        checkButton.setForeground(Color.WHITE);
        checkButton.setFont(new Font("Arial", Font.BOLD, 12));
        checkButton.setFocusPainted(false);
        checkButton.setBorderPainted(false);
        checkButton.setOpaque(true);
        checkButton.addActionListener(e -> checkTeacher());
        cardPanel.add(checkButton);

        yPos += 80;

        teacherInfoLabel = new JLabel("");
        teacherInfoLabel.setBounds(20, yPos, 510, 40);
        teacherInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        teacherInfoLabel.setForeground(TEXT_COLOR);
        cardPanel.add(teacherInfoLabel);

        yPos += 50;

        currentInfoLabel = new JLabel("");
        currentInfoLabel.setBounds(20, yPos, 510, 60);
        currentInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        currentInfoLabel.setForeground(new Color(52, 73, 94));
        cardPanel.add(currentInfoLabel);

        yPos += 70;

        JLabel deptLabel = new JLabel("Select NEW Department:");
        deptLabel.setBounds(20, yPos, 200, 25);
        deptLabel.setFont(new Font("Arial", Font.BOLD, 13));
        deptLabel.setForeground(TEXT_COLOR);
        cardPanel.add(deptLabel);

        String[] departments = {"-- Select Department --", "GIC", "GIM", "GEE"};
        departmentCombo = new JComboBox<>(departments);
        departmentCombo.setBounds(20, yPos + 30, 510, 35);
        departmentCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        departmentCombo.addActionListener(e -> loadMajorsForDepartment());
        cardPanel.add(departmentCombo);

        yPos += 80;

        JLabel majorLabel = new JLabel("Select NEW Major:");
        majorLabel.setBounds(20, yPos, 150, 25);
        majorLabel.setFont(new Font("Arial", Font.BOLD, 13));
        majorLabel.setForeground(TEXT_COLOR);
        cardPanel.add(majorLabel);

        majorCombo = new JComboBox<>();
        majorCombo.setBounds(20, yPos + 30, 510, 35);
        majorCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        majorCombo.addItem("-- Select Major --");
        majorCombo.setEnabled(false);
        majorCombo.addActionListener(e -> loadCoursesForMajor());
        cardPanel.add(majorCombo);

        yPos += 80;

        JLabel courseLabel = new JLabel("Select Course to Add:");
        courseLabel.setBounds(20, yPos, 200, 25);
        courseLabel.setFont(new Font("Arial", Font.BOLD, 13));
        courseLabel.setForeground(TEXT_COLOR);
        cardPanel.add(courseLabel);

        courseCombo = new JComboBox<>();
        courseCombo.setBounds(20, yPos + 30, 390, 35);
        courseCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        courseCombo.addItem("-- Select Course --");
        courseCombo.setEnabled(false);
        cardPanel.add(courseCombo);

        JButton addCourseButton = new JButton("Add");
        addCourseButton.setBounds(420, yPos + 30, 110, 35);
        addCourseButton.setBackground(PRIMARY_COLOR);
        addCourseButton.setForeground(Color.WHITE);
        addCourseButton.setFont(new Font("Arial", Font.BOLD, 12));
        addCourseButton.setFocusPainted(false);
        addCourseButton.setBorderPainted(false);
        addCourseButton.setOpaque(true);
        addCourseButton.addActionListener(e -> addCourseToList());
        cardPanel.add(addCourseButton);

        yPos += 80;

        JLabel selectedLabel = new JLabel("Selected Courses:");
        selectedLabel.setBounds(20, yPos, 200, 25);
        selectedLabel.setFont(new Font("Arial", Font.BOLD, 13));
        selectedLabel.setForeground(TEXT_COLOR);
        cardPanel.add(selectedLabel);

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
        courseTable.setSelectionBackground(new Color(243, 156, 18, 50));
        courseTable.setGridColor(new Color(189, 195, 199));
        
        courseTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        courseTable.getColumnModel().getColumn(1).setPreferredWidth(400);

        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBounds(20, yPos, 510, 120);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        cardPanel.add(scrollPane);

        yPos += 135;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBounds(20, yPos, 510, 40);
        buttonPanel.setBackground(CARD_BG);

        JButton submitButton = new JButton("Update Teacher");
        submitButton.setPreferredSize(new Dimension(140, 35));
        submitButton.setBackground(WARNING_COLOR);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 13));
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setOpaque(true);
        submitButton.addActionListener(e -> updateTeacher());
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

    private void checkTeacher() {
        String teacherId = teacherIdField.getText().trim();

        if (teacherId.isEmpty()) {
            teacherInfoLabel.setText("<html><b style='color: #e74c3c;'>✗ Teacher ID cannot be empty</b></html>");
            currentInfoLabel.setText("");
            return;
        }

        if (!TeacherInfoManager.teacherExists(teacherId)) {
            teacherInfoLabel.setText("<html><b style='color: #e74c3c;'>✗ Teacher not found</b></html>");
            currentInfoLabel.setText("");
            return;
        }

        teacherInfoLabel.setText("<html><b style='color: #27ae60;'>✓ Teacher Found: " + teacherId + "</b></html>");

        String[] teacherInfo = TeacherInfoManager.getTeacherInfo(teacherId);
        if (teacherInfo != null) {
            currentInfoLabel.setText("<html><b>Current Assignment:</b><br>" + 
                                    "Department: " + teacherInfo[1] + "<br>" +
                                    "Major: " + teacherInfo[2] + "</html>");
            
            System.out.println("\n--- Current Courses ---");
            TeacherCourseManager.getTeacherCourses(teacherId);
        }
    }

    private void loadMajorsForDepartment() {
        String department = (String) departmentCombo.getSelectedItem();
        
        majorCombo.removeAllItems();
        majorCombo.addItem("-- Select Major --");
        courseCombo.removeAllItems();
        courseCombo.addItem("-- Select Course --");
        courseCombo.setEnabled(false);
        
        if (department == null || department.equals("-- Select Department --")) {
            majorCombo.setEnabled(false);
            return;
        }

        majorCombo.setEnabled(true);

        if (department.equals("GIC")) {
            majorCombo.addItem("Software Engineering");
            majorCombo.addItem("Cyber Security");
            majorCombo.addItem("Artificial Intelligence");
        } else if (department.equals("GIM")) {
            majorCombo.addItem("Mechanical Engineering");
            majorCombo.addItem("Manufacturing Engineering");
            majorCombo.addItem("Industrial Engineering");
        } else if (department.equals("GEE")) {
            majorCombo.addItem("Electrical Engineering");
            majorCombo.addItem("Electronics Engineering");
            majorCombo.addItem("Automation Engineering");
        }
    }

    private void loadCoursesForMajor() {
        String selectedMajor = (String) majorCombo.getSelectedItem();
        
        courseCombo.removeAllItems();
        courseCombo.addItem("-- Select Course --");
        
        if (selectedMajor == null || selectedMajor.equals("-- Select Major --")) {
            courseCombo.setEnabled(false);
            return;
        }

        courseCombo.setEnabled(true);

        String[] courses = Course.getCoursesForMajor(selectedMajor);
        
        if (courses != null) {
            for (String course : courses) {
                courseCombo.addItem(course);
            }
        }
    }

    private void addCourseToList() {
        String selectedCourse = (String) courseCombo.getSelectedItem();
        
        if (selectedCourse == null || selectedCourse.equals("-- Select Course --")) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course to add.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedCourses.contains(selectedCourse)) {
            JOptionPane.showMessageDialog(this,
                    "This course is already added.",
                    "Duplicate Course",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedCourses.add(selectedCourse);
        tableModel.addRow(new Object[]{selectedCourses.size(), selectedCourse});
    }

    private void updateTeacher() {
        String teacherId = teacherIdField.getText().trim();

        if (teacherId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Teacher ID.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!TeacherInfoManager.teacherExists(teacherId)) {
            JOptionPane.showMessageDialog(this,
                    "Teacher not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String department = (String) departmentCombo.getSelectedItem();
        if (department == null || department.equals("-- Select Department --")) {
            JOptionPane.showMessageDialog(this,
                    "Please select a department.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedMajor = (String) majorCombo.getSelectedItem();
        if (selectedMajor == null || selectedMajor.equals("-- Select Major --")) {
            JOptionPane.showMessageDialog(this,
                    "Please select a major.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please add at least one course.",
                    "No Courses",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Update teacher info (from backend logic)
        TeacherInfoManager.updateTeacherInfo(teacherId, department, selectedMajor);

        // Remove old courses (from backend logic)
        TeacherCourseManager.deleteTeacherAllCourses(teacherId);

        // Assign new courses (from backend logic)
        for (String courseName : selectedCourses) {
            String courseId = CourseManager.getCourseIdByName(courseName);
            if (courseId != null) {
                TeacherCourseManager.addTeacherCourse(teacherId, courseId);
            }
        }

        JOptionPane.showMessageDialog(this,
                "Teacher updated successfully!\n\n" +
                        "Teacher ID: " + teacherId + "\n" +
                        "Department: " + department + "\n" +
                        "Major: " + selectedMajor + "\n" +
                        "Courses: " + selectedCourses.size(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}