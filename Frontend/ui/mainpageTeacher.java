package Frontend.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class mainpageTeacher extends JFrame {

    // Colors (matching login page theme)
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_RED = new Color(239, 68, 68);

    public mainpageTeacher() {
        initComponent();
    }

    public void initComponent() {
        setTitle("Teacher Dashboard - Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

                g2d.setColor(new Color(147, 197, 253, 30));
                g2d.fillOval(-50, -50, 200, 200);
                g2d.fillOval(getWidth() - 150, getHeight() - 150, 200, 200);
            }
        };

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Content panel with card
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

        JPanel cardPanel = createCardPanel();
        contentPanel.add(cardPanel);

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(backgroundPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel welcomeLabel = new JLabel("Teacher Dashboard");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Welcome back! Select an option to continue");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(welcomeLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);

        JButton logoutButton = createLogoutButton();

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createCardPanel() {
        JPanel cardPanel = new JPanel() {
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
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBorder(new EmptyBorder(50, 60, 50, 60));
        cardPanel.setPreferredSize(new Dimension(780, 520));
        cardPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Row 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        cardPanel.add(createOptionCard("Attendance", "Mark and manage student attendance", 
                                       ACCENT_GREEN, "📋"), gbc);

        gbc.gridx = 1;
        cardPanel.add(createOptionCard("Assign Grades", "Grade assignments and exams", 
                                       ACCENT_BLUE, "📝"), gbc);

        // Row 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        cardPanel.add(createOptionCard("View Students", "Browse student information", 
                                       ACCENT_ORANGE, "👥"), gbc);

        gbc.gridx = 1;
        cardPanel.add(createOptionCard("Manage Classes", "View and organize your classes", 
                                       ACCENT_PURPLE, "📚"), gbc);

        // Row 3
        gbc.gridx = 0;
        gbc.gridy = 2;
        cardPanel.add(createOptionCard("Reports", "Generate academic reports", 
                                       new Color(236, 72, 153), "📊"), gbc);

        gbc.gridx = 1;
        cardPanel.add(createOptionCard("Announcements", "Post class announcements", 
                                       ACCENT_RED, "📢"), gbc);

        return cardPanel;
    }

    private static final Color ACCENT_BLUE = new Color(59, 130, 246);

    private JPanel createOptionCard(String title, String description, Color accentColor, String icon) {
        JPanel card = new JPanel() {
            private boolean isHovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isHovered) {
                    g2d.setColor(new Color(0, 0, 0, 30));
                    g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 16, 16);
                }

                g2d.setColor(new Color(248, 250, 252));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                g2d.setColor(accentColor);
                g2d.fillRoundRect(0, 0, getWidth(), 6, 16, 16);
            }
        };
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(new EmptyBorder(25, 20, 25, 20));
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon label
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));

        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(descLabel);

        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setOpaque(false);
        contentPanel.add(iconLabel, BorderLayout.WEST);
        contentPanel.add(textPanel, BorderLayout.CENTER);

        card.add(contentPanel, BorderLayout.CENTER);

        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.repaint();
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(card, "Opening " + title + "...", 
                                            "Navigation", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return card;
    }

    private JButton createLogoutButton() {
        JButton button = new JButton("Logout") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(220, 38, 38));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(239, 68, 68));
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
        button.setPreferredSize(new Dimension(100, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent evt) { button.repaint(); }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new mainpageTeacher().setVisible(true));
    }
}