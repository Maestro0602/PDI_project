package Frontend;

import Backend.src.login.LoginApplication;
import Backend.src.register.RegistrationManager;
import Backend.src.database.DatabaseManager;
import Backend.src.database.EmailManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainGUI extends JFrame {
    
    private JPanel mainPanel;
    private Color primaryColor = new Color(52, 73, 94);
    private Color accentColor = new Color(46, 204, 113);
    private Color secondaryColor = new Color(52, 152, 219);
    private Color dangerColor = new Color(231, 76, 60);
    
    public MainGUI() {
        initializeDatabases();
        initializeUI();
    }
    
    private void initializeDatabases() {
        // Initialize databases in background
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DatabaseManager.DatabaseConnection.createDatabase();
                DatabaseManager.createUserTable();
                EmailManager.AccountDatabaseConnection.initializeEmailTables();
                return null;
            }
        };
        worker.execute();
    }
    
    private void initializeUI() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with custom paint
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(34, 49, 63), w, h, new Color(44, 62, 80));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                
                // Draw decorative circles
                g2d.setColor(new Color(52, 152, 219, 30));
                g2d.fillOval(-100, -100, 400, 400);
                g2d.fillOval(w - 300, h - 300, 400, 400);
            }
        };
        mainPanel.setLayout(null);
        
        // Create central card panel
        JPanel cardPanel = createCardPanel();
        mainPanel.add(cardPanel);
        
        add(mainPanel);
    }
    
    private JPanel createCardPanel() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded rectangle with shadow
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 30, 30);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 10, getHeight() - 10, 30, 30);
            }
        };
        card.setLayout(null);
        card.setBounds(250, 100, 500, 500);
        card.setOpaque(false);
        
        // Logo/Icon area
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw circle background
                GradientPaint gp = new GradientPaint(0, 0, new Color(52, 152, 219), 
                                                     getWidth(), getHeight(), new Color(41, 128, 185));
                g2.setPaint(gp);
                g2.fillOval(10, 10, 80, 80);
                
                // Draw icon
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(35, 40, 45, 50);
                g2.drawLine(45, 50, 65, 30);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 40));
                g2.drawString("🎓", 25, 65);
            }
        };
        logoPanel.setBounds(200, 30, 100, 100);
        logoPanel.setOpaque(false);
        card.add(logoPanel);
        
        // Title
        JLabel titleLabel = new JLabel("Welcome");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(primaryColor);
        titleLabel.setBounds(0, 140, 500, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Student Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setBounds(0, 190, 500, 30);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(subtitleLabel);
        
        // Divider line
        JSeparator separator = new JSeparator();
        separator.setBounds(100, 235, 300, 2);
        separator.setForeground(new Color(189, 195, 199));
        card.add(separator);
        
        // Login Button
        JButton loginBtn = createStyledButton("Login", accentColor, new Color(39, 174, 96), 1);
        loginBtn.setBounds(100, 270, 300, 55);
        card.add(loginBtn);
        
        // Register Button
        JButton registerBtn = createStyledButton("Register", secondaryColor, new Color(41, 128, 185), 2);
        registerBtn.setBounds(100, 340, 300, 55);
        card.add(registerBtn);
        
        // Exit Button
        JButton exitBtn = createStyledButton("Exit", dangerColor, new Color(192, 57, 43), 3);
        exitBtn.setBounds(100, 410, 300, 55);
        card.add(exitBtn);
        
        return card;
    }
    
    private JButton createStyledButton(String text, Color normalColor, Color hoverColor, int action) {
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
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Draw text
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action listener
        button.addActionListener(e -> handleAction(action));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });
        
        return button;
    }
    
    private void handleAction(int action) {
        switch (action) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleRegister();
                break;
            case 3:
                handleExit();
                break;
        }
    }
    
    private void handleLogin() {
        this.dispose(); // Close the main menu
        SwingUtilities.invokeLater(() -> {
            LoginApplicationGUI loginFrame = new LoginApplicationGUI();
            loginFrame.setVisible(true);
        });
    }

    private void handleRegister() {
        this.dispose(); // Close the main menu
        SwingUtilities.invokeLater(() -> {
            RegistrationManagerGUI registerFrame = new RegistrationManagerGUI();
            registerFrame.setVisible(true);
        });
    }
    
    private void handleExit() {
        // Create custom dialog
        JDialog dialog = new JDialog(this, "Confirm Exit", true);
        dialog.setLayout(null);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(Color.WHITE);
        
        JLabel messageLabel = new JLabel("Are you sure you want to exit?");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(primaryColor);
        messageLabel.setBounds(0, 40, 400, 30);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialog.add(messageLabel);
        
        JButton yesBtn = createStyledButton("Yes, Exit", dangerColor, new Color(192, 57, 43), -1);
        yesBtn.setBounds(50, 100, 130, 45);
        yesBtn.addActionListener(e -> {
            System.out.println("\nThank you for using the system. Goodbye!");
            System.exit(0);
        });
        dialog.add(yesBtn);
        
        JButton noBtn = createStyledButton("Cancel", new Color(149, 165, 166), new Color(127, 140, 141), -1);
        noBtn.setBounds(220, 100, 130, 45);
        noBtn.addActionListener(e -> dialog.dispose());
        dialog.add(noBtn);
        
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Launch GUI
        SwingUtilities.invokeLater(() -> {
            MainGUI frame = new MainGUI();
            frame.setVisible(true);
        });
    }
}
