package Frontend.student;

import javax.swing.*;

import Frontend.ManageDepartmentGUI;

import java.awt.*;

public class StudentManagerGUI extends JDialog {

    // Color scheme matching the Department Management System
    private static final Color PRIMARY_COLOR = new Color(39, 174, 96);
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color INFO_COLOR = new Color(52, 152, 219);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color CARD_BG = Color.WHITE;

    private JFrame parent;

    // Constructor accepting JFrame parent
    public StudentManagerGUI(JFrame parent) {
        super(parent, "Student Management", true);
        this.parent = parent;
        initDialog();
    }

    // Constructor for standalone testing (accepts null parent)
    public StudentManagerGUI(Object parent) {
        super((JFrame) parent, "Student Management", true);
        this.parent = (JFrame) parent;
        initDialog();
    }

    private void initDialog() {
        setSize(600, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, 0, getHeight(), SECONDARY_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);

        // Header
        JLabel headerLabel = new JLabel("STUDENT MANAGEMENT", SwingConstants.CENTER);
        headerLabel.setBounds(50, 25, 500, 40);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        mainPanel.add(headerLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Choose an operation to manage students", SwingConstants.CENTER);
        subtitleLabel.setBounds(50, 65, 500, 20);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        mainPanel.add(subtitleLabel);

        // Card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBounds(50, 110, 500, 500);
        cardPanel.setBackground(CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        int yPos = 30;
        int btnHeight = 70;
        int spacing = 20;

        // Add Student Button
        JPanel addPanel = createStyledButton("Add Student to Department",
                "Assign a student to a department and major", SUCCESS_COLOR, yPos);
        addPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                 StudentManagerGUI.this.setVisible(false);
                new AddStudentGUI(StudentManagerGUI.this);
            }
        });
        cardPanel.add(addPanel);
        yPos += btnHeight + spacing;

        // Update Student Button
        JPanel updatePanel = createStyledButton("Update Student Department",
                "Change a student's department and major", WARNING_COLOR, yPos);
        updatePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StudentManagerGUI.this.setVisible(false);
                new UpdateStudentGUI(StudentManagerGUI.this);
            }
        });
        cardPanel.add(updatePanel);
        yPos += btnHeight + spacing;

        // Remove Student Button
        JPanel removePanel = createStyledButton("Remove Student from Department",
                "Remove department assignment from a student", DANGER_COLOR, yPos);
        removePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StudentManagerGUI.this.setVisible(false);
                new RemoveStudentGUI(StudentManagerGUI.this);
            }
        });
        cardPanel.add(removePanel);
        yPos += btnHeight + spacing;

        // View Students Button
        JPanel viewPanel = createStyledButton("View Students by Department",
                "Display students grouped by department", INFO_COLOR, yPos);
        viewPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StudentManagerGUI.this.setVisible(false);
                new ViewStudentsGUI(StudentManagerGUI.this);
            }
        });
        cardPanel.add(viewPanel);
        yPos += btnHeight + spacing;

        // Back Button
        JPanel backPanel = createStyledButton("Back to Main Menu",
                "Return to department management", new Color(236, 240, 241), yPos);
        backPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ManageDepartmentGUI mainPage = new ManageDepartmentGUI();
                StudentManagerGUI.this.dispose();
                mainPage.setVisible(true);
            }
        });
        // Add text color for back button labels
        Component[] components = backPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setForeground(TEXT_COLOR);
            }
        }
        cardPanel.add(backPanel);

        mainPanel.add(cardPanel);
        add(mainPanel);
    }

    private JPanel createStyledButton(String text, String description, Color bgColor, int yPos) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(30, yPos, 440, 70);
        buttonPanel.setBackground(bgColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder());
        buttonPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(text);
        titleLabel.setBounds(15, 12, 410, 25);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        buttonPanel.add(titleLabel);

        JLabel descLabel = new JLabel(description);
        descLabel.setBounds(15, 35, 410, 20);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        descLabel.setForeground(new Color(255, 255, 255, 180));
        buttonPanel.add(descLabel);

        return buttonPanel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new StudentManagerGUI(null));
    }
}