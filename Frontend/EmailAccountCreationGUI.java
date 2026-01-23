package Frontend;

import Backend.src.database.EmailManager;
import Backend.src.database.EmailManager.AccountType;
import Backend.main.MainPageOwner;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class EmailAccountCreationGUI extends JFrame {
    
    private JTextField idField;
    private JTextField emailField;
    private JComboBox<String> accountTypeCombo;
    private JLabel prefixLabel;
    private JTextArea resultArea;
    
    // Modern colors matching login page
    private static final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private static final Color SECONDARY_BLUE = new Color(59, 130, 246);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color SUCCESS_GREEN = new Color(34, 197, 94);
    private static final Color WARNING_YELLOW = new Color(234, 179, 8);
    private static final Color DANGER_RED = new Color(239, 68, 68);
    
    private AccountType selectedType = null;
    
    public EmailAccountCreationGUI() {
        initializeDatabases();
        initializeUI();
    }
    
    private void initializeDatabases() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                EmailManager.AccountDatabaseConnection.createAccountDatabase();
                EmailManager.AccountDatabaseConnection.initializeEmailTables();
                return null;
            }
        };
        worker.execute();
    }
    
    private void initializeUI() {
        setTitle("Email Account Creation System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 850);
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
        JPanel cardPanel = createMainCard();
        backgroundPanel.add(cardPanel);
        
        setContentPane(backgroundPanel);
    }
    
    private JPanel createMainCard() {
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
        card.setPreferredSize(new Dimension(500, 760));
        card.setOpaque(false);
        
        // Animated icon
        JPanel iconPanel = createAnimatedIcon();
        JPanel iconWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconWrapper.setOpaque(false);
        iconWrapper.setBorder(new EmptyBorder(0, 0, 20, 0));
        iconWrapper.add(iconPanel);
        
        // Title
        JLabel title = new JLabel("Create Email Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Fill in the details below");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Account Type
        JPanel accountTypePanel = createInputGroup("Account Type", createAccountTypeCombo());
        
        // Prefix label
        prefixLabel = new JLabel("");
        prefixLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        prefixLabel.setForeground(PRIMARY_BLUE);
        prefixLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        prefixLabel.setVisible(false);
        
        // ID Field
        idField = createStyledTextField();
        JPanel idPanel = createInputGroup("Account ID", idField);
        
        // Email Field
        emailField = createStyledTextField();
        JPanel emailPanel = createInputGroup("Email Address", emailField);
        
        // Result area
        JLabel resultLabel = new JLabel("Result");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        resultLabel.setForeground(TEXT_PRIMARY);
        resultLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        resultArea = new JTextArea();
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(12, 16, 12, 16)
        ));
        resultArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        resultArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JButton createBtn = createStyledButton("Create Account", SUCCESS_GREEN);
        createBtn.setPreferredSize(new Dimension(150, 45));
        createBtn.addActionListener(e -> handleCreateAccount());
        
        JButton clearBtn = createStyledButton("Clear", WARNING_YELLOW);
        clearBtn.setPreferredSize(new Dimension(100, 45));
        clearBtn.addActionListener(e -> clearForm());
        
        JButton backBtn = createStyledButton("← Back", DANGER_RED);
        backBtn.setPreferredSize(new Dimension(100, 45));
        backBtn.addActionListener(e -> handleBack());
        
        buttonPanel.add(createBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(backBtn);
        
        // Add components
        card.add(iconWrapper);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(accountTypePanel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(prefixLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(idPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(emailPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(resultLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(resultArea);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(buttonPanel);
        
        return card;
    }
    
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

                // Email icon
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(25, 35, 50, 30, 8, 8);
                g2d.drawLine(25, 35, 50, 50);
                g2d.drawLine(75, 35, 50, 50);
            }
        };

        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(100, 100));
        iconPanel.setMaximumSize(new Dimension(100, 100));
        return iconPanel;
    }
    
    private JComboBox<String> createAccountTypeCombo() {
        String[] accountTypes = {"Select Account Type", "Student (ID: P2024...)", "Teacher (ID: T2024...)", "Owner (ID: O2024...)"};
        accountTypeCombo = new JComboBox<>(accountTypes);
        accountTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accountTypeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        accountTypeCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(12, 16, 12, 16)
        ));
        accountTypeCombo.setBackground(Color.WHITE);
        accountTypeCombo.addActionListener(e -> handleAccountTypeChange());
        return accountTypeCombo;
    }
    
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
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color btnColor = color;
                if (getModel().isPressed()) {
                    btnColor = color.darker();
                } else if (getModel().isRollover()) {
                    btnColor = new Color(
                        Math.min(color.getRed() + 20, 255),
                        Math.min(color.getGreen() + 20, 255),
                        Math.min(color.getBlue() + 20, 255)
                    );
                }

                g2d.setColor(btnColor);
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

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { button.repaint(); }
        });

        return button;
    }
    
    private void handleAccountTypeChange() {
        int index = accountTypeCombo.getSelectedIndex();
        
        switch (index) {
            case 1: // Student
                selectedType = AccountType.STUDENT;
                prefixLabel.setText("✓ ID must start with P2024");
                prefixLabel.setForeground(SUCCESS_GREEN);
                prefixLabel.setVisible(true);
                break;
            case 2: // Teacher
                selectedType = AccountType.TEACHER;
                prefixLabel.setText("✓ ID must start with T2024");
                prefixLabel.setForeground(SUCCESS_GREEN);
                prefixLabel.setVisible(true);
                break;
            case 3: // Owner
                selectedType = AccountType.OWNER;
                prefixLabel.setText("✓ ID must start with O2024");
                prefixLabel.setForeground(SUCCESS_GREEN);
                prefixLabel.setVisible(true);
                break;
            default:
                selectedType = null;
                prefixLabel.setVisible(false);
                break;
        }
    }
    
    private void handleCreateAccount() {
        // Clear previous results
        resultArea.setText("");
        
        // Validate account type
        if (selectedType == null) {
            resultArea.setForeground(DANGER_RED);
            resultArea.setText("❌ Please select an account type");
            return;
        }
        
        String id = idField.getText().trim();
        String email = emailField.getText().trim();
        
        // Validate ID
        if (id.isEmpty()) {
            resultArea.setForeground(DANGER_RED);
            resultArea.setText("❌ ID cannot be empty");
            idField.requestFocus();
            return;
        }
        
        // Validate email
        if (email.isEmpty()) {
            resultArea.setForeground(DANGER_RED);
            resultArea.setText("❌ Email cannot be empty");
            emailField.requestFocus();
            return;
        }
        
        // Validate ID format
        if (!EmailManager.validateIdFormat(id, selectedType)) {
            resultArea.setForeground(DANGER_RED);
            resultArea.setText("❌ Invalid ID format. Must start with " + selectedType.getPrefix());
            idField.requestFocus();
            return;
        }
        
        // Validate email format
        if (!EmailManager.validateEmailFormat(email)) {
            resultArea.setForeground(DANGER_RED);
            resultArea.setText("❌ Invalid email format. Use format: example@domain.com");
            emailField.requestFocus();
            return;
        }
        
        // Check if ID exists
        if (EmailManager.idExists(id, selectedType)) {
            resultArea.setForeground(DANGER_RED);
            resultArea.setText("❌ Error: This ID already has an email account");
            return;
        }
        
        // Check if email exists
        if (EmailManager.emailExistsInSystem(email)) {
            resultArea.setForeground(DANGER_RED);
            resultArea.setText("❌ Error: This email is already in use");
            return;
        }
        
        // Create email account
        boolean success = EmailManager.createEmailAccount(selectedType, id, email);
        
        if (success) {
            resultArea.setForeground(SUCCESS_GREEN);
            resultArea.setText("✅ EMAIL ACCOUNT CREATED SUCCESSFULLY!\n\n" +
                             "Account Type: " + getAccountTypeLabel(selectedType) + "\n" +
                             "ID: " + id + "\n" +
                             "Email: " + email);
            
            // Ask if user wants to create another
            int option = JOptionPane.showConfirmDialog(
                this,
                "Account created successfully!\n\nDo you want to create another account?",
                "Success",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (option == JOptionPane.YES_OPTION) {
                clearForm();
            } else {
                handleBack();
            }
        } else {
            resultArea.setForeground(DANGER_RED);
            resultArea.setText("❌ Failed to create email account. Please try again.");
        }
    }
    
    private void clearForm() {
        accountTypeCombo.setSelectedIndex(0);
        idField.setText("");
        emailField.setText("");
        resultArea.setText("");
        prefixLabel.setVisible(false);
        selectedType = null;
        accountTypeCombo.requestFocus();
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
                MainPageOwnerGUI ownerGUI = new MainPageOwnerGUI();
                ownerGUI.setVisible(true);
            });
        }
    }
    
    private String getAccountTypeLabel(AccountType type) {
        switch (type) {
            case STUDENT:
                return "Student";
            case TEACHER:
                return "Teacher";
            case OWNER:
                return "Owner";
            default:
                return "Unknown";
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            EmailAccountCreationGUI frame = new EmailAccountCreationGUI();
            frame.setVisible(true);
        });
    }
}