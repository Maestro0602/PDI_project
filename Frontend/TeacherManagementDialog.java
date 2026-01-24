package Frontend;

import javax.swing.*;
import java.awt.*;
import Backend.src.database.*;
import Backend.src.major.major;
import Backend.src.course.Course;
import Backend.src.department.*;

// ============================================
// TEACHER MANAGEMENT DIALOG
// ============================================
class TeacherManagementDialog extends JDialog {
    private static final Color PRIMARY_COLOR = new Color(243, 156, 18);
    private static final Color SECONDARY_COLOR = new Color(230, 126, 34);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color LIGHT_BG = new Color(236, 240, 241);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    
    private TeacheinforManager teacherManager;
    
    public TeacherManagementDialog(JFrame parent, TeacheinforManager teacherManager) {
        super(parent, "Teacher Management", true);
        this.teacherManager = teacherManager;
        
        setSize(550, 750);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        initComponents();
        setVisible(true);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, 0, getHeight(), SECONDARY_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);
        
        // Header
        JLabel header = new JLabel("TEACHER MANAGEMENT", SwingConstants.CENTER);
        header.setBounds(50, 20, 450, 35);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(Color.WHITE);
        mainPanel.add(header);
        
        // Card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBounds(50, 80, 450, 600);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        
        int yPos = 30;
        int btnHeight = 50;
        int spacing = 12;
        
        // View All Teachers
        JButton viewAllBtn = createMenuButton("View All Teachers", yPos, SUCCESS_COLOR);
        viewAllBtn.addActionListener(e -> {
            TeacherInfoManager.displayAllTeachers();
            JOptionPane.showMessageDialog(this, 
                "Teachers displayed in console.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        cardPanel.add(viewAllBtn);
        yPos += btnHeight + spacing;
        
        // View by Department
        JButton viewDeptBtn = createMenuButton("View Teachers by Department", yPos, SUCCESS_COLOR);
        viewDeptBtn.addActionListener(e -> viewTeachersByDepartment());
        cardPanel.add(viewDeptBtn);
        yPos += btnHeight + spacing;
        
        // View by Course
        JButton viewCourseBtn = createMenuButton("View Teachers by Course", yPos, SUCCESS_COLOR);
        viewCourseBtn.addActionListener(e -> viewTeachersByCourse());
        cardPanel.add(viewCourseBtn);
        yPos += btnHeight + spacing;
        
        // Add Teacher
        JButton addBtn = createMenuButton("Add New Teacher", yPos, PRIMARY_COLOR);
        addBtn.addActionListener(e -> {
            DatabaseManager.displayTemporaryTeacherUsers();
            addNewTeacher();
        });
        cardPanel.add(addBtn);
        yPos += btnHeight + spacing;
        
        // Update Teacher
        JButton updateBtn = createMenuButton("Update Teacher Information", yPos, new Color(52, 152, 219));
        updateBtn.addActionListener(e -> {
            DatabaseManager.displayTemporaryTeacherUsers();
            updateTeacher();
        });
        cardPanel.add(updateBtn);
        yPos += btnHeight + spacing;
        
        // Delete Teacher
        JButton deleteBtn = createMenuButton("Delete Teacher", yPos, DANGER_COLOR);
        deleteBtn.addActionListener(e -> {
            DatabaseManager.displayTemporaryTeacherUsers();
            deleteTeacher();
        });
        cardPanel.add(deleteBtn);
        yPos += btnHeight + spacing;
        
        // Back Button
        JButton backBtn = createMenuButton("Back to Main Menu", yPos, LIGHT_BG);
        backBtn.setForeground(TEXT_COLOR);
        backBtn.addActionListener(e -> dispose());
        cardPanel.add(backBtn);
        
        mainPanel.add(cardPanel);
        add(mainPanel);
    }
    
    private JButton createMenuButton(String text, int yPos, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBounds(30, yPos, 390, 50);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void viewTeachersByDepartment() {
        String[] departments = {"GIC (General IT & Computing)", 
                                "GIM (General IT & Management)", 
                                "GEE (General Electrical & Engineering)"};
        
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select Department:",
            "View Teachers by Department",
            JOptionPane.QUESTION_MESSAGE,
            null,
            departments,
            departments[0]);
        
        if (selected != null) {
            String dept = selected.substring(0, 3);
            TeacherInfoManager.displayTeachersByDepartment(dept);
            JOptionPane.showMessageDialog(this, 
                "Teachers from " + dept + " displayed in console.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void viewTeachersByCourse() {
        JTextField courseField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.add(new JLabel("Available courses: AI00(1-4), AU10(1-4), CS00(1-4),"));
        panel.add(new JLabel("EE10(1-4), EL10(1-4), IE10(1-4), ME10(1-4), MF10(1-4), SE10(1-4)"));
        panel.add(courseField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Enter Course ID", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String course = courseField.getText().trim();
            if (!course.isEmpty()) {
                TeacherInfoManager.displayTeachersByCourse(course);
                JOptionPane.showMessageDialog(this, 
                    "Teachers for " + course + " displayed in console.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void addNewTeacher() {
        JTextField teacherIDField = new JTextField();
        
        while (true) {
            JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
            panel.add(new JLabel("Enter Teacher ID (must start with T2024, min 8 chars):"));
            panel.add(teacherIDField);
            panel.add(new JLabel("Check console for existing temporary teacher users"));
            
            int result = JOptionPane.showConfirmDialog(this, panel, 
                "Add New Teacher", JOptionPane.OK_CANCEL_OPTION);
            
            if (result != JOptionPane.OK_OPTION) {
                return;
            }
            
            String teacherID = teacherIDField.getText().trim();
            
            // Validation
            if (teacherID.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Teacher ID cannot be empty!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (teacherID.length() < 8) {
                JOptionPane.showMessageDialog(this, "Teacher ID must be at least 8 characters!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!teacherID.startsWith("T2024")) {
                JOptionPane.showMessageDialog(this, "Teacher ID must start with 'T2024'!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            
            // Check if exists
            if (TeacherInfoManager.teacherExists(teacherID)) {
                String[] teacherInfo = TeacherInfoManager.getTeacherInfo(teacherID);
                if (teacherInfo != null) {
                    String message = "Teacher already exists!\n" +
                                   "Department: " + teacherInfo[1] + "\n" +
                                   "Major: " + teacherInfo[2] + "\n\n" +
                                   "Assign additional courses?";
                    
                    int choice = JOptionPane.showConfirmDialog(this, message, 
                        "Teacher Exists", JOptionPane.YES_NO_OPTION);
                    
                    if (choice == JOptionPane.YES_OPTION) {
                        System.out.println("\n--- Current Courses ---");
                        TeacherCourseManager.getTeacherCourses(teacherID);
                        assignMultipleCoursesToTeacher(teacherID, teacherInfo[2]);
                    }
                }
                return;
            }
            
            // Select department and major
            String[] departments = {"GIC", "GIM", "GEE"};
            String department = (String) JOptionPane.showInputDialog(this,
                "Select Department:",
                "Department Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                departments,
                departments[0]);
            
            if (department == null) return;
            
            String selectedMajor = null;
            switch (department) {
                case "GIC":
                    selectedMajor = major.getGICMajor();
                    break;
                case "GIM":
                    selectedMajor = major.getGIMMajor();
                    break;
                case "GEE":
                    selectedMajor = major.getGEEMajor();
                    break;
            }
            
            if (selectedMajor == null) {
                JOptionPane.showMessageDialog(this, "Major selection cancelled.", 
                    "Cancelled", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Save teacher
            TeacherInfoManager.saveTeacherInfo(teacherID, department, selectedMajor);
            JOptionPane.showMessageDialog(this, 
                "Teacher added successfully!\nNow assign courses.", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Assign courses
            assignMultipleCoursesToTeacher(teacherID, selectedMajor);
            return;
        }
    }
    
    private void updateTeacher() {
        String teacherID = JOptionPane.showInputDialog(this, 
            "Enter Teacher ID to update:\n(Check console for existing teachers)", 
            "Update Teacher", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (teacherID == null || teacherID.trim().isEmpty()) return;
        
        teacherID = teacherID.trim();
        
        if (!TeacherInfoManager.teacherExists(teacherID)) {
            JOptionPane.showMessageDialog(this, 
                "Teacher with ID " + teacherID + " not found!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Select department
        String[] departments = {"GIC", "GIM", "GEE"};
        String department = (String) JOptionPane.showInputDialog(this,
            "Select New Department:",
            "Update Department",
            JOptionPane.QUESTION_MESSAGE,
            null,
            departments,
            departments[0]);
        
        if (department == null) return;
        
        String selectedMajor = null;
        switch (department) {
            case "GIC":
                selectedMajor = major.getGICMajor();
                break;
            case "GIM":
                selectedMajor = major.getGIMMajor();
                break;
            case "GEE":
                selectedMajor = major.getGEEMajor();
                break;
        }
        
        if (selectedMajor == null) {
            JOptionPane.showMessageDialog(this, "Major selection cancelled.", 
                "Cancelled", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Update database
        TeacherInfoManager.updateTeacherInfo(teacherID, department, selectedMajor);
        TeacherCourseManager.deleteTeacherAllCourses(teacherID);
        
        JOptionPane.showMessageDialog(this, 
            "Teacher updated! Now assign new courses.", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
        
        assignMultipleCoursesToTeacher(teacherID, selectedMajor);
    }
    
    private void deleteTeacher() {
        String teacherID = JOptionPane.showInputDialog(this, 
            "Enter Teacher ID to delete:\n(Check console for existing teachers)", 
            "Delete Teacher", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (teacherID == null || teacherID.trim().isEmpty()) return;
        
        teacherID = teacherID.trim();
        
        if (!TeacherInfoManager.teacherExists(teacherID)) {
            JOptionPane.showMessageDialog(this, 
                "Teacher with ID " + teacherID + " not found!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete teacher " + teacherID + "?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            TeacherInfoManager.deleteTeacher(teacherID);
            TeacherCourseManager.deleteTeacherAllCourses(teacherID);
            JOptionPane.showMessageDialog(this, 
                "Teacher deleted successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void assignMultipleCoursesToTeacher(String teacherID, String majorName) {
        boolean addMore = true;
        
        while (addMore) {
            String[] courses = Course.getCoursesForMajor(majorName);
            
            if (courses == null || courses.length == 0) {
                JOptionPane.showMessageDialog(this, 
                    "No courses found for this major.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String selectedCourse = (String) JOptionPane.showInputDialog(this,
                "Select Course for " + majorName + ":",
                "Course Assignment",
                JOptionPane.QUESTION_MESSAGE,
                null,
                courses,
                courses[0]);
            
            if (selectedCourse == null) {
                break;
            }
            
            // Get course ID
            String courseId = CourseManager.getCourseIdByName(selectedCourse);
            if (courseId == null) {
                JOptionPane.showMessageDialog(this, 
                    "Course ID not found for: " + selectedCourse, 
                    "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            
            // Assign course
            boolean courseAssigned = TeacherCourseManager.addTeacherCourse(teacherID, courseId);
            if (courseAssigned) {
                JOptionPane.showMessageDialog(this, 
                    "Course assigned: " + selectedCourse + " (" + courseId + ")", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            
            // Ask to add more
            int choice = JOptionPane.showConfirmDialog(this, 
                "Do you want to assign another course?", 
                "Add More Courses", 
                JOptionPane.YES_NO_OPTION);
            
            if (choice != JOptionPane.YES_OPTION) {
                addMore = false;
                JOptionPane.showMessageDialog(this, 
                    "Teacher courses assignment completed!", 
                    "Complete", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
