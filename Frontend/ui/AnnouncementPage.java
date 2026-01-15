package Frontend.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnnouncementPage extends JFrame {

    // Colors matching the mainpage theme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);

    // Announcement data
    private List<Announcement> announcements = new ArrayList<>();
    private JPanel feedPanel;
    
    // Inner class for Announcement
    private static class Announcement {
        String author;
        String title;
        String content;
        Date timestamp;
        int upvotes;
        int comments;
        String category;
        
        Announcement(String author, String title, String content, String category) {
            this.author = author;
            this.title = title;
            this.content = content;
            this.timestamp = new Date();
            this.upvotes = 0;
            this.comments = 0;
            this.category = category;
        }
    }

    public AnnouncementPage() {
        // Sample data
        announcements.add(new Announcement("Dr. Smith", "Quiz Next Week", 
            "There will be a quiz on Chapter 5 next Wednesday. Please review the study materials posted on the course page.", "Academic"));
        announcements.add(new Announcement("Prof. Johnson", "Lab Session Rescheduled", 
            "Tomorrow's lab session has been moved to Friday at 2 PM. Please adjust your schedules accordingly.", "Schedule"));
        announcements.add(new Announcement("Admin", "Campus Event: Tech Fest", 
            "Join us for the annual Tech Fest on March 15th! Register now to showcase your projects and innovations.", "Event"));
        
        initComponent();
    }

    public void initComponent() {
        setTitle("Announcements Feed");
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

        // Header panel
        JPanel headerPanel = createHeaderPanel();

        // Main content area with sidebar and feed
        JPanel mainContentPanel = new JPanel(new BorderLayout(20, 0));
        mainContentPanel.setOpaque(false);
        mainContentPanel.setBorder(new EmptyBorder(10, 30, 30, 30));

        // Left sidebar
        JPanel sidebarPanel = createSidebarPanel();
        mainContentPanel.add(sidebarPanel, BorderLayout.WEST);

        // Center feed
        JPanel feedContainer = createFeedContainer();
        mainContentPanel.add(feedContainer, BorderLayout.CENTER);

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(mainContentPanel, BorderLayout.CENTER);

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

                GradientPaint redGradient = new GradientPaint(
                    0, 0, new Color(239, 68, 68, 200),
                    0, getHeight(), new Color(220, 38, 38, 150)
                );
                g2d.setPaint(redGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                g2d.setColor(new Color(239, 68, 68, 8));
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
                    0, 0, ACCENT_RED,
                    getWidth(), getHeight(), new Color(220, 38, 38)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("📢");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Announcements");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Stay updated with class announcements and news");
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

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new Dimension(220, 0));

        // New Post Button
        JButton newPostButton = createNewPostButton();
        sidebar.add(newPostButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        // Stats card
        JPanel statsCard = createStatsCard();
        sidebar.add(statsCard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        // Categories card
        JPanel categoriesCard = createCategoriesCard();
        sidebar.add(categoriesCard);

        return sidebar;
    }

    private JButton createNewPostButton() {
        JButton button = new JButton("New Announcement") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(37, 99, 235));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(59, 130, 246));
                } else {
                    g2d.setColor(ACCENT_BLUE);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

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
        button.setPreferredSize(new Dimension(220, 45));
        button.setMaximumSize(new Dimension(220, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> showNewAnnouncementDialog());

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent evt) { button.repaint(); }
        });

        return button;
    }

    private JPanel createStatsCard() {
        JPanel card = new JPanel() {
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
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setMaximumSize(new Dimension(220, 200));

        JLabel statsTitle = new JLabel(" Statistics");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        statsTitle.setForeground(TEXT_PRIMARY);
        statsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(statsTitle);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        card.add(createStatItem("Total Posts", String.valueOf(announcements.size()), ACCENT_BLUE));
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(createStatItem("This Week", "3", ACCENT_GREEN));
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(createStatItem("Active Users", "24", ACCENT_PURPLE));

        return card;
    }

    private JPanel createStatItem(String label, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelText.setForeground(TEXT_SECONDARY);

        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueText.setForeground(color);

        panel.add(labelText, BorderLayout.WEST);
        panel.add(valueText, BorderLayout.EAST);

        return panel;
    }

    private JPanel createCategoriesCard() {
        JPanel card = new JPanel() {
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
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setMaximumSize(new Dimension(220, 250));

        JLabel categoriesTitle = new JLabel("Categories");
        categoriesTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        categoriesTitle.setForeground(TEXT_PRIMARY);
        categoriesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(categoriesTitle);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        card.add(createCategoryButton("All", ACCENT_BLUE));
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(createCategoryButton("Academic", ACCENT_PURPLE));
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(createCategoryButton("Schedule", ACCENT_ORANGE));
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(createCategoryButton("Event", ACCENT_GREEN));
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(createCategoryButton("Important", ACCENT_RED));

        return card;
    }

    private JButton createCategoryButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed() || getModel().isRollover()) {
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                } else {
                    g2d.setColor(new Color(248, 250, 252));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2d.setColor(color);
                g2d.fillRoundRect(0, getHeight()/2 - 2, 3, 4, 2, 2);

                if (getModel().isRollover() || getModel().isPressed()) {
                    g2d.setColor(color);
                } else {
                    g2d.setColor(TEXT_PRIMARY);
                }
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = 15;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };

        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(190, 35));
        button.setMaximumSize(new Dimension(190, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent evt) { button.repaint(); }
        });

        return button;
    }

    private JPanel createFeedContainer() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        feedPanel = new JPanel();
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));
        feedPanel.setOpaque(false);

        refreshFeed();

        JScrollPane scrollPane = new JScrollPane(feedPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }

    private void refreshFeed() {
        feedPanel.removeAll();

        for (Announcement announcement : announcements) {
            feedPanel.add(createAnnouncementCard(announcement));
            feedPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        feedPanel.revalidate();
        feedPanel.repaint();
    }

    private JPanel createAnnouncementCard(Announcement announcement) {
        JPanel card = new JPanel() {
            private boolean isHovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int shadowSize = isHovered ? 6 : 3;
                g2d.setColor(new Color(0, 0, 0, isHovered ? 15 : 8));
                g2d.fillRoundRect(shadowSize/2, shadowSize/2, getWidth() - shadowSize, 
                                getHeight() - shadowSize, 15, 15);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Left accent bar
                Color categoryColor = getCategoryColor(announcement.category);
                g2d.setColor(categoryColor);
                g2d.fillRoundRect(0, 0, 4, getHeight(), 15, 15);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout(15, 10));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { card.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent evt) { card.repaint(); }
        });

        // Left side - Voting
        JPanel votingPanel = createVotingPanel(announcement);
        card.add(votingPanel, BorderLayout.WEST);

        // Center - Content
        JPanel contentPanel = createAnnouncementContent(announcement);
        card.add(contentPanel, BorderLayout.CENTER);

        // Right side - Metadata
        JPanel metaPanel = createMetadataPanel(announcement);
        card.add(metaPanel, BorderLayout.EAST);

        return card;
    }

    private JPanel createVotingPanel(Announcement announcement) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(60, 0));

        JButton upvoteBtn = new JButton("▲") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(ACCENT_ORANGE);
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(ACCENT_ORANGE.getRed(), ACCENT_ORANGE.getGreen(), 
                                          ACCENT_ORANGE.getBlue(), 50));
                } else {
                    g2d.setColor(new Color(248, 250, 252));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2d.setColor(getModel().isRollover() ? ACCENT_ORANGE : TEXT_SECONDARY);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        upvoteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        upvoteBtn.setContentAreaFilled(false);
        upvoteBtn.setBorderPainted(false);
        upvoteBtn.setFocusPainted(false);
        upvoteBtn.setPreferredSize(new Dimension(40, 35));
        upvoteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        upvoteBtn.addActionListener(e -> {
            announcement.upvotes++;
            refreshFeed();
        });

        JLabel voteCount = new JLabel(String.valueOf(announcement.upvotes));
        voteCount.setFont(new Font("Segoe UI", Font.BOLD, 18));
        voteCount.setForeground(TEXT_PRIMARY);
        voteCount.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton downvoteBtn = new JButton("▼") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(ACCENT_BLUE);
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(ACCENT_BLUE.getRed(), ACCENT_BLUE.getGreen(), 
                                          ACCENT_BLUE.getBlue(), 50));
                } else {
                    g2d.setColor(new Color(248, 250, 252));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2d.setColor(getModel().isRollover() ? ACCENT_BLUE : TEXT_SECONDARY);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        downvoteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        downvoteBtn.setContentAreaFilled(false);
        downvoteBtn.setBorderPainted(false);
        downvoteBtn.setFocusPainted(false);
        downvoteBtn.setPreferredSize(new Dimension(40, 35));
        downvoteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        downvoteBtn.addActionListener(e -> {
            if (announcement.upvotes > 0) announcement.upvotes--;
            refreshFeed();
        });

        upvoteBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { upvoteBtn.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent evt) { upvoteBtn.repaint(); }
        });

        downvoteBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { downvoteBtn.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent evt) { downvoteBtn.repaint(); }
        });

        panel.add(upvoteBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(voteCount);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(downvoteBtn);

        return panel;
    }

    private JPanel createAnnouncementContent(Announcement announcement) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel(announcement.title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Author and time
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy • hh:mm a");
        JLabel metaLabel = new JLabel("Posted by " + announcement.author + " • " + sdf.format(announcement.timestamp));
        metaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        metaLabel.setForeground(TEXT_SECONDARY);
        metaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Content
        JTextArea contentArea = new JTextArea(announcement.content);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setForeground(TEXT_PRIMARY);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setOpaque(false);
        contentArea.setEditable(false);
        contentArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setOpaque(false);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton commentBtn = createActionButton("💬 " + announcement.comments + " Comments", ACCENT_BLUE);
        commentBtn.addActionListener(e -> {
            announcement.comments++;
            refreshFeed();
            JOptionPane.showMessageDialog(this, "Comment feature coming soon!", "Comments", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton shareBtn = createActionButton("🔗 Share", ACCENT_GREEN);
        shareBtn.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Link copied to clipboard!", "Share", JOptionPane.INFORMATION_MESSAGE));

        actionPanel.add(commentBtn);
        actionPanel.add(shareBtn);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(metaLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(contentArea);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(actionPanel);

        return panel;
    }

    private JPanel createMetadataPanel(Announcement announcement) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 0));

        // Category badge
        JLabel categoryBadge = new JLabel(announcement.category) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color categoryColor = getCategoryColor(announcement.category);
                g2d.setColor(new Color(categoryColor.getRed(), categoryColor.getGreen(), 
                                      categoryColor.getBlue(), 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                super.paintComponent(g);
            }
        };
        categoryBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        categoryBadge.setForeground(getCategoryColor(announcement.category));
        categoryBadge.setOpaque(false);
        categoryBadge.setBorder(new EmptyBorder(4, 8, 4, 8));
        categoryBadge.setAlignmentX(Component.RIGHT_ALIGNMENT);

        panel.add(categoryBadge);

        return panel;
    }

    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed() || getModel().isRollover()) {
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                } else {
                    g2d.setColor(new Color(248, 250, 252));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                if (getModel().isRollover() || getModel().isPressed()) {
                    g2d.setColor(color);
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

        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(130, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent evt) { button.repaint(); }
        });

        return button;
    }

    private Color getCategoryColor(String category) {
        switch (category) {
            case "Academic": return ACCENT_PURPLE;
            case "Schedule": return ACCENT_ORANGE;
            case "Event": return ACCENT_GREEN;
            case "Important": return ACCENT_RED;
            default: return ACCENT_BLUE;
        }
    }

    private void showNewAnnouncementDialog() {
        JDialog dialog = new JDialog(this, "Create New Announcement", true);
        dialog.setSize(550, 450);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel titleFieldLabel = new JLabel("Title:");
        titleFieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField titleField = new JTextField();
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleField.setPreferredSize(new Dimension(500, 35));
        titleField.setMaximumSize(new Dimension(500, 35));

        JLabel contentLabel = new JLabel("Content:");
        contentLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextArea contentArea = new JTextArea(8, 40);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setPreferredSize(new Dimension(500, 150));

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        String[] categories = {"Academic", "Schedule", "Event", "Important"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        categoryBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryBox.setPreferredSize(new Dimension(500, 35));
        categoryBox.setMaximumSize(new Dimension(500, 35));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton postButton = new JButton("Post Announcement");
        postButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        postButton.setBackground(ACCENT_BLUE);
        postButton.setForeground(Color.WHITE);
        postButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        postButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            String category = (String) categoryBox.getSelectedItem();

            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            announcements.add(0, new Announcement("You", title, content, category));
            refreshFeed();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Announcement posted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelButton.setBackground(new Color(248, 250, 252));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(postButton);

        panel.add(titleFieldLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(titleField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(contentLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(categoryLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(categoryBox);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(buttonPanel);

        dialog.add(panel);
        dialog.setVisible(true);
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
        SwingUtilities.invokeLater(() -> new AnnouncementPage().setVisible(true));
    }
}
