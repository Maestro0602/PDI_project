package Frontend;

import Backend.src.database.StudentInfoManager;
import Backend.src.database.MajorManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TotalStudentGUI extends JFrame {

    private JTable statsTable;
    private DefaultTableModel tableModel;
    private JLabel totalStudentsLabel;
    private JLabel malePercentageLabel;
    private JLabel femalePercentageLabel;
    private JPanel mainPanel;

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
        setSize(900, 600);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Footer Panel (Statistics Summary)
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("STUDENT STATISTICS REPORT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        totalStudentsLabel = new JLabel("Total Students: 0");
        totalStudentsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        totalStudentsLabel.setForeground(Color.WHITE);
        totalStudentsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(totalStudentsLabel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            "Students by Department and Gender",
            0,
            0,
            new Font("Arial", Font.BOLD, 14)
        ));

        // Create table with columns
        String[] columnNames = {"Department", "Total", "Male", "Female"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        statsTable = new JTable(tableModel);
        statsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        statsTable.setRowHeight(30);
        statsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        statsTable.getTableHeader().setBackground(new Color(52, 152, 219));
        statsTable.getTableHeader().setForeground(Color.WHITE);
        statsTable.setSelectionBackground(new Color(174, 214, 241));

        // Center align all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < statsTable.getColumnCount(); i++) {
            statsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set column widths
        statsTable.getColumnModel().getColumn(0).setPreferredWidth(350);
        statsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        statsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        statsTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(statsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new GridLayout(3, 1, 5, 5));
        footerPanel.setBackground(new Color(245, 245, 245));
        footerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            "Gender Percentage Breakdown",
            0,
            0,
            new Font("Arial", Font.BOLD, 14)
        ));

        malePercentageLabel = new JLabel("Male Students: 0 (0.00%)");
        malePercentageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        malePercentageLabel.setForeground(new Color(52, 73, 94));

        femalePercentageLabel = new JLabel("Female Students: 0 (0.00%)");
        femalePercentageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        femalePercentageLabel.setForeground(new Color(52, 73, 94));

        JButton refreshButton = new JButton("Refresh Statistics");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(46, 204, 113));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> loadStudentStatistics());

        footerPanel.add(malePercentageLabel);
        footerPanel.add(femalePercentageLabel);
        footerPanel.add(refreshButton);

        return footerPanel;
    }

    private void loadStudentStatistics() {
        // Clear existing table data
        tableModel.setRowCount(0);

        // Get total student count
        int totalStudents = StudentInfoManager.getTotalStudentCount();
        
        if (totalStudents == 0) {
            totalStudentsLabel.setText("Total Students: 0");
            malePercentageLabel.setText("Male Students: 0 (0.00%)");
            femalePercentageLabel.setText("Female Students: 0 (0.00%)");
            JOptionPane.showMessageDialog(this, 
                "No students found in the system.", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        totalStudentsLabel.setText("Total Students: " + totalStudents);

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

        // Add total row with bold formatting
        tableModel.addRow(new Object[]{"TOTAL", totalStudents, grandTotalMale, grandTotalFemale});

        // Bold the last row (TOTAL)
        statsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (row == table.getRowCount() - 1) {
                    c.setFont(new Font("Arial", Font.BOLD, 14));
                    c.setBackground(new Color(236, 240, 241));
                } else {
                    c.setFont(new Font("Arial", Font.PLAIN, 14));
                    c.setBackground(Color.WHITE);
                }
                
                if (isSelected) {
                    c.setBackground(new Color(174, 214, 241));
                }
                
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        // Update percentage labels
        if (totalStudents > 0) {
            double malePercentage = (grandTotalMale * 100.0) / totalStudents;
            double femalePercentage = (grandTotalFemale * 100.0) / totalStudents;

            malePercentageLabel.setText(String.format("Male Students: %d (%.2f%%)", grandTotalMale, malePercentage));
            femalePercentageLabel.setText(String.format("Female Students: %d (%.2f%%)", grandTotalFemale, femalePercentage));
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
