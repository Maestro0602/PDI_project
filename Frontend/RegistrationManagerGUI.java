package Frontend;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Backend.src.database.DatabaseManager;
import Backend.src.database.CheckEmail;
import Frontend.LoginApplicationGUI;;

public class RegistrationManagerGUI extends JFrame {
    
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel userTypeLabel;
    private JLabel generatedUsernameLabel;
    
    private Color primaryColor = new Color(52, 73, 94);
    private Color accentColor = new Color(46, 204, 113);
    private Color secondaryColor = new Color(52, 152, 219);
    private Color dangerColor = new Color(231, 76, 60);
    private Color warningColor = new Color(243, 156, 18);
    
    private String currentUserType = null;
    private String currentGeneratedUsername = null;
    
    public RegistrationManagerGUI() {
        initializeDatabases();
        initializeUI();
    }
    
    private void initializeDatabases() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DatabaseManager.DatabaseConnection.createDatabase();
                DatabaseManager.createUserTable();
                return null;
            }
        };
        worker.execute();
    }
    
    private void initializeUI() {
        setTitle("Registration System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 750);
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
        
        // Registration card
        JPanel registerCard = createRegisterCard();
        mainPanel.add(registerCard);
        
        add(mainPanel);
    }
    
    private JPanel createRegisterCard() {
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
        card.setBounds(50, 25, 450, 700);
        card.setOpaque(false);
        
        // Icon
        JLabel iconLabel = new JLabel("📝");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setBounds(0, 20, 450, 80);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(iconLabel);
        
        // Title
        JLabel titleLabel = new JLabel("REGISTRATION");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(primaryColor);
        titleLabel.setBounds(0, 100, 450, 40);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Create your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setBounds(0, 140, 450, 25);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(subtitleLabel);
        
        // Name label and field
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(primaryColor);
        nameLabel.setBounds(75, 185, 300, 25);
        card.add(nameLabel);
        
        nameField = createStyledTextField();
        nameField.setBounds(75, 210, 300, 40);
        card.add(nameField);
        
        // Email label and field
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        emailLabel.setForeground(primaryColor);
        emailLabel.setBounds(75, 260, 300, 25);
        card.add(emailLabel);
        
        emailField = createStyledTextField();
        emailField.setBounds(75, 285, 300, 40);
        emailField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                validateEmail();
            }
        });
        card.add(emailField);
        
        // User type display
        userTypeLabel = new JLabel("");
        userTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userTypeLabel.setBounds(75, 330, 300, 20);
        userTypeLabel.setVisible(false);
        card.add(userTypeLabel);
        
        // Generated username display
        generatedUsernameLabel = new JLabel("");
        generatedUsernameLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        generatedUsernameLabel.setForeground(new Color(127, 140, 141));
        generatedUsernameLabel.setBounds(75, 350, 300, 20);
        generatedUsernameLabel.setVisible(false);
        card.add(generatedUsernameLabel);
        
        // Password label and field
        JLabel passwordLabel = new JLabel("Password (min 8 characters)");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(primaryColor);
        passwordLabel.setBounds(75, 380, 300, 25);
        card.add(passwordLabel);
        
        passwordField = createStyledPasswordField();
        passwordField.setBounds(75, 405, 300, 40);
        card.add(passwordField);
        
        // Confirm password label and field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        confirmPasswordLabel.setForeground(primaryColor);
        confirmPasswordLabel.setBounds(75, 455, 300, 25);
        card.add(confirmPasswordLabel);
        
        confirmPasswordField = createStyledPasswordField();
        confirmPasswordField.setBounds(75, 480, 300, 40);
        card.add(confirmPasswordField);
        
        // Register button
        JButton registerBtn = createStyledButton("Register", accentColor, new Color(39, 174, 96));
        registerBtn.setBounds(75, 545, 300, 50);
        registerBtn.addActionListener(e -> handleRegistration());
        card.add(registerBtn);
        
        // Login link
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setBounds(75, 610, 300, 30);
        loginPanel.setOpaque(false);
        
        JLabel haveAccountLabel = new JLabel("Already have an account?");
        haveAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        haveAccountLabel.setForeground(new Color(127, 140, 141));
        
        JLabel loginLink = new JLabel("<html><u>Login</u></html>");
        loginLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginLink.setForeground(secondaryColor);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                handleLoginClick();
            }
            public void mouseEntered(MouseEvent e) {
                loginLink.setForeground(secondaryColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                loginLink.setForeground(secondaryColor);
            }
        });
        
        loginPanel.add(haveAccountLabel);
        loginPanel.add(loginLink);
        card.add(loginPanel);
        
        // Enter key listener
        ActionListener enterAction = e -> handleRegistration();
        confirmPasswordField.addActionListener(enterAction);
        
        return card;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(secondaryColor, 2),
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
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(secondaryColor, 2),
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
    
    private void validateEmail() {
        String email = emailField.getText().trim();
        String name = nameField.getText().trim();
        
        if (email.isEmpty()) {
            userTypeLabel.setVisible(false);
            generatedUsernameLabel.setVisible(false);
            currentUserType = null;
            currentGeneratedUsername = null;
            return;
        }
        
        String userType = getEmailType(email);
        
        if (userType == null) {
            userTypeLabel.setText("⚠ Email not found in system");
            userTypeLabel.setForeground(dangerColor);
            userTypeLabel.setVisible(true);
            generatedUsernameLabel.setVisible(false);
            currentUserType = null;
            currentGeneratedUsername = null;
        } else {
            userTypeLabel.setText("✓ Account Type: " + userType);
            userTypeLabel.setForeground(accentColor);
            userTypeLabel.setVisible(true);
            
            currentUserType = userType;
            
            if (!name.isEmpty()) {
                currentGeneratedUsername = generateUsername(userType, name);
                generatedUsernameLabel.setText("Username: " + currentGeneratedUsername);
                generatedUsernameLabel.setVisible(true);
            }
        }
    }
    
    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validate name
        if (name.isEmpty()) {
            showMessage("Name cannot be empty!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }
        
        // Validate email
        if (email.isEmpty()) {
            showMessage("Email cannot be empty!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return;
        }
        
        // Check if email exists in system
        String userType = getEmailType(email);
        if (userType == null) {
            int option = JOptionPane.showConfirmDialog(
                this,
                "Email not found in our system!\n" +
                "Your email must be registered as an Owner, Student, or Teacher.\n\n" +
                "Would you like to try with a different email?",
                "Email Not Found",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
            );
            
            if (option == JOptionPane.YES_OPTION) {
                emailField.setText("");
                emailField.requestFocus();
            }
            return;
        }
        
        // Validate password match
        if (!password.equals(confirmPassword)) {
            showMessage("Passwords don't match!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            confirmPasswordField.setText("");
            passwordField.requestFocus();
            return;
        }
        
        // Validate password length
        if (password.length() < 8) {
            showMessage("Password must be at least 8 characters long!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        // Generate username
        String generatedUsername = generateUsername(userType, name);
        
        // Register user in database
        boolean success = DatabaseManager.registerUser(generatedUsername, email, password);
        
        if (success) {
            String successMessage = String.format(
                "Registration successful!\n\n" +
                "Username: %s\n" +
                "Email: %s\n" +
                "Account Type: %s\n\n" +
                "You can now login with your credentials.",
                generatedUsername, email, userType
            );
            
            JOptionPane.showMessageDialog(
                this,
                successMessage,
                "Registration Successful",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Redirect to login
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                LoginApplicationGUI loginFrame = new LoginApplicationGUI();
                loginFrame.setVisible(true);
            });
        } else {
            int option = JOptionPane.showConfirmDialog(
                this,
                "Registration failed! Account already exists.\n\n" +
                "Would you like to try again with different details?",
                "Registration Failed",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
            );
            
            if (option == JOptionPane.YES_OPTION) {
                clearFields();
                nameField.requestFocus();
            } else {
                this.dispose();
            }
        }
    }
    
    private void handleLoginClick() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            LoginApplicationGUI loginFrame = new LoginApplicationGUI();
            loginFrame.setVisible(true);
        });
    }
    
    private String getEmailType(String email) {
        return CheckEmail.getEmailType(email);
    }
    
    private String generateUsername(String userType, String name) {
        String cleanName = name.replaceAll("\\s+", "").replaceAll("[^a-zA-Z0-9]", "");
        return userType + "_" + cleanName + "_2026";
    }
    
    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        userTypeLabel.setVisible(false);
        generatedUsernameLabel.setVisible(false);
        currentUserType = null;
        currentGeneratedUsername = null;
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
            RegistrationManagerGUI frame = new RegistrationManagerGUI();
            frame.setVisible(true);
        });
    }
    
    // For backward compatibility with console version
    public static void startRegistration(java.util.Scanner scanner) {
        SwingUtilities.invokeLater(() -> {
            RegistrationManagerGUI frame = new RegistrationManagerGUI();
            frame.setVisible(true);
        });
    }
    
    public static void handleRegistration(java.util.Scanner scanner) {
        startRegistration(scanner);
    }
}
