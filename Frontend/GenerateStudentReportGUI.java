package Frontend;

import Backend.src.database.Genreport;
import Backend.src.report.Report;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateStudentReportGUI extends JFrame {

    private JTextField studentIDField;
    private JPanel reportPanel;
    private JScrollPane reportScrollPane;

    // Modern Color Palette
    private Color BG_START = new Color(241, 245, 249);
    private Color BG_END = new Color(226, 232, 240);
    private Color TEXT_PRIMARY = new Color(15, 23, 42);
    private Color TEXT_SECONDARY = new Color(100, 116, 139);
    private Color ACCENT_BLUE = new Color(59, 130, 246);
    private Color ACCENT_GREEN = new Color(34, 197, 94);
    private Color ACCENT_RED = new Color(239, 68, 68);

    public GenerateStudentReportGUI() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Academic Report System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 850);
        setLocationRelativeTo(null);

        JPanel mainBackground = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, BG_START, 0, getHeight(), BG_END));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Header
        JPanel topPanel = createHeaderPanel();
        mainBackground.add(topPanel, BorderLayout.NORTH);

        // Content
        reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
        reportPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Search for a student to view their academic profile");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcomeLabel.setForeground(TEXT_SECONDARY);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        reportPanel.add(Box.createVerticalStrut(100));
        reportPanel.add(welcomeLabel);

        reportScrollPane = new JScrollPane(reportPanel);
        reportScrollPane.setOpaque(false);
        reportScrollPane.getViewport().setOpaque(false);
        reportScrollPane.setBorder(null);
        
        mainBackground.add(reportScrollPane, BorderLayout.CENTER);
        setContentPane(mainBackground);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(1000, 140));

        // Search Card
        JPanel searchCard = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(0, 0, 0, 20)); // Shadow
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            }
        };
        searchCard.setBounds(30, 20, 940, 100);

        JLabel title = new JLabel("Academic Search");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_PRIMARY);
        title.setBounds(25, 15, 300, 30);
        searchCard.add(title);

        studentIDField = new JTextField();
        studentIDField.setBounds(25, 50, 600, 35);
        studentIDField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        searchCard.add(studentIDField);

        JButton genBtn = createStyledButton("Generate Report", ACCENT_BLUE);
        genBtn.setBounds(635, 50, 160, 35);
        genBtn.addActionListener(e -> generateReport());
        searchCard.add(genBtn);

        JButton backBtn = createStyledButton("Back", ACCENT_RED);
        backBtn.setBounds(810, 50, 100, 35);
        backBtn.addActionListener(e -> dispose());
        searchCard.add(backBtn);

        panel.add(searchCard);
        return panel;
    }

    private void generateReport() {
        String id = studentIDField.getText().trim();
        if (id.isEmpty()) return;
        
        Report report = Genreport.getInformationByStudentID(id);
        if (report == null) {
            JOptionPane.showMessageDialog(this, "Student Not Found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        displayReport(report);
    }

    private void displayReport(Report report) {
        reportPanel.removeAll();
        reportPanel.add(Box.createVerticalStrut(20));

        // Profile Card
        reportPanel.add(createReportCard(createProfileContent(report), "Student Profile", "👤"));
        reportPanel.add(Box.createVerticalStrut(20));
        
        // Table Card
        reportPanel.add(createReportCard(createCourseTable(report), "Course Results", "📊"));
        reportPanel.add(Box.createVerticalStrut(20));

        // GPA Card
        reportPanel.add(createReportCard(createGPAContent(report), "Academic Standing", "🎓"));
        reportPanel.add(Box.createVerticalStrut(40));

        reportPanel.revalidate();
        reportPanel.repaint();
    }

    private JPanel createReportCard(Component content, String title, String icon) {
        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(940, 400));
        card.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JPanel inner = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        inner.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLbl = new JLabel(icon + " " + title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setForeground(TEXT_PRIMARY);
        
        inner.add(titleLbl, BorderLayout.NORTH);
        inner.add(content, BorderLayout.CENTER);
        
        card.add(inner);
        return card;
    }

    private JPanel createProfileContent(Report r) {
        JPanel p = new JPanel(new GridLayout(2, 3, 20, 10));
        p.setOpaque(false);
        String[][] details = {
            {"ID", r.getStudentID()}, {"Name", r.getStudentName()}, {"Gender", r.getGender()},
            {"Year", r.getYear()}, {"Dept", r.getDepartment()}, {"Major", r.getMajor()}
        };
        for (String[] d : details) {
            JPanel chunk = new JPanel(new BorderLayout());
            chunk.setOpaque(false);
            JLabel l1 = new JLabel(d[0]);
            l1.setFont(new Font("Segoe UI", Font.BOLD, 12));
            l1.setForeground(TEXT_SECONDARY);
            JLabel l2 = new JLabel(d[1]);
            l2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            chunk.add(l1, BorderLayout.NORTH);
            chunk.add(l2, BorderLayout.CENTER);
            p.add(chunk);
        }
        return p;
    }

    private JPanel createCourseTable(Report r) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        String[] cols = {"Course ID", "Name", "Score", "Grade"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (int i = 0; i < r.getCourseIDs().size(); i++) {
            model.addRow(new Object[]{r.getCourseIDs().get(i), r.getCourseNames().get(i), r.getScores().get(i), r.getGrades().get(i)});
        }
        JTable t = new JTable(model);
        t.setRowHeight(30);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        
        JTableHeader h = t.getTableHeader();
        h.setBackground(new Color(248, 250, 252));
        h.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JScrollPane sp = new JScrollPane(t);
        sp.setPreferredSize(new Dimension(800, 150));
        sp.setBorder(BorderFactory.createLineBorder(new Color(241, 245, 249)));
        p.add(sp);
        return p;
    }

    private JPanel createGPAContent(Report r) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        p.setOpaque(false);
        double total = r.getScores().stream().mapToDouble(Double::doubleValue).sum();
        double gpa = (total / 400.0) * 4.0;
        
        JLabel gpaLbl = new JLabel(String.format("GPA: %.2f / 4.00", gpa));
        gpaLbl.setFont(new Font("Segoe UI", Font.BOLD, 32));
        gpaLbl.setForeground(ACCENT_BLUE);
        p.add(gpaLbl);
        
        return p;
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? baseColor.darker() : (getModel().isRollover() ? baseColor.brighter() : baseColor));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GenerateStudentReportGUI().setVisible(true));
    }
}