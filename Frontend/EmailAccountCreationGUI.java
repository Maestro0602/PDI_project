package Frontend;

import Backend.src.database.EmailManager;
import Backend.src.database.EmailManager.AccountType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EmailAccountCreationGUI extends JFrame {
    
    private JTextField idField;
    private JTextField emailField;
    private JComboBox<String> accountTypeCombo;
    private JLabel prefixLabel;
    private JTextArea resultArea;
    
    private Color primaryColor = new Color(41, 128, 185);
    private Color successColor = new Color(46, 204, 113);
    private Color dangerColor = new Color(231, 76, 60);
    private Color warningColor = new Color(243, 156, 18);
    
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
        setSize(700, 750);
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
        card.setBounds(50, 25, 600, 700);
        card.setOpaque(false);
        
        // Icon
        JLabel iconLabel = new JLabel("📧");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setBounds(0, 20, 600, 80);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(iconLabel);
        
        // Title
        JLabel titleLabel = new JLabel("Create Email Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBounds(0, 100, 600, 40);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Fill in the details below");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setBounds(0, 140, 600, 25);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(subtitleLabel);
        
        // Account Type
        JLabel typeLabel = new JLabel("Account Type");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setForeground(new Color(52, 73, 94));
        typeLabel.setBounds(75, 185, 450, 25);
        card.add(typeLabel);
        
        String[] accountTypes = {"Select Account Type", "Student (ID: P2024...)", "Teacher (ID: T2024...)", "Owner (ID: O2024...)"};
        accountTypeCombo = new JComboBox<>(accountTypes);
        accountTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accountTypeCombo.setBounds(75, 215, 450, 45);
        accountTypeCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        accountTypeCombo.setBackground(Color.WHITE);
        accountTypeCombo.addActionListener(e -> handleAccountTypeChange());
        card.add(accountTypeCombo);
        
        // Prefix label (shows required ID format)
        prefixLabel = new JLabel("");
        prefixLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        prefixLabel.setForeground(primaryColor);
        prefixLabel.setBounds(75, 265, 450, 20);
        prefixLabel.setVisible(false);
        card.add(prefixLabel);
        
        // ID Field
        JLabel idLabel = new JLabel("Account ID");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(52, 73, 94));
        idLabel.setBounds(75, 290, 450, 25);
        card.add(idLabel);
        
        idField = createStyledTextField();
        idField.setBounds(75, 320, 450, 45);
        card.add(idField);
        
        // Email Field
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emailLabel.setForeground(new Color(52, 73, 94));
        emailLabel.setBounds(75, 375, 450, 25);
        card.add(emailLabel);
        
        emailField = createStyledTextField();
        emailField.setBounds(75, 405, 450, 45);
        card.add(emailField);
        
        // Result area
        JLabel resultLabel = new JLabel("Result");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resultLabel.setForeground(new Color(52, 73, 94));
        resultLabel.setBounds(75, 465, 450, 25);
        card.add(resultLabel);
        
        resultArea = new JTextArea();
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBounds(75, 495, 450, 100);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        card.add(scrollPane);
        
        //Back Button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBounds(20, 20, 200, 50);
        topPanel.setOpaque(false);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBounds(75, 615, 450, 50);
        buttonPanel.setOpaque(false);
        
        JButton createBtn = createStyledButton("Create Account", successColor, new Color(39, 174, 96));
        createBtn.setPreferredSize(new Dimension(180, 45));
        createBtn.addActionListener(e -> handleCreateAccount());
        buttonPanel.add(createBtn);
        
        JButton clearBtn = createStyledButton("Clear", warningColor, new Color(230, 126, 34));
        clearBtn.setPreferredSize(new Dimension(130, 45));
        clearBtn.addActionListener(e -> clearForm());
        buttonPanel.add(clearBtn);
        
        JButton backBtn = createStyledButton("← Back", primaryColor, new Color(41, 128, 185));
        backBtn.setPreferredSize(new Dimension(100, 45));
        backBtn.addActionListener(e -> handleBack());
        topPanel.add(backBtn);
        
        card.add(buttonPanel);
        card.add(topPanel);

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
    
    private void handleAccountTypeChange() {
        int index = accountTypeCombo.getSelectedIndex();
        
        switch (index) {
            case 1: // Student
                selectedType = AccountType.STUDENT;
                prefixLabel.setText(" ID must start with P2024");
                prefixLabel.setForeground(successColor);
                prefixLabel.setVisible(true);
                break;
            case 2: // Teacher
                selectedType = AccountType.TEACHER;
                prefixLabel.setText(" ID must start with T2024");
                prefixLabel.setForeground(successColor);
                prefixLabel.setVisible(true);
                break;
            case 3: // Owner
                selectedType = AccountType.OWNER;
                prefixLabel.setText(" ID must start with O2024");
                prefixLabel.setForeground(successColor);
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
            resultArea.setForeground(dangerColor);
            resultArea.setText(" Please select an account type");
            return;
        }
        
        String id = idField.getText().trim();
        String email = emailField.getText().trim();
        
        // Validate ID
        if (id.isEmpty()) {
            resultArea.setForeground(dangerColor);
            resultArea.setText(" ID cannot be empty");
            idField.requestFocus();
            return;
        }
        
        // Validate email
        if (email.isEmpty()) {
            resultArea.setForeground(dangerColor);
            resultArea.setText(" Email cannot be empty");
            emailField.requestFocus();
            return;
        }
        
        // Validate ID format
        if (!EmailManager.validateIdFormat(id, selectedType)) {
            resultArea.setForeground(dangerColor);
            resultArea.setText(" Invalid ID format. Must start with " + selectedType.getPrefix());
            idField.requestFocus();
            return;
        }
        
        // Validate email format
        if (!EmailManager.validateEmailFormat(email)) {
            resultArea.setForeground(dangerColor);
            resultArea.setText(" Invalid email format. Use format: example@domain.com");
            emailField.requestFocus();
            return;
        }
        
        // Check if ID exists
        if (EmailManager.idExists(id, selectedType)) {
            resultArea.setForeground(dangerColor);
            resultArea.setText(" Error: This ID already has an email account");
            return;
        }
        
        // Check if email exists
        if (EmailManager.emailExistsInSystem(email)) {
            resultArea.setForeground(dangerColor);
            resultArea.setText("❌ Error: This email is already in use");
            return;
        }
        
        // Create email account
        boolean success = EmailManager.createEmailAccount(selectedType, id, email);
        
        if (success) {
            resultArea.setForeground(successColor);
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
            resultArea.setForeground(dangerColor);
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
