package Frontend;

import attendance.AttendanceRecord;
import attendance.AttendanceStatus;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;
import database.Attendance;
import department.Student;

public class AttendanceSwingUI extends JFrame {
    
    // Colors matching the modern theme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    
    private Attendance database;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JLabel dateLabel;
    private JButton saveButton;
    private JButton refreshButton;
    private JButton viewAllButton;
    private JButton backButton;
    
    public AttendanceSwingUI() {
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
        setTitle("Student Attendance System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

                g2d.setColor(new Color(147, 197, 253, 25));
                g2d.fillOval(-80, -80, 240, 240);
                g2d.fillOval(getWidth() - 160, getHeight() - 160, 240, 240);
            }
        };
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        
        // Main content area
        JPanel mainContentPanel = new JPanel(new BorderLayout(0, 15));
        mainContentPanel.setOpaque(false);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        
        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainContentPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainContentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(mainContentPanel, BorderLayout.CENTER);
        
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

                // Green gradient accent on the left
                GradientPaint greenGradient = new GradientPaint(
                    0, 0, new Color(34, 197, 94, 200),
                    0, getHeight(), new Color(22, 163, 74, 150)
                );
                g2d.setPaint(greenGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                // Subtle green background pattern
                g2d.setColor(new Color(34, 197, 94, 8));
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // Icon and title container
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        // Attendance icon badge
        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, ACCENT_GREEN,
                    getWidth(), getHeight(), new Color(22, 163, 74)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("✓");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        iconLabel.setForeground(Color.WHITE);
        iconBadge.add(iconLabel);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Student Attendance System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        dateLabel = new JLabel("Date: " + LocalDate.now());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(dateLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        // Back button
        backButton = createBackButton();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 12));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 3, 20, 20);

                // Card background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Legend Panel
        JPanel legendPanel = createLegendPanel();
        tablePanel.add(legendPanel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"#", "Student ID", "Student Name", "Attendance Status", "Score"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return JComboBox.class;
                return String.class;
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(40);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setShowGrid(false);
        studentTable.setIntercellSpacing(new Dimension(0, 0));
        studentTable.setSelectionBackground(new Color(226, 232, 240));
        studentTable.setSelectionForeground(TEXT_PRIMARY);
        
        // Table header styling
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        studentTable.getTableHeader().setBackground(new Color(248, 250, 252));
        studentTable.getTableHeader().setForeground(TEXT_PRIMARY);
        studentTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));
        studentTable.getTableHeader().setPreferredSize(new Dimension(0, 45));
        
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
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        legendPanel.setOpaque(false);
        
        JLabel legendTitle = new JLabel("Attendance Options:");
        legendTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        legendTitle.setForeground(TEXT_PRIMARY);
        legendPanel.add(legendTitle);
        
        String[][] options = {
            {"Present", "1.0", String.valueOf(ACCENT_GREEN.getRGB())},
            {"Late", "0.8", String.valueOf(ACCENT_ORANGE.getRGB())},
            {"Absent", "0.0", String.valueOf(ACCENT_RED.getRGB())},
            {"Excused", "0.5", String.valueOf(ACCENT_BLUE.getRGB())}
        };
        
        for (String[] option : options) {
            JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            optionPanel.setOpaque(false);
            
            Color color = new Color(Integer.parseInt(option[2]));
            
            JPanel colorBox = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(color);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                }
            };
            colorBox.setOpaque(false);
            colorBox.setPreferredSize(new Dimension(20, 20));
            
            JLabel textLabel = new JLabel(option[0] + " (" + option[1] + ")");
            textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            textLabel.setForeground(TEXT_SECONDARY);
            
            optionPanel.add(colorBox);
            optionPanel.add(textLabel);
            legendPanel.add(optionPanel);
        }
        
        return legendPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        saveButton = createStyledButton("💾 Save Attendance", ACCENT_GREEN);
        refreshButton = createStyledButton("🔄 Refresh", ACCENT_BLUE);
        viewAllButton = createStyledButton("📋 View All Records", new Color(168, 85, 247));
        
        saveButton.addActionListener(e -> saveAttendance());
        refreshButton.addActionListener(e -> loadStudents());
        viewAllButton.addActionListener(e -> viewAllAttendance());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(viewAllButton);
        
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
        button.setPreferredSize(new Dimension(200, 45));
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

        button.addActionListener(e -> exitApplication());

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { button.repaint(); }
            @Override
            public void mouseExited(MouseEvent evt) { button.repaint(); }
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
                
                AttendanceRecord existingRecord = database.getExistingAttendance(
                    studentId,
                    LocalDate.now()
                );
                
                if (existingRecord != null) {
                    database.updateAttendance(record);
                } else {
                    database.saveAttendance(record);
                }
                
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
            viewFrame.setSize(950, 550);
            viewFrame.setLocationRelativeTo(this);
            
            // Background panel
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
                }
            };
            
            JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
            contentPanel.setOpaque(false);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
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
            viewTable.setRowHeight(35);
            viewTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            viewTable.setShowGrid(false);
            viewTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            viewTable.getTableHeader().setBackground(new Color(248, 250, 252));
            viewTable.getTableHeader().setForeground(TEXT_PRIMARY);
            
            JScrollPane scrollPane = new JScrollPane(viewTable);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            
            JLabel countLabel = new JLabel("Total Records: " + records.size());
            countLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            countLabel.setForeground(TEXT_PRIMARY);
            
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(countLabel, BorderLayout.SOUTH);
            
            backgroundPanel.add(contentPanel);
            viewFrame.add(backgroundPanel);
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
            "Are you sure you want to go back?",
            "Confirm",
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
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setSelectedItem(value);
            
            String status = (String) value;
            if (status.equals("Present")) {
                setBackground(ACCENT_GREEN);
                setForeground(Color.WHITE);
            } else if (status.equals("Late")) {
                setBackground(ACCENT_ORANGE);
                setForeground(Color.WHITE);
            } else if (status.equals("Absent")) {
                setBackground(ACCENT_RED);
                setForeground(Color.WHITE);
            } else if (status.equals("Excused")) {
                setBackground(ACCENT_BLUE);
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                setForeground(TEXT_SECONDARY);
            }
            
            return this;
        }
    }
    
    // Custom ComboBox Editor
    class AttendanceComboBoxEditor extends DefaultCellEditor {
        public AttendanceComboBoxEditor() {
            super(new JComboBox<>(new String[]{"-- Select --", "Present", "Late", "Absent", "Excused"}));
            ((JComboBox<?>)getComponent()).setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            AttendanceSwingUI ui = new AttendanceSwingUI();
            ui.setVisible(true);
        });
    }
}