package Frontend.ui;

import Backend.src.database.CourseManager;
import java.awt.*;
import java.awt.event.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class SchedulePage extends JFrame {

    // Colors matching the theme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color HEADER_BG = new Color(51, 65, 85);
    private static final Color GRID_BORDER = new Color(203, 213, 225);
    
    // Course type colors (matching reference)
    private static final Color LECTURE_COLOR = new Color(255, 236, 179);      // Light yellow
    private static final Color TUTORIAL_COLOR = new Color(179, 229, 252);     // Light blue
    private static final Color PRACTICAL_COLOR = new Color(255, 205, 178);    // Light orange
    private static final Color LAB_COLOR = new Color(200, 230, 201);          // Light green
    private static final Color SEMINAR_COLOR = new Color(225, 190, 231);      // Light purple
    
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);

    // Time slots matching the reference
    private static final String[][] TIME_SLOTS = {
        {"7:00 - 7:55", "7-8"},
        {"8:00 - 8:55", "8-9"},
        {"9:10 - 10:05", "9-10"},
        {"10:10 - 11:05", "10-11"},
        {"13:00 - 13:55", "13-14"},
        {"14:00 - 14:55", "14-15"},
        {"15:10 - 16:05", "15-16"},
        {"16:10 - 17:05", "16-17"}
    };

    // Days of week
    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    // Schedule data: Week -> Day -> TimeSlot -> CourseInfo
    private Map<String, Map<String, Map<String, CourseInfo>>> scheduleData = new HashMap<>();
    private LocalDate currentWeekStart;
    private JPanel mainContentPanel;
    private JLabel weekLabel;
    
    // Available courses
    private List<String> availableCourses = new ArrayList<>();
    
    // Navigation flag
    private boolean isTeacher;
    
    // View mode
    private String viewMode = "week"; // "week" or "day"
    private String selectedDay = null;

    public SchedulePage() {
        this(true);
    }
    
    public SchedulePage(boolean isTeacher) {
        this.isTeacher = isTeacher;
        currentWeekStart = getStartOfWeek(LocalDate.now());
        loadDataFromDatabase();
        initComponent();
    }

    private LocalDate getStartOfWeek(LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }

    private void loadDataFromDatabase() {
        String[] dbCourses = CourseManager.getAllCoursesArray();
        if (dbCourses != null && dbCourses.length > 0) {
            for (String course : dbCourses) {
                if (course != null && !course.isEmpty()) {
                    availableCourses.add(course);
                }
            }
        }
    }

    private String getWeekKey(LocalDate weekStart) {
        return weekStart.toString();
    }

    private void addCourseToSchedule(LocalDate weekStart, String day, String timeSlot, CourseInfo course) {
        String weekKey = getWeekKey(weekStart);
        scheduleData.computeIfAbsent(weekKey, k -> new HashMap<>())
                    .computeIfAbsent(day, k -> new HashMap<>())
                    .put(timeSlot, course);
    }

    private void removeCourseFromSchedule(LocalDate weekStart, String day, String timeSlot) {
        String weekKey = getWeekKey(weekStart);
        if (scheduleData.containsKey(weekKey) && scheduleData.get(weekKey).containsKey(day)) {
            scheduleData.get(weekKey).get(day).remove(timeSlot);
        }
    }

    private CourseInfo getCourse(LocalDate weekStart, String day, String timeSlot) {
        String weekKey = getWeekKey(weekStart);
        if (scheduleData.containsKey(weekKey) && 
            scheduleData.get(weekKey).containsKey(day) &&
            scheduleData.get(weekKey).get(day).containsKey(timeSlot)) {
            return scheduleData.get(weekKey).get(day).get(timeSlot);
        }
        return null;
    }

    public void initComponent() {
        setTitle("Teaching Schedule");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(241, 245, 249), getWidth(), getHeight(), new Color(226, 232, 240));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JPanel headerPanel = createHeaderPanel();
        mainContentPanel = createWeekViewPanel();

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
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 4, 20, 20);
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(168, 85, 247, 200), 0, getHeight(), new Color(192, 132, 252, 150));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Left - Title
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, ACCENT_PURPLE, getWidth(), getHeight(), new Color(126, 34, 206));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(50, 50));
        iconBadge.setLayout(new GridBagLayout());
        JLabel iconLabel = new JLabel("📅");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconBadge.add(iconLabel);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Teaching Schedule");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Semester 1 • Week View");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 2)));
        titlePanel.add(subtitleLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        // Center - Week navigation
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        navPanel.setOpaque(false);

        JButton prevBtn = createNavButton("<", () -> {
            if (viewMode.equals("week")) {
                currentWeekStart = currentWeekStart.minusWeeks(1);
            } else {
                currentWeekStart = currentWeekStart.minusDays(1);
            }
            refreshView();
        });

        weekLabel = new JLabel(getWeekDisplayText());
        weekLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        weekLabel.setForeground(TEXT_PRIMARY);
        weekLabel.setPreferredSize(new Dimension(300, 30));
        weekLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton nextBtn = createNavButton(">", () -> {
            if (viewMode.equals("week")) {
                currentWeekStart = currentWeekStart.plusWeeks(1);
            } else {
                currentWeekStart = currentWeekStart.plusDays(1);
            }
            refreshView();
        });

        JButton todayBtn = createTodayButton();

        navPanel.add(prevBtn);
        navPanel.add(weekLabel);
        navPanel.add(nextBtn);
        navPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        navPanel.add(todayBtn);

        // Right - Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        if (viewMode.equals("day")) {
            JButton weekViewBtn = createActionButton("Week View", ACCENT_BLUE, () -> {
                viewMode = "week";
                selectedDay = null;
                refreshView();
            });
            buttonPanel.add(weekViewBtn);
        }

        JButton backButton = createBackButton();
        buttonPanel.add(backButton);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(navPanel, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private String getWeekDisplayText() {
        if (viewMode.equals("day") && selectedDay != null) {
            int dayIndex = Arrays.asList(DAYS).indexOf(selectedDay);
            LocalDate date = currentWeekStart.plusDays(dayIndex);
            return selectedDay + ", " + date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + date.getDayOfMonth() + ", " + date.getYear();
        }
        LocalDate weekEnd = currentWeekStart.plusDays(5);
        return "Week " + currentWeekStart.get(WeekFields.ISO.weekOfYear()) + " • " +
               currentWeekStart.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + currentWeekStart.getDayOfMonth() + 
               " - " + weekEnd.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + weekEnd.getDayOfMonth() + ", " + currentWeekStart.getYear();
    }

    private JPanel createWeekViewPanel() {
        JPanel container = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(20, 30, 30, 30));

        // Create grid
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);

        // Header row - Days
        gbc.gridy = 0;
        gbc.weightx = 0.08;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gridPanel.add(createHeaderCell("Times", true), gbc);

        gbc.weightx = 0.15;
        for (int i = 0; i < DAYS.length; i++) {
            gbc.gridx = i + 1;
            LocalDate dayDate = currentWeekStart.plusDays(i);
            String dayText = DAYS[i] + " (" + dayDate.getDayOfMonth() + ")";
            JPanel headerCell = createDayHeaderCell(dayText, DAYS[i]);
            gridPanel.add(headerCell, gbc);
        }

        // Time slot rows
        gbc.weighty = 1.0 / TIME_SLOTS.length;
        for (int row = 0; row < TIME_SLOTS.length; row++) {
            gbc.gridy = row + 1;
            gbc.gridx = 0;
            gbc.weightx = 0.08;
            gridPanel.add(createTimeCell(TIME_SLOTS[row][0]), gbc);

            gbc.weightx = 0.15;
            for (int col = 0; col < DAYS.length; col++) {
                gbc.gridx = col + 1;
                String day = DAYS[col];
                String timeKey = TIME_SLOTS[row][1];
                CourseInfo course = getCourse(currentWeekStart, day, timeKey);
                JPanel cell = createCourseCell(course, day, timeKey);
                gridPanel.add(cell, gbc);
            }
        }

        // Legend
        JPanel legendPanel = createLegendPanel();

        container.add(gridPanel, BorderLayout.CENTER);
        container.add(legendPanel, BorderLayout.SOUTH);

        return container;
    }

    private JPanel createDayViewPanel(String day) {
        JPanel container = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(20, 30, 30, 30));

        // Day header
        JPanel dayHeader = new JPanel(new BorderLayout());
        dayHeader.setOpaque(false);
        dayHeader.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel dayTitle = new JLabel("Schedule for " + day);
        dayTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        dayTitle.setForeground(TEXT_PRIMARY);

        JButton backToWeekBtn = createActionButton("← Back to Week View", ACCENT_PURPLE, () -> {
            viewMode = "week";
            selectedDay = null;
            refreshView();
        });

        dayHeader.add(dayTitle, BorderLayout.WEST);
        dayHeader.add(backToWeekBtn, BorderLayout.EAST);

        // Time slots grid for single day
        JPanel gridPanel = new JPanel(new GridLayout(TIME_SLOTS.length, 1, 0, 10));
        gridPanel.setOpaque(false);

        for (String[] slot : TIME_SLOTS) {
            String timeKey = slot[1];
            CourseInfo course = getCourse(currentWeekStart, day, timeKey);
            JPanel slotPanel = createDayTimeSlotPanel(slot[0], timeKey, day, course);
            gridPanel.add(slotPanel);
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        container.add(dayHeader, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }

    private JPanel createDayTimeSlotPanel(String timeDisplay, String timeKey, String day, CourseInfo course) {
        Color bgColor = course != null ? course.color : new Color(248, 250, 252);
        
        JPanel panel = new JPanel(new BorderLayout(20, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(GRID_BORDER);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        panel.setPreferredSize(new Dimension(0, 80));

        // Time
        JLabel timeLabel = new JLabel(timeDisplay);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        timeLabel.setForeground(TEXT_PRIMARY);
        timeLabel.setPreferredSize(new Dimension(120, 0));

        // Course info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        if (course != null) {
            JLabel typeLabel = new JLabel(course.type);
            typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            typeLabel.setForeground(TEXT_SECONDARY);

            JLabel nameLabel = new JLabel(course.name);
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            nameLabel.setForeground(TEXT_PRIMARY);

            JLabel teacherLabel = new JLabel(course.teacher);
            teacherLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            teacherLabel.setForeground(TEXT_SECONDARY);

            JLabel roomLabel = new JLabel("Room " + course.room);
            roomLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            roomLabel.setForeground(TEXT_SECONDARY);

            infoPanel.add(typeLabel);
            infoPanel.add(nameLabel);
            infoPanel.add(teacherLabel);
            infoPanel.add(roomLabel);
        } else {
            JLabel emptyLabel = new JLabel("No class scheduled");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            emptyLabel.setForeground(TEXT_SECONDARY);
            infoPanel.add(emptyLabel);
        }

        // Add/Edit button
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        if (course != null) {
            JButton deleteBtn = createSmallButton("×", new Color(239, 68, 68), () -> {
                int confirm = JOptionPane.showConfirmDialog(this, "Remove this class?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    removeCourseFromSchedule(currentWeekStart, day, timeKey);
                    refreshView();
                }
            });
            actionPanel.add(deleteBtn);
        }

        JButton addBtn = createSmallButton(course != null ? "✎" : "+", ACCENT_PURPLE, () -> showAddCourseDialog(day, timeKey));
        actionPanel.add(addBtn);

        panel.add(timeLabel, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createHeaderCell(String text, boolean isCorner) {
        JPanel cell = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(HEADER_BG);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        cell.setBorder(new LineBorder(GRID_BORDER, 1));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(Color.WHITE);
        cell.add(label);

        return cell;
    }

    private JPanel createDayHeaderCell(String text, String day) {
        JPanel cell = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(HEADER_BG);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        cell.setBorder(new LineBorder(GRID_BORDER, 1));
        cell.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(Color.WHITE);
        cell.add(label);

        // Double click to switch to day view
        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewMode = "day";
                    selectedDay = day;
                    refreshView();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cell.setBackground(new Color(71, 85, 105));
                cell.repaint();
            }
        });

        return cell;
    }

    private JPanel createTimeCell(String time) {
        JPanel cell = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(241, 245, 249));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        cell.setBorder(new LineBorder(GRID_BORDER, 1));

        JLabel label = new JLabel("<html><center>" + time.replace(" - ", "<br>") + "</center></html>");
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(TEXT_PRIMARY);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        cell.add(label);

        return cell;
    }

    private JPanel createCourseCell(CourseInfo course, String day, String timeKey) {
        Color bgColor = course != null ? course.color : Color.WHITE;
        
        JPanel cell = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(bgColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        cell.setBorder(new LineBorder(GRID_BORDER, 1));
        cell.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (course != null) {
            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setOpaque(false);
            content.setBorder(new EmptyBorder(5, 8, 5, 8));

            JLabel typeLabel = new JLabel(course.type);
            typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 9));
            typeLabel.setForeground(new Color(100, 100, 100));
            typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel nameLabel = new JLabel("<html><b>" + course.name + "</b></html>");
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            nameLabel.setForeground(TEXT_PRIMARY);
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel teacherLabel = new JLabel(course.teacher);
            teacherLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            teacherLabel.setForeground(TEXT_SECONDARY);
            teacherLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel roomLabel = new JLabel("Room " + course.room);
            roomLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            roomLabel.setForeground(TEXT_SECONDARY);
            roomLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            content.add(typeLabel);
            content.add(nameLabel);
            content.add(teacherLabel);
            content.add(roomLabel);

            cell.add(content, BorderLayout.CENTER);
        }

        // Click handlers
        cell.addMouseListener(new MouseAdapter() {
            private Color originalBg = bgColor;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showAddCourseDialog(day, timeKey);
                } else if (e.getClickCount() == 1 && course != null && e.getButton() == MouseEvent.BUTTON3) {
                    // Right click to delete
                    int confirm = JOptionPane.showConfirmDialog(SchedulePage.this, "Remove this class?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        removeCourseFromSchedule(currentWeekStart, day, timeKey);
                        refreshView();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cell.setBackground(course != null ? course.color.darker() : new Color(241, 245, 249));
                cell.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cell.repaint();
            }
        });

        return cell;
    }

    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        legendPanel.add(createLegendItem("Lecture", LECTURE_COLOR));
        legendPanel.add(createLegendItem("Tutorial", TUTORIAL_COLOR));
        legendPanel.add(createLegendItem("Practical", PRACTICAL_COLOR));
        legendPanel.add(createLegendItem("Lab", LAB_COLOR));
        legendPanel.add(createLegendItem("Seminar", SEMINAR_COLOR));

        JLabel tipLabel = new JLabel("  •  Double-click on a cell to add/edit  •  Right-click to delete  •  Double-click day header for day view");
        tipLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        tipLabel.setForeground(TEXT_SECONDARY);
        legendPanel.add(tipLabel);

        return legendPanel;
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setOpaque(false);

        JPanel colorBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2d.setColor(GRID_BORDER);
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);
            }
        };
        colorBox.setOpaque(false);
        colorBox.setPreferredSize(new Dimension(16, 16));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        label.setForeground(TEXT_SECONDARY);

        item.add(colorBox);
        item.add(label);

        return item;
    }

    private void showAddCourseDialog(String day, String timeKey) {
        CourseInfo existing = getCourse(currentWeekStart, day, timeKey);
        
        JDialog dialog = new JDialog(this, existing != null ? "Edit Class" : "Add Class", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(25, 25, 25, 25));
        content.setBackground(Color.WHITE);

        // Type dropdown
        JLabel typeLabel = new JLabel("Class Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] types = {"Lecture", "Tutorial", "Practical", "Lab", "Seminar"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        typeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        typeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (existing != null) typeCombo.setSelectedItem(existing.type);

        // Course dropdown
        JLabel courseLabel = new JLabel("Course:");
        courseLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        courseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> courseCombo = new JComboBox<>();
        for (String c : availableCourses) courseCombo.addItem(c);
        courseCombo.setEditable(true);
        courseCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        courseCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (existing != null) courseCombo.setSelectedItem(existing.name);

        // Teacher field
        JLabel teacherLabel = new JLabel("Teacher:");
        teacherLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        teacherLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField teacherField = new JTextField(existing != null ? existing.teacher : "Mr. ");
        teacherField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        teacherField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Room field
        JLabel roomLabel = new JLabel("Room:");
        roomLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        roomLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField roomField = new JTextField(existing != null ? existing.room : "A-101");
        roomField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        roomField.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(typeLabel);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(typeCombo);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(courseLabel);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(courseCombo);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(teacherLabel);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(teacherField);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(roomLabel);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(roomField);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(Color.WHITE);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton saveBtn = new JButton(existing != null ? "Update" : "Add");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveBtn.setBackground(ACCENT_PURPLE);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            String course = (String) courseCombo.getSelectedItem();
            String teacher = teacherField.getText().trim();
            String room = roomField.getText().trim();

            if (course == null || course.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please select a course!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Color color = getColorForType(type);
            CourseInfo newCourse = new CourseInfo(type, course, teacher, room, color);
            addCourseToSchedule(currentWeekStart, day, timeKey, newCourse);
            refreshView();
            dialog.dispose();
        });

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        dialog.add(content, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private Color getColorForType(String type) {
        switch (type) {
            case "Lecture": return LECTURE_COLOR;
            case "Tutorial": return TUTORIAL_COLOR;
            case "Practical": return PRACTICAL_COLOR;
            case "Lab": return LAB_COLOR;
            case "Seminar": return SEMINAR_COLOR;
            default: return LECTURE_COLOR;
        }
    }

    private void refreshView() {
        weekLabel.setText(getWeekDisplayText());
        
        Container parent = mainContentPanel.getParent();
        parent.remove(mainContentPanel);
        
        if (viewMode.equals("day") && selectedDay != null) {
            mainContentPanel = createDayViewPanel(selectedDay);
        } else {
            mainContentPanel = createWeekViewPanel();
        }
        
        parent.add(mainContentPanel, BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();

        // Refresh header for view mode buttons
        Component[] components = getContentPane().getComponents();
        for (Component c : components) {
            if (c instanceof JPanel) {
                JPanel p = (JPanel) c;
                if (p != mainContentPanel) {
                    Container headerParent = p.getParent();
                    headerParent.remove(p);
                    headerParent.add(createHeaderPanel(), BorderLayout.NORTH);
                    break;
                }
            }
        }
        revalidate();
        repaint();
    }

    private JButton createNavButton(String text, Runnable onClick) {
        JButton button = new JButton(text) {
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
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.setColor(TEXT_PRIMARY);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(40, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> onClick.run());
        return button;
    }

    private JButton createTodayButton() {
        JButton button = new JButton("Today") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(126, 34, 206));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(147, 51, 234));
                } else {
                    g2d.setColor(ACCENT_PURPLE);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(70, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> {
            currentWeekStart = getStartOfWeek(LocalDate.now());
            if (viewMode.equals("day")) {
                selectedDay = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            }
            refreshView();
        });
        return button;
    }

    private JButton createActionButton(String text, Color color, Runnable onClick) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(color.brighter());
                } else {
                    g2d.setColor(color);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> onClick.run());
        return button;
    }

    private JButton createSmallButton(String text, Color color, Runnable onClick) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
                    g2d.fillOval(0, 0, getWidth(), getHeight());
                }
                g2d.setColor(color);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(32, 32));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> onClick.run());
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
        button.setPreferredSize(new Dimension(90, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> {
            if (isTeacher) {
                mainpageTeacher teacherPage = new mainpageTeacher();
                teacherPage.setVisible(true);
            } else {
                mainpageStudent studentPage = new mainpageStudent();
                studentPage.setVisible(true);
            }
            dispose();
        });
        return button;
    }

    // Inner class for course information
    private static class CourseInfo {
        String type;
        String name;
        String teacher;
        String room;
        Color color;

        CourseInfo(String type, String name, String teacher, String room, Color color) {
            this.type = type;
            this.name = name;
            this.teacher = teacher;
            this.room = room;
            this.color = color;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SchedulePage().setVisible(true));
    }
}
