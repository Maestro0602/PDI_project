package Frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AboutMePage extends JFrame {
    
    // Colors (matching modern theme)
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_PINK = new Color(236, 72, 153);
    private static final Color ACCENT_TEAL = new Color(20, 184, 166);
    private static final Color ACCENT_YELLOW = new Color(234, 179, 8);
    
    public AboutMePage() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("About Us - Team Members");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
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
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 30, 20));

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
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // Icon and title container
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        // Team icon badge
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

        JLabel iconLabel = new JLabel("👥");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Meet Our Team");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Get to know the people behind the project");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(welcomeLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(subtitleLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        // Back button
        JButton backButton = createBackButton();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }
    
    private JPanel createCardsContainer() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridy = 0; // Single row

        // Person 1
        gbc.gridx = 0;
        container.add(createProfileCard(
            "Person 1 Name",
            "Software Developer",
            "person1@example.com",
            "+1 234 567 890",
            "New York, USA",
            ACCENT_PURPLE,
            new String[]{"Java", "Python", "Design"}
        ), gbc);

        // Person 2
        gbc.gridx = 1;
        container.add(createProfileCard(
            "Person 2 Name",
            "UI/UX Designer",
            "person2@example.com",
            "+1 234 567 891",
            "Los Angeles, USA",
            ACCENT_BLUE,
            new String[]{"Figma", "Adobe XD", "Design"}
        ), gbc);

        // Person 3
        gbc.gridx = 2;
        container.add(createProfileCard(
            "Person 3 Name",
            "Project Manager",
            "person3@example.com",
            "+1 234 567 892",
            "Chicago, USA",
            ACCENT_TEAL,
            new String[]{"Leadership", "Agile", "Strategy"}
        ), gbc);

        // Person 4
        gbc.gridx = 3;
        container.add(createProfileCard(
            "Person 4 Name",
            "Data Analyst",
            "person4@example.com",
            "+1 234 567 893",
            "Boston, USA",
            ACCENT_ORANGE,
            new String[]{"Python", "SQL", "Analytics"}
        ), gbc);

        return container;
    }
    
    private JPanel createProfileCard(String name, String role, String email, 
                                     String phone, String location, 
                                     Color accentColor, String[] skills) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 20, 20);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Gradient border
                g2d.setStroke(new BasicStroke(3f));
                GradientPaint gradient = new GradientPaint(
                    0, 0, accentColor,
                    getWidth(), getHeight(), new Color(
                        Math.min(accentColor.getRed() + 50, 255),
                        Math.min(accentColor.getGreen() + 50, 255),
                        Math.min(accentColor.getBlue() + 50, 255)
                    )
                );
                g2d.setPaint(gradient);
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);

                // Top accent
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), 
                                     accentColor.getBlue(), 40));
                g2d.fillRoundRect(0, 0, getWidth(), 60, 20, 20);
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(25, 18, 25, 18));
        card.setOpaque(false);

        // Profile Photo Container
        JPanel photoContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Outer colored ring
                g2d.setColor(accentColor);
                g2d.fillOval(0, 0, getWidth(), getHeight());
                
                // Inner white circle
                g2d.setColor(Color.WHITE);
                g2d.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
                
                // Photo circle background (light gray placeholder)
                g2d.setColor(new Color(241, 245, 249));
                g2d.fillOval(8, 8, getWidth() - 16, getHeight() - 16);
                
                // ========================================
                // TODO: ADD PROFILE PHOTO HERE FOR: name
                // ========================================
                // Option 1: Load image from file
                // ImageIcon profileImg = new ImageIcon("path/to/" + name.replace(" ", "_") + ".jpg");
                // Image scaledImg = profileImg.getImage().getScaledInstance(getWidth() - 16, getHeight() - 16, Image.SCALE_SMOOTH);
                // g2d.setClip(new Ellipse2D.Float(8, 8, getWidth() - 16, getHeight() - 16));
                // g2d.drawImage(scaledImg, 8, 8, null);
                //
                // Option 2: Use ImageIO for better control
                // try {
                //     BufferedImage img = ImageIO.read(new File("images/" + name.replace(" ", "_") + ".jpg"));
                //     g2d.setClip(new Ellipse2D.Float(8, 8, getWidth() - 16, getHeight() - 16));
                //     g2d.drawImage(img, 8, 8, getWidth() - 16, getHeight() - 16, null);
                // } catch (IOException e) { e.printStackTrace(); }
                // ========================================
                
                // Placeholder icon (remove this when you add photos)
                g2d.setColor(TEXT_SECONDARY);
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
                String emoji = "👤";
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(emoji)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(emoji, x, y);
            }
        };
        photoContainer.setOpaque(false);
        photoContainer.setPreferredSize(new Dimension(100, 100));
        photoContainer.setMaximumSize(new Dimension(100, 100));
        photoContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(photoContainer);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Name
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Role
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(TEXT_SECONDARY);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(roleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Divider line
        JPanel divider = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(226, 232, 240));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        divider.setOpaque(false);
        divider.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(divider);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        // Contact Info
        card.add(createCompactInfoRow("📧", email));
        card.add(Box.createRigidArea(new Dimension(0, 7)));
        card.add(createCompactInfoRow("📱", phone));
        card.add(Box.createRigidArea(new Dimension(0, 7)));
        card.add(createCompactInfoRow("📍", location));
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        // Skills tags
        JPanel skillsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
        skillsPanel.setOpaque(false);
        skillsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        for (String skill : skills) {
            skillsPanel.add(createSmallSkillTag(skill, accentColor));
        }
        
        card.add(skillsPanel);

        return card;
    }
    
    private JPanel createCompactInfoRow(String emoji, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));

        JLabel iconLabel = new JLabel(emoji);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        textLabel.setForeground(TEXT_SECONDARY);

        row.add(iconLabel);
        row.add(textLabel);

        return row;
    }
    
    private JPanel createSmallSkillTag(String skill, Color accentColor) {
        JPanel tag = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), 
                                     accentColor.getBlue(), 25));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), 
                                     accentColor.getBlue(), 80));
                g2d.setStroke(new BasicStroke(1.0f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        tag.setOpaque(false);
        tag.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 3));

        JLabel label = new JLabel(skill);
        label.setFont(new Font("Segoe UI", Font.BOLD, 9));
        label.setForeground(accentColor);

        tag.add(label);
        return tag;
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
        button.setPreferredSize(new Dimension(100, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            this.dispose();
            // Navigate back to previous page
            // MainpageTeacher.main(null);
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(MouseEvent evt) { button.repaint(); }
        });

        return button;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            AboutMePage frame = new AboutMePage();
            frame.setVisible(true);
            ImageIcon image = new ImageIcon("src/Calculator/360_F_557111711_9aQFvZG2aM5o8G6jWr5BvN0FyzPlC3Cf.jpg");
            frame.setIconImage(image.getImage());
        });
    }
}