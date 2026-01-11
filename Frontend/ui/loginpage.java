package Frontend.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class loginpage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    // Colors
    private static final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private static final Color SECONDARY_BLUE = new Color(59, 130, 246);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);

    public loginpage() {
        initComponent();
    }

    public void initComponent() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 680);
        setLocationRelativeTo(null);

        // Main background panel
        JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
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

        // Card panel
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
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(40, 50, 40, 50));
        cardPanel.setPreferredSize(new Dimension(420, 540));
        cardPanel.setOpaque(false);

        // Animated icon
        JPanel iconPanel = createAnimatedIcon();
        JPanel iconWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconWrapper.setOpaque(false);
        iconWrapper.setBorder(new EmptyBorder(0, 0, 20, 0));
        iconWrapper.add(iconPanel);

        // Title
        JLabel title = new JLabel("Student Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input fields
        usernameField = createStyledTextField();
        passwordField = createStyledPasswordField();

        JPanel usernamePanel = createInputGroup("Username", usernameField);
        JPanel passwordPanel = createInputGroup("Password", passwordField);

        // Login button
        JButton loginButton = createStyledButton();

        // Footer
        JLabel footerLabel = new JLabel("Secure Login • Powered ITC");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(TEXT_SECONDARY);
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components
        cardPanel.add(iconWrapper);
        cardPanel.add(title);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(usernamePanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(passwordPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(loginButton);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(footerLabel);

        backgroundPanel.add(cardPanel);
        setContentPane(backgroundPanel);
    }

    // Animated icon
    private JPanel createAnimatedIcon() {
        JPanel iconPanel = new JPanel() {
            private float rotation = 0;
            private Timer timer;

            {
                timer = new Timer(50, e -> {
                    rotation += 0.02f;
                    repaint();
                });
                timer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                GradientPaint gp = new GradientPaint(
                        (float)(centerX + Math.cos(rotation) * 30),
                        (float)(centerY + Math.sin(rotation) * 30),
                        TEXT_PRIMARY,
                        (float)(centerX - Math.cos(rotation) * 30),
                        (float)(centerY - Math.sin(rotation) * 30),
                        SECONDARY_BLUE
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);

                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.fillOval(15, 15, 70, 70);

                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int[] xPoints = {30, 50, 70, 50};
                int[] yPoints = {45, 35, 45, 55};
                g2d.fillPolygon(xPoints, yPoints, 4);
                g2d.drawLine(50, 55, 50, 65);
                g2d.fillOval(47, 63, 6, 6);
            }
        };

        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(100, 100));
        iconPanel.setMaximumSize(new Dimension(100, 100));
        return iconPanel;
    }

    // Input group
    private JPanel createInputGroup(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(field);

        return panel;
    }

    // Styled text field
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(12, 16, 12, 16)
        ));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_BLUE, 2, true),
                        new EmptyBorder(12, 16, 12, 16)
                ));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                        new EmptyBorder(12, 16, 12, 16)
                ));
            }
        });

        return field;
    }

    // Styled password field
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(12, 16, 12, 16)
        ));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_BLUE, 2, true),
                        new EmptyBorder(12, 16, 12, 16)
                ));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                        new EmptyBorder(12, 16, 12, 16)
                ));
            }
        });

        return field;
    }

    // button
    private JButton createStyledButton() {
        JButton button = new JButton("LOGIN") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(PRIMARY_BLUE);
                } else if (getModel().isRollover()) {
                    GradientPaint gp = new GradientPaint(0, 0, SECONDARY_BLUE, 0, getHeight(), PRIMARY_BLUE);
                    g2d.setPaint(gp);
                } else {
                    GradientPaint gp = new GradientPaint(0, 0, PRIMARY_BLUE, 0, getHeight(), SECONDARY_BLUE);
                    g2d.setPaint(gp);
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

        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent evt) { button.repaint(); }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new loginpage().setVisible(true));
    }
}
