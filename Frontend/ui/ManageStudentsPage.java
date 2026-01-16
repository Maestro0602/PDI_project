package Frontend.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ManageStudentsPage extends JFrame {

    // Colors matching the theme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);

    // Filter components
    private JTextField searchField;
    private JComboBox<String> classCombo;
    private JComboBox<String> majorCombo;
    private JComboBox<String> departmentCombo;
    private JPanel studentsPanel;

    // Sample data
    private List<StudentInfo> allStudents = new ArrayList<>();
    private List<String> classes = new ArrayList<>();
    private List<String> majors = new ArrayList<>();
    private List<String> departments = new ArrayList<>();

    public ManageStudentsPage() {
        initSampleData();
        initComponent();
    }

    private void initSampleData() {
        // Departments
        departments.add("All Departments");
        departments.add("Engineering");
        departments.add("Science");
        departments.add("Arts");
        departments.add("Business");

        // Majors
        majors.add("All Majors");
        majors.add("Computer Science");
        majors.add("Mathematics");
        majors.add("Physics");
        majors.add("Chemistry");
        majors.add("English Literature");
        majors.add("Business Administration");

        // Classes
        classes.add("All Classes");
        classes.add("CS101 - Intro to Programming");
        classes.add("CS201 - Data Structures");
        classes.add("MATH101 - Calculus I");
        classes.add("PHYS101 - Physics I");
        classes.add("ENG101 - English Composition");

        // Sample students
        allStudents.add(new StudentInfo("John Smith", "S001", "Computer Science", "Engineering", "CS101 - Intro to Programming"));
        allStudents.add(new StudentInfo("Emily Johnson", "S002", "Mathematics", "Science", "MATH101 - Calculus I"));
        allStudents.add(new StudentInfo("Michael Brown", "S003", "Computer Science", "Engineering", "CS201 - Data Structures"));
        allStudents.add(new StudentInfo("Sarah Davis", "S004", "Physics", "Science", "PHYS101 - Physics I"));
        allStudents.add(new StudentInfo("James Wilson", "S005", "English Literature", "Arts", "ENG101 - English Composition"));
        allStudents.add(new StudentInfo("Jessica Martinez", "S006", "Computer Science", "Engineering", "CS101 - Intro to Programming"));
        allStudents.add(new StudentInfo("David Anderson", "S007", "Business Administration", "Business", "CS101 - Intro to Programming"));
        allStudents.add(new StudentInfo("Ashley Taylor", "S008", "Chemistry", "Science", "PHYS101 - Physics I"));
        allStudents.add(new StudentInfo("Christopher Thomas", "S009", "Mathematics", "Science", "MATH101 - Calculus I"));
        allStudents.add(new StudentInfo("Amanda Jackson", "S010", "Computer Science", "Engineering", "CS201 - Data Structures"));
    }

    public void initComponent() {
        setTitle("Manage Students");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
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

        JPanel headerPanel = createHeaderPanel();
        JPanel contentPanel = createContentPanel();

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

                GradientPaint orangeGradient = new GradientPaint(
                    0, 0, new Color(249, 115, 22, 200),
                    0, getHeight(), new Color(251, 146, 60, 150)
                );
                g2d.setPaint(orangeGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                g2d.setColor(new Color(249, 115, 22, 8));
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
                    0, 0, ACCENT_ORANGE,
                    getWidth(), getHeight(), new Color(234, 88, 12)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("👥");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Manage Students");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("View and manage student information");
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

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(15, 30, 30, 30));

        // Filter panel
        JPanel filterPanel = createFilterPanel();
        contentPanel.add(filterPanel, BorderLayout.NORTH);

        // Students list
        JPanel studentsContainer = createStudentsContainer();
        contentPanel.add(studentsContainer, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 2, 15, 15);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        filterPanel.setOpaque(false);
        filterPanel.setLayout(new GridBagLayout());
        filterPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Search field
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        
        JPanel searchContainer = createSearchField();
        filterPanel.add(searchContainer, gbc);

        // Filter labels and combos
        gbc.gridwidth = 1;
        gbc.weightx = 0.33;
        gbc.gridy = 1;

        // Class filter
        gbc.gridx = 0;
        JPanel classFilterPanel = createFilterCombo("Class", classes, true);
        filterPanel.add(classFilterPanel, gbc);

        // Major filter
        gbc.gridx = 1;
        JPanel majorFilterPanel = createFilterCombo("Major", majors, false);
        filterPanel.add(majorFilterPanel, gbc);

        // Department filter
        gbc.gridx = 2;
        JPanel deptFilterPanel = createFilterCombo("Department", departments, false);
        filterPanel.add(deptFilterPanel, gbc);

        // Clear filters button
        gbc.gridx = 3;
        gbc.weightx = 0;
        JButton clearBtn = createClearFiltersButton();
        filterPanel.add(clearBtn, gbc);

        return filterPanel;
    }

    private JPanel createSearchField() {
        JPanel container = new JPanel(new BorderLayout(10, 0));
        container.setOpaque(false);

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));

        searchField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(248, 250, 252));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                super.paintComponent(g);
            }
        };
        searchField.setOpaque(false);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(new EmptyBorder(12, 15, 12, 15));
        searchField.setForeground(TEXT_PRIMARY);
        
        // Placeholder text
        searchField.setText("Search by name or student ID...");
        searchField.setForeground(TEXT_SECONDARY);
        
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search by name or student ID...")) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_PRIMARY);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search by name or student ID...");
                    searchField.setForeground(TEXT_SECONDARY);
                }
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { applyFilters(); }
            @Override
            public void removeUpdate(DocumentEvent e) { applyFilters(); }
            @Override
            public void changedUpdate(DocumentEvent e) { applyFilters(); }
        });

        JPanel fieldContainer = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(248, 250, 252));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2d.setColor(new Color(226, 232, 240));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
        };
        fieldContainer.setOpaque(false);
        fieldContainer.setBorder(new EmptyBorder(0, 15, 0, 10));
        fieldContainer.add(searchIcon, BorderLayout.WEST);
        fieldContainer.add(searchField, BorderLayout.CENTER);

        container.add(fieldContainer, BorderLayout.CENTER);
        return container;
    }

    private JPanel createFilterCombo(String label, List<String> options, boolean isClass) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);

        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(TEXT_SECONDARY);

        JComboBox<String> combo = new JComboBox<>(options.toArray(new String[0]));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(new Color(248, 250, 252));
        combo.setBorder(new EmptyBorder(8, 10, 8, 10));

        combo.addActionListener(e -> applyFilters());

        // Store reference for filtering
        if (isClass) {
            classCombo = combo;
        } else if (label.equals("Major")) {
            majorCombo = combo;
        } else {
            departmentCombo = combo;
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);

        return panel;
    }

    private JButton createClearFiltersButton() {
        JButton button = new JButton("Clear Filters") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(220, 38, 38));
                } else if (getModel().isRollover()) {
                    g2d.setColor(ACCENT_RED);
                } else {
                    g2d.setColor(new Color(254, 226, 226));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                if (getModel().isRollover() || getModel().isPressed()) {
                    g2d.setColor(Color.WHITE);
                } else {
                    g2d.setColor(ACCENT_RED);
                }
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> clearFilters());

        return button;
    }

    private void clearFilters() {
        searchField.setText("Search by name or student ID...");
        searchField.setForeground(TEXT_SECONDARY);
        classCombo.setSelectedIndex(0);
        majorCombo.setSelectedIndex(0);
        departmentCombo.setSelectedIndex(0);
        applyFilters();
    }

    private void applyFilters() {
        String searchText = searchField.getText();
        if (searchText.equals("Search by name or student ID...")) {
            searchText = "";
        }
        searchText = searchText.toLowerCase();

        String selectedClass = (String) classCombo.getSelectedItem();
        String selectedMajor = (String) majorCombo.getSelectedItem();
        String selectedDept = (String) departmentCombo.getSelectedItem();

        final String finalSearchText = searchText;
        
        List<StudentInfo> filteredStudents = allStudents.stream()
            .filter(s -> {
                // Search filter
                if (!finalSearchText.isEmpty()) {
                    boolean matchesSearch = s.name.toLowerCase().contains(finalSearchText) ||
                                          s.studentId.toLowerCase().contains(finalSearchText);
                    if (!matchesSearch) return false;
                }
                
                // Class filter
                if (!selectedClass.equals("All Classes") && !s.className.equals(selectedClass)) {
                    return false;
                }
                
                // Major filter
                if (!selectedMajor.equals("All Majors") && !s.major.equals(selectedMajor)) {
                    return false;
                }
                
                // Department filter
                if (!selectedDept.equals("All Departments") && !s.department.equals(selectedDept)) {
                    return false;
                }
                
                return true;
            })
            .collect(Collectors.toList());

        updateStudentsList(filteredStudents);
    }

    private JPanel createStudentsContainer() {
        JPanel container = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 2, 15, 15);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header row
        JPanel headerRow = createTableHeader();
        container.add(headerRow, BorderLayout.NORTH);

        // Students list
        studentsPanel = new JPanel();
        studentsPanel.setLayout(new BoxLayout(studentsPanel, BoxLayout.Y_AXIS));
        studentsPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(studentsPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        container.add(scrollPane, BorderLayout.CENTER);

        // Initial load
        updateStudentsList(allStudents);

        return container;
    }

    private JPanel createTableHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(10, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);

        String[] columns = {"Student", "ID", "Major", "Department", "Class", "Actions"};
        double[] weights = {0.2, 0.1, 0.2, 0.15, 0.25, 0.1};

        for (int i = 0; i < columns.length; i++) {
            gbc.gridx = i;
            gbc.weightx = weights[i];
            
            JLabel label = new JLabel(columns[i]);
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            label.setForeground(TEXT_SECONDARY);
            header.add(label, gbc);
        }

        return header;
    }

    private void updateStudentsList(List<StudentInfo> students) {
        studentsPanel.removeAll();
        studentsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        if (students.isEmpty()) {
            JLabel emptyLabel = new JLabel("No students found matching the criteria");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyLabel.setForeground(TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            studentsPanel.add(Box.createVerticalGlue());
            studentsPanel.add(emptyLabel);
            studentsPanel.add(Box.createVerticalGlue());
        } else {
            for (StudentInfo student : students) {
                studentsPanel.add(createStudentRow(student));
                studentsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        studentsPanel.revalidate();
        studentsPanel.repaint();
    }

    private JPanel createStudentRow(StudentInfo student) {
        JPanel row = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(248, 250, 252));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(12, 15, 12, 15));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.anchor = GridBagConstraints.WEST;

        double[] weights = {0.2, 0.1, 0.2, 0.15, 0.25, 0.1};

        // Student name with avatar
        gbc.gridx = 0;
        gbc.weightx = weights[0];
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        namePanel.setOpaque(false);
        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        JLabel nameLabel = new JLabel(student.name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(TEXT_PRIMARY);
        namePanel.add(avatarLabel);
        namePanel.add(nameLabel);
        row.add(namePanel, gbc);

        // Student ID
        gbc.gridx = 1;
        gbc.weightx = weights[1];
        JLabel idLabel = new JLabel(student.studentId);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setForeground(TEXT_SECONDARY);
        row.add(idLabel, gbc);

        // Major
        gbc.gridx = 2;
        gbc.weightx = weights[2];
        JLabel majorLabel = createBadge(student.major, ACCENT_BLUE);
        row.add(majorLabel, gbc);

        // Department
        gbc.gridx = 3;
        gbc.weightx = weights[3];
        JLabel deptLabel = new JLabel(student.department);
        deptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        deptLabel.setForeground(TEXT_SECONDARY);
        row.add(deptLabel, gbc);

        // Class
        gbc.gridx = 4;
        gbc.weightx = weights[4];
        JLabel classLabel = new JLabel(student.className);
        classLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        classLabel.setForeground(TEXT_PRIMARY);
        row.add(classLabel, gbc);

        // Actions
        gbc.gridx = 5;
        gbc.weightx = weights[5];
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        actionsPanel.setOpaque(false);
        
        JButton viewBtn = createIconButton("👁", "View Details", ACCENT_BLUE);
        JButton editBtn = createIconButton("✏", "Edit", ACCENT_ORANGE);
        
        viewBtn.addActionListener(e -> showStudentDetails(student));
        editBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Edit student: " + student.name, "Edit", JOptionPane.INFORMATION_MESSAGE));
        
        actionsPanel.add(viewBtn);
        actionsPanel.add(editBtn);
        row.add(actionsPanel, gbc);

        return row;
    }

    private JLabel createBadge(String text, Color color) {
        JLabel badge = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        badge.setForeground(color);
        badge.setBorder(new EmptyBorder(4, 8, 4, 8));
        return badge;
    }

    private JButton createIconButton(String icon, String tooltip, Color color) {
        JButton button = new JButton(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isRollover()) {
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                } else {
                    g2d.setColor(new Color(248, 250, 252));
                }
                g2d.fillOval(0, 0, getWidth(), getHeight());

                g2d.setColor(color);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };

        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(30, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);

        return button;
    }

    private void showStudentDetails(StudentInfo student) {
        JDialog dialog = new JDialog(this, "Student Details", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        contentPanel.setBackground(Color.WHITE);

        // Avatar
        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Name
        JLabel nameLabel = new JLabel(student.name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setForeground(TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(avatarLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Details
        addDetailRow(contentPanel, "Student ID", student.studentId);
        addDetailRow(contentPanel, "Major", student.major);
        addDetailRow(contentPanel, "Department", student.department);
        addDetailRow(contentPanel, "Current Class", student.className);

        // Close button
        JButton closeBtn = new JButton("Close");
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeBtn.addActionListener(e -> dialog.dispose());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(closeBtn);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelComp = new JLabel(label + ":");
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelComp.setForeground(TEXT_SECONDARY);
        labelComp.setPreferredSize(new Dimension(120, 25));

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        valueComp.setForeground(TEXT_PRIMARY);

        row.add(labelComp, BorderLayout.WEST);
        row.add(valueComp, BorderLayout.CENTER);

        panel.add(row);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
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

        button.addActionListener(e -> {
            mainpageTeacher teacherPage = new mainpageTeacher();
            teacherPage.setVisible(true);
            dispose();
        });

        return button;
    }

    // Inner class for student data
    private static class StudentInfo {
        String name;
        String studentId;
        String major;
        String department;
        String className;

        StudentInfo(String name, String studentId, String major, String department, String className) {
            this.name = name;
            this.studentId = studentId;
            this.major = major;
            this.department = department;
            this.className = className;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageStudentsPage().setVisible(true));
    }
}
