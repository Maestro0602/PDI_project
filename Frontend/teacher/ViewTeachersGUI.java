package Frontend.teacher;

import Backend.src.database.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewTeachersGUI extends JDialog {

    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SECONDARY_COLOR = new Color(41, 128, 185);
    private static final Color INFO_COLOR = new Color(52, 152, 219);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color CARD_BG = Color.WHITE;

    private JComboBox<String> viewTypeCombo;
    private JComboBox<String> departmentCombo;
    private JTextField courseIdField;
    private JPanel selectionPanel;
    private JTable teacherTable;
    private DefaultTableModel tableModel;
    private JLabel countLabel;

    public ViewTeachersGUI(JDialog parent) {
        super(parent, "View Teachers", true);
        setSize(800, 650);
        setLocationRelativeTo(parent);
        setResizable(false);
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

        JLabel headerLabel = new JLabel("VIEW TEACHERS", SwingConstants.CENTER);
        headerLabel.setBounds(50, 20, 700, 35);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        mainPanel.add(headerLabel);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBounds(50, 70, 700, 530);
        cardPanel.setBackground(CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        int yPos = 20;

        JLabel viewTypeLabel = new JLabel("Select View Type:");
        viewTypeLabel.setBounds(20, yPos, 150, 25);
        viewTypeLabel.setFont(new Font("Arial", Font.BOLD, 13));
        viewTypeLabel.setForeground(TEXT_COLOR);
        cardPanel.add(viewTypeLabel);

        String[] viewTypes = {
                "-- Select View Type --",
                "View All Teachers",
                "View by Department",
                "View by Course"
        };

        viewTypeCombo = new JComboBox<>(viewTypes);
        viewTypeCombo.setBounds(20, yPos + 30, 460, 35);
        viewTypeCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        viewTypeCombo.addActionListener(e -> updateSelectionPanel());
        cardPanel.add(viewTypeCombo);

        JButton viewButton = new JButton("Load");
        viewButton.setBounds(490, yPos + 30, 170, 35);
        viewButton.setBackground(INFO_COLOR);
        viewButton.setForeground(Color.WHITE);
        viewButton.setFont(new Font("Arial", Font.BOLD, 12));
        viewButton.setFocusPainted(false);
        viewButton.setBorderPainted(false);
        viewButton.setOpaque(true);
        viewButton.addActionListener(e -> viewTeachers());
        cardPanel.add(viewButton);

        yPos += 80;

        // Dynamic selection panel
        selectionPanel = new JPanel();
        selectionPanel.setLayout(null);
        selectionPanel.setBounds(20, yPos, 640, 80);
        selectionPanel.setBackground(CARD_BG);
        cardPanel.add(selectionPanel);

        yPos += 95;

        // Count Label
        countLabel = new JLabel("Select a view type to display teachers");
        countLabel.setBounds(20, yPos, 640, 25);
        countLabel.setFont(new Font("Arial", Font.BOLD, 12));
        countLabel.setForeground(TEXT_COLOR);
        cardPanel.add(countLabel);

        yPos += 35;

        // Teacher Table
        String[] columnNames = {"Teacher ID", "Department", "Major"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        teacherTable = new JTable(tableModel);
        teacherTable.setFont(new Font("Arial", Font.PLAIN, 12));
        teacherTable.setRowHeight(25);
        teacherTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        teacherTable.getTableHeader().setBackground(INFO_COLOR);
        teacherTable.getTableHeader().setForeground(Color.WHITE);
        teacherTable.setSelectionBackground(new Color(52, 152, 219, 50));
        teacherTable.setSelectionForeground(TEXT_COLOR);
        teacherTable.setGridColor(new Color(189, 195, 199));

        // Set column widths
        teacherTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        teacherTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        teacherTable.getColumnModel().getColumn(2).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(teacherTable);
        scrollPane.setBounds(20, yPos, 640, 250);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        cardPanel.add(scrollPane);

        yPos += 265;

        // Close Button
        JButton closeButton = new JButton("Close");
        closeButton.setBounds(280, yPos, 140, 35);
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

    private void updateSelectionPanel() {
        selectionPanel.removeAll();
        String viewType = (String) viewTypeCombo.getSelectedItem();

        if (viewType == null || viewType.equals("-- Select View Type --")) {
            selectionPanel.revalidate();
            selectionPanel.repaint();
            return;
        }

        if (viewType.equals("View by Department")) {
            JLabel deptLabel = new JLabel("Select Department:");
            deptLabel.setBounds(0, 0, 150, 25);
            deptLabel.setFont(new Font("Arial", Font.BOLD, 13));
            deptLabel.setForeground(TEXT_COLOR);
            selectionPanel.add(deptLabel);

            String[] departments = {"-- Select Department --", "GIC", "GIM", "GEE"};
            departmentCombo = new JComboBox<>(departments);
            departmentCombo.setBounds(0, 30, 640, 35);
            departmentCombo.setFont(new Font("Arial", Font.PLAIN, 13));
            selectionPanel.add(departmentCombo);

        } else if (viewType.equals("View by Course")) {
            JLabel courseLabel = new JLabel("Enter Course ID:");
            courseLabel.setBounds(0, 0, 150, 25);
            courseLabel.setFont(new Font("Arial", Font.BOLD, 13));
            courseLabel.setForeground(TEXT_COLOR);
            selectionPanel.add(courseLabel);

            JLabel hintLabel = new JLabel("(e.g., AI001, CS001, EE101, etc.)");
            hintLabel.setBounds(0, 20, 300, 20);
            hintLabel.setFont(new Font("Arial", Font.ITALIC, 11));
            hintLabel.setForeground(new Color(127, 140, 141));
            selectionPanel.add(hintLabel);

            courseIdField = new JTextField();
            courseIdField.setBounds(0, 45, 640, 35);
            courseIdField.setFont(new Font("Arial", Font.PLAIN, 13));
            selectionPanel.add(courseIdField);
        }

        selectionPanel.revalidate();
        selectionPanel.repaint();
    }

    private void viewTeachers() {
        String viewType = (String) viewTypeCombo.getSelectedItem();

        if (viewType == null || viewType.equals("-- Select View Type --")) {
            JOptionPane.showMessageDialog(this,
                    "Please select a view type.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Clear existing data
        tableModel.setRowCount(0);

        if (viewType.equals("View All Teachers")) {
            List<String[]> teachers = TeacherInfoManager.getAllTeachers();
            
            if (teachers == null || teachers.isEmpty()) {
                countLabel.setText("No teachers found");
                JOptionPane.showMessageDialog(this,
                        "No teachers found in the system.",
                        "No Data",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (String[] teacher : teachers) {
                // teacher array: [teacherId, department, major]
                tableModel.addRow(teacher);
            }

            countLabel.setText("Total Teachers: " + teachers.size());
            
            // Also display in console
            System.out.println("\n========================================");
            System.out.println("  ALL TEACHERS");
            System.out.println("========================================");
            TeacherInfoManager.displayAllTeachers();

        } else if (viewType.equals("View by Department")) {
            String department = (String) departmentCombo.getSelectedItem();
            
            if (department == null || department.equals("-- Select Department --")) {
                JOptionPane.showMessageDialog(this,
                        "Please select a department.",
                        "Selection Required",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<String[]> teachers = TeacherInfoManager.getTeachersByDepartment(department);
            
            if (teachers == null || teachers.isEmpty()) {
                countLabel.setText("No teachers found in " + department);
                JOptionPane.showMessageDialog(this,
                        "No teachers found in " + department,
                        "No Data",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (String[] teacher : teachers) {
                tableModel.addRow(teacher);
            }

            countLabel.setText("Total Teachers in " + department + ": " + teachers.size());
            
            // Also display in console
            System.out.println("\n========================================");
            System.out.println("  VIEW TEACHERS BY DEPARTMENT");
            System.out.println("========================================");
            TeacherInfoManager.getTeachersByDepartment(department);

        } else if (viewType.equals("View by Course")) {
            String courseId = courseIdField.getText().trim();
            
            if (courseId.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a course ID.",
                        "Input Required",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<String[]> teachers = TeacherInfoManager.getTeachersByCourse(courseId);
            
            if (teachers == null || teachers.isEmpty()) {
                countLabel.setText("No teachers found for course " + courseId);
                JOptionPane.showMessageDialog(this,
                        "No teachers found for course " + courseId,
                        "No Data",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (String[] teacher : teachers) {
                tableModel.addRow(teacher);
            }

            countLabel.setText("Total Teachers for course " + courseId + ": " + teachers.size());
            
            // Also display in console
            System.out.println("\n========================================");
            System.out.println("   VIEW TEACHERS BY COURSE");
            System.out.println("========================================");
            TeacherInfoManager.getTeachersByCourse(courseId);
        }
    }
}