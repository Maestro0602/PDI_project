package Frontend;

import java.awt.*;
import javax.swing.*;

public class CourseSchedule extends JFrame {
    
    public CourseSchedule() {
        setTitle("Weekly Course Schedule");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel("Weekly Course Schedule", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(41, 98, 255));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Schedule panel with scroll
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BoxLayout(schedulePanel, BoxLayout.Y_AXIS));
        schedulePanel.setBackground(Color.WHITE);
        
        // Monday - 4 hours
        schedulePanel.add(createDayPanel("MONDAY", new String[][]{
            {"Mathematics 101", "9:00 AM - 11:00 AM", "Room 201"},
            {"English Literature", "1:00 PM - 3:00 PM", "Room 150"}
        }));
        
        // Tuesday - 2 hours
        schedulePanel.add(createDayPanel("TUESDAY", new String[][]{
            {"Computer Science Lab", "2:00 PM - 4:00 PM", "Lab 305"}
        }));
        
        // Wednesday - 4 hours
        schedulePanel.add(createDayPanel("WEDNESDAY", new String[][]{
            {"Physics", "10:00 AM - 12:00 PM", "Room 180"},
            {"Chemistry", "2:00 PM - 4:00 PM", "Lab Building A"}
        }));
        
        // Thursday - 1 hour
        schedulePanel.add(createDayPanel("THURSDAY", new String[][]{
            {"History", "11:00 AM - 12:00 PM", "Room 220"}
        }));
        
        // Friday - 2 hours
        schedulePanel.add(createDayPanel("FRIDAY", new String[][]{
            {"Biology Lab", "9:00 AM - 11:00 AM", "Lab 102"}
        }));
        
        JScrollPane scrollPane = new JScrollPane(schedulePanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createDayPanel(String day, String[][] courses) {
        JPanel dayPanel = new JPanel();
        dayPanel.setLayout(new BorderLayout(10, 10));
        dayPanel.setBackground(Color.WHITE);
        dayPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        dayPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        // Day header
        JLabel dayLabel = new JLabel(day);
        dayLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dayLabel.setForeground(new Color(51, 51, 51));
        dayLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
        
        // Courses for the day
        JPanel coursesPanel = new JPanel();
        coursesPanel.setLayout(new GridLayout(courses.length, 1, 0, 8));
        coursesPanel.setBackground(Color.WHITE);
        
        for (String[] courseInfo : courses) {
            coursesPanel.add(createCoursePanel(courseInfo[0], courseInfo[1], courseInfo[2]));
        }
        
        dayPanel.add(dayLabel, BorderLayout.NORTH);
        dayPanel.add(coursesPanel, BorderLayout.CENTER);
        
        return dayPanel;
    }
    
    private JPanel createCoursePanel(String course, String time, String room) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 5));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 240), 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        // Course name
        JLabel courseLabel = new JLabel(course);
         courseLabel.setFont(new Font("Arial", Font.BOLD, 15));
        courseLabel.setForeground(new Color(51, 51, 51));
        
        // Time and room info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        infoPanel.setBackground(new Color(240, 248, 255));
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        timeLabel.setForeground(new Color(85, 85, 85));
        
        JLabel roomLabel = new JLabel(room);
        roomLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        roomLabel.setForeground(new Color(85, 85, 85));
        
        infoPanel.add(timeLabel);
        infoPanel.add(roomLabel);
        
        panel.add(courseLabel, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CourseSchedule());
    }
}