package Frontend;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
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
        setResizable(true);
        
        // Set icon
        try {
            ImageIcon icon = new ImageIcon("src/Calculator/360_F_557111711_9aQFvZG2aM5o8G6jWr5BvN0FyzPlC3Cf.jpg");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Icon image not found: " + e.getMessage());
        }
        
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
        
        // Content panel with scroll for cards
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 30, 20));

        JPanel cardsContainer = createCardsContainer();
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        
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
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        container.setOpaque(false);

        // Person 1
        container.add(createProfileCard(
            "Hen Chhordavattey",
            "Software Developer",
            "hen.chhordavattey@gmail.com",
            "6767",
            "ITC",
            ACCENT_PURPLE,
            new String[]{"Java", "Python", "Design"},
            "D:/java project/Pulll/Frontend/vattey.jpg"  // Use forward slashes
        ));

        // Person 2
        container.add(createProfileCard(
            "Theng Van Heng",
            "UI/UX Designer",
            "thengvanheng@gmail.com",
            "6767",
            "ITC",
            ACCENT_BLUE,
            new String[]{"Figma", "C++", "Design"},
            "D:/java project/Pulll/Frontend/heng.jpg"
        ));

        // Person 3
        container.add(createProfileCard(
            "Pi sereyvathanak",
            "Project Manager",
            "pi.sereyvathanak@gmail.com",
            "6767",
            "ITC",
            ACCENT_TEAL,
            new String[]{"Leadership", "coding", "Strategy"},
            "D:/java project/Pulll/Frontend/nak.jpg"
        ));

        // Person 4
        container.add(createProfileCard(
            "Chu, kimchun",
            "Data Analyst",
            "chu.kimchun@gmail.com",
            "6767",
            "ITC    ",
            ACCENT_ORANGE,
            new String[]{"Python", "SQL", "Analytics"},
            "D:/java project/Pulll/Frontend/aura.jpg"
        ));

        return container;
    }
    
    private JPanel createProfileCard(String name, String role, String email, 
                                     String phone, String location, 
                                     Color accentColor, String[] skills, String imagePath) {
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
        card.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(300, 480));

        // Profile Photo Container
        JPanel photoContainer = new JPanel() {
            private BufferedImage profileImage = null;
            
            {
                // Load image in constructor block
                try {
                    File imageFile = new File(imagePath);
                    System.out.println("Attempting to load image from: " + imageFile.getAbsolutePath());
                    System.out.println("File exists: " + imageFile.exists());
                    
                    if (imageFile.exists()) {
                        profileImage = ImageIO.read(imageFile);
                        if (profileImage != null) {
                            System.out.println("✓ Image loaded successfully: " + imagePath);
                            System.out.println("  Image size: " + profileImage.getWidth() + "x" + profileImage.getHeight());
                        } else {
                            System.out.println("✗ Failed to read image (null): " + imagePath);
                        }
                    } else {
                        System.out.println("✗ File not found: " + imagePath);
                    }
                } catch (Exception e) {
                    System.out.println("✗ Error loading image: " + imagePath);
                    e.printStackTrace();
                }
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                
                // Outer colored ring
                g2d.setColor(accentColor);
                g2d.fillOval(0, 0, getWidth(), getHeight());
                
                // Inner white circle
                g2d.setColor(Color.WHITE);
                g2d.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
                
                // Photo circle background (light gray placeholder)
                g2d.setColor(new Color(241, 245, 249));
                g2d.fillOval(8, 8, getWidth() - 16, getHeight() - 16);
                
                // Display profile photo if loaded
                if (profileImage != null) {
                    try {
                        // Create circular clip
                        g2d.setClip(new java.awt.geom.Ellipse2D.Float(8, 8, getWidth() - 16, getHeight() - 16));
                        
                        // Draw the image scaled to fit
                        g2d.drawImage(profileImage, 8, 8, getWidth() - 16, getHeight() - 16, null);
                    } catch (Exception e) {
                        System.out.println("Error drawing image: " + e.getMessage());
                        drawPlaceholder(g2d);
                    }
                } else {
                    // Fallback to placeholder icon if image not loaded
                    drawPlaceholder(g2d);
                }
            }
            
            private void drawPlaceholder(Graphics2D g2d) {
                g2d.setClip(null); // Reset clip
                g2d.setColor(TEXT_SECONDARY);
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
                String emoji = "👤";
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(emoji)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(emoji, x, y);
            }
        };
        photoContainer.setOpaque(false);
        photoContainer.setPreferredSize(new Dimension(120, 120));
        photoContainer.setMaximumSize(new Dimension(120, 120));
        photoContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(photoContainer);
        card.add(Box.createRigidArea(new Dimension(0, 18)));

        // Name
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Role
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roleLabel.setForeground(TEXT_SECONDARY);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(roleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 18)));

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
        divider.setPreferredSize(new Dimension(260, 1));
        divider.setMaximumSize(new Dimension(260, 1));
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(divider);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Contact Info
        card.add(createCompactInfoRow("📧", email));
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(createCompactInfoRow("📱", phone));
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(createCompactInfoRow("📍", location));
        card.add(Box.createRigidArea(new Dimension(0, 18)));

        // Skills tags
        JPanel skillsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        skillsPanel.setOpaque(false);
        skillsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        skillsPanel.setMaximumSize(new Dimension(260, 100));
        
        for (String skill : skills) {
            skillsPanel.add(createSmallSkillTag(skill, accentColor));
        }
        
        card.add(skillsPanel);

        return card;
    }
    
    private JPanel createCompactInfoRow(String emoji, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setMaximumSize(new Dimension(260, 25));

        JLabel iconLabel = new JLabel(emoji);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
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
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), 
                                     accentColor.getBlue(), 80));
                g2d.setStroke(new BasicStroke(1.2f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        tag.setOpaque(false);
        tag.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JLabel label = new JLabel(skill);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
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