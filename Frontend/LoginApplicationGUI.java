package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import database.DatabaseManager;
import database.MajorManager;
import register.RegistrationManager;
import Backend.main.*;
import javax.swing.border.EmptyBorder;

public class LoginApplicationGUI extends JFrame {
    
    public static String loggedInUser;
    
    private JTextField nameField;
    private JPasswordField passwordField;
    
    // Colors matching the second design
    private static final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private static final Color SECONDARY_BLUE = new Color(59, 130, 246);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    
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
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main background panel with gradient
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
        JPanel cardPanel = createLoginCard();
        backgroundPanel.add(cardPanel);
        
        setContentPane(backgroundPanel);
    }
    
    private JPanel createLoginCard() {
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
        card.setBorder(new EmptyBorder(40, 50, 40, 50));
        card.setPreferredSize(new Dimension(420, 540));
        card.setOpaque(false);
        
        // Animated icon
        JPanel iconPanel = createAnimatedIcon();
        JPanel iconWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconWrapper.setOpaque(false);
        iconWrapper.setBorder(new EmptyBorder(0, 0, 20, 0));
        iconWrapper.add(iconPanel);
        
        // Title
        JLabel title = new JLabel("Student Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Input fields
        nameField = createStyledTextField();
        passwordField = createStyledPasswordField();
        
        JPanel usernamePanel = createInputGroup("Username", nameField);
        JPanel passwordPanel = createInputGroup("Password", passwordField);
        
        // Login button
        JButton loginButton = createStyledButton();
        loginButton.addActionListener(e -> handleLogin());
        
        // Footer
        JLabel footerLabel = new JLabel("Secure Login • Powered ITC");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(TEXT_SECONDARY);
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Register link panel
        JPanel registerLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerLinkPanel.setOpaque(false);
        
        JLabel registerText = new JLabel("Don't have an account?");
        registerText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        registerText.setForeground(TEXT_SECONDARY);
        
        JButton registerLink = new JButton("Register here");
        registerLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerLink.setForeground(PRIMARY_BLUE);
        registerLink.setContentAreaFilled(false);
        registerLink.setBorderPainted(false);
        registerLink.setFocusPainted(false);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addActionListener(e -> handleRegisterClick());
        registerLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerLink.setForeground(SECONDARY_BLUE);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerLink.setForeground(PRIMARY_BLUE);
            }
        });
        
        registerLinkPanel.add(registerText);
        registerLinkPanel.add(registerLink);
        
        // Add components
        card.add(iconWrapper);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(usernamePanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(passwordPanel);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(loginButton);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(registerLinkPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(footerLabel);
        
        // Enter key listener
        ActionListener enterAction = e -> handleLogin();
        nameField.addActionListener(enterAction);
        passwordField.addActionListener(enterAction);
        
        return card;
    }
    
    // Animated icon
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

                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int[] xPoints = {30, 50, 70, 50};
                int[] yPoints = {45, 35, 45, 55};
                g2d.fillPolygon(xPoints, yPoints, 4);
                g2d.drawLine(50, 55, 50, 65);
                g2d.fillOval(47, 63, 6, 6);
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
    
    private JButton createStyledButton() {
        JButton button = new JButton("LOGIN") {
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
    
    private void handleForgotPassword(String username) {
        JDialog resetDialog = new JDialog(this, "Password Reset", true);
        resetDialog.setSize(450, 350);
        resetDialog.setLocationRelativeTo(this);
        resetDialog.setResizable(false);
        resetDialog.getContentPane().setBackground(Color.WHITE);
        resetDialog.setLayout(null);
        
        JLabel titleLabel = new JLabel("Reset Password");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBounds(0, 20, 450, 35);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resetDialog.add(titleLabel);
        
        JLabel userLabel = new JLabel("For user: " + username);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(TEXT_SECONDARY);
        userLabel.setBounds(0, 55, 450, 25);
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resetDialog.add(userLabel);
        
        JLabel newPassLabel = new JLabel("New Password (min 8 characters)");
        newPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        newPassLabel.setForeground(TEXT_PRIMARY);
        newPassLabel.setBounds(75, 100, 300, 25);
        resetDialog.add(newPassLabel);
        
        JPasswordField newPassField = createStyledPasswordField();
        newPassField.setMaximumSize(null);
        newPassField.setBounds(75, 130, 300, 40);
        resetDialog.add(newPassField);
        
        JLabel confirmPassLabel = new JLabel("Confirm Password");
        confirmPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        confirmPassLabel.setForeground(TEXT_PRIMARY);
        confirmPassLabel.setBounds(75, 180, 300, 25);
        resetDialog.add(confirmPassLabel);
        
        JPasswordField confirmPassField = createStyledPasswordField();
        confirmPassField.setMaximumSize(null);
        confirmPassField.setBounds(75, 210, 300, 40);
        resetDialog.add(confirmPassField);
        
        JButton resetBtn = createDialogButton("Reset Password", PRIMARY_BLUE, SECONDARY_BLUE);
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
        
        JButton cancelBtn = createDialogButton("Cancel", new Color(149, 165, 166), new Color(127, 140, 141));
        cancelBtn.setBounds(235, 270, 140, 40);
        cancelBtn.addActionListener(e -> resetDialog.dispose());
        resetDialog.add(cancelBtn);
        
        resetDialog.setVisible(true);
    }
    
    private JButton createDialogButton(String text, Color normalColor, Color hoverColor) {
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
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void handleRegisterClick() {
        this.dispose();
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