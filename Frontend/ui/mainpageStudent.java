package Frontend.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class mainpageStudent extends JFrame {

    // Colors (matching login page theme)
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_PINK = new Color(236, 72, 153);

    public mainpageStudent() {
        initComponent();
    }

    public void initComponent() {
        setTitle("Teacher Mainpage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(880, 680);
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
        
        // Content panel with cards
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(5, 30, 30, 30));

        JPanel cardsContainer = createCardsContainer();
        contentPanel.add(cardsContainer);

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

                // Card background with shadow
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
        headerPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        // Icon and title container
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        // Teacher icon badge
        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
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

        JLabel iconLabel = new JLabel("🎓");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Student Mainpage");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Welcome to mainpage!");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(welcomeLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(subtitleLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        JButton logoutButton = createLogoutButton();

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createCardsContainer() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.setPreferredSize(new Dimension(800, 540));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        container.add(createOptionCard("Attendance", "Use attendance code", 
                                       ACCENT_GREEN, "📋", () -> {
            // Navigate to student attendance page
            JOptionPane.showMessageDialog(container, "Opening Attendance...");
        }), gbc);

        gbc.gridx = 1;
        container.add(createOptionCard("Grades", "Check exam grade", 
                                       ACCENT_BLUE, "📝", () -> {
            JOptionPane.showMessageDialog(container, "Opening Grades...");
        }), gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        container.add(createOptionCard("Schedule", "View calendar & track classes", 
                                       ACCENT_PURPLE, "📅", () -> {
            SchedulePage page = new SchedulePage(false);
            page.setVisible(true);
            setVisible(false);
        }), gbc);

        gbc.gridx = 1;
        container.add(createOptionCard("Announcements", "Check class announcements", 
                                       ACCENT_RED, "📢", () -> {
            JOptionPane.showMessageDialog(container, "Opening Announcements...");
        }), gbc);

        return container;
    }

    private JPanel createOptionCard(String title, String description, Color accentColor, String icon, Runnable onClick) {
        JPanel card = new JPanel() {
            private boolean isHovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Elevated shadow
                int shadowOffset = isHovered ? 8 : 4;
                int shadowAlpha = isHovered ? 40 : 20;
                g2d.setColor(new Color(0, 0, 0, shadowAlpha));
                g2d.fillRoundRect(shadowOffset/2, shadowOffset/2, getWidth() - shadowOffset/2, 
                                getHeight() - shadowOffset/2, 20, 20);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Accent border with gradient
                g2d.setStroke(new BasicStroke(3f));
                GradientPaint gradient = new GradientPaint(
                    0, 0, accentColor,
                    getWidth(), getHeight(), new Color(
                        Math.min(accentColor.getRed() + 30, 255),
                        Math.min(accentColor.getGreen() + 30, 255),
                        Math.min(accentColor.getBlue() + 30, 255)
                    )
                );
                g2d.setPaint(gradient);
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);

                // Top accent bar
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), 
                                      accentColor.getBlue(), 40));
                g2d.fillRoundRect(0, 0, getWidth(), 50, 20, 20);
            }
        };

        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(new EmptyBorder(20, 18, 20, 18));
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon with colored background
        JPanel iconContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), 
                                      accentColor.getBlue(), 25));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        iconContainer.setOpaque(false);
        iconContainer.setPreferredSize(new Dimension(50, 50));
        iconContainer.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconContainer.add(iconLabel);

        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(descLabel);

        card.add(iconContainer, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);

        // Hover effect with animation
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.repaint();
            }
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (onClick != null) onClick.run();
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
                    g2d.setColor(ACCENT_RED);
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
        
        button.addActionListener(e -> {
            loginpage login = new loginpage();
            login.setVisible(true);
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
        SwingUtilities.invokeLater(() -> new mainpageStudent().setVisible(true));
    }
}