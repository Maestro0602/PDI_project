package Frontend;


import Backend.src.database.HeadTeacherpw;
import Backend.main.MainPageOwner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AssignPasswordGUI extends JFrame {
    
    private JTextField headTeacherIDField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel statusLabel;
    
    private Color primaryColor = new Color(192, 57, 43);
    private Color successColor = new Color(46, 204, 113);
    private Color dangerColor = new Color(231, 76, 60);
    private Color warningColor = new Color(243, 156, 18);
    
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
        setSize(600, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(192, 57, 43), 0, getHeight(), new Color(231, 76, 60));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);
        
        // Main card
        JPanel card = createMainCard();
        mainPanel.add(card);
        
        add(mainPanel);
    }
    
    private JPanel createMainCard() {
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
        card.setBounds(50, 50, 500, 670);
        card.setOpaque(false);
        
        // Icon
        JLabel iconLabel = new JLabel("🔑");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 70));
        iconLabel.setBounds(0, 30, 500, 100);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(iconLabel);
        
        // Title
        JLabel titleLabel = new JLabel("Assign Password");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBounds(0, 125, 500, 40);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Head Teacher Password Assignment");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setBounds(0, 165, 500, 25);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(subtitleLabel);
        
        // HeadTeacher ID Field
        JLabel idLabel = new JLabel("Head Teacher ID");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(52, 73, 94));
        idLabel.setBounds(75, 220, 350, 25);
        card.add(idLabel);
        
        headTeacherIDField = createStyledTextField();
        headTeacherIDField.setBounds(75, 250, 350, 45);
        card.add(headTeacherIDField);
        
        // Password Field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(52, 73, 94));
        passwordLabel.setBounds(75, 310, 350, 25);
        card.add(passwordLabel);
        
        passwordField = createStyledPasswordField();
        passwordField.setBounds(75, 340, 350, 45);
        card.add(passwordField);
        
        // Confirm Password Field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmPasswordLabel.setForeground(new Color(52, 73, 94));
        confirmPasswordLabel.setBounds(75, 400, 350, 25);
        card.add(confirmPasswordLabel);
        
        confirmPasswordField = createStyledPasswordField();
        confirmPasswordField.setBounds(75, 430, 350, 45);
        card.add(confirmPasswordField);
        
        // Status Label
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setBounds(75, 485, 350, 25);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setVisible(false);
        card.add(statusLabel);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBounds(75, 500, 350, 50);
        buttonPanel.setOpaque(false);
        
        JButton assignBtn = createStyledButton("Assign Password", successColor, new Color(39, 174, 96));
        assignBtn.setPreferredSize(new Dimension(180, 45));
        assignBtn.addActionListener(e -> handleAssignPassword());
        buttonPanel.add(assignBtn);
        
        JButton clearBtn = createStyledButton("Clear", warningColor, new Color(230, 126, 34));
        clearBtn.setPreferredSize(new Dimension(100, 45));
        clearBtn.addActionListener(e -> clearForm());
        buttonPanel.add(clearBtn);
        
        card.add(buttonPanel);
        
        // Back button
        JButton backBtn = createStyledButton("← Back to Owner Page", new Color(149, 165, 166), new Color(127, 140, 141));
        backBtn.setBounds(75, 570, 350, 40);
        backBtn.addActionListener(e -> handleBack());
        card.add(backBtn);
        
        // Enter key listener
        ActionListener enterAction = e -> handleAssignPassword();
        confirmPasswordField.addActionListener(enterAction);
        
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
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void handleAssignPassword() {
        String headTeacherID = headTeacherIDField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validate HeadTeacher ID
        if (headTeacherID.isEmpty()) {
            showStatus("❌ Head Teacher ID cannot be empty!", dangerColor);
            headTeacherIDField.requestFocus();
            return;
        }
        
        // Validate Password
        if (password.isEmpty()) {
            showStatus("❌ Password cannot be empty!", dangerColor);
            passwordField.requestFocus();
            return;
        }
        
        // Validate Password Match
        if (!password.equals(confirmPassword)) {
            showStatus("❌ Passwords don't match!", dangerColor);
            passwordField.setText("");
            confirmPasswordField.setText("");
            passwordField.requestFocus();
            return;
        }
        
        // Validate Password Length
        if (password.length() < 6) {
            showStatus("⚠ Password should be at least 6 characters", warningColor);
            passwordField.requestFocus();
            return;
        }
        
        // Assign password
        boolean success = HeadTeacherpw.saveHeadTeacherInfo(headTeacherID, password);
        
        if (success) {
            showStatus("✅ Password assigned successfully!", successColor);
            
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
            showStatus("❌ Failed to assign password!", dangerColor);
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
            "Are you sure you want to go back?\nAny unsaved data will be lost.",
            "Confirm Exit",
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
