    package Frontend;

    import Backend.src.database.HeadTeacherpw;
    import java.awt.*;
    import java.awt.event.*;
    import javax.swing.*;

    public class MainpageTeacher extends JFrame {
        
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
        
        public MainpageTeacher() {
            initializeUI();
        }
        
        private void initializeUI() {
            setTitle("Teacher Management System");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(880, 680);
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
            contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 30, 30, 30));

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

            JLabel iconLabel = new JLabel("👨‍🏫");
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
            iconBadge.add(iconLabel);

            // Title panel
            JPanel titlePanel = new JPanel();
            titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
            titlePanel.setOpaque(false);

            JLabel welcomeLabel = new JLabel("Teacher Management Portal");
            welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            welcomeLabel.setForeground(TEXT_PRIMARY);

            JLabel subtitleLabel = new JLabel("Welcome! Select an option to proceed");
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            subtitleLabel.setForeground(TEXT_SECONDARY);

            titlePanel.add(welcomeLabel);
            titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
            titlePanel.add(subtitleLabel);

            titleContainer.add(iconBadge);
            titleContainer.add(titlePanel);

            // Right side buttons
            JButton headTeacherButton = createHeadTeacherButton();
            JButton logoutButton = createLogoutButton();

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setOpaque(false);
            buttonPanel.add(headTeacherButton);
            buttonPanel.add(logoutButton);

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

            // Row 1
            gbc.gridx = 0; gbc.gridy = 0;
            container.add(createOptionCard(
                    "Manage Students",
                    "View and manage student information",
                    ACCENT_ORANGE,
                    "📚",
                    1
            ), gbc);

            gbc.gridx = 1;
            container.add(createOptionCard(
                    "Score & Grades",
                    "Assign grades and manage scoring",
                    ACCENT_BLUE,
                    "📊",
                    2
            ), gbc);

            gbc.gridx = 2;
            container.add(createOptionCard(
                    "Attendance",
                    "Mark and track student attendance",
                    ACCENT_GREEN,
                    "✓",
                    3
            ), gbc);

            // Row 2
            gbc.gridx = 0; gbc.gridy = 1;
            container.add(createOptionCard(
                    "Generate Reports",
                    "Create academic reports",
                    ACCENT_PINK,
                    "📄",
                    4
            ), gbc);

            gbc.gridx = 1;
            container.add(createOptionCard(
                    "Department",
                    "Manage department settings",
                    ACCENT_PURPLE,
                    "🏢",
                    5
            ), gbc);

            gbc.gridx = 2;
            container.add(createOptionCard(
                    "Search Students",
                    "Find and view student details",
                    ACCENT_RED,
                    "🔍",
                    6
            ), gbc);

            return container;
        }
        
        private JPanel createOptionCard(String title, String description, Color accentColor, String icon, int option) {
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
            card.setBorder(BorderFactory.createEmptyBorder(20, 18, 20, 18));
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

            // Hover effect
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    card.repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    card.repaint();
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleMenuClick(option, title);
                }
            });

            return card;
        }
        
        private JButton createHeadTeacherButton() {
            JButton button = new JButton("Head Teacher") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (getModel().isPressed()) {
                        g2d.setColor(new Color(147, 51, 234));
                    } else if (getModel().isRollover()) {
                        g2d.setColor(ACCENT_PURPLE);
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
            button.setPreferredSize(new Dimension(150, 38));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addActionListener(e -> showHeadTeacherLogin());


            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evt) { button.repaint(); }
                @Override
                public void mouseExited(MouseEvent evt) { button.repaint(); }
            });

            return button;
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
            
            button.addActionListener(e -> handleLogout());

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evt) { button.repaint(); }
                @Override
                public void mouseExited(MouseEvent evt) { button.repaint(); }
            });

            return button;
        }
        
        private void showHeadTeacherLogin() {
            // Create custom dialog
            JDialog loginDialog = new JDialog(this, "Head Teacher Login", true);
            loginDialog.setSize(420, 450);
            loginDialog.setLocationRelativeTo(this);
            loginDialog.setResizable(false);
            
            JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
            mainPanel.setBackground(Color.WHITE);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
            
            // Header
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
            headerPanel.setBackground(Color.WHITE);
            
            JLabel iconLabel = new JLabel("🔐");
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
            
            JPanel titlePanel = new JPanel();
            titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
            titlePanel.setBackground(Color.WHITE);
            
            JLabel titleLabel = new JLabel("Head Teacher Access");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            titleLabel.setForeground(TEXT_PRIMARY);
            
            JLabel subtitleLabel = new JLabel("Enter your credentials to continue");
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            subtitleLabel.setForeground(TEXT_SECONDARY);
            
            titlePanel.add(titleLabel);
            titlePanel.add(Box.createRigidArea(new Dimension(0, 3)));
            titlePanel.add(subtitleLabel);
            
            headerPanel.add(iconLabel);
            headerPanel.add(titlePanel);
            
            // Form panel
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.setBackground(Color.WHITE);
            
            // Head Teacher ID
            JLabel idLabel = new JLabel("Head Teacher ID");
            idLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            idLabel.setForeground(TEXT_PRIMARY);
            idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            
            JTextField idField = new JTextField();
            idField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            idField.setPreferredSize(new Dimension(0, 40));
            idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            idField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            idField.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Password
            JLabel passwordLabel = new JLabel("Password");
            passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            passwordLabel.setForeground(TEXT_PRIMARY);
            passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JPasswordField passwordField = new JPasswordField();
            passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            passwordField.setPreferredSize(new Dimension(0, 40));
            passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            formPanel.add(idLabel);
            formPanel.add(Box.createRigidArea(new Dimension(0, 6)));
            formPanel.add(idField);
            formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            formPanel.add(passwordLabel);
            formPanel.add(Box.createRigidArea(new Dimension(0, 6)));
            formPanel.add(passwordField);
            
            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setBackground(Color.WHITE);
            
            JButton cancelButton = new JButton("Cancel") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(new Color(241, 245, 249));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    
                    g2d.setColor(TEXT_SECONDARY);
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                    int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2d.drawString(getText(), textX, textY);
                }
            };
            cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            cancelButton.setPreferredSize(new Dimension(100, 38));
            cancelButton.setContentAreaFilled(false);
            cancelButton.setBorderPainted(false);
            cancelButton.setFocusPainted(false);
            cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cancelButton.addActionListener(e -> loginDialog.dispose());
            
            JButton loginButton = new JButton("Login") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    if (getModel().isPressed()) {
                        g2d.setColor(new Color(147, 51, 234));
                    } else {
                        g2d.setColor(ACCENT_PURPLE);
                    }
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                    int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2d.drawString(getText(), textX, textY);
                }
            };
            loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            loginButton.setPreferredSize(new Dimension(100, 38));
            loginButton.setContentAreaFilled(false);
            loginButton.setBorderPainted(false);
            loginButton.setFocusPainted(false);
            loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            loginButton.addActionListener(e -> {
                String headTeacherId = idField.getText().trim();
                String password = new String(passwordField.getPassword());
                
                if (headTeacherId.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(loginDialog,
                        "Please enter both ID and Password",
                        "Input Required",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Verify credentials
                if (HeadTeacherpw.verifyHeadTeacherPassword(headTeacherId, password)) {
                    JOptionPane.showMessageDialog(loginDialog,
                        "Login successful! Welcome " + headTeacherId,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    loginDialog.dispose();
                    this.dispose();
                    // Navigate to department management
                    MainpageHeadTeacher.main(null);
                } else {
                    JOptionPane.showMessageDialog(loginDialog,
                        "Invalid ID or Password!\nPlease try again.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                    idField.requestFocus();
                }
            });
            
            // Enter key support
            passwordField.addActionListener(e -> loginButton.doClick());
            
            buttonPanel.add(cancelButton);
            buttonPanel.add(loginButton);
            
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(formPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            loginDialog.add(mainPanel);
            loginDialog.setVisible(true);
        }
        
        private void handleMenuClick(int option, String title) {
            switch (option) {
                case 1:
                    System.out.println("Managing Students information...");
                    StudentAssignGUI.main(null);
                    break;
                case 2:
                    System.out.println("Managing grades and GPA...");
                    this.dispose();
                    GradingGUI.main(null);
                    break;
                case 3:
                    System.out.println("Managing Attendance...");
                    this.dispose();
                    AttendanceSwingUI.main(null);
                    break;
                case 4:
                    System.out.println("Generating Reports...");
                    this.dispose();
                    GenerateStudentReportGUI.main(null);
                    break;
                case 5:
                    System.out.println("Managing Department...");
                    this.dispose();
                    StudentDepartmentAssignmentGUI.main(null);
                    break;
                case 6:
                    System.out.println("Searching Students...");
                    this.dispose();
                    StudentSearchGUI.main(null);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Invalid option selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        private void handleLogout() {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Logging out...");
                this.dispose();
                SwingUtilities.invokeLater(() -> {
                    MainGUI Mainframe = new MainGUI();
                    Mainframe.setVisible(true);
                });
            }
        }
        
        public static void main(String[] args) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            SwingUtilities.invokeLater(() -> {
                MainpageTeacher frame = new MainpageTeacher();
                frame.setVisible(true);
            });
        }
    }