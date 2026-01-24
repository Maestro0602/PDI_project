package Frontend;

import Backend.src.database.Genreport;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import Backend.src.report.Report;

public class GenerateStudentReportGUI extends JFrame {

    private JTextField studentIDField;
    private JPanel reportPanel;
    private JScrollPane reportScrollPane;

    private Color primaryColor = new Color(41, 128, 185);
    private Color accentColor = new Color(46, 204, 113);
    private Color dangerColor = new Color(231, 76, 60);
    private Color universityBlue = new Color(0, 51, 102);
    private Color universityGold = new Color(218, 165, 32);

    public GenerateStudentReportGUI() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Report System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(true);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Top panel for search
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel for report display
        reportPanel = new JPanel();
        reportPanel.setLayout(new BorderLayout());
        reportPanel.setBackground(Color.WHITE);

        // Initial message
        JLabel welcomeLabel = new JLabel("Enter Student ID to generate report");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcomeLabel.setForeground(new Color(127, 140, 141));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        reportPanel.add(welcomeLabel, BorderLayout.CENTER);

        reportScrollPane = new JScrollPane(reportPanel);
        reportScrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(reportScrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, universityBlue, getWidth(), 0, new Color(0, 76, 153));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(1000, 120));

        // Title
        JLabel titleLabel = new JLabel("STUDENT REPORT SYSTEM");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(universityGold);
        titleLabel.setBounds(0, 15, 1000, 35);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel);

        // Search label
        JLabel searchLabel = new JLabel("Student ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setBounds(280, 65, 100, 30);
        panel.add(searchLabel);

        // Student ID field
        studentIDField = new JTextField();
        studentIDField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentIDField.setBounds(380, 65, 200, 35);
        studentIDField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        panel.add(studentIDField);

        // Generate button
        JButton generateBtn = createStyledButton("Generate Report", accentColor, new Color(39, 174, 96));
        generateBtn.setBounds(590, 65, 150, 35);
        generateBtn.addActionListener(e -> generateReport());
        panel.add(generateBtn);

        // Back button
        JButton backBtn = createStyledButton("← Back", dangerColor, new Color(192, 57, 43));
        backBtn.setBounds(850, 15, 120, 35);
        backBtn.addActionListener(e -> handleBack());
        panel.add(backBtn);

        // Enter key listener
        studentIDField.addActionListener(e -> generateReport());

        return panel;
    }

    private void generateReport() {
        String studentID = studentIDField.getText().trim();

        if (studentID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (studentID.equals("0")) {
            handleBack();
            return;
        }

        Report report = Genreport.getInformationByStudentID(studentID);

        if (report == null) {
            JOptionPane.showMessageDialog(this,
                    "No report found for Student ID: " + studentID,
                    "Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        displayReport(report);
    }

    private void displayReport(Report report) {
        // Clear previous report
        reportPanel.removeAll();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
        reportPanel.setBackground(Color.WHITE);

        // Add spacing
        reportPanel.add(Box.createVerticalStrut(30));

        // University Header
        JPanel headerPanel = createUniversityHeader();
        reportPanel.add(headerPanel);

        reportPanel.add(Box.createVerticalStrut(20));

        // Student Profile
        JPanel profilePanel = createProfilePanel(report);
        reportPanel.add(profilePanel);

        reportPanel.add(Box.createVerticalStrut(20));

        // Course Results Table
        JPanel coursePanel = createCoursePanel(report);
        reportPanel.add(coursePanel);

        reportPanel.add(Box.createVerticalStrut(20));

        // GPA Panel
        JPanel gpaPanel = createGPAPanel(report);
        reportPanel.add(gpaPanel);

        reportPanel.add(Box.createVerticalStrut(30));

        // Refresh display
        reportPanel.revalidate();
        reportPanel.repaint();
        reportScrollPane.getVerticalScrollBar().setValue(0);
    }

    private JPanel createUniversityHeader() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(900, 120));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(universityBlue, 3),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        JLabel universityName = new JLabel("Angkor UNIVERSITY OF PHNOM PENH");
        universityName.setFont(new Font("Serif", Font.BOLD, 24));
        universityName.setForeground(universityBlue);
        universityName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel reportTitle = new JLabel("ACADEMIC TRANSCRIPT");
        reportTitle.setFont(new Font("Serif", Font.BOLD, 20));
        reportTitle.setForeground(universityGold);
        reportTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        JLabel dateLabel = new JLabel("Printed Date " + dateFormat.format(new Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        dateLabel.setForeground(new Color(127, 140, 141));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(universityName);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(reportTitle);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(dateLabel);

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfilePanel(Report report) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 20, 10));
        panel.setMaximumSize(new Dimension(900, 160));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                        "STUDENT PROFILE",
                        0, 0,
                        new Font("Segoe UI", Font.BOLD, 16),
                        universityBlue),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        panel.add(createInfoLabel("Student ID:", report.getStudentID()));
        panel.add(createInfoLabel("Name:", report.getStudentName()));
        panel.add(createInfoLabel("Gender:", report.getGender()));
        panel.add(createInfoLabel("Year:", report.getYear()));
        panel.add(createInfoLabel("Department:", report.getDepartment()));
        panel.add(createInfoLabel("Major:", report.getMajor()));

        return panel;
    }

    private JPanel createInfoLabel(String label, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(new Color(236, 240, 241));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelComp.setForeground(new Color(52, 73, 94));

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        valueComp.setForeground(new Color(44, 62, 80));

        panel.add(labelComp);
        panel.add(valueComp);

        return panel;
    }

    private JPanel createCoursePanel(Report report) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(900, 400));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                        "COURSE RESULTS",
                        0, 0,
                        new Font("Segoe UI", Font.BOLD, 16),
                        universityBlue),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Create table
        String[] columns = { "Course ID", "Course Name", "Score", "Grade" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add data
        for (int i = 0; i < report.getCourseIDs().size(); i++) {
            Object[] row = {
                    report.getCourseIDs().get(i),
                    report.getCourseNames().get(i),
                    String.format("%.2f", report.getScores().get(i)),
                    report.getGrades().get(i)
            };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setGridColor(new Color(189, 195, 199));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(universityBlue);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Center align score and grade columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(400);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGPAPanel(Report report) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(900, 120));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(universityGold, 3),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)));

        // Calculate GPA
        double totalScore = 0.0;
        for (double score : report.getScores()) {
            totalScore += score;
        }
        double gpa = (totalScore / 400.0) * 4.0;
        if (gpa > 4.0) {
            gpa = 4.0;
        }

        JLabel gpaLabel = new JLabel(String.format("CUMULATIVE GPA: %.2f / 4.00", gpa));
        gpaLabel.setFont(new Font("Serif", Font.BOLD, 26));
        gpaLabel.setForeground(universityBlue);
        gpaLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add classification
        String classification = "";
        Color classColor = universityBlue;
        if (gpa >= 3.7) {
            classification = "Valedictorian";
            classColor = new Color(46, 204, 113);
        } else if (gpa >= 3.3) {
            classification = "Great Job! Try more";
            classColor = new Color(52, 152, 219);
        } else if (gpa >= 3.0) {
            classification = "Good Effort! Keep Improving";
            classColor = new Color(52, 152, 219);
        }

        JLabel classLabel = new JLabel(classification);
        classLabel.setFont(new Font("Serif", Font.ITALIC, 18));
        classLabel.setForeground(classColor);
        classLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(236, 240, 241));

        gpaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(gpaLabel);
        if (!classification.isEmpty()) {
            centerPanel.add(Box.createVerticalStrut(5));
            centerPanel.add(classLabel);
        }

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
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

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void handleBack() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
            MainPageTeacherGUI.main(null);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            GenerateStudentReportGUI frame = new GenerateStudentReportGUI();
            frame.setVisible(true);
        });
    }
}
