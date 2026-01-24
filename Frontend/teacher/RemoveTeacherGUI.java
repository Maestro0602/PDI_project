package Frontend.teacher;

import Backend.src.database.*;
import javax.swing.*;
import java.awt.*;

public class RemoveTeacherGUI extends JDialog {

    private static final Color PRIMARY_COLOR = new Color(231, 76, 60);
    private static final Color SECONDARY_COLOR = new Color(192, 57, 43);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color CARD_BG = Color.WHITE;

    private JTextField teacherIdField;
    private JLabel teacherInfoLabel;
    private JLabel currentInfoLabel;

    public RemoveTeacherGUI(JDialog parent) {
        super(parent, "Delete Teacher", true);
        setSize(550, 450);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        System.out.println("\n--- Delete Teacher ---");
        DatabaseManager.displayTemporaryTeacherUsers();
        
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
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

        JLabel headerLabel = new JLabel("DELETE TEACHER", SwingConstants.CENTER);
        headerLabel.setBounds(50, 20, 450, 35);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        mainPanel.add(headerLabel);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBounds(40, 70, 470, 330);
        cardPanel.setBackground(CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        int yPos = 20;

        JLabel warningLabel = new JLabel("⚠ This will permanently delete the teacher and all course assignments");
        warningLabel.setBounds(20, yPos, 430, 25);
        warningLabel.setFont(new Font("Arial", Font.BOLD, 12));
        warningLabel.setForeground(DANGER_COLOR);
        cardPanel.add(warningLabel);

        yPos += 40;

        JLabel teacherIdLabel = new JLabel("Teacher ID:");
        teacherIdLabel.setBounds(20, yPos, 120, 25);
        teacherIdLabel.setFont(new Font("Arial", Font.BOLD, 13));
        teacherIdLabel.setForeground(TEXT_COLOR);
        cardPanel.add(teacherIdLabel);

        teacherIdField = new JTextField();
        teacherIdField.setBounds(20, yPos + 30, 250, 35);
        teacherIdField.setFont(new Font("Arial", Font.PLAIN, 13));
        cardPanel.add(teacherIdField);

        JButton checkButton = new JButton("Check");
        checkButton.setBounds(280, yPos + 30, 100, 35);
        checkButton.setBackground(PRIMARY_COLOR);
        checkButton.setForeground(Color.WHITE);
        checkButton.setFont(new Font("Arial", Font.BOLD, 12));
        checkButton.setFocusPainted(false);
        checkButton.setBorderPainted(false);
        checkButton.setOpaque(true);
        checkButton.addActionListener(e -> checkTeacher());
        cardPanel.add(checkButton);

        yPos += 80;

        teacherInfoLabel = new JLabel("");
        teacherInfoLabel.setBounds(20, yPos, 410, 40);
        teacherInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        teacherInfoLabel.setForeground(TEXT_COLOR);
        cardPanel.add(teacherInfoLabel);

        yPos += 50;

        currentInfoLabel = new JLabel("");
        currentInfoLabel.setBounds(20, yPos, 410, 60);
        currentInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        currentInfoLabel.setForeground(new Color(52, 73, 94));
        cardPanel.add(currentInfoLabel);

        yPos += 80;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBounds(20, yPos, 410, 40);
        buttonPanel.setBackground(CARD_BG);

        JButton submitButton = new JButton("Delete Teacher");
        submitButton.setPreferredSize(new Dimension(150, 35));
        submitButton.setBackground(DANGER_COLOR);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 13));
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setOpaque(true);
        submitButton.addActionListener(e -> deleteTeacher());
        buttonPanel.add(submitButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(150, 35));
        cancelButton.setBackground(new Color(149, 165, 166));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 13));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setOpaque(true);
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        cardPanel.add(buttonPanel);

        mainPanel.add(cardPanel);
        add(mainPanel);
    }

    private void checkTeacher() {
        String teacherId = teacherIdField.getText().trim();

        if (teacherId.isEmpty()) {
            teacherInfoLabel.setText("<html><b style='color: #e74c3c;'>✗ Teacher ID cannot be empty</b></html>");
            currentInfoLabel.setText("");
            return;
        }

        if (!TeacherInfoManager.teacherExists(teacherId)) {
            teacherInfoLabel.setText("<html><b style='color: #e74c3c;'>✗ Teacher not found</b></html>");
            currentInfoLabel.setText("");
            return;
        }

        teacherInfoLabel.setText("<html><b style='color: #27ae60;'>✓ Teacher Found: " + teacherId + "</b></html>");

        String[] teacherInfo = TeacherInfoManager.getTeacherInfo(teacherId);
        if (teacherInfo != null) {
            currentInfoLabel.setText("<html><b>Current Information:</b><br>" + 
                                    "Department: " + teacherInfo[1] + "<br>" +
                                    "Major: " + teacherInfo[2] + "</html>");
        }
    }

    private void deleteTeacher() {
        String teacherId = teacherIdField.getText().trim();

        if (teacherId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Teacher ID.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!TeacherInfoManager.teacherExists(teacherId)) {
            JOptionPane.showMessageDialog(this,
                    "Teacher with ID " + teacherId + " not found!",
                    "Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete teacher " + teacherId + "?\n\n" +
                        "This will also remove all course assignments.\n" +
                        "This action cannot be undone!",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Delete teacher (from backend logic)
            TeacherInfoManager.deleteTeacher(teacherId);
            TeacherCourseManager.deleteTeacherAllCourses(teacherId);

            JOptionPane.showMessageDialog(this,
                    "Teacher " + teacherId + " deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
