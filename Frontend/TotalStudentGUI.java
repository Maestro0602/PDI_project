package Frontend;

import Backend.src.database.MajorManager;
import Backend.src.database.StudentInfoManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TotalStudentGUI extends JFrame {

    private JTable statsTable;
    private DefaultTableModel tableModel;
    private JLabel totalStudentsLabel;
    private JLabel maleCountLabel;
    private JLabel femaleCountLabel;
    private JLabel malePercentLabel;
    private JLabel femalePercentLabel;
    
    // Modern color scheme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_PINK = new Color(236, 72, 153);
    private static final Color BG_LIGHT = new Color(241, 245, 249);

    public TotalStudentGUI() {
        initializeDatabase();
        initializeUI();
        loadStudentStatistics();
    }

    private void initializeDatabase() {
        StudentInfoManager.createStudentInfoTable();
        MajorManager.createDepartmentMajorTable();
    }

    private void initializeUI() {
        setTitle("Student Statistics Report");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
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

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        
        // Main content area
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        // Stats Cards Panel
        JPanel statsCardsPanel = createStatsCardsPanel();
        contentPanel.add(statsCardsPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

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

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);

                // Purple gradient accent
                GradientPaint purpleGradient = new GradientPaint(
                    0, 0, new Color(168, 85, 247, 200),
                    0, getHeight(), new Color(147, 51, 234, 150)
                );
                g2d.setPaint(purpleGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                // Subtle background pattern
                g2d.setColor(new Color(168, 85, 247, 8));
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // Icon and title container
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        // Icon badge
        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, ACCENT_PURPLE,
                    getWidth(), getHeight(), new Color(147, 51, 234)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("📊");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Student Statistics Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Overview of students by department and gender");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(titleLabel);
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

    private JPanel createStatsCardsPanel() {
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setOpaque(false);

        // Total Students Card
        totalStudentsLabel = new JLabel("0");
        JPanel totalCard = createStatCard("👥", "Total Students", totalStudentsLabel, ACCENT_BLUE, null);

        // Male Students Card
        maleCountLabel = new JLabel("0");
        malePercentLabel = new JLabel("0%");
        JPanel maleCard = createStatCard("👨", "Male Students", maleCountLabel, ACCENT_PURPLE, malePercentLabel);

        // Female Students Card
        femaleCountLabel = new JLabel("0");
        femalePercentLabel = new JLabel("0%");
        JPanel femaleCard = createStatCard("👩", "Female Students", femaleCountLabel, ACCENT_PINK, femalePercentLabel);

        cardsPanel.add(totalCard);
        cardsPanel.add(maleCard);
        cardsPanel.add(femaleCard);

        return cardsPanel;
    }

    private JPanel createStatCard(String icon, String label, JLabel valueLabel, Color accentColor, JLabel percentLabel) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 12));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 3, 16, 16);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                // Top accent bar
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), 
                                      accentColor.getBlue(), 40));
                g2d.fillRoundRect(0, 0, getWidth(), 60, 16, 16);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout(15, 10));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(TEXT_PRIMARY);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(valueLabel);
        
        // Add percentage label if provided
        if (percentLabel != null) {
            percentLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            percentLabel.setForeground(accentColor);
            percentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            textPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            textPanel.add(percentLabel);
        }

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 12));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 3, 16, 16);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("📋 Department Breakdown");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Create table
        String[] columnNames = {"Department", "Total", "Male", "Female"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        statsTable = new JTable(tableModel);
        statsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statsTable.setRowHeight(45);
        statsTable.setShowGrid(true);
        statsTable.setGridColor(new Color(226, 232, 240));
        statsTable.setIntercellSpacing(new Dimension(1, 1));
        statsTable.setSelectionBackground(new Color(219, 234, 254));
        statsTable.setSelectionForeground(TEXT_PRIMARY);

        // Table header styling
        statsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        statsTable.getTableHeader().setBackground(new Color(248, 250, 252));
        statsTable.getTableHeader().setForeground(TEXT_PRIMARY);
        statsTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(203, 213, 225)));
        statsTable.getTableHeader().setPreferredSize(new Dimension(0, 50));

        // Set column widths
        statsTable.getColumnModel().getColumn(0).setPreferredWidth(500);
        statsTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        statsTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        statsTable.getColumnModel().getColumn(3).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(statsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(titleLabel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        JButton refreshButton = createStyledButton("🔄 Refresh Statistics", ACCENT_GREEN);
        refreshButton.addActionListener(e -> loadStudentStatistics());

        buttonPanel.add(refreshButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(color);
                } else {
                    g2d.setColor(color.darker().darker());
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
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(MouseEvent evt) { button.repaint(); }
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
            this.dispose();
            MainPageOwnerGUI.main(new String[]{});
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(MouseEvent evt) { button.repaint(); }
        });

        return button;
    }

    private void loadStudentStatistics() {
        // Clear existing table data
        tableModel.setRowCount(0);

        // Get total student count
        int totalStudents = StudentInfoManager.getTotalStudentCount();
        
        if (totalStudents == 0) {
            totalStudentsLabel.setText("0");
            maleCountLabel.setText("0");
            femaleCountLabel.setText("0");
            malePercentLabel.setText("0%");
            femalePercentLabel.setText("0%");
            JOptionPane.showMessageDialog(this, 
                "No students found in the system.", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        totalStudentsLabel.setText(String.valueOf(totalStudents));

        // Departments to check
        String[] departments = {"GIC", "GIM", "GEE"};
        int grandTotalMale = 0;
        int grandTotalFemale = 0;

        // Populate table with department statistics
        for (String dept : departments) {
            int[] stats = StudentInfoManager.getStudentsByDepartmentAndGender(dept);
            if (stats != null && stats.length >= 3) {
                int total = stats[0];
                int males = stats[1];
                int females = stats[2];

                grandTotalMale += males;
                grandTotalFemale += females;

                String deptName = getDepartmentFullName(dept);
                tableModel.addRow(new Object[]{deptName, total, males, females});
            }
        }

        // Add total row
        tableModel.addRow(new Object[]{"TOTAL", totalStudents, grandTotalMale, grandTotalFemale});

        // Custom renderer for table
        statsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (row == table.getRowCount() - 1) {
                    c.setFont(new Font("Segoe UI", Font.BOLD, 15));
                    c.setForeground(TEXT_PRIMARY);
                    c.setBackground(new Color(241, 245, 249));
                } else {
                    c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    c.setForeground(TEXT_PRIMARY);
                    c.setBackground(Color.WHITE);
                }
                
                if (isSelected && row != table.getRowCount() - 1) {
                    c.setBackground(new Color(219, 234, 254));
                }
                
                // Center align numeric columns
                if (column > 0) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                return c;
            }
        });

        // Update stat cards
        maleCountLabel.setText(String.valueOf(grandTotalMale));
        femaleCountLabel.setText(String.valueOf(grandTotalFemale));
        
        // Calculate and update percentages
        if (totalStudents > 0) {
            double malePercentage = (grandTotalMale * 100.0) / totalStudents;
            double femalePercentage = (grandTotalFemale * 100.0) / totalStudents;
            
            malePercentLabel.setText(String.format("%.1f%%", malePercentage));
            femalePercentLabel.setText(String.format("%.1f%%", femalePercentage));
        } else {
            malePercentLabel.setText("0%");
            femalePercentLabel.setText("0%");
        }
    }

    private String getDepartmentFullName(String deptCode) {
        switch (deptCode) {
            case "GIC":
                return "GIC (General IT & Computing)";
            case "GIM":
                return "GIM (General IT & Management)";
            case "GEE":
                return "GEE (General Electrical & Engineering)";
            default:
                return deptCode;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            TotalStudentGUI gui = new TotalStudentGUI();
            gui.setVisible(true);
        });
    }
}