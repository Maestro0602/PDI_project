package Frontend;

import Backend.src.attendance.AttendanceRecord;
import Backend.src.attendance.AttendanceStatus;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;
import Backend.src.database.Attendance;
import Backend.src.department.Student;

public class AttendanceSwingUI extends JFrame {
    private Attendance database;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JLabel dateLabel;
    private JButton saveButton;
    private JButton refreshButton;
    private JButton viewAllButton;
    private JButton exitButton;
    
    public AttendanceSwingUI() {
        setTitle("Student Attendance System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            this.database = new Attendance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Failed to initialize database: " + e.getMessage(),
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        
        initComponents();
        loadStudents();
    }
    
    private void initComponents() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 245));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("STUDENT ATTENDANCE SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        dateLabel = new JLabel("Date: " + LocalDate.now());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        dateLabel.setForeground(Color.WHITE);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(new Color(240, 240, 245));
        
        // Legend Panel
        JPanel legendPanel = createLegendPanel();
        tablePanel.add(legendPanel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"#", "Student ID", "Student Name", "Attendance Status", "Score"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only attendance status column is editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return JComboBox.class;
                return String.class;
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(35);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 13));
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        studentTable.getTableHeader().setBackground(new Color(52, 73, 94));
        studentTable.getTableHeader().setForeground(Color.WHITE);
        studentTable.setSelectionBackground(new Color(189, 195, 199));
        
        // Set column widths
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Custom renderer and editor for attendance status
        studentTable.getColumnModel().getColumn(3).setCellRenderer(new AttendanceComboBoxRenderer());
        studentTable.getColumnModel().getColumn(3).setCellEditor(new AttendanceComboBoxEditor());
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        legendPanel.setBackground(new Color(236, 240, 241));
        legendPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            "Attendance Options",
            0, 0,
            new Font("Arial", Font.BOLD, 12)
        ));
        
        String[][] options = {
            {"Present", "1.0", "#27ae60"},
            {"Late", "0.8", "#f39c12"},
            {"Absent", "0.0", "#e74c3c"},
            {"Excused", "0.5", "#3498db"}
        };
        
        for (String[] option : options) {
            JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            optionPanel.setBackground(new Color(236, 240, 241));
            
            JLabel colorBox = new JLabel("  ");
            colorBox.setOpaque(true);
            colorBox.setBackground(Color.decode(option[2]));
            colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            
            JLabel textLabel = new JLabel(option[0] + " (Score: " + option[1] + ")");
            textLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            
            optionPanel.add(colorBox);
            optionPanel.add(textLabel);
            legendPanel.add(optionPanel);
        }
        
        return legendPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));
        
        saveButton = createStyledButton(" Save Attendance", new Color(46, 204, 113));
        refreshButton = createStyledButton(" Refresh", new Color(52, 152, 219));
        viewAllButton = createStyledButton(" View All Records", new Color(155, 89, 182));
        exitButton = createStyledButton(" Exit", new Color(231, 76, 60));
        
        saveButton.addActionListener(e -> saveAttendance());
        refreshButton.addActionListener(e -> loadStudents());
        viewAllButton.addActionListener(e -> viewAllAttendance());
        exitButton.addActionListener(e -> exitApplication());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(viewAllButton);
        buttonPanel.add(exitButton);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void loadStudents() {
        try {
            tableModel.setRowCount(0);
            List<Student> students = database.getAllStudents();
            
            if (students.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No students found in the database!",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int studentNumber = 1;
            for (Student student : students) {
                // Check if attendance already exists for today
                AttendanceRecord existingRecord = database.getExistingAttendance(
                    student.getStudentId(),
                    LocalDate.now()
                );
                
                String status = "-- Select --";
                String score = "-";
                
                if (existingRecord != null) {
                    status = AttendanceStatus.getStautsText(existingRecord.getStatus());
                    score = String.format("%.1f", existingRecord.getScore());
                }
                
                tableModel.addRow(new Object[]{
                    studentNumber++,
                    student.getStudentId(),
                    student.getStudentName(),
                    status,
                    score
                });
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Failed to load students: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void saveAttendance() {
        try {
            int savedCount = 0;
            int skippedCount = 0;
            
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String studentId = (String) tableModel.getValueAt(i, 1);
                String statusText = (String) tableModel.getValueAt(i, 3);
                
                if (statusText.equals("-- Select --")) {
                    skippedCount++;
                    continue;
                }
                
                int status = AttendanceStatus.getStatusFromText(statusText);
                double score = AttendanceStatus.getScore(status);
                
                AttendanceRecord record = new AttendanceRecord(
                    studentId,
                    LocalDate.now(),
                    status,
                    score
                );
                
                // Check if record exists
                AttendanceRecord existingRecord = database.getExistingAttendance(
                    studentId,
                    LocalDate.now()
                );
                
                if (existingRecord != null) {
                    database.updateAttendance(record);
                } else {
                    database.saveAttendance(record);
                }
                
                // Update score in table
                tableModel.setValueAt(String.format("%.1f", score), i, 4);
                savedCount++;
            }
            
            String message = "Attendance saved successfully!\n" +
                           "Saved: " + savedCount + " records\n" +
                           "Skipped: " + skippedCount + " records";
            
            JOptionPane.showMessageDialog(this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Failed to save attendance: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void viewAllAttendance() {
        try {
            List<AttendanceRecord> records = database.getAllAttendance();
            
            JFrame viewFrame = new JFrame("All Attendance Records");
            viewFrame.setSize(900, 500);
            viewFrame.setLocationRelativeTo(this);
            
            String[] columnNames = {"Student ID", "Student Name", "Date", "Status", "Score"};
            DefaultTableModel viewTableModel = new DefaultTableModel(columnNames, 0);
            
            for (AttendanceRecord record : records) {
                viewTableModel.addRow(new Object[]{
                    record.getStudentId(),
                    record.getStudentName(),
                    record.getAttendanceDate(),
                    AttendanceStatus.getStautsText(record.getStatus()),
                    String.format("%.1f", record.getScore())
                });
            }
            
            JTable viewTable = new JTable(viewTableModel);
            viewTable.setRowHeight(30);
            viewTable.setFont(new Font("Arial", Font.PLAIN, 13));
            viewTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
            viewTable.getTableHeader().setBackground(new Color(52, 73, 94));
            viewTable.getTableHeader().setForeground(Color.WHITE);
            
            JScrollPane scrollPane = new JScrollPane(viewTable);
            
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(scrollPane, BorderLayout.CENTER);
            
            JLabel countLabel = new JLabel("Total Records: " + records.size());
            countLabel.setFont(new Font("Arial", Font.BOLD, 14));
            countLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(countLabel, BorderLayout.SOUTH);
            
            viewFrame.add(panel);
            viewFrame.setVisible(true);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Failed to retrieve attendance records: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (database != null) {
                    database.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            MainPageTeacherGUI.main(null);
            this.dispose();
        }
    }
    
    // Custom ComboBox Renderer
    class AttendanceComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {
        public AttendanceComboBoxRenderer() {
            super(new String[]{"-- Select --", "Present", "Late", "Absent", "Excused"});
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setSelectedItem(value);
            
            String status = (String) value;
            if (status.equals("Present")) {
                setBackground(new Color(39, 174, 96));
                setForeground(Color.WHITE);
            } else if (status.equals("Late")) {
                setBackground(new Color(243, 156, 18));
                setForeground(Color.WHITE);
            } else if (status.equals("Absent")) {
                setBackground(new Color(231, 76, 60));
                setForeground(Color.WHITE);
            } else if (status.equals("Excused")) {
                setBackground(new Color(52, 152, 219));
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
            
            return this;
        }
    }
    
    // Custom ComboBox Editor
    class AttendanceComboBoxEditor extends DefaultCellEditor {
        public AttendanceComboBoxEditor() {
            super(new JComboBox<>(new String[]{"-- Select --", "Present", "Late", "Absent", "Excused"}));
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            AttendanceSwingUI ui = new AttendanceSwingUI();
            ui.setVisible(true);
        });
    }
}