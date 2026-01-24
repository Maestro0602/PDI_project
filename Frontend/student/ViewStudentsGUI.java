package Frontend.student;

import Backend.src.department.*;
import Backend.src.database.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewStudentsGUI extends JDialog {

    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SECONDARY_COLOR = new Color(41, 128, 185);
    private static final Color INFO_COLOR = new Color(52, 152, 219);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color CARD_BG = Color.WHITE;

    private JComboBox<String> departmentCombo;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JLabel countLabel;

    public ViewStudentsGUI(JDialog parent) {
        super(parent, "View Students by Department", true);
        setSize(700, 600);
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
        JLabel headerLabel = new JLabel("VIEW STUDENTS", SwingConstants.CENTER);
        headerLabel.setBounds(50, 20, 600, 35);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        mainPanel.add(headerLabel);

        // Card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBounds(40, 70, 620, 480);
        cardPanel.setBackground(CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        int yPos = 20;

        // Department Selection Section
        JLabel deptLabel = new JLabel("Select Department:");
        deptLabel.setBounds(20, yPos, 150, 25);
        deptLabel.setFont(new Font("Arial", Font.BOLD, 13));
        deptLabel.setForeground(TEXT_COLOR);
        cardPanel.add(deptLabel);

        String[] departments = {
                Department.GIC.getDisplayName(),
                Department.GIM.getDisplayName(),
                Department.GEE.getDisplayName()
        };

        departmentCombo = new JComboBox<>(departments);
        departmentCombo.setBounds(20, yPos + 30, 400, 35);
        departmentCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        cardPanel.add(departmentCombo);

        JButton loadButton = new JButton("Load Students");
        loadButton.setBounds(430, yPos + 30, 150, 35);
        loadButton.setBackground(INFO_COLOR);
        loadButton.setForeground(Color.WHITE);
        loadButton.setFont(new Font("Arial", Font.BOLD, 12));
        loadButton.setFocusPainted(false);
        loadButton.setBorderPainted(false);
        loadButton.setOpaque(true);
        loadButton.addActionListener(e -> loadStudents());
        cardPanel.add(loadButton);

        yPos += 80;

        // Count Label
        countLabel = new JLabel("Select a department to view students");
        countLabel.setBounds(20, yPos, 560, 25);
        countLabel.setFont(new Font("Arial", Font.BOLD, 12));
        countLabel.setForeground(TEXT_COLOR);
        cardPanel.add(countLabel);

        yPos += 35;

        // Table
        String[] columnNames = { "Student ID", "Student Name", "Department", "Major" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        studentTable.setRowHeight(25);
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        studentTable.getTableHeader().setBackground(Color.white);
        studentTable.getTableHeader().setForeground(TEXT_COLOR);
        studentTable.setSelectionBackground(new Color(52, 152, 219));
        studentTable.setSelectionForeground(Color.WHITE);
        studentTable.setGridColor(new Color(189, 195, 199));

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBounds(20, yPos, 560, 260);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        cardPanel.add(scrollPane);

        yPos += 275;

        // Close Button
        JButton closeButton = new JButton("Close");
        closeButton.setBounds(245, yPos, 130, 35);
        closeButton.setBackground(new Color(149, 165, 166));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("Arial", Font.BOLD, 13));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setOpaque(true);
        closeButton.addActionListener(e -> dispose());
        cardPanel.add(closeButton);

        mainPanel.add(cardPanel);
        add(mainPanel);
    }

    private void loadStudents() {
        String selectedDept = (String) departmentCombo.getSelectedItem();

        if (selectedDept == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a department.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Clear existing data
        tableModel.setRowCount(0);
        //fetch students from backend
        List<Student> students = StudentInfoManager.getStudentsByDepartment(selectedDept);
        // Populate table
        if (students != null && !students.isEmpty()) {
    for (Student s : students) {
        tableModel.addRow(new Object[] {
            s.getStudentId(),
            s.getStudentName(),
            s.getDepartment(),
            s.getMajor() // major is empty unless you fetch it from MajorManager
        });
    }
    countLabel.setText(students.size() + " student(s) found in " + selectedDept + " department");
} else {
    countLabel.setText("No students found in " + selectedDept + " department");
}

        
    }
}