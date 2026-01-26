package Backend.src.studentassign;

import Backend.main.MainPageTeacher;
import Backend.src.database.DatabaseManager;
import Backend.src.database.StudentInfoManager;
import java.util.Scanner;

public class studentassign {
    public static void main(String[] args) {
        // Create the studentInfo table if it doesn't exist
        StudentInfoManager.createStudentInfoTable();

        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        while (!exit) {
            System.out.println("=".repeat(20));
            System.out.println("Welcome to Student information management");
            System.out.println("=".repeat(20));
            System.out.println("1. Add Student information");
            System.out.println("2. Update Student information");
            System.out.println("3. Delete Student information");
            System.out.println("4. Exit");
            System.out.print("Choose an option to proceed: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline after nextInt()

            switch (choice) {
                case 1:
                    System.out.println("Adding Student information...");
                    // Add your student management logic here
                    System.out.print("Enter student name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter student ID: ");
                    String studentID = scanner.next();

                    // Check if UserID exists in users table
                    if (!DatabaseManager.ConditionChecker.checkUserIDExists(studentID)) {
                        System.out.println(" UserID not found in users table. Student not added.");
                        break;
                    }

                    System.out.print("Enter student gender: ");
                    String gender = scanner.next();
                    System.out.print("Enter student year: ");
                    String year = scanner.next();
                    StudentInfoManager.saveStudentInfo(name, studentID, gender, year);
                    MainPageTeacher.main(args);
                    break;
                case 2:
                    System.out.println("Updating Student information...");
                    System.out.print("Enter student ID to update: ");
                    String idToUpdate = scanner.next();
                    scanner.nextLine();

                    // Check if student exists
                    String[] studentInfo = StudentInfoManager.getStudentInfo(idToUpdate);
                    if (studentInfo == null) {
                        System.out.println(" Student ID not found!");
                        break;
                    }

                    System.out.println("\nStudent Found:");
                    System.out.println("Name: " + studentInfo[0]);
                    System.out.println("ID: " + studentInfo[1]);
                    System.out.println("Gender: " + studentInfo[2]);
                    System.out.println("Year: " + studentInfo[3]);

                    System.out.println("\nWhat do you want to update?");
                    System.out.println("1. Gender");
                    System.out.println("2. Year");
                    System.out.print("Enter your choice (1-2): ");
                    int updateChoice = scanner.nextInt();
                    scanner.nextLine();

                    String updatedGender = studentInfo[2];
                    String updatedYear = studentInfo[3];

                    switch (updateChoice) {
                        case 1:
                            System.out.print("Enter new gender: ");
                            updatedGender = scanner.nextLine();
                            break;
                        case 2:
                            System.out.print("Enter new year: ");
                            updatedYear = scanner.nextLine();
                            break;
                        default:
                            System.out.println("Invalid choice!");
                            break;
                    }

                    if (StudentInfoManager.updateStudentInfo(idToUpdate, studentInfo[0], updatedGender, updatedYear)) {
                        System.out.println(" Student information updated successfully!");
                        MainPageTeacher.main(args);
                    } else {
                        System.out.println(" Failed to update student information.");
                    }
                    break;
                case 3:
                    System.out.println("Deleting Student information...");
                    // Add your attendance management logic here
                    System.out.print("Enter student ID to delete: ");
                    String idToDelete = scanner.next();
                    StudentInfoManager.deleteStudent(idToDelete);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    // Add your reports generation logic here
                    MainPageTeacher.main(args);
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }

        }
    }
}