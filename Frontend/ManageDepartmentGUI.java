package Frontend;

import javax.swing.*;
import java.awt.*;
import Backend.src.database.*;
import Backend.src.department.*;
import Frontend.student.*;
import Frontend.teacher.*;
public class ManageDepartmentGUI extends JFrame {
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color LIGHT_BG = new Color(236, 240, 241);
    private static final Color CARD_BG = Color.WHITE;
    
    private StudentManager studentManager;
    private TeacheinforManager teacherManager;
    
    public ManageDepartmentGUI() {
        // Initialize database tables
        StudentInfoManager.createStudentInfoTable();
        MajorManager.createDepartmentMajorTable();
        CourseManager.createCourseTable();
        TeacherCourseManager.createTeacherCourseTable();
        TeacherInfoManager.createTeacherInfoTable();
        
        studentManager = new StudentManager();
        teacherManager = new TeacheinforManager();
        
        setTitle("Department Management System");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
        setVisible(true);
    }
    
    private void initComponents() {
        // Main panel with gradient background
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
        JLabel headerLabel = new JLabel("DEPARTMENT MANAGEMENT SYSTEM", SwingConstants.CENTER);
        headerLabel.setBounds(50, 30, 500, 40);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        mainPanel.add(headerLabel);
        
        // Menu panel (white card)
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(null);
        menuPanel.setBounds(75, 100, 450, 520);
        menuPanel.setBackground(CARD_BG);
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Title inside card
        JLabel menuTitle = new JLabel("Main Menu", SwingConstants.CENTER);
        menuTitle.setBounds(30, 20, 390, 30);
        menuTitle.setFont(new Font("Arial", Font.BOLD, 18));
        menuTitle.setForeground(TEXT_COLOR);
        menuPanel.add(menuTitle);
        
        // Student Management Button
        JButton studentBtn = createStyledButton("STUDENT MANAGEMENT", SUCCESS_COLOR, 30, 80, 390, 60);
        studentBtn.addActionListener(e -> openStudentManagement());
        menuPanel.add(studentBtn);
        
        // Teacher Management Button
        JButton teacherBtn = createStyledButton("TEACHER MANAGEMENT", WARNING_COLOR, 30, 160, 390, 60);
        teacherBtn.addActionListener(e -> openTeacherManagement());
        menuPanel.add(teacherBtn);
        
        // Exit Button
        JButton exitBtn = createStyledButton("EXIT", DANGER_COLOR, 30, 240, 390, 60);
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to exit?", 
                "Confirm Exit", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                MainPageTeacherGUI mainPage = new MainPageTeacherGUI();
                mainPage.setVisible(true);
            }
        });
        menuPanel.add(exitBtn);
        
        // Footer info
        JLabel footerLabel = new JLabel("Select an option to continue", SwingConstants.CENTER);
        footerLabel.setBounds(30, 420, 390, 30);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(new Color(127, 140, 141));
        menuPanel.add(footerLabel);
        
        mainPanel.add(menuPanel);
        add(mainPanel);
    }
    
    private JButton createStyledButton(String text, Color bgColor, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void openStudentManagement() {
        this.setVisible(false);
        new StudentManagerGUI(this);
    }
    
    private void openTeacherManagement() {
        this.setVisible(false);
        new TeacherManagementDialog(this, teacherManager);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new ManageDepartmentGUI());
    }
}