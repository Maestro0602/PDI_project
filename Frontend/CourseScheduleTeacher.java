package Frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CourseScheduleTeacher extends JFrame {
    
    // Modern color scheme matching MainpageTeacher
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    
    public CourseScheduleTeacher() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Weekly Course Schedule");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
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

                // Decorative circles
                g2d.setColor(new Color(147, 197, 253, 25));
                g2d.fillOval(-80, -80, 240, 240);
                g2d.fillOval(getWidth() - 160, getHeight() - 160, 240, 240);
            }
        };
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Schedule content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 30, 30, 30));
        
        JPanel scheduleContainer = createScheduleContainer();
        JScrollPane scrollPane = new JScrollPane(scheduleContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
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

                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);

                // Main card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);

                // Blue gradient accent on the left
                GradientPaint blueGradient = new GradientPaint(
                    0, 0, new Color(59, 130, 246, 200),
                    0, getHeight(), new Color(147, 197, 253, 150)
                );
                g2d.setPaint(blueGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                // Subtle blue background pattern
                g2d.setColor(new Color(59, 130, 246, 8));
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // Icon and title container
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        // Calendar icon badge
        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(59, 130, 246),
                    getWidth(), getHeight(), new Color(37, 99, 235)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("📅");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Weekly Course Schedule");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Your academic timetable for the week • 4 Subjects • 13 Hours Total");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(welcomeLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(subtitleLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        // Back button
        JButton backButton = createBackButton();

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        return headerPanel;
    }
    
    private JButton createBackButton() {
        JButton button = new JButton("← Back") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(37, 99, 235));
                } else if (getModel().isRollover()) {
                    g2d.setColor(ACCENT_BLUE);
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
        button.setPreferredSize(new Dimension(120, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                MainpageTeacher mainFrame = new MainpageTeacher ();
                mainFrame.setVisible(true);
            });
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(MouseEvent evt) { button.repaint(); }
        });

        return button;
    }
    
    private JPanel createScheduleContainer() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Monday - 4 hours
        container.add(createDayCard("MONDAY", "📘", ACCENT_BLUE, new String[][]{
            {"Linear Algebra", "9:00 AM - 11:00 AM", "Room 402", "2 hours"},
            {"Project and seminar", "1:00 PM - 3:00 PM", "Room 402", "2 hours"}
        }));
        
        container.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Tuesday - 2 hours
        container.add(createDayCard("TUESDAY", "💻", ACCENT_PURPLE, new String[][]{
            {"Computer Science Lab", "2:00 PM - 4:00 PM", "Room J420", "2 hours"}
        }));
        
        container.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Wednesday - 4 hours
        container.add(createDayCard("WEDNESDAY", "🔬", ACCENT_GREEN, new String[][]{
            {"Programming with C++", "10:00 AM - 12:00 PM", "Room A420", "2 hours"},
            {"Programming with java", "2:00 PM - 4:00 PM", "Room A20", "2 hours"}
        }));
        
        container.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Thursday - 1 hour
        container.add(createDayCard("THURSDAY", "📜", ACCENT_ORANGE, new String[][]{
            {"programming with java", "11:00 AM - 12:00 PM", "Room A420", "1 hour"}
        }));
        
        container.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Friday - 2 hours
        container.add(createDayCard("FRIDAY", "🧬", ACCENT_RED, new String[][]{
            {"Computer Science Lab", "9:00 AM - 11:00 AM", "Room J602", "2 hours"}
        }));
        
        return container;
    }
    
    private JPanel createDayCard(String day, String emoji, Color accentColor, String[][] courses) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Accent border
                g2d.setStroke(new BasicStroke(3f));
                g2d.setColor(accentColor);
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
            }
        };
        
        card.setLayout(new BorderLayout(0, 15));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        
        // Day header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        headerPanel.setOpaque(false);
        
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JLabel dayLabel = new JLabel(day);
        dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        dayLabel.setForeground(TEXT_PRIMARY);
        
        headerPanel.add(emojiLabel);
        headerPanel.add(dayLabel);
        
        // Courses panel
        JPanel coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setOpaque(false);
        
        for (int i = 0; i < courses.length; i++) {
            coursesPanel.add(createCoursePanel(courses[i][0], courses[i][1], courses[i][2], courses[i][3], accentColor));
            if (i < courses.length - 1) {
                coursesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(coursesPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createCoursePanel(String course, String time, String room, String duration, Color accentColor) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background with accent tint
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), 
                                      accentColor.getBlue(), 15));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Left accent bar
                g2d.setColor(accentColor);
                g2d.fillRoundRect(0, 0, 5, getHeight(), 12, 12);
            }
        };
        
        panel.setLayout(new BorderLayout(12, 8));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        
        // Course name
        JLabel courseLabel = new JLabel(course);
        courseLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        courseLabel.setForeground(TEXT_PRIMARY);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        infoPanel.setOpaque(false);
        
        JLabel timeLabel = new JLabel("🕐 " + time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        timeLabel.setForeground(TEXT_SECONDARY);
        
        JLabel roomLabel = new JLabel("📍 " + room);
        roomLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roomLabel.setForeground(TEXT_SECONDARY);
        
        JLabel durationLabel = new JLabel("⏱ " + duration);
        durationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        durationLabel.setForeground(TEXT_SECONDARY);
        
        infoPanel.add(timeLabel);
        infoPanel.add(roomLabel);
        infoPanel.add(durationLabel);
        
        panel.add(courseLabel, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            CourseSchedule frame = new CourseSchedule();
            frame.setVisible(true);
        });
    }
}