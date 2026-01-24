package Frontend.student;

import Backend.src.database.*;
import javax.swing.*;
import java.awt.*;

public class RemoveStudentGUI extends JDialog {

    private static final Color PRIMARY_COLOR = new Color(231, 76, 60);
    private static final Color SECONDARY_COLOR = new Color(192, 57, 43);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color CARD_BG = Color.WHITE;

    private JTextField studentIdField;
    private JLabel studentInfoLabel;
    private JLabel currentAssignmentLabel;
    private String[] currentDeptMajor;

    public RemoveStudentGUI(JDialog parent) {
        super(parent, "Remove Student from Department", true);
        setSize(550, 450);
        setLocationRelativeTo(parent);
        setResizable(false);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        // Main panel with gradient
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
        JLabel headerLabel = new JLabel("REMOVE STUDENT", SwingConstants.CENTER);
        headerLabel.setBounds(50, 20, 450, 35);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        mainPanel.add(headerLabel);

        // Card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBounds(40, 70, 470, 330);
        cardPanel.setBackground(CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        int yPos = 20;

        // Warning message
        JLabel warningLabel = new JLabel("⚠ This will remove the student's department assignment");
        warningLabel.setBounds(20, yPos, 410, 25);
        warningLabel.setFont(new Font("Arial", Font.BOLD, 12));
        warningLabel.setForeground(DANGER_COLOR);
        cardPanel.add(warningLabel);

        yPos += 40;

        // Student ID Section
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setBounds(20, yPos, 120, 25);
        studentIdLabel.setFont(new Font("Arial", Font.BOLD, 13));
        studentIdLabel.setForeground(TEXT_COLOR);
        cardPanel.add(studentIdLabel);

        studentIdField = new JTextField();
        studentIdField.setBounds(20, yPos + 30, 250, 35);
        studentIdField.setFont(new Font("Arial", Font.PLAIN, 13));
        cardPanel.add(studentIdField);

        JButton checkButton = new JButton("Check");
        checkButton.setBounds(280, yPos + 30, 100, 35);
        checkButton.setBackground(PRIMARY_COLOR);
        checkButton.setForeground(Color.WHITE);
        checkButton.setFont(new Font("Arial", Font.BOLD, 12));
        checkButton.setFocusPainted(false);
        checkButton.setBorderPainted(false);
        checkButton.setOpaque(true);
        checkButton.addActionListener(e -> checkStudent());
        cardPanel.add(checkButton);

        yPos += 80;

        // Student Info Display
        studentInfoLabel = new JLabel("");
        studentInfoLabel.setBounds(20, yPos, 410, 40);
        studentInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        studentInfoLabel.setForeground(TEXT_COLOR);
        cardPanel.add(studentInfoLabel);

        yPos += 50;

        // Current Assignment Display
        currentAssignmentLabel = new JLabel("");
        currentAssignmentLabel.setBounds(20, yPos, 410, 60);
        currentAssignmentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        currentAssignmentLabel.setForeground(new Color(52, 73, 94));
        cardPanel.add(currentAssignmentLabel);

        yPos += 80;

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBounds(20, yPos, 410, 40);
        buttonPanel.setBackground(CARD_BG);

        JButton submitButton = new JButton("Remove Student");
        submitButton.setPreferredSize(new Dimension(150, 35));
        submitButton.setBackground(DANGER_COLOR);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 13));
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setOpaque(true);
        submitButton.addActionListener(e -> removeStudent());
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

    private void checkStudent() {
        String studentId = studentIdField.getText().trim();

        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Student ID.",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get student info
        String[] studentInfo = StudentInfoManager.getStudentInfo(studentId);
        if (studentInfo == null) {
            studentInfoLabel.setText("<html><b style='color: #e74c3c;'>✗ Student not found</b></html>");
            currentAssignmentLabel.setText("");
            currentDeptMajor = null;
            return;
        }

        studentInfoLabel.setText("<html><b style='color: #27ae60;'>✓ Student Found:</b> " + 
                                 studentInfo[1] + " (ID: " + studentInfo[0] + ")</html>");

        // Check current assignment
        currentDeptMajor = MajorManager.getDepartmentMajor(studentId);
        if (currentDeptMajor != null) {
            currentAssignmentLabel.setText("<html><b>Current Assignment:</b><br>" + 
                                          "Department: " + currentDeptMajor[0] + "<br>" +
                                          "Major: " + currentDeptMajor[1] + "</html>");
        } else {
            currentAssignmentLabel.setText("<html><b style='color: #e74c3c;'>⚠ No assignment found - Nothing to remove</b></html>");
        }
    }

    private void removeStudent() {
        String studentId = studentIdField.getText().trim();

        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Student ID.",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verify student exists
        String[] studentInfo = StudentInfoManager.getStudentInfo(studentId);
        if (studentInfo == null) {
            JOptionPane.showMessageDialog(this,
                    "Student ID not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if student has assignment
        String[] deptMajor = MajorManager.getDepartmentMajor(studentId);
        if (deptMajor == null) {
            JOptionPane.showMessageDialog(this,
                    "Student is not assigned to any department!",
                    "Not Assigned",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirm removal
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove student " + studentInfo[1] + " from:\n\n" +
                        "Department: " + deptMajor[0] + "\n" +
                        "Major: " + deptMajor[1] + "\n\n" +
                        "Are you sure?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (MajorManager.deleteDepartmentMajorByStudentId(studentId)) {
                JOptionPane.showMessageDialog(this,
                        "Student removed successfully!\n\n" +
                                "Student: " + studentInfo[1] + "\n" +
                                "Removed from: " + deptMajor[0],
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to remove student from department.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}