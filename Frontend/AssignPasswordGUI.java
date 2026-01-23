package Frontend;

import Backend.src.database.HeadTeacherpw;
import Backend.main.MainPageOwner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AssignPasswordGUI extends JFrame {
    
    // Colors matching the modern theme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    
    private JTextField headTeacherIDField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel statusLabel;
    
    public AssignPasswordGUI() {
        initializeDatabases();
        initializeUI();
    }
    
    private void initializeDatabases() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                HeadTeacherpw.createDatabase();
                HeadTeacherpw.createUserTable();
                return null;
            }
        };
        worker.execute();
    }
    
    private void initializeUI() {
        setTitle("Assign Head Teacher Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(880, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main background panel with gradient
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
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

                g2d.setColor(new Color(147, 197, 253, 25));
                g2d.fillOval(-80, -80, 240, 240);
                g2d.fillOval(getWidth() - 160, getHeight() - 160, 240, 240);
            }
        };
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        JPanel formCard = createFormCard();
        contentPanel.add(formCard);

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(backgroundPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background with shadow
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);

                // Main card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);

                // Purple gradient accent on the left
                GradientPaint purpleGradient = new GradientPaint(
                    0, 0, new Color(168, 85, 247, 200),
                    0, getHeight(), new Color(192, 132, 252, 150)
                );
                g2d.setPaint(purpleGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                // Subtle purple background pattern
                g2d.setColor(new Color(168, 85, 247, 8));
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // Icon and title container
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        // Password icon badge
        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, ACCENT_PURPLE,
                    getWidth(), getHeight(), new Color(147, 51, 234)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("🔑");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Assign Password");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Set up Head Teacher account credentials");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(subtitleLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        // Back button
        JButton backButton = createBackButton();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }
    
    private JPanel createFormCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 12));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setLayout(null);
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(550, 480));
        
        // Info panel with icon
        JPanel infoPanel = createInfoPanel();
        infoPanel.setBounds(50, 30, 450, 80);
        card.add(infoPanel);
        
        // HeadTeacher ID Field
        JLabel idLabel = new JLabel("Head Teacher ID");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLabel.setForeground(TEXT_PRIMARY);
        idLabel.setBounds(50, 130, 450, 25);
        card.add(idLabel);
        
        headTeacherIDField = createStyledTextField();
        headTeacherIDField.setBounds(50, 160, 450, 45);
        card.add(headTeacherIDField);
        
        // Password Field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(TEXT_PRIMARY);
        passwordLabel.setBounds(50, 220, 450, 25);
        card.add(passwordLabel);
        
        passwordField = createStyledPasswordField();
        passwordField.setBounds(50, 250, 450, 45);
        card.add(passwordField);
        
        // Confirm Password Field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        confirmPasswordLabel.setForeground(TEXT_PRIMARY);
        confirmPasswordLabel.setBounds(50, 310, 450, 25);
        card.add(confirmPasswordLabel);
        
        confirmPasswordField = createStyledPasswordField();
        confirmPasswordField.setBounds(50, 340, 450, 45);
        card.add(confirmPasswordField);
        
        // Status Label
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setBounds(50, 395, 450, 25);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setVisible(false);
        card.add(statusLabel);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBounds(50, 415, 450, 50);
        buttonPanel.setOpaque(false);
        
        JButton assignBtn = createStyledButton("Assign Password", ACCENT_GREEN);
        assignBtn.setPreferredSize(new Dimension(200, 45));
        assignBtn.addActionListener(e -> handleAssignPassword());
        buttonPanel.add(assignBtn);
        
        JButton clearBtn = createStyledButton("Clear Form", ACCENT_ORANGE);
        clearBtn.setPreferredSize(new Dimension(150, 45));
        clearBtn.addActionListener(e -> clearForm());
        buttonPanel.add(clearBtn);
        
        card.add(buttonPanel);
        
        // Enter key listener
        ActionListener enterAction = e -> handleAssignPassword();
        confirmPasswordField.addActionListener(enterAction);
        
        return card;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(168, 85, 247, 15));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                g2d.setColor(new Color(168, 85, 247, 100));
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        panel.setLayout(new BorderLayout(15, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel infoIcon = new JLabel("ℹ️");
        infoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel infoTitle = new JLabel("Password Requirements");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        infoTitle.setForeground(TEXT_PRIMARY);
        infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel infoText = new JLabel("Password must be at least 6 characters long");
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoText.setForeground(TEXT_SECONDARY);
        infoText.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(infoTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        textPanel.add(infoText);
        
        panel.add(infoIcon, BorderLayout.WEST);
        panel.add(textPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_PURPLE, 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });
        
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_PURPLE, 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });
        
        return field;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(color);
                } else {
                    g2d.setColor(color.darker().darker());
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

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(MouseEvent evt) { button.repaint(); }
        });

        return button;
    }
    
    private JButton createBackButton() {
        JButton button = new JButton("← Back") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(226, 232, 240));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(241, 245, 249));
                } else {
                    g2d.setColor(Color.WHITE);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.setColor(TEXT_PRIMARY);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> handleBack());

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(MouseEvent evt) { button.repaint(); }
        });

        return button;
    }
    
    private void handleAssignPassword() {
        String headTeacherID = headTeacherIDField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validate HeadTeacher ID
        if (headTeacherID.isEmpty()) {
            showStatus("Head Teacher ID cannot be empty!", ACCENT_RED);
            headTeacherIDField.requestFocus();
            return;
        }
        
        // Validate Password
        if (password.isEmpty()) {
            showStatus("Password cannot be empty!", ACCENT_RED);
            passwordField.requestFocus();
            return;
        }
        
        // Validate Password Match
        if (!password.equals(confirmPassword)) {
            showStatus("Passwords don't match!", ACCENT_RED);
            passwordField.setText("");
            confirmPasswordField.setText("");
            passwordField.requestFocus();
            return;
        }
        
        // Validate Password Length
        if (password.length() < 6) {
            showStatus("Password must be at least 6 characters!", ACCENT_ORANGE);
            passwordField.requestFocus();
            return;
        }
        
        // Assign password
        boolean success = HeadTeacherpw.saveHeadTeacherInfo(headTeacherID, password);
        
        if (success) {
            showStatus("✓ Password assigned successfully!", ACCENT_GREEN);
            
            // Show success dialog
            JOptionPane.showMessageDialog(
                this,
                "Password assigned successfully!\n\n" +
                "Head Teacher ID: " + headTeacherID + "\n" +
                "Password has been set.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            int option = JOptionPane.showConfirmDialog(
                this,
                "Do you want to assign another password?",
                "Continue?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (option == JOptionPane.YES_OPTION) {
                clearForm();
            } else {
                handleBack();
            }
        } else {
            showStatus("Failed to assign password!", ACCENT_RED);
            JOptionPane.showMessageDialog(
                this,
                "Failed to assign password.\nPlease check the database connection and try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
        statusLabel.setVisible(true);
        
        // Hide status after 5 seconds
        Timer timer = new Timer(5000, e -> statusLabel.setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }
    
    private void clearForm() {
        headTeacherIDField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        statusLabel.setVisible(false);
        headTeacherIDField.requestFocus();
    }
    
    private void handleBack() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to go back?",
            "Confirm",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                MainPageOwnerGUI ownerFrame = new MainPageOwnerGUI();
                ownerFrame.setVisible(true);
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
            AssignPasswordGUI frame = new AssignPasswordGUI();
            frame.setVisible(true);
        });
    }
}