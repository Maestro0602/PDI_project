package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Backend.src.database.DatabaseManager;
import Backend.src.database.CheckEmail;
import Frontend.LoginApplicationGUI;
import javax.swing.border.EmptyBorder;

public class RegistrationManagerGUI extends JFrame {
    
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel userTypeLabel;
    private JLabel generatedUsernameLabel;
    
    // Colors matching LoginApplicationGUI
    private static final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private static final Color SECONDARY_BLUE = new Color(59, 130, 246);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    
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
        setSize(520, 780);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main background panel with gradient (matching login)
        JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(241, 245, 249),
                        getWidth(), getHeight(), new Color(226, 232, 240)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(147, 197, 253, 30));
                g2d.fillOval(-50, -50, 200, 200);
                g2d.fillOval(getWidth() - 150, getHeight() - 150, 200, 200);
            }
        };
        
        // Card panel
        JPanel cardPanel = createRegisterCard();
        backgroundPanel.add(cardPanel);
        
        setContentPane(backgroundPanel);
    }
    
    private JPanel createRegisterCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 24, 24);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 50, 40, 50));
        card.setPreferredSize(new Dimension(420, 680));
        card.setOpaque(false);
        
        // Animated icon
        JPanel iconPanel = createAnimatedIcon();
        JPanel iconWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconWrapper.setOpaque(false);
        iconWrapper.setBorder(new EmptyBorder(10, 0, 20, 0));
        iconWrapper.add(iconPanel);
        
        // Title
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitle = new JLabel("Register to get started");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Input fields
        nameField = createStyledTextField();
        emailField = createStyledTextField();
        passwordField = createStyledPasswordField();
        confirmPasswordField = createStyledPasswordField();
        
        // Email validation listener
        emailField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                validateEmail();
            }
        });
        
        JPanel namePanel = createInputGroup("Full Name", nameField);
        JPanel emailPanel = createInputGroup("Email Address", emailField);
        
        // User type and username labels
        userTypeLabel = new JLabel("");
        userTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        userTypeLabel.setVisible(false);
        
        generatedUsernameLabel = new JLabel("");
        generatedUsernameLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        generatedUsernameLabel.setForeground(TEXT_SECONDARY);
        generatedUsernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        generatedUsernameLabel.setVisible(false);
        
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setOpaque(false);
        statusPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        statusPanel.add(userTypeLabel);
        statusPanel.add(generatedUsernameLabel);
        
        JPanel passwordPanel = createInputGroup("Password (min 8 characters)", passwordField);
        JPanel confirmPasswordPanel = createInputGroup("Confirm Password", confirmPasswordField);
        
        // Register button
        JButton registerButton = createStyledButton("REGISTER");
        registerButton.addActionListener(e -> handleRegistration());
        
        // Login link panel
        JPanel loginLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginLinkPanel.setOpaque(false);
        
        JLabel loginText = new JLabel("Already have an account?");
        loginText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loginText.setForeground(TEXT_SECONDARY);
        
        JButton loginLink = new JButton("Login here");
        loginLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginLink.setForeground(PRIMARY_BLUE);
        loginLink.setContentAreaFilled(false);
        loginLink.setBorderPainted(false);
        loginLink.setFocusPainted(false);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addActionListener(e -> handleLoginClick());
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginLink.setForeground(SECONDARY_BLUE);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginLink.setForeground(PRIMARY_BLUE);
            }
        });
        
        loginLinkPanel.add(loginText);
        loginLinkPanel.add(loginLink);
        
        // Footer
        JLabel footerLabel = new JLabel("Secure Registration • Powered ITC");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(TEXT_SECONDARY);
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components
        card.add(iconWrapper);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(namePanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(emailPanel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(statusPanel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(passwordPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(confirmPasswordPanel);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(registerButton);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(loginLinkPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(footerLabel);
        
        // Enter key listener
        ActionListener enterAction = e -> handleRegistration();
        confirmPasswordField.addActionListener(enterAction);
        
        return card;
    }
    
    // Animated icon (registration themed)
    private JPanel createAnimatedIcon() {
        JPanel iconPanel = new JPanel() {
            private float rotation = 0;
            private Timer timer;

            {
                timer = new Timer(50, e -> {
                    rotation += 0.02f;
                    repaint();
                });
                timer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                GradientPaint gp = new GradientPaint(
                        (float)(centerX + Math.cos(rotation) * 30),
                        (float)(centerY + Math.sin(rotation) * 30),
                        TEXT_PRIMARY,
                        (float)(centerX - Math.cos(rotation) * 30),
                        (float)(centerY - Math.sin(rotation) * 30),
                        SECONDARY_BLUE
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);

                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.fillOval(15, 15, 70, 70);

                // Draw user icon with pen
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                // Head
                g2d.fillOval(42, 30, 16, 16);
                
                // Body
                int[] xPoints = {50, 38, 62};
                int[] yPoints = {46, 65, 65};
                g2d.fillPolygon(xPoints, yPoints, 3);
                
                // Pen
                g2d.setColor(new Color(34, 197, 94));
                g2d.fillRect(62, 58, 3, 10);
                g2d.fillPolygon(new int[]{62, 65, 63}, new int[]{68, 68, 72}, 3);
            }
        };

        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(100, 100));
        iconPanel.setMaximumSize(new Dimension(100, 100));
        return iconPanel;
    }
    
    // Input group
    private JPanel createInputGroup(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(field);

        return panel;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(12, 16, 12, 16)
        ));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_BLUE, 2, true),
                        new EmptyBorder(12, 16, 12, 16)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                        new EmptyBorder(12, 16, 12, 16)
                ));
            }
        });

        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(12, 16, 12, 16)
        ));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_BLUE, 2, true),
                        new EmptyBorder(12, 16, 12, 16)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                        new EmptyBorder(12, 16, 12, 16)
                ));
            }
        });

        return field;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(PRIMARY_BLUE);
                } else if (getModel().isRollover()) {
                    GradientPaint gp = new GradientPaint(0, 0, SECONDARY_BLUE, 0, getHeight(), PRIMARY_BLUE);
                    g2d.setPaint(gp);
                } else {
                    GradientPaint gp = new GradientPaint(0, 0, PRIMARY_BLUE, 0, getHeight(), SECONDARY_BLUE);
                    g2d.setPaint(gp);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { button.repaint(); }
        });

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
            userTypeLabel.setForeground(DANGER_COLOR);
            userTypeLabel.setVisible(true);
            generatedUsernameLabel.setVisible(false);
            currentUserType = null;
            currentGeneratedUsername = null;
        } else {
            userTypeLabel.setText("✓ Account Type: " + userType);
            userTypeLabel.setForeground(SUCCESS_COLOR);
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