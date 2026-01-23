package Frontend;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPageStudentGUI extends JFrame {
    
    private JPanel mainPanel;
    private Color primaryColor = new Color(52, 152, 219);
    private Color hoverColor = new Color(41, 128, 185);
    private Color backgroundColor = Color.WHITE;
    private Color cardColor = new Color(236, 240, 241);
    private Color accentColor = new Color(46, 204, 113);
    
    public MainPageStudentGUI() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Student Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with white background
        mainPanel = new JPanel();
        mainPanel.setBackground(backgroundColor);
        mainPanel.setLayout(null);
        
        // Header panel with gradient
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(52, 152, 219), getWidth(), 0, new Color(41, 128, 185));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(null);
        headerPanel.setBounds(0, 0, 900, 150);
        mainPanel.add(headerPanel);
        
        // Student icon
        JLabel iconLabel = new JLabel("🎓");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setBounds(0, 20, 900, 70);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(iconLabel);
        
        // Header title
        JLabel headerLabel = new JLabel("Student Portal");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBounds(0, 90, 900, 50);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(headerLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Welcome! Select an option to proceed");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(52, 73, 94));
        subtitleLabel.setBounds(0, 170, 900, 30);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(subtitleLabel);
        
        // Create card panel for menu options
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(1, 2, 30, 30));
        cardPanel.setBackground(backgroundColor);
        cardPanel.setBounds(150, 230, 600, 280);
        
        // Create menu buttons
        cardPanel.add(createMenuButton("Search Student", "🔍", 1));
        cardPanel.add(createMenuButton("Grades & GPA", "📊", 2));
        
        mainPanel.add(cardPanel);
        
        // Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(750, 560, 120, 45);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
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
                
                // Draw shadow
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 25, 25);
                
                // Draw card
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 10, getHeight() - 10, 25, 25);
                
                // Draw border
                g2.setColor(new Color(189, 195, 199));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 10, getHeight() - 10, 25, 25);
            }
        };
        card.setLayout(new BorderLayout());
        card.setBackground(cardColor);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setOpaque(false);
        
        // Icon label
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 70));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        
        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 35, 0));
        
        card.add(iconLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        
        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            private Color originalBg = cardColor;
            
            public void mouseEntered(MouseEvent e) {
                card.setBackground(primaryColor);
                titleLabel.setForeground(Color.WHITE);
                card.repaint();
            }
            
            public void mouseExited(MouseEvent e) {
                card.setBackground(originalBg);
                titleLabel.setForeground(new Color(44, 62, 80));
                card.repaint();
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
                System.out.println("Searching Students information...");
                this.dispose(); // Close the main menu
                SwingUtilities.invokeLater(() -> {
                TeacherSearchGUI searchGUIFrame = new TeacherSearchGUI();
                    searchGUIFrame.setVisible(true);
                });
                break;
            case 2:
                System.out.println("See Grades and GPA...");
                this.dispose(); // Close the main menu
                SwingUtilities.invokeLater(() -> {
                    GenerateStudentReportGUI ReportFrame = new GenerateStudentReportGUI();
                    ReportFrame.setVisible(true);
                });
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
            MainPageStudentGUI frame = new MainPageStudentGUI();
            frame.setVisible(true);
        });
    }
}