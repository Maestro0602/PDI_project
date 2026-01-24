package Frontend;

import javax.swing.*;
import java.awt.*;
import Backend.src.database.HeadTeacherpw;

public class LogHeadTeacherGUI extends JFrame {
    private JTextField idField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color LIGHT_BG = new Color(236, 240, 241);
    
    public LogHeadTeacherGUI() {
        setTitle("Head Teacher Login");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, 0, h, SECONDARY_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);
        
        // Header label
        JLabel headerLabel = new JLabel("HEAD TEACHER LOGIN", SwingConstants.CENTER);
        headerLabel.setBounds(50, 30, 350, 40);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        mainPanel.add(headerLabel);
        
        // Login panel (white card)
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBounds(50, 100, 350, 350);
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // ID Label
        JLabel idLabel = new JLabel("Head Teacher ID");
        idLabel.setBounds(30, 30, 290, 25);
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        idLabel.setForeground(TEXT_COLOR);
        loginPanel.add(idLabel);
        
        // ID Field
        idField = new JTextField();
        idField.setBounds(30, 60, 290, 40);
        idField.setFont(new Font("Arial", Font.PLAIN, 14));
        idField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        loginPanel.add(idField);
        
        // Password Label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(30, 120, 290, 25);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        loginPanel.add(passwordLabel);
        
        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(30, 150, 290, 40);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        loginPanel.add(passwordField);
        
        // Message Label (for success/error messages)
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setBounds(30, 200, 290, 25);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 12));
        loginPanel.add(messageLabel);
        
        // Login Button
        JButton loginButton = new JButton("LOGIN");
        loginButton.setBounds(30, 240, 290, 45);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> performLogin());
        loginPanel.add(loginButton);
        
        // Exit Button
        JButton exitButton = new JButton("EXIT");
        exitButton.setBounds(30, 295, 290, 45);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setForeground(TEXT_COLOR);
        exitButton.setBackground(LIGHT_BG);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> {
            dispose();
            MainPageTeacherGUI.main(null);
        });
        loginPanel.add(exitButton);
        
        mainPanel.add(loginPanel);
        
        // Add Enter key listener for password field
        passwordField.addActionListener(e -> performLogin());
        
        add(mainPanel);
        setVisible(true);
    }
    
    private void performLogin() {
        String headteacherID = idField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (headteacherID.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields!", ERROR_COLOR);
            return;
        }
        
        // Verify credentials
        if (HeadTeacherpw.verifyHeadTeacherPassword(headteacherID, password)) {
            showMessage("Login successful! Welcome " + headteacherID, SUCCESS_COLOR);
            
            // Delay before opening next window
            Timer timer = new Timer(1500, e -> {
                dispose();
                ManageDepartmentGUI.main(null);
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            showMessage("Invalid ID or Password!", ERROR_COLOR);
            passwordField.setText("");
        }
    }
    
    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
        
        // Fade out message after 3 seconds
        Timer timer = new Timer(3000, e -> messageLabel.setText(""));
        timer.setRepeats(false);
        timer.start();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new LogHeadTeacherGUI());
    }
}
