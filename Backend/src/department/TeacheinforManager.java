package Backend.src.department;

import Backend.src.course.Course;
import Backend.src.database.CourseManager;
import Backend.src.database.DatabaseManager;
import Backend.src.database.TeacherCourseManager;
import Backend.src.database.TeacherInfoManager;
import Backend.src.major.major;
import java.util.ArrayList;
import java.util.Scanner;

public class TeacheinforManager {
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private Scanner input = new Scanner(System.in);

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public void displayAllDepartments() {
        System.out.println("\n Available Departments:");
        System.out.println("1. " + Department.GIC.getDisplayName());
        System.out.println("2. " + Department.GIM.getDisplayName());
        System.out.println("3. " + Department.GEE.getDisplayName());
    }

    public void viewTeachersByDepartment(Scanner input) {
        System.out.println("\n--- View Teachers by Department ---");
        displayAllDepartments();
        System.out.print("Enter department choice (1-3): ");
        int deptChoice = input.nextInt();
        input.nextLine();

        String selectedDepartment = "";
        switch (deptChoice) {
            case 1:
                selectedDepartment = Department.GIC.getDisplayName();
                break;
            case 2:
                selectedDepartment = Department.GIM.getDisplayName();
                break;
            case 3:
                selectedDepartment = Department.GEE.getDisplayName();
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        System.out.println("\n Teachers in " + selectedDepartment + " Department:");
        boolean found = false;
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).getDepartment() != null &&
                    teachers.get(i).getDepartment().equals(selectedDepartment)) {
                System.out.println((i + 1) + ". " + teachers.get(i));
                found = true;
            }
        }
        if (!found) {
            System.out.println(" No teachers in " + selectedDepartment + " department!");
        }
    }

    public void addTeacherToDepartment() {
        System.out.println("\n--- Add Teacher to Department ---");

        if (teachers.isEmpty()) {
            System.out.println(" No teachers available!");
            return;
        }

        System.out.println(" Available Teachers:");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i));
        }
        String teacherId = "";
        Teacher targetTeacher = null;
        while (targetTeacher == null) {
            System.out.print("Enter Teacher ID to assign to department (or 'q' to cancel): ");
            teacherId = input.nextLine();
            if (teacherId.equalsIgnoreCase("q")) {
                System.out.println("Cancelled.");
                return;
            }

            // Check if UserID exists in users table
            boolean userIDExists = DatabaseManager.ConditionChecker.checkUserIDExists(teacherId);
            if (!userIDExists) {
                System.out.println(" UserID not found in users table. Please try again.");
                continue;
            }

            for (Teacher teacher : teachers) {
                if (teacher.getTeacherId().equals(teacherId)) {
                    targetTeacher = teacher;
                    break;
                }
            }
            if (targetTeacher == null) {
                System.out.println(" Teacher ID not found. Please try again.");
            }
        }

        displayAllDepartments();
        System.out.print("Choose department (1-3): ");
        int deptChoice = input.nextInt();
        input.nextLine();

        String department = "";
        switch (deptChoice) {
            case 1:
                department = Department.GIC.getDisplayName();
                break;
            case 2:
                department = Department.GIM.getDisplayName();
                break;
            case 3:
                department = Department.GEE.getDisplayName();
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        targetTeacher.setDepartment(department);
        System.out.println(" Teacher assigned to " + department + " department successfully!");
        return;
    }

    public void removeTeacherFromDepartment() {
        System.out.println("\n--- Remove Teacher from Department ---");

        if (teachers.isEmpty()) {
            System.out.println(" No teachers available!");
            return;
        }

        System.out.println(" Available Teachers:");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i));
        }
        String teacherId = "";
        Teacher targetTeacher = null;
        while (targetTeacher == null) {
            System.out.print("Enter Teacher ID to remove from department (or 'q' to cancel): ");
            teacherId = input.nextLine();
            if (teacherId.equalsIgnoreCase("q")) {
                System.out.println("Cancelled.");
                return;
            }
            for (Teacher teacher : teachers) {
                if (teacher.getTeacherId().equals(teacherId)) {
                    targetTeacher = teacher;
                    break;
                }
            }
            if (targetTeacher == null) {
                System.out.println(" Teacher ID not found. Please try again.");
            }
        }

        if (targetTeacher.getDepartment() == null || targetTeacher.getDepartment().isEmpty()) {
            System.out.println(" Teacher is not assigned to any department!");
        } else {
            String prevDept = targetTeacher.getDepartment();
            targetTeacher.setDepartment(null);
            System.out.println(" Teacher removed from " + prevDept + " department successfully!");
        }
        return;
    }

    public void updateTeacherDepartment() {
        System.out.println("\n--- Update Teacher Department ---");

        if (teachers.isEmpty()) {
            System.out.println(" No teachers available!");
            return;
        }

        System.out.println("Available Teachers:");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i));
        }
        String teacherId = "";
        Teacher targetTeacher = null;
        while (targetTeacher == null) {
            System.out.print("Enter Teacher ID to update department (or 'q' to cancel): ");
            teacherId = input.nextLine();
            if (teacherId.equalsIgnoreCase("q")) {
                System.out.println("Cancelled.");
                return;
            }
            for (Teacher teacher : teachers) {
                if (teacher.getTeacherId().equals(teacherId)) {
                    targetTeacher = teacher;
                    break;
                }
            }
            if (targetTeacher == null) {
                System.out.println(" Teacher ID not found. Please try again.");
            }
        }

        displayAllDepartments();
        System.out.print("Choose new department (1-3): ");
        int deptChoice = input.nextInt();
        input.nextLine();

        String department = "";
        switch (deptChoice) {
            case 1:
                department = Department.GIC.getDisplayName();
                break;
            case 2:
                department = Department.GIM.getDisplayName();
                break;
            case 3:
                department = Department.GEE.getDisplayName();
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        targetTeacher.setDepartment(department);

        // Select Major based on department
        System.out.println("\n--- Select Major ---");
        String selectedMajor = selectMajorForDepartment(department);
        if (selectedMajor == null) {
            System.out.println("Major selection cancelled.");
            return;
        }

        targetTeacher.setMajor(selectedMajor);
        System.out.println(" Major selected: " + selectedMajor);

        // Select Multiple Courses based on major
        System.out.println("\n--- Select Courses ---");
        String[] courses = Course.getCoursesForMajor(selectedMajor);
        ArrayList<String> selectedCourses = new ArrayList<>();
        ArrayList<String> selectedCourseIds = new ArrayList<>();

        if (courses != null && courses.length > 0) {
            boolean addingCourses = true;
            while (addingCourses) {
                displayCoursesForMajor(courses);
                System.out.print("Choose a course (1-" + courses.length + ") or 'q' to finish: ");
                String input_choice = input.nextLine();

                if (input_choice.equalsIgnoreCase("q")) {
                    addingCourses = false;
                } else {
                    try {
                        int courseChoice = Integer.parseInt(input_choice);
                        if (courseChoice > 0 && courseChoice <= courses.length) {
                            String selectedCourse = courses[courseChoice - 1];

                            // Check if already selected
                            if (!selectedCourses.contains(selectedCourse)) {
                                selectedCourses.add(selectedCourse);

                                // Get course ID from course name
                                String courseId = CourseManager.getCourseIdByName(selectedCourse);
                                if (courseId != null) {
                                    selectedCourseIds.add(courseId);
                                    System.out.println(" Course selected: " + selectedCourse + " (" + courseId + ")");
                                } else {
                                    System.out.println(" Course ID not found for: " + selectedCourse);
                                    selectedCourses.remove(selectedCourse);
                                }
                            } else {
                                System.out.println(" This course is already selected!");
                            }
                        } else {
                            System.out.println("Invalid course choice!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number!");
                    }
                }
            }

            if (!selectedCourses.isEmpty() && !selectedCourseIds.isEmpty()) {
                // Courses joined for display
                String coursesJoined = String.join(", ", selectedCourses);
                targetTeacher.setCourse(coursesJoined);

                // Now save all courses to teacher_course table automatically
                int successCount = 0;
                for (String courseId : selectedCourseIds) {
                    boolean courseSaved = TeacherCourseManager.addTeacherCourse(teacherId, courseId);
                    if (courseSaved) {
                        successCount++;
                    } else {
                        System.out.println(
                                "⚠ Warning: Could not save course " + courseId + " for teacher " + teacherId);
                    }
                }

                // Get the actual course count from teacher_course table
                int courseCount = TeacherInfoManager.getCourseCountForTeacher(teacherId);

                // Save teacher info to database with courses and course count
                boolean saved = TeacherInfoManager.updateTeacherInfo(teacherId, department, selectedMajor);
                if (saved) {
                    System.out.println("\nTeacher information saved to teacherInfo table.");
                    System.out.println(
                            "Successfully assigned " + successCount + " course(s) to the teacher_course table.");
                    System.out.println("\nSuccessfully updated teacher with:");
                    System.out.println("  - Department: " + department);
                    System.out.println("  - Major: " + selectedMajor);
                    System.out.println("  - Courses: " + coursesJoined);
                    System.out.println("  - Total Course Count: " + courseCount);
                } else {
                    System.out.println("\nFailed to save teacher information to database.");
                }
            } else {
                System.out.println("No courses selected or course IDs could not be found!");
            }
        } else {
            System.out.println(" No courses available for this major!");
        }
        return;
    }

    private String selectMajorForDepartment(String department) {
        String selectedMajor = null;

        if (department.equals(Department.GIC.getDisplayName())) {
            selectedMajor = major.getGICMajor();
        } else if (department.equals(Department.GIM.getDisplayName())) {
            selectedMajor = major.getGIMMajor();
        } else if (department.equals(Department.GEE.getDisplayName())) {
            selectedMajor = major.getGEEMajor();
        }

        return selectedMajor;
    }

    private void displayCoursesForMajor(String[] courses) {
        for (int i = 0; i < courses.length; i++) {
            System.out.println((i + 1) + ". " + courses[i]);
        }
    }
}
