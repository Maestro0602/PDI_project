package Frontend.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AttendancePage extends JFrame {

    // Colors matching the teacher mainpage theme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_YELLOW = new Color(255, 255, 0);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    // private static final Color ACCENT_BLUE = new Color(59, 130, 246); // for later use

    // Attendance status tracking
    private Map<String, String> attendanceStatus = new HashMap<>();
    private List<String> students = new ArrayList<>();

    public AttendancePage() {
        // testing
        students.add("Nak");
        students.add("vattey");
        students.add("kimchun");
        students.add("both");

        initComponent();
    }

    public void initComponent() {
        setTitle("Attendance Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
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

        // Content panel with student list
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(10, 30, 30, 30));

        JPanel attendanceContainer = createAttendanceContainer();
        contentPanel.add(attendanceContainer, BorderLayout.CENTER);

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

                GradientPaint greenGradient = new GradientPaint(
                    0, 0, new Color(34, 197, 94, 200),
                    0, getHeight(), new Color(74, 222, 128, 150)
                );
                g2d.setPaint(greenGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                g2d.setColor(new Color(34, 197, 94, 8));
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
                    0, 0, ACCENT_GREEN,
                    getWidth(), getHeight(), new Color(22, 163, 74)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("📋");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Attendance Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Mark student attendance for today");
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

    private JPanel createAttendanceContainer() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);

        // Legend panel
        JPanel legendPanel = createLegendPanel();
        mainContainer.add(legendPanel, BorderLayout.NORTH);

        // Students list with scroll
        JPanel studentsPanel = new JPanel();
        studentsPanel.setLayout(new BoxLayout(studentsPanel, BoxLayout.Y_AXIS));
        studentsPanel.setOpaque(false);
        studentsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        for (String student : students) {
            studentsPanel.add(createStudentRow(student));
            studentsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(studentsPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainContainer.add(scrollPane, BorderLayout.CENTER);

        return mainContainer;
    }

    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel() {
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
        legendPanel.setOpaque(false);
        legendPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 15));
        legendPanel.setBorder(new EmptyBorder(5, 20, 5, 20));

        legendPanel.add(createLegendItem("Present", ACCENT_GREEN));
        legendPanel.add(createLegendItem("Absent", ACCENT_RED));
        legendPanel.add(createLegendItem("Permission", ACCENT_ORANGE));
        legendPanel.add(createLegendItem("Dropout", ACCENT_YELLOW));

        return legendPanel;
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        item.setOpaque(false);

        JPanel colorBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
            }
        };
        colorBox.setOpaque(false);
        colorBox.setPreferredSize(new Dimension(20, 20));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_SECONDARY);

        item.add(colorBox);
        item.add(label);

        return item;
    }

    private JPanel createStudentRow(String studentName) {
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
        row.setBorder(new EmptyBorder(15, 20, 15, 20));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        // Student name panel
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        namePanel.setOpaque(false);

        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JLabel nameLabel = new JLabel(studentName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(TEXT_PRIMARY);

        namePanel.add(avatarLabel);
        namePanel.add(nameLabel);

        // Attendance buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonsPanel.setOpaque(false);

        JToggleButton presentBtn = createAttendanceButton("Present", ACCENT_GREEN);
        JToggleButton absentBtn = createAttendanceButton("Absent", ACCENT_RED);
        JToggleButton permissionBtn = createAttendanceButton("Permission", ACCENT_ORANGE);
        JToggleButton dropoutBtn = createAttendanceButton("Dropout", ACCENT_YELLOW);

        ButtonGroup group = new ButtonGroup();
        group.add(presentBtn);
        group.add(absentBtn);
        group.add(permissionBtn);
        group.add(dropoutBtn);
        
        // Store button group for clearing later
        buttonGroups.add(group);

        // Add action listeners
        presentBtn.addActionListener(e -> attendanceStatus.put(studentName, "Present"));
        absentBtn.addActionListener(e -> attendanceStatus.put(studentName, "Absent"));
        permissionBtn.addActionListener(e -> attendanceStatus.put(studentName, "Permission"));
        dropoutBtn.addActionListener(e -> attendanceStatus.put(studentName, "Dropout"));

        buttonsPanel.add(presentBtn);
        buttonsPanel.add(absentBtn);
        buttonsPanel.add(permissionBtn);
        buttonsPanel.add(dropoutBtn);

        row.add(namePanel, BorderLayout.WEST);
        row.add(buttonsPanel, BorderLayout.EAST);

        return row;
    }

    private JToggleButton createAttendanceButton(String text, Color color) {
        JToggleButton button = new JToggleButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw circle instead of rounded rectangle
                int size = Math.min(getWidth(), getHeight());
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                if (isSelected()) {
                    g2d.setColor(color);
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
                } else {
                    g2d.setColor(new Color(241, 245, 249));
                }

                g2d.fillOval(x, y, size, size);

                if (isSelected()) {
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

        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(40, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(text);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { button.repaint(); }
        });

        return button;
    }

    private List<ButtonGroup> buttonGroups = new ArrayList<>();

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        bottomPanel.setOpaque(false);

        JButton saveButton = createActionButton("Save Attendance", ACCENT_GREEN, true);
        JButton clearButton = createActionButton("Clear All", ACCENT_RED, false);

        saveButton.addActionListener(e -> {
            int marked = attendanceStatus.size();
            int total = students.size();
            JOptionPane.showMessageDialog(this,
                "Attendance saved successfully!\n" +
                "Marked: " + marked + " out of " + total + " students",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        });

        clearButton.addActionListener(e -> {
            attendanceStatus.clear();
            // Clear all button group selections
            for (ButtonGroup group : buttonGroups) {
                group.clearSelection();
            }
            repaint();
        });

        bottomPanel.add(saveButton);
        bottomPanel.add(clearButton);

        return bottomPanel;
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
        button.setPreferredSize(new Dimension(180, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            @Override
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

        button.addActionListener(e -> {
            mainpageTeacher teacherPage = new mainpageTeacher();
            teacherPage.setVisible(true);
            dispose();
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { button.repaint(); }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AttendancePage().setVisible(true));
    }
}