package Frontend;

import Backend.src.database.StudentInfoManager;
import Backend.src.database.DatabaseManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class StudentAssignGUI extends JFrame {
    
    private JTextField nameField, studentIDField, genderField, yearField;
    private JTextArea resultArea;
    
    // Update panel fields
    private JTextField updateSearchField, updateNameField, updateGenderField, updateYearField;
    private JTextArea updateResultArea;
    
    // Delete panel fields
    private JTextField deleteSearchField;
    private JTextArea deleteResultArea;
    
    // Search panel fields
    private JTextField searchField;
    private JTextArea searchResultArea;
    
    private static final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private static final Color SECONDARY_BLUE = new Color(59, 130, 246);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    private static final Color WARNING_COLOR = new Color(249, 115, 22);
    
    public StudentAssignGUI() {
        initializeDatabase();
        initializeUI();
    }
    
    private void initializeDatabase() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                StudentInfoManager.createStudentInfoTable();
                return null;
            }
        };
        worker.execute();
    }
    
    private void initializeUI() {
        setTitle("Student Information Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(241, 245, 249),
                        getWidth(), getHeight(), new Color(226, 232, 240));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(new Color(147, 197, 253, 25));
                g2d.fillOval(-80, -80, 240, 240);
                g2d.fillOval(getWidth() - 160, getHeight() - 160, 240, 240);
            }
        };
        
        JPanel headerPanel = createHeaderPanel();
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        tabbedPane.addTab("  Add Student  ", createAddPanel());
        tabbedPane.addTab("  Update Student  ", createUpdatePanel());
        tabbedPane.addTab("  Delete Student  ", createDeletePanel());
        tabbedPane.addTab("  Search Student  ", createSearchPanel());
        
        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(tabbedPane, BorderLayout.CENTER);
        setContentPane(backgroundPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
                GradientPaint blueGradient = new GradientPaint(0, 0, new Color(59, 130, 246, 200),
                    0, getHeight(), new Color(147, 197, 253, 150));
                g2d.setPaint(blueGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);
                g2d.setColor(new Color(59, 130, 246, 8));
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_BLUE,
                    getWidth(), getHeight(), SECONDARY_BLUE);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());
        JLabel iconLabel = new JLabel("📚");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        JLabel welcomeLabel = new JLabel("Student Information Management");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcomeLabel.setForeground(TEXT_PRIMARY);
        JLabel subtitleLabel = new JLabel("Add, update, delete, and search student records");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        titlePanel.add(welcomeLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(subtitleLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        JButton backBtn = createHeaderButton("← Back", DANGER_COLOR);
        backBtn.addActionListener(e -> {
            dispose();
            new MainpageTeacher().setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backBtn);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        return headerPanel;
    }
    
    private JPanel createAddPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JPanel formCard = createCard();
        formCard.setLayout(null);
        formCard.setPreferredSize(new Dimension(850, 320));
        formCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));
        
        JLabel nameLabel = new JLabel("Student Name");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(TEXT_PRIMARY);
        nameLabel.setBounds(30, 20, 350, 25);
        formCard.add(nameLabel);
        nameField = createStyledTextField();
        nameField.setBounds(30, 45, 350, 45);
        formCard.add(nameField);
        
        JLabel idLabel = new JLabel("Student ID");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLabel.setForeground(TEXT_PRIMARY);
        idLabel.setBounds(420, 20, 350, 25);
        formCard.add(idLabel);
        studentIDField = createStyledTextField();
        studentIDField.setBounds(420, 45, 350, 45);
        formCard.add(studentIDField);
        
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        genderLabel.setForeground(TEXT_PRIMARY);
        genderLabel.setBounds(30, 110, 350, 25);
        formCard.add(genderLabel);
        genderField = createStyledTextField();
        genderField.setBounds(30, 135, 350, 45);
        formCard.add(genderField);
        
        JLabel yearLabel = new JLabel("Year");
        yearLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        yearLabel.setForeground(TEXT_PRIMARY);
        yearLabel.setBounds(420, 110, 350, 25);
        formCard.add(yearLabel);
        yearField = createStyledTextField();
        yearField.setBounds(420, 135, 350, 45);
        formCard.add(yearField);
        
        JButton addBtn = createModernButton("Add Student", SUCCESS_COLOR);
        addBtn.setBounds(270, 210, 180, 50);
        addBtn.addActionListener(e -> handleAddStudent());
        formCard.add(addBtn);
        
        JButton clearBtn = createModernButton("Clear", TEXT_SECONDARY);
        clearBtn.setBounds(470, 210, 120, 50);
        clearBtn.addActionListener(e -> clearAddForm());
        formCard.add(clearBtn);
        
        panel.add(formCard);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel resultCard = createCard();
        resultCard.setLayout(new BorderLayout());
        resultCard.setPreferredSize(new Dimension(850, 260));
        resultCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        JLabel resultLabel = new JLabel("  Results / Messages");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resultLabel.setForeground(TEXT_PRIMARY);
        resultLabel.setBorder(new EmptyBorder(10, 10, 5, 10));
        resultArea = new JTextArea();
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(new EmptyBorder(10, 15, 10, 15));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        resultCard.add(resultLabel, BorderLayout.NORTH);
        resultCard.add(scrollPane, BorderLayout.CENTER);
        panel.add(resultCard);
        panel.add(Box.createVerticalGlue());
        return panel;
    }
    
    private JPanel createUpdatePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // Search Card
        JPanel searchCard = createCard();
        searchCard.setLayout(null);
        searchCard.setPreferredSize(new Dimension(850, 130));
        searchCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        
        JLabel searchLabel = new JLabel("Search by Student ID");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLabel.setForeground(TEXT_PRIMARY);
        searchLabel.setBounds(30, 20, 740, 25);
        searchCard.add(searchLabel);
        
        updateSearchField = createStyledTextField();
        updateSearchField.setBounds(30, 45, 520, 45);
        searchCard.add(updateSearchField);
        
        JButton searchBtn = createModernButton("Search", PRIMARY_BLUE);
        searchBtn.setBounds(570, 45, 200, 45);
        searchBtn.addActionListener(e -> handleSearchForUpdate());
        searchCard.add(searchBtn);
        
        panel.add(searchCard);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Update Form Card
        JPanel formCard = createCard();
        formCard.setLayout(null);
        formCard.setPreferredSize(new Dimension(850, 250));
        formCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        
        JLabel nameLabel = new JLabel("Student Name");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(TEXT_PRIMARY);
        nameLabel.setBounds(30, 20, 350, 25);
        formCard.add(nameLabel);
        updateNameField = createStyledTextField();
        updateNameField.setBounds(30, 45, 350, 45);
        formCard.add(updateNameField);
        
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        genderLabel.setForeground(TEXT_PRIMARY);
        genderLabel.setBounds(420, 20, 350, 25);
        formCard.add(genderLabel);
        updateGenderField = createStyledTextField();
        updateGenderField.setBounds(420, 45, 350, 45);
        formCard.add(updateGenderField);
        
        JLabel yearLabel = new JLabel("Year");
        yearLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        yearLabel.setForeground(TEXT_PRIMARY);
        yearLabel.setBounds(30, 110, 350, 25);
        formCard.add(yearLabel);
        updateYearField = createStyledTextField();
        updateYearField.setBounds(30, 135, 350, 45);
        formCard.add(updateYearField);
        
        JButton updateBtn = createModernButton("Update Student", WARNING_COLOR);
        updateBtn.setBounds(420, 135, 180, 45);
        updateBtn.addActionListener(e -> handleUpdateStudent());
        formCard.add(updateBtn);
        
        JButton clearBtn = createModernButton("Clear", TEXT_SECONDARY);
        clearBtn.setBounds(620, 135, 150, 45);
        clearBtn.addActionListener(e -> clearUpdateForm());
        formCard.add(clearBtn);
        
        panel.add(formCard);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Result Card
        JPanel resultCard = createCard();
        resultCard.setLayout(new BorderLayout());
        resultCard.setPreferredSize(new Dimension(850, 180));
        resultCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        JLabel resultLabel = new JLabel("  Results / Messages");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resultLabel.setForeground(TEXT_PRIMARY);
        resultLabel.setBorder(new EmptyBorder(10, 10, 5, 10));
        updateResultArea = new JTextArea();
        updateResultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        updateResultArea.setEditable(false);
        updateResultArea.setLineWrap(true);
        updateResultArea.setWrapStyleWord(true);
        updateResultArea.setBorder(new EmptyBorder(10, 15, 10, 15));
        JScrollPane scrollPane = new JScrollPane(updateResultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        resultCard.add(resultLabel, BorderLayout.NORTH);
        resultCard.add(scrollPane, BorderLayout.CENTER);
        panel.add(resultCard);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createDeletePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // Search and Delete Card
        JPanel deleteCard = createCard();
        deleteCard.setLayout(null);
        deleteCard.setPreferredSize(new Dimension(850, 200));
        deleteCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        JLabel titleLabel = new JLabel("Delete Student Record");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBounds(30, 20, 740, 30);
        deleteCard.add(titleLabel);
        
        JLabel searchLabel = new JLabel("Enter Student ID to Delete");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLabel.setForeground(TEXT_SECONDARY);
        searchLabel.setBounds(30, 60, 740, 25);
        deleteCard.add(searchLabel);
        
        deleteSearchField = createStyledTextField();
        deleteSearchField.setBounds(30, 85, 740, 45);
        deleteCard.add(deleteSearchField);
        
        JButton deleteBtn = createModernButton("Delete Student", DANGER_COLOR);
        deleteBtn.setBounds(280, 145, 180, 45);
        deleteBtn.addActionListener(e -> handleDeleteStudent());
        deleteCard.add(deleteBtn);
        
        JButton clearBtn = createModernButton("Clear", TEXT_SECONDARY);
        clearBtn.setBounds(480, 145, 120, 45);
        clearBtn.addActionListener(e -> clearDeleteForm());
        deleteCard.add(clearBtn);
        
        panel.add(deleteCard);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Result Card
        JPanel resultCard = createCard();
        resultCard.setLayout(new BorderLayout());
        resultCard.setPreferredSize(new Dimension(850, 340));
        resultCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));
        JLabel resultLabel = new JLabel("  Results / Messages");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resultLabel.setForeground(TEXT_PRIMARY);
        resultLabel.setBorder(new EmptyBorder(10, 10, 5, 10));
        deleteResultArea = new JTextArea();
        deleteResultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        deleteResultArea.setEditable(false);
        deleteResultArea.setLineWrap(true);
        deleteResultArea.setWrapStyleWord(true);
        deleteResultArea.setBorder(new EmptyBorder(10, 15, 10, 15));
        JScrollPane scrollPane = new JScrollPane(deleteResultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        resultCard.add(resultLabel, BorderLayout.NORTH);
        resultCard.add(scrollPane, BorderLayout.CENTER);
        panel.add(resultCard);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // Search Card
        JPanel searchCard = createCard();
        searchCard.setLayout(null);
        searchCard.setPreferredSize(new Dimension(850, 180));
        searchCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        JLabel titleLabel = new JLabel("Search Student Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBounds(30, 20, 740, 30);
        searchCard.add(titleLabel);
        
        JLabel searchLabel = new JLabel("Enter Student ID");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLabel.setForeground(TEXT_SECONDARY);
        searchLabel.setBounds(30, 60, 740, 25);
        searchCard.add(searchLabel);
        
        searchField = createStyledTextField();
        searchField.setBounds(30, 85, 520, 45);
        searchCard.add(searchField);
        
        JButton searchBtn = createModernButton("Search", PRIMARY_BLUE);
        searchBtn.setBounds(570, 85, 200, 45);
        searchBtn.addActionListener(e -> handleSearchStudent());
        searchCard.add(searchBtn);
        
        panel.add(searchCard);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Result Card
        JPanel resultCard = createCard();
        resultCard.setLayout(new BorderLayout());
        resultCard.setPreferredSize(new Dimension(850, 380));
        resultCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 380));
        JLabel resultLabel = new JLabel("  Search Results");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resultLabel.setForeground(TEXT_PRIMARY);
        resultLabel.setBorder(new EmptyBorder(10, 10, 5, 10));
        searchResultArea = new JTextArea();
        searchResultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchResultArea.setEditable(false);
        searchResultArea.setLineWrap(true);
        searchResultArea.setWrapStyleWord(true);
        searchResultArea.setBorder(new EmptyBorder(10, 15, 10, 15));
        JScrollPane scrollPane = new JScrollPane(searchResultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        resultCard.add(resultLabel, BorderLayout.NORTH);
        resultCard.add(scrollPane, BorderLayout.CENTER);
        panel.add(resultCard);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 20, 20);
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setOpaque(false);
        return card;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(12, 16, 12, 16)));
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_BLUE, 2, true),
                        new EmptyBorder(12, 16, 12, 16)));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                        new EmptyBorder(12, 16, 12, 16)));
            }
        });
        return field;
    }
    
    private JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(color.brighter());
                } else {
                    g2d.setColor(color);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private JButton createHeaderButton(String text, Color color) {
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
                    g2d.setColor(new Color(248, 250, 252));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                if (getModel().isRollover() || getModel().isPressed()) {
                    g2d.setColor(Color.WHITE);
                } else {
                    g2d.setColor(TEXT_SECONDARY);
                }
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
        return button;
    }
    
    // Clear form methods
    private void clearAddForm() {
        nameField.setText("");
        studentIDField.setText("");
        genderField.setText("");
        yearField.setText("");
        resultArea.setText("");
        nameField.requestFocus();
    }
    
    private void clearUpdateForm() {
        updateSearchField.setText("");
        updateNameField.setText("");
        updateGenderField.setText("");
        updateYearField.setText("");
        updateResultArea.setText("");
        updateSearchField.requestFocus();
    }
    
    private void clearDeleteForm() {
        deleteSearchField.setText("");
        deleteResultArea.setText("");
        deleteSearchField.requestFocus();
    }
    
    // Handler methods
    private void handleAddStudent() {
        String name = nameField.getText().trim();
        String studentID = studentIDField.getText().trim();
        String gender = genderField.getText().trim();
        String year = yearField.getText().trim();
        
        if (name.isEmpty() || studentID.isEmpty() || gender.isEmpty() || year.isEmpty()) {
            resultArea.setForeground(DANGER_COLOR);
            resultArea.setText("✗ ERROR: All fields are required!\n\nPlease fill in all fields before adding a student.");
            return;
        }
        
        if (!DatabaseManager.ConditionChecker.checkUserIDExists(studentID)) {
            resultArea.setForeground(DANGER_COLOR);
            resultArea.setText("✗ ERROR: UserID not found in users table!\n\n" +
                             "Student ID: " + studentID + " does not exist in the system.\n" +
                             "Please ensure the student is registered first.");
            return;
        }
        
        boolean success = StudentInfoManager.saveStudentInfo(name, studentID, gender, year);
        
        if (success) {
            resultArea.setForeground(SUCCESS_COLOR);
            resultArea.setText("✓ SUCCESS: Student Added!\n\n" +
                             "Name: " + name + "\n" +
                             "Student ID: " + studentID + "\n" +
                             "Gender: " + gender + "\n" +
                             "Year: " + year + "\n\n" +
                             "Student information has been saved successfully.");
            clearAddForm();
        } else {
            resultArea.setForeground(DANGER_COLOR);
            resultArea.setText("✗ ERROR: Failed to add student!\n\n" +
                             "The student may already exist in the database.\n" +
                             "Please check and try again.");
        }
    }
    
    private void handleSearchForUpdate() {
        String studentID = updateSearchField.getText().trim();
        
        if (studentID.isEmpty()) {
            updateResultArea.setForeground(DANGER_COLOR);
            updateResultArea.setText("✗ ERROR: Please enter a Student ID to search.");
            return;
        }
        
        String[] studentInfo = StudentInfoManager.getStudentInfo(studentID);
        
        if (studentInfo != null) {
            // studentInfo array: [name, studentID, gender, year]
            updateNameField.setText(studentInfo[0]);
            updateGenderField.setText(studentInfo[2]);
            updateYearField.setText(studentInfo[3]);
            updateResultArea.setForeground(SUCCESS_COLOR);
            updateResultArea.setText("✓ Student Found!\n\n" +
                                   "Name: " + studentInfo[0] + "\n" +
                                   "ID: " + studentInfo[1] + "\n" +
                                   "Gender: " + studentInfo[2] + "\n" +
                                   "Year: " + studentInfo[3] + "\n\n" +
                                   "You can now update the information.");
        } else {
            updateResultArea.setForeground(DANGER_COLOR);
            updateResultArea.setText("✗ ERROR: Student ID not found!");
            updateNameField.setText("");
            updateGenderField.setText("");
            updateYearField.setText("");
        }
    }
    
    private void handleUpdateStudent() {
        String studentID = updateSearchField.getText().trim();
        String name = updateNameField.getText().trim();
        String gender = updateGenderField.getText().trim();
        String year = updateYearField.getText().trim();
        
        if (studentID.isEmpty()) {
            updateResultArea.setForeground(DANGER_COLOR);
            updateResultArea.setText("✗ ERROR: Please search for a student first.");
            return;
        }
        
        if (name.isEmpty() || gender.isEmpty() || year.isEmpty()) {
            updateResultArea.setForeground(DANGER_COLOR);
            updateResultArea.setText("✗ ERROR: All fields are required!");
            return;
        }
        
        boolean success = StudentInfoManager.updateStudentInfo(studentID, name, gender, year);
        
        if (success) {
            updateResultArea.setForeground(SUCCESS_COLOR);
            updateResultArea.setText("✓ SUCCESS: Student Updated!\n\n" +
                                   "Student ID: " + studentID + "\n" +
                                   "Name: " + name + "\n" +
                                   "Gender: " + gender + "\n" +
                                   "Year: " + year + "\n\n" +
                                   "Student information has been updated successfully.");
        } else {
            updateResultArea.setForeground(DANGER_COLOR);
            updateResultArea.setText("✗ ERROR: Failed to update student!\n\nPlease try again.");
        }
    }
    
    private void handleDeleteStudent() {
        String studentID = deleteSearchField.getText().trim();
        
        if (studentID.isEmpty()) {
            deleteResultArea.setForeground(DANGER_COLOR);
            deleteResultArea.setText("✗ ERROR: Please enter a Student ID to delete.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete student with ID: " + studentID + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = StudentInfoManager.deleteStudent(studentID);
            
            if (success) {
                deleteResultArea.setForeground(SUCCESS_COLOR);
                deleteResultArea.setText("✓ SUCCESS: Student Deleted!\n\n" +
                                       "Student ID: " + studentID + "\n\n" +
                                       "The student record has been permanently removed from the database.");
                clearDeleteForm();
            } else {
                deleteResultArea.setForeground(DANGER_COLOR);
                deleteResultArea.setText("✗ ERROR: Failed to delete student!\n\n" +
                                       "Student ID: " + studentID + " not found in the database.");
            }
        }
    }
    
    private void handleSearchStudent() {
        String studentID = searchField.getText().trim();
        
        if (studentID.isEmpty()) {
            searchResultArea.setForeground(DANGER_COLOR);
            searchResultArea.setText("✗ ERROR: Please enter a Student ID to search.");
            return;
        }
        
        String[] studentInfo = StudentInfoManager.getStudentInfo(studentID);
        
        if (studentInfo != null) {
            // studentInfo array: [name, studentID, gender, year]
            searchResultArea.setForeground(SUCCESS_COLOR);
            searchResultArea.setText("✓ STUDENT FOUND\n\n" +
                                   "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                                   "Name:        " + studentInfo[0] + "\n" +
                                   "Student ID:  " + studentInfo[1] + "\n" +
                                   "Gender:      " + studentInfo[2] + "\n" +
                                   "Year:        " + studentInfo[3] + "\n\n" +
                                   "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        } else {
            searchResultArea.setForeground(DANGER_COLOR);
            searchResultArea.setText("✗ STUDENT NOT FOUND\n\n" +
                                   "No student record exists with ID: " + studentID + "\n\n" +
                                   "Please verify the Student ID and try again.");
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            StudentAssignGUI frame = new StudentAssignGUI();
            frame.setVisible(true);
        });
    }
}