package Frontend.ui;

import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SchedulePage extends JFrame {

    // Colors matching the theme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_PURPLE = new Color(168, 85, 247);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_PINK = new Color(236, 72, 153);
    private static final Color ACCENT_CYAN = new Color(63, 240, 199);
    private static final Color ACCENT_YELLOW = new Color(234, 179, 8);

    // Class colors palette
    private static final Color[] CLASS_COLORS = {
        ACCENT_BLUE, ACCENT_GREEN, ACCENT_PURPLE, ACCENT_ORANGE, 
        ACCENT_PINK, ACCENT_CYAN, ACCENT_YELLOW, ACCENT_RED
    };

    // Schedule data structure
    private Map<LocalDate, List<ScheduledClass>> scheduleData = new HashMap<>();
    private Set<ScheduledClass> missedClasses = new HashSet<>();
    private YearMonth currentMonth;
    private JPanel calendarPanel;
    private JPanel classListPanel;
    
    // Available classes
    private List<ClassInfo> availableClasses = new ArrayList<>();
    
    // Navigation flag - true for teacher, false for student
    private boolean isTeacher;

    public SchedulePage() {
        this(true); // Default to teacher
    }
    
    public SchedulePage(boolean isTeacher) {
        this.isTeacher = isTeacher;
        currentMonth = YearMonth.now();
        initSampleData();
        initComponent();
    }

    private void initSampleData() {
        // Sample classes
        availableClasses.add(new ClassInfo("Mathematics", CLASS_COLORS[0]));
        availableClasses.add(new ClassInfo("Physics", CLASS_COLORS[1]));
        availableClasses.add(new ClassInfo("Computer Science", CLASS_COLORS[2]));
        availableClasses.add(new ClassInfo("English", CLASS_COLORS[3]));
        availableClasses.add(new ClassInfo("Chemistry", CLASS_COLORS[4]));

        // Sample schedule
        LocalDate today = LocalDate.now();
        addClassToDate(today, new ScheduledClass("Mathematics", "09:00", "10:30", CLASS_COLORS[0]));
        addClassToDate(today, new ScheduledClass("Physics", "11:00", "12:30", CLASS_COLORS[1]));
        addClassToDate(today.plusDays(1), new ScheduledClass("Computer Science", "14:00", "15:30", CLASS_COLORS[2]));
        addClassToDate(today.plusDays(2), new ScheduledClass("English", "10:00", "11:30", CLASS_COLORS[3]));
        addClassToDate(today.plusDays(3), new ScheduledClass("Chemistry", "09:00", "10:30", CLASS_COLORS[4]));
    }

    private void addClassToDate(LocalDate date, ScheduledClass scheduledClass) {
        scheduleData.computeIfAbsent(date, k -> new ArrayList<>()).add(scheduledClass);
    }

    public void initComponent() {
        setTitle("Class Schedule");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);

        // Main background panel
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

        JPanel headerPanel = createHeaderPanel();
        JPanel contentPanel = createContentPanel();

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

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

                GradientPaint purpleGradient = new GradientPaint(
                    0, 0, new Color(168, 85, 247, 200),
                    0, getHeight(), new Color(192, 132, 252, 150)
                );
                g2d.setPaint(purpleGradient);
                g2d.fillRoundRect(0, 0, 6, getHeight(), 20, 20);

                g2d.setColor(new Color(168, 85, 247, 8));
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight(), 20, 20);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleContainer.setOpaque(false);

        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, ACCENT_PURPLE,
                    getWidth(), getHeight(), new Color(126, 34, 206)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(55, 55));
        iconBadge.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel("📅");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconBadge.add(iconLabel);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Class Schedule");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("View and manage your class schedule");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(subtitleLabel);

        titleContainer.add(iconBadge);
        titleContainer.add(titlePanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton addClassBtn = createAddClassButton();
        JButton backButton = createBackButton();
        
        buttonPanel.add(addClassBtn);
        buttonPanel.add(backButton);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(15, 30, 30, 30));

        // Left side - Calendar
        JPanel leftPanel = createCalendarPanel();
        
        // Right side - Class list for selected date
        JPanel rightPanel = createClassListContainer();

        contentPanel.add(leftPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);

        return contentPanel;
    }

    private JPanel createCalendarPanel() {
        JPanel container = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 2, 20, 20);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Month navigation
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JButton prevBtn = createNavButton("◀", () -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
        });
        
        JLabel monthLabel = new JLabel(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + currentMonth.getYear());
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        monthLabel.setForeground(TEXT_PRIMARY);
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton nextBtn = createNavButton("▶", () -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar();
        });

        navPanel.add(prevBtn, BorderLayout.WEST);
        navPanel.add(monthLabel, BorderLayout.CENTER);
        navPanel.add(nextBtn, BorderLayout.EAST);

        // Day headers
        JPanel dayHeaderPanel = new JPanel(new GridLayout(1, 7, 5, 5));
        dayHeaderPanel.setOpaque(false);
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            dayLabel.setForeground(TEXT_SECONDARY);
            dayHeaderPanel.add(dayLabel);
        }

        // Calendar grid
        calendarPanel = new JPanel(new GridLayout(6, 7, 5, 5));
        calendarPanel.setOpaque(false);
        updateCalendarDays();

        container.add(navPanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(dayHeaderPanel, BorderLayout.NORTH);
        centerPanel.add(calendarPanel, BorderLayout.CENTER);
        container.add(centerPanel, BorderLayout.CENTER);

        // Legend
        JPanel legendPanel = createCalendarLegend();
        container.add(legendPanel, BorderLayout.SOUTH);

        return container;
    }

    private void updateCalendar() {
        // Update month label
        Container parent = calendarPanel.getParent();
        while (parent != null && !(parent.getLayout() instanceof BorderLayout)) {
            parent = parent.getParent();
        }
        if (parent != null) {
            BorderLayout layout = (BorderLayout) parent.getLayout();
            Component north = layout.getLayoutComponent(BorderLayout.NORTH);
            if (north instanceof JPanel) {
                JPanel navPanel = (JPanel) north;
                BorderLayout navLayout = (BorderLayout) navPanel.getLayout();
                Component center = navLayout.getLayoutComponent(BorderLayout.CENTER);
                if (center instanceof JLabel) {
                    ((JLabel) center).setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + currentMonth.getYear());
                }
            }
        }
        updateCalendarDays();
    }

    private void updateCalendarDays() {
        calendarPanel.removeAll();

        LocalDate firstOfMonth = currentMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday = 0
        int daysInMonth = currentMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();

        // Empty cells before first day
        for (int i = 0; i < dayOfWeek; i++) {
            calendarPanel.add(createEmptyDayCell());
        }

        // Day cells
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            List<ScheduledClass> classes = scheduleData.getOrDefault(date, new ArrayList<>());
            boolean isToday = date.equals(today);
            calendarPanel.add(createDayCell(day, date, classes, isToday));
        }

        // Fill remaining cells
        int remainingCells = 42 - (dayOfWeek + daysInMonth);
        for (int i = 0; i < remainingCells; i++) {
            calendarPanel.add(createEmptyDayCell());
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private JPanel createEmptyDayCell() {
        JPanel cell = new JPanel();
        cell.setOpaque(false);
        return cell;
    }

    private JPanel createDayCell(int day, LocalDate date, List<ScheduledClass> classes, boolean isToday) {
        JPanel cell = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isToday) {
                    g2d.setColor(new Color(59, 130, 246, 30));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2d.setColor(ACCENT_BLUE);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 10, 10);
                } else {
                    g2d.setColor(new Color(248, 250, 252));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
            }
        };
        cell.setLayout(new BorderLayout(2, 2));
        cell.setBorder(new EmptyBorder(5, 5, 5, 5));
        cell.setOpaque(false);
        cell.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Day number
        JLabel dayLabel = new JLabel(String.valueOf(day));
        dayLabel.setFont(new Font("Segoe UI", isToday ? Font.BOLD : Font.PLAIN, 14));
        dayLabel.setForeground(isToday ? ACCENT_BLUE : TEXT_PRIMARY);
        cell.add(dayLabel, BorderLayout.NORTH);

        // Class indicators
        if (!classes.isEmpty()) {
            JPanel classIndicators = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
            classIndicators.setOpaque(false);
            
            int maxIndicators = Math.min(classes.size(), 3);
            for (int i = 0; i < maxIndicators; i++) {
                ScheduledClass sc = classes.get(i);
                JPanel dot = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        Color dotColor = sc.color;
                        if (missedClasses.contains(sc)) {
                            // Draw strikethrough for missed
                            g2d.setColor(new Color(dotColor.getRed(), dotColor.getGreen(), dotColor.getBlue(), 100));
                            g2d.fillOval(0, 0, getWidth(), getHeight());
                            g2d.setColor(ACCENT_RED);
                            g2d.setStroke(new BasicStroke(2));
                            g2d.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
                        } else {
                            g2d.setColor(dotColor);
                            g2d.fillOval(0, 0, getWidth(), getHeight());
                        }
                    }
                };
                dot.setOpaque(false);
                dot.setPreferredSize(new Dimension(10, 10));
                classIndicators.add(dot);
            }
            
            if (classes.size() > 3) {
                JLabel moreLabel = new JLabel("+" + (classes.size() - 3));
                moreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                moreLabel.setForeground(TEXT_SECONDARY);
                classIndicators.add(moreLabel);
            }
            
            cell.add(classIndicators, BorderLayout.SOUTH);
        }

        // Click to show details
        cell.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showClassesForDate(date, classes);
            }
        });

        return cell;
    }

    private void showClassesForDate(LocalDate date, List<ScheduledClass> classes) {
        updateClassListPanel(date, classes);
    }

    private JPanel createClassListContainer() {
        JPanel container = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 2, 20, 20);

                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        container.setOpaque(false);
        container.setPreferredSize(new Dimension(280, 0));
        container.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Classes for Today");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(TEXT_PRIMARY);
        container.add(headerLabel, BorderLayout.NORTH);

        classListPanel = new JPanel();
        classListPanel.setLayout(new BoxLayout(classListPanel, BoxLayout.Y_AXIS));
        classListPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(classListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        container.add(scrollPane, BorderLayout.CENTER);

        // Show today's classes by default
        updateClassListPanel(LocalDate.now(), scheduleData.getOrDefault(LocalDate.now(), new ArrayList<>()));

        return container;
    }

    private void updateClassListPanel(LocalDate date, List<ScheduledClass> classes) {
        classListPanel.removeAll();
        classListPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Update header
        Container parent = classListPanel.getParent();
        while (parent != null) {
            if (parent.getLayout() instanceof BorderLayout) {
                BorderLayout layout = (BorderLayout) parent.getLayout();
                Component north = layout.getLayoutComponent(BorderLayout.NORTH);
                if (north instanceof JLabel) {
                    ((JLabel) north).setText("Classes for " + date.getDayOfMonth() + " " + 
                        date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
                }
                break;
            }
            parent = parent.getParent();
        }

        if (classes.isEmpty()) {
            JLabel emptyLabel = new JLabel("No classes scheduled");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            emptyLabel.setForeground(TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            classListPanel.add(emptyLabel);
        } else {
            for (ScheduledClass sc : classes) {
                classListPanel.add(createClassCard(sc, date));
                classListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        classListPanel.revalidate();
        classListPanel.repaint();
    }

    private JPanel createClassCard(ScheduledClass sc, LocalDate date) {
        boolean isMissed = missedClasses.contains(sc);
        
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(248, 250, 252));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Left color bar
                g2d.setColor(isMissed ? new Color(sc.color.getRed(), sc.color.getGreen(), sc.color.getBlue(), 100) : sc.color);
                g2d.fillRoundRect(0, 0, 5, getHeight(), 12, 12);
                g2d.fillRect(5, 0, 5, getHeight());

                // Strikethrough if missed
                if (isMissed) {
                    g2d.setColor(ACCENT_RED);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawLine(15, getHeight()/2, getWidth() - 15, getHeight()/2);
                }
            }
        };
        card.setLayout(new BorderLayout(10, 5));
        card.setBorder(new EmptyBorder(12, 15, 12, 12));
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(sc.className);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(isMissed ? TEXT_SECONDARY : TEXT_PRIMARY);

        JLabel timeLabel = new JLabel(sc.startTime + " - " + sc.endTime);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(TEXT_SECONDARY);

        textPanel.add(nameLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        textPanel.add(timeLabel);

        // Miss button
        JButton missBtn = new JButton(isMissed ? "✓" : "✗") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isRollover()) {
                    g2d.setColor(isMissed ? new Color(34, 197, 94, 30) : new Color(239, 68, 68, 30));
                } else {
                    g2d.setColor(new Color(241, 245, 249));
                }
                g2d.fillOval(0, 0, getWidth(), getHeight());

                g2d.setColor(isMissed ? ACCENT_GREEN : ACCENT_RED);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        missBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        missBtn.setContentAreaFilled(false);
        missBtn.setBorderPainted(false);
        missBtn.setFocusPainted(false);
        missBtn.setPreferredSize(new Dimension(35, 35));
        missBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        missBtn.setToolTipText(isMissed ? "Mark as attended" : "Mark as missed");

        missBtn.addActionListener(e -> {
            if (missedClasses.contains(sc)) {
                missedClasses.remove(sc);
            } else {
                missedClasses.add(sc);
            }
            updateCalendarDays();
            updateClassListPanel(date, scheduleData.getOrDefault(date, new ArrayList<>()));
        });

        card.add(textPanel, BorderLayout.CENTER);
        card.add(missBtn, BorderLayout.EAST);

        return card;
    }

    private JPanel createCalendarLegend() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        legendPanel.setOpaque(false);

        for (ClassInfo classInfo : availableClasses) {
            JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            item.setOpaque(false);

            JPanel colorDot = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(classInfo.color);
                    g2d.fillOval(0, 0, getWidth(), getHeight());
                }
            };
            colorDot.setOpaque(false);
            colorDot.setPreferredSize(new Dimension(12, 12));

            JLabel label = new JLabel(classInfo.name);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            label.setForeground(TEXT_SECONDARY);

            item.add(colorDot);
            item.add(label);
            legendPanel.add(item);
        }

        return legendPanel;
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

    private JButton createAddClassButton() {
        JButton button = new JButton("+ Add Class") {
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

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.setColor(Color.WHITE);
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
        button.setPreferredSize(new Dimension(120, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> showAddClassDialog());

        return button;
    }

    private void showAddClassDialog() {
        JDialog dialog = new JDialog(this, "Add New Class", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Class selection
        JLabel classLabel = new JLabel("Select Class:");
        classLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        classLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JComboBox<String> classCombo = new JComboBox<>();
        for (ClassInfo ci : availableClasses) {
            classCombo.addItem(ci.name);
        }
        classCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        classCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Date selection
        JLabel dateLabel = new JLabel("Select Date:");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        dateSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Time selection
        JLabel startTimeLabel = new JLabel("Start Time (HH:MM):");
        startTimeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        startTimeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField startTimeField = new JTextField("09:00");
        startTimeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        startTimeField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel endTimeLabel = new JLabel("End Time (HH:MM):");
        endTimeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        endTimeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField endTimeField = new JTextField("10:30");
        endTimeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        endTimeField.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(classLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(classCombo);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(dateLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(dateSpinner);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(startTimeLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(startTimeField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(endTimeLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(endTimeField);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton addBtn = new JButton("Add");
        addBtn.setBackground(ACCENT_PURPLE);
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> {
            String className = (String) classCombo.getSelectedItem();
            Date selectedDate = (Date) dateSpinner.getValue();
            LocalDate date = selectedDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            String startTime = startTimeField.getText();
            String endTime = endTimeField.getText();

            Color classColor = CLASS_COLORS[classCombo.getSelectedIndex() % CLASS_COLORS.length];
            ScheduledClass newClass = new ScheduledClass(className, startTime, endTime, classColor);
            addClassToDate(date, newClass);
            updateCalendarDays();
            dialog.dispose();

            JOptionPane.showMessageDialog(this, "Class added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanel.add(cancelBtn);
        buttonPanel.add(addBtn);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
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

    // Inner classes for data structures
    private static class ScheduledClass {
        String className;
        String startTime;
        String endTime;
        Color color;

        ScheduledClass(String className, String startTime, String endTime, Color color) {
            this.className = className;
            this.startTime = startTime;
            this.endTime = endTime;
            this.color = color;
        }
    }

    private static class ClassInfo {
        String name;
        Color color;

        ClassInfo(String name, Color color) {
            this.name = name;
            this.color = color;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SchedulePage().setVisible(true));
    }
}
