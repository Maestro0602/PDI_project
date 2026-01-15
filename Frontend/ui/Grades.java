package Frontend.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grades extends JFrame {

    // Colors matching the mainpage theme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);

    // Grade tracking
    private Map<String, Map<String, Double>> studentGrades = new HashMap<>();
    private List<String> students = new ArrayList<>();
    private List<String> subjects = new ArrayList<>();
    private String selectedSubject = null;
    private JComboBox<String> subjectComboBox;
    private JPanel studentsPanel;

    public Grades() {
        // Sample data
        students.add("Nak");
        students.add("Vattey");
        students.add("Kimchun");
        students.add("Both");

        subjects.add("Mathematics");
        subjects.add("Physics");
        subjects.add("Chemistry");
        subjects.add("Biology");
        subjects.add("English");
        subjects.add("Computer Science");

        // Initialize grade maps for each student
        for (String student : students) {
            studentGrades.put(student, new HashMap<>());
        }

        initComponent();
    }

    public void initComponent() {
        setTitle("Grade Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 750);
        setLocationRelativeTo(null);

        // Main background panel
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
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(10, 30, 30, 30));

        // Subject selection panel
        JPanel subjectPanel = createSubjectSelectionPanel();
        contentPanel.add(subjectPanel, BorderLayout.NORTH);

        // Students grades container
        JPanel gradesContainer = createGradesContainer();
        contentPanel.add(gradesContainer, BorderLayout.CENTER);

        // Bottom button panel
        JPanel bottomPanel = createBottomPanel();
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

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

                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);

                GradientPaint blueGradient = new GradientPaint(
                    0, 0, new Color(168, 85, 247),
                    0, getHeight(), new Color(139, 92, 246)
                );
                g2d.setPaint(blueGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                g2d.setColor(new Color(168, 85, 247, 8));
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, ACCENT_PURPLE,
                    getWidth(), getHeight(), new Color(139, 92, 246)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("📝");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Grade Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Enter and manage student grades by subject");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(subtitleLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        JButton backButton = createBackButton();

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSubjectSelectionPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 2, 15, 15);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel selectLabel = new JLabel("📚 Select Subject:");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        selectLabel.setForeground(TEXT_PRIMARY);

        subjectComboBox = createStyledComboBox();
        subjectComboBox.addItem("-- Choose a Subject --");
        for (String subject : subjects) {
            subjectComboBox.addItem(subject);
        }

        subjectComboBox.addActionListener(e -> {
            int index = subjectComboBox.getSelectedIndex();
            if (index > 0) {
                selectedSubject = subjects.get(index - 1);
                refreshStudentsList();
            } else {
                selectedSubject = null;
                refreshStudentsList();
            }
        });

        panel.add(selectLabel);
        panel.add(subjectComboBox);

        return panel;
    }

    @SuppressWarnings("unchecked")
    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> comboBox = new JComboBox<String>() {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(248, 250, 252));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2d.setColor(new Color(203, 213, 225));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                
                super.paintComponent(g);
            }
        };
        
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setPreferredSize(new Dimension(250, 40));
        comboBox.setBackground(new Color(248, 250, 252));
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setBorder(new EmptyBorder(5, 15, 5, 15));
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return comboBox;
    }

    private JPanel createGradesContainer() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);
        mainContainer.setBorder(new EmptyBorder(15, 0, 0, 0));

        studentsPanel = new JPanel();
        studentsPanel.setLayout(new BoxLayout(studentsPanel, BoxLayout.Y_AXIS));
        studentsPanel.setOpaque(false);

        // Show placeholder when no subject selected
        JPanel placeholderPanel = createPlaceholderPanel();
        studentsPanel.add(placeholderPanel);

        JScrollPane scrollPane = new JScrollPane(studentsPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainContainer.add(scrollPane, BorderLayout.CENTER);

        return mainContainer;
    }

    private JPanel createPlaceholderPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 2, 15, 15);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(800, 300));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel iconLabel = new JLabel("📋");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("Select a subject to start grading");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        messageLabel.setForeground(TEXT_SECONDARY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hintLabel = new JLabel("Choose a subject from the dropdown above");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hintLabel.setForeground(TEXT_SECONDARY);
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(iconLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(hintLabel);

        panel.add(contentPanel);

        return panel;
    }

    private void refreshStudentsList() {
        studentsPanel.removeAll();

        if (selectedSubject == null) {
            studentsPanel.add(createPlaceholderPanel());
        } else {
            // Subject header
            JPanel subjectHeader = createSubjectHeader();
            studentsPanel.add(subjectHeader);
            studentsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

            // Student rows
            for (String student : students) {
                studentsPanel.add(createStudentGradeRow(student));
                studentsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        studentsPanel.revalidate();
        studentsPanel.repaint();
    }

    private JPanel createSubjectHeader() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(ACCENT_PURPLE.getRed(), ACCENT_PURPLE.getGreen(), ACCENT_PURPLE.getBlue(), 30),
                    getWidth(), 0, new Color(ACCENT_BLUE.getRed(), ACCENT_BLUE.getGreen(), ACCENT_BLUE.getBlue(), 30)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.setColor(ACCENT_PURPLE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel subjectLabel = new JLabel("📖 " + selectedSubject);
        subjectLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        subjectLabel.setForeground(TEXT_PRIMARY);

        JLabel gradeHeaderLabel = new JLabel("Enter Grade (0-100)");
        gradeHeaderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gradeHeaderLabel.setForeground(TEXT_SECONDARY);

        panel.add(subjectLabel, BorderLayout.WEST);
        panel.add(gradeHeaderLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createStudentGradeRow(String studentName) {
        JPanel row = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 2, 15, 15);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        row.setOpaque(false);
        row.setLayout(new BorderLayout(15, 0));
        row.setBorder(new EmptyBorder(18, 20, 18, 20));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Student info panel
        JPanel studentInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        studentInfoPanel.setOpaque(false);

        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);

        JLabel nameLabel = new JLabel(studentName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(TEXT_PRIMARY);

        // Get existing grade if any
        Double existingGrade = studentGrades.get(studentName).get(selectedSubject);
        String gradeStatus = existingGrade != null ? "Grade: " + existingGrade : "No grade yet";
        
        JLabel statusLabel = new JLabel(gradeStatus);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_SECONDARY);

        namePanel.add(nameLabel);
        namePanel.add(statusLabel);

        studentInfoPanel.add(avatarLabel);
        studentInfoPanel.add(namePanel);

        // Grade input panel
        JPanel gradePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        gradePanel.setOpaque(false);

        JTextField gradeField = createGradeTextField(existingGrade);
        JButton saveBtn = createGradeSaveButton(studentName, gradeField, statusLabel);

        gradePanel.add(gradeField);
        gradePanel.add(saveBtn);

        row.add(studentInfoPanel, BorderLayout.WEST);
        row.add(gradePanel, BorderLayout.EAST);

        return row;
    }

    private JTextField createGradeTextField(Double existingGrade) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(248, 250, 252));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                super.paintComponent(g);
            }
        };
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(100, 40));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        if (existingGrade != null) {
            field.setText(String.valueOf(existingGrade.intValue()));
        }

        // Add focus listener for visual feedback
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_PURPLE, 2, true),
                    new EmptyBorder(7, 11, 7, 11)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                    new EmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return field;
    }

    private JButton createGradeSaveButton(String studentName, JTextField gradeField, JLabel statusLabel) {
        JButton button = new JButton("Save") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(22, 163, 74));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(34, 197, 94));
                } else {
                    g2d.setColor(ACCENT_GREEN);
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

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(70, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            try {
                String text = gradeField.getText().trim();
                if (text.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a grade!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                double grade = Double.parseDouble(text);
                if (grade < 0 || grade > 100) {
                    JOptionPane.showMessageDialog(this, "Grade must be between 0 and 100!", "Invalid Grade", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                studentGrades.get(studentName).put(selectedSubject, grade);
                statusLabel.setText("Grade: " + (int)grade + " ✓");
                statusLabel.setForeground(ACCENT_GREEN);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent evt) { button.repaint(); }
        });

        return button;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        bottomPanel.setOpaque(false);

        JButton saveAllButton = createActionButton("Save All Grades", ACCENT_GREEN, true);
        JButton viewSummaryButton = createActionButton("View Summary", ACCENT_BLUE, true);
        JButton clearButton = createActionButton("Clear All", ACCENT_RED, false);

        saveAllButton.addActionListener(e -> {
            if (selectedSubject == null) {
                JOptionPane.showMessageDialog(this, "Please select a subject first!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int gradesCount = 0;
            for (String student : students) {
                if (studentGrades.get(student).containsKey(selectedSubject)) {
                    gradesCount++;
                }
            }
            
            JOptionPane.showMessageDialog(this,
                "Grades saved successfully!\n" +
                "Subject: " + selectedSubject + "\n" +
                "Students graded: " + gradesCount + " out of " + students.size(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        });

        viewSummaryButton.addActionListener(e -> showGradeSummary());

        clearButton.addActionListener(e -> {
            if (selectedSubject != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Clear all grades for " + selectedSubject + "?",
                    "Confirm Clear",
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    for (String student : students) {
                        studentGrades.get(student).remove(selectedSubject);
                    }
                    refreshStudentsList();
                }
            }
        });

        bottomPanel.add(saveAllButton);
        bottomPanel.add(viewSummaryButton);
        bottomPanel.add(clearButton);

        return bottomPanel;
    }

    private void showGradeSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("═══════════════════════════════════════\n");
        summary.append("           GRADE SUMMARY REPORT\n");
        summary.append("═══════════════════════════════════════\n\n");

        for (String student : students) {
            summary.append("👤 ").append(student).append("\n");
            summary.append("─────────────────────────────────────\n");
            
            Map<String, Double> grades = studentGrades.get(student);
            if (grades.isEmpty()) {
                summary.append("   No grades recorded\n");
            } else {
                double total = 0;
                int count = 0;
                for (Map.Entry<String, Double> entry : grades.entrySet()) {
                    summary.append("   📖 ").append(entry.getKey()).append(": ")
                           .append(entry.getValue().intValue()).append("\n");
                    total += entry.getValue();
                    count++;
                }
                if (count > 0) {
                    summary.append("   ────────────────\n");
                    summary.append("   📊 Average: ").append(String.format("%.1f", total / count)).append("\n");
                }
            }
            summary.append("\n");
        }

        JTextArea textArea = new JTextArea(summary.toString());
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setBackground(new Color(248, 250, 252));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 450));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Grade Summary", JOptionPane.PLAIN_MESSAGE);
    }

    private JButton createActionButton(String text, Color color, boolean isPrimary) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(
                        Math.max(color.getRed() - 30, 0),
                        Math.max(color.getGreen() - 30, 0),
                        Math.max(color.getBlue() - 30, 0)
                    ));
                } else if (getModel().isRollover()) {
                    g2d.setColor(color);
                } else {
                    g2d.setColor(isPrimary ? color : new Color(248, 250, 252));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                if (isPrimary || getModel().isRollover() || getModel().isPressed()) {
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

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(160, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent evt) { button.repaint(); }
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

        button.addActionListener(e -> dispose());

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent evt) { button.repaint(); }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Grades().setVisible(true));
    }
}