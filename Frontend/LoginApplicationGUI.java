package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Backend.src.database.DatabaseManager;
import Backend.src.database.MajorManager;
import Backend.src.register.RegistrationManager;
import Backend.main.*;

public class LoginApplicationGUI extends JFrame {
    
    public static String loggedInUser;
    
    private JTextField nameField;
    private JPasswordField passwordField;
    private Color primaryColor = new Color(41, 128, 185);
    private Color accentColor = new Color(46, 204, 113);
    private Color dangerColor = new Color(231, 76, 60);
    private Color warningColor = new Color(243, 156, 18);
    
    public LoginApplicationGUI() {
        initializeDatabases();
        initializeUI();
    }
    
    private void initializeDatabases() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DatabaseManager.DatabaseConnection.createDatabase();
                DatabaseManager.createUserTable();
                MajorManager.createDepartmentMajorTable();
                return null;
            }
        };
        worker.execute();
    }
    
    private void initializeUI() {
        setTitle("Login System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(52, 73, 94), 0, getHeight(), new Color(44, 62, 80));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);
        
        // Login card
        JPanel loginCard = createLoginCard();
        mainPanel.add(loginCard);
        
        add(mainPanel);
    }
    
    private JPanel createLoginCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 25, 25);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 10, getHeight() - 10, 25, 25);
            }
        };
        card.setLayout(null);
        card.setBounds(50, 50, 400, 550);
        card.setOpaque(false);
        
        // Icon
        JLabel iconLabel = new JLabel("🔐");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setBounds(0, 30, 400, 80);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(iconLabel);
        
        // Title
        JLabel titleLabel = new JLabel("LOGIN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBounds(0, 120, 400, 40);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Welcome back! Please login to continue");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setBounds(0, 160, 400, 25);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(subtitleLabel);
        
        // Name label and field
        JLabel nameLabel = new JLabel("Username");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(52, 73, 94));
        nameLabel.setBounds(50, 210, 300, 25);
        card.add(nameLabel);
        
        nameField = createStyledTextField();
        nameField.setBounds(50, 240, 300, 45);
        card.add(nameField);
        
        // Password label and field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(52, 73, 94));
        passwordLabel.setBounds(50, 300, 300, 25);
        card.add(passwordLabel);
        
        passwordField = createStyledPasswordField();
        passwordField.setBounds(50, 330, 300, 45);
        card.add(passwordField);
        
        // Forgot password link
        JLabel forgotLabel = new JLabel("<html><u>Forgot Password?</u></html>");
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotLabel.setForeground(primaryColor);
        forgotLabel.setBounds(50, 380, 300, 20);
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                handleForgotPasswordClick();
            }
            public void mouseEntered(MouseEvent e) {
                forgotLabel.setForeground(primaryColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                forgotLabel.setForeground(primaryColor);
            }
        });
        card.add(forgotLabel);
        
        // Login button
        JButton loginBtn = createStyledButton("Login", accentColor, new Color(39, 174, 96));
        loginBtn.setBounds(50, 420, 300, 50);
        loginBtn.addActionListener(e -> handleLogin());
        card.add(loginBtn);
        
        // Register link
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerPanel.setBounds(50, 485, 300, 30);
        registerPanel.setOpaque(false);
        
        JLabel noAccountLabel = new JLabel("Don't have an account?");
        noAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noAccountLabel.setForeground(new Color(127, 140, 141));
        
        JLabel registerLink = new JLabel("<html><u>Register</u></html>");
        registerLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerLink.setForeground(primaryColor);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                handleRegisterClick();
            }
            public void mouseEntered(MouseEvent e) {
                registerLink.setForeground(primaryColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                registerLink.setForeground(primaryColor);
            }
        });
        
        registerPanel.add(noAccountLabel);
        registerPanel.add(registerLink);
        card.add(registerPanel);
        
        // Enter key listener
        ActionListener enterAction = e -> handleLogin();
        nameField.addActionListener(enterAction);
        passwordField.addActionListener(enterAction);
        
        return card;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(primaryColor, 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });
        
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(primaryColor, 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
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
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void handleLogin() {
        String name = nameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (name.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!DatabaseManager.checkUserExists(name)) {
            int option = JOptionPane.showConfirmDialog(
                this,
                "User not found!\nYou need to register first.\n\nWould you like to register now?",
                "User Not Found",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (option == JOptionPane.YES_OPTION) {
                handleRegisterClick();
            }
            return;
        }
        
        loggedInUser = DatabaseManager.verifyLogin(name, password);
        
        if (loggedInUser != null) {
            showMessage("Welcome, " + loggedInUser + "!\nYou are now logged in.", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            redirectToUserPage();
        } else {
            handleLoginFailure(name);
        }
    }
    
    private void handleLoginFailure(String username) {
        String[] options = {"Try Again", "Forgot Password", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "Login failed! Invalid password.\n\nWhat would you like to do?",
            "Login Failed",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null,
            options,
            options[0]
        );
        
        switch (choice) {
            case 0: // Try Again
                passwordField.setText("");
                passwordField.requestFocus();
                break;
            case 1: // Forgot Password
                handleForgotPassword(username);
                break;
            case 2: // Cancel
            default:
                passwordField.setText("");
                break;
        }
    }
    
    private void handleForgotPasswordClick() {
        String name = nameField.getText().trim();
        
        if (name.isEmpty()) {
            showMessage("Please enter your username first", "Username Required", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }
        
        if (!DatabaseManager.checkUserExists(name)) {
            showMessage("User not found! Please check your username.", "User Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        handleForgotPassword(name);
    }
    
    private void handleForgotPassword(String username) {
        JDialog resetDialog = new JDialog(this, "Password Reset", true);
        resetDialog.setSize(450, 350);
        resetDialog.setLocationRelativeTo(this);
        resetDialog.setResizable(false);
        resetDialog.getContentPane().setBackground(Color.WHITE);
        resetDialog.setLayout(null);
        
        JLabel titleLabel = new JLabel("Reset Password");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBounds(0, 20, 450, 35);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resetDialog.add(titleLabel);
        
        JLabel userLabel = new JLabel("For user: " + username);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(127, 140, 141));
        userLabel.setBounds(0, 55, 450, 25);
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resetDialog.add(userLabel);
        
        JLabel newPassLabel = new JLabel("New Password (min 8 characters)");
        newPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        newPassLabel.setForeground(new Color(52, 73, 94));
        newPassLabel.setBounds(75, 100, 300, 25);
        resetDialog.add(newPassLabel);
        
        JPasswordField newPassField = createStyledPasswordField();
        newPassField.setBounds(75, 130, 300, 40);
        resetDialog.add(newPassField);
        
        JLabel confirmPassLabel = new JLabel("Confirm Password");
        confirmPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        confirmPassLabel.setForeground(new Color(52, 73, 94));
        confirmPassLabel.setBounds(75, 180, 300, 25);
        resetDialog.add(confirmPassLabel);
        
        JPasswordField confirmPassField = createStyledPasswordField();
        confirmPassField.setBounds(75, 210, 300, 40);
        resetDialog.add(confirmPassField);
        
        JButton resetBtn = createStyledButton("Reset Password", accentColor, new Color(39, 174, 96));
        resetBtn.setBounds(75, 270, 140, 40);
        resetBtn.addActionListener(e -> {
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());
            
            if (newPass.length() < 8) {
                showMessage("Password must be at least 8 characters long!", "Invalid Password", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!newPass.equals(confirmPass)) {
                showMessage("Passwords don't match!", "Password Mismatch", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = DatabaseManager.resetPassword(username, newPass);
            
            if (success) {
                showMessage("Password reset successful!\nYou can now login with your new password.", "Success", JOptionPane.INFORMATION_MESSAGE);
                resetDialog.dispose();
                passwordField.setText("");
                passwordField.requestFocus();
            } else {
                showMessage("Password reset failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        resetDialog.add(resetBtn);
        
        JButton cancelBtn = createStyledButton("Cancel", new Color(149, 165, 166), new Color(127, 140, 141));
        cancelBtn.setBounds(235, 270, 140, 40);
        cancelBtn.addActionListener(e -> resetDialog.dispose());
        resetDialog.add(cancelBtn);
        
        resetDialog.setVisible(true);
    }
    
    private void handleRegisterClick() {
        this.dispose(); // Close the main menu
        SwingUtilities.invokeLater(() -> {
        RegistrationManagerGUI registerFrame = new RegistrationManagerGUI();
        registerFrame.setVisible(true);
    });
    }
    
    private void redirectToUserPage() {
        if (loggedInUser.startsWith("TEACHER")) {
            System.out.println("\nRedirecting to Teacher Page...");
            SwingUtilities.invokeLater(() -> {
            MainPageTeacherGUI teacherFrame = new MainPageTeacherGUI();
            teacherFrame.setVisible(true);
        });
        } else if (loggedInUser.startsWith("STUDENT")) {
            System.out.println("\nRedirecting to Student Page...");
           SwingUtilities.invokeLater(() -> {
            MainPageStudentGUI studentFrame = new MainPageStudentGUI();
            studentFrame.setVisible(true);
        });
        } else if (loggedInUser.startsWith("OWNER")) {
            System.out.println("\nRedirecting to Owner Page...");
            SwingUtilities.invokeLater(() -> {
            MainPageOwnerGUI ownerFrame = new MainPageOwnerGUI();
            ownerFrame.setVisible(true);
        });
        }
    }
    
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginApplicationGUI frame = new LoginApplicationGUI();
            frame.setVisible(true);
        });
    }
    
    // For backward compatibility with console version
    public static void startLogin(java.util.Scanner scanner) {
        SwingUtilities.invokeLater(() -> {
            LoginApplicationGUI frame = new LoginApplicationGUI();
            frame.setVisible(true);
        });
    }
}