package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Backend.src.department.LogHeadTeacher;
import Backend.src.searching.SearchingTeacher;
import Backend.src.studentassign.studentassign;
import Backend.src.grading.Grading;
import Backend.src.report.GenerateStudentReport;
import Backend.src.attendance.AttendanceUI;
import Backend.main.Main;

public class MainPageTeacherGUI extends JFrame {
    
    private JPanel mainPanel;
    private Color primaryColor = new Color(41, 128, 185);
    private Color hoverColor = new Color(52, 152, 219);
    private Color backgroundColor = new Color(236, 240, 241);
    private Color cardColor = Color.WHITE;
    
    public MainPageTeacherGUI() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Teacher Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(41, 128, 185), 0, h, new Color(109, 213, 250));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);
        
        // Header
        JLabel headerLabel = new JLabel("Teacher Management Portal");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBounds(0, 30, 900, 50);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(headerLabel);
        
        JLabel subtitleLabel = new JLabel("Welcome! Select an option to proceed");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(236, 240, 241));
        subtitleLabel.setBounds(0, 80, 900, 30);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(subtitleLabel);
        
        // Create card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(3, 2, 25, 25));
        cardPanel.setBackground(new Color(0, 0, 0, 0));
        cardPanel.setBounds(80, 140, 740, 420);
        
        // Create menu buttons
        cardPanel.add(createMenuButton("Manage Students", "📚", 1));
        cardPanel.add(createMenuButton("Score & Grades", "📊", 2));
        cardPanel.add(createMenuButton("Attendance", "✓", 3));
        cardPanel.add(createMenuButton("Generate Reports", "📄", 4));
        cardPanel.add(createMenuButton("Department", "🏢", 5));
        cardPanel.add(createMenuButton("Search Students", "🔍", 6));
        
        mainPanel.add(cardPanel);
        
        // Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(750, 565, 120, 40);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> handleLogout());
        
        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(new Color(231, 76, 60));
            }
        });
        
        mainPanel.add(logoutBtn);
        
        add(mainPanel);
    }
    
    private JPanel createMenuButton(String title, String icon, int option) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setLayout(new BorderLayout());
        card.setBackground(cardColor);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setOpaque(false);
        
        // Icon label
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        
        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        card.add(iconLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        
        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(52, 152, 219));
                titleLabel.setForeground(Color.WHITE);
            }
            
            public void mouseExited(MouseEvent e) {
                card.setBackground(cardColor);
                titleLabel.setForeground(new Color(44, 62, 80));
            }
            
            public void mouseClicked(MouseEvent e) {
                handleMenuClick(option, title);
            }
        });
        
        return card;
    }
    
    private void handleMenuClick(int option, String title) {
        switch (option) {
            case 1:
                System.out.println("Managing Students information...");
                StudentAssignGUI.main(null);
                break;
            case 2:
                System.out.println("Managing grades and GPA...");
                this.dispose();
                GradingGUI.main(null);
                break;
            case 3:
                System.out.println("Managing Attendance...");
                this.dispose();
               AttendanceSwingUI.main(null);
                break;
            case 4:
                System.out.println("Generating Reports...");
                this.dispose();
                GenerateStudentReportGUI.main(null);
                break;
            case 5:
                System.out.println("Managing Department...");
                LogHeadTeacher.main(null);
                break;
            case 6:
                System.out.println("Searching Students...");
                this.dispose();
                StudentSearchGUI.main(null);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid option selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("Logging out...");
            this.dispose();
            SwingUtilities.invokeLater(() -> {
            MainGUI Mainframe = new MainGUI();
            Mainframe.setVisible(true);
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
            MainPageTeacherGUI frame = new MainPageTeacherGUI();
            frame.setVisible(true);
        });
    }
}
