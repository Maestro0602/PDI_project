package Backend.src.course;

import java.util.Scanner;

public class Course {

    /**
     * Get 4 courses for a specific major
     * 
     * param major The major name
     * return Array of 4 course names, or null if major not found
     */
    public static String[] getCoursesForMajor(String major) {
        switch (major) {
            // GIC Department Majors
            case "Software Engineering":
                return new String[] {
                        "Programming Fundamentals",
                        "Object-Oriented Programming",
                        "Software Engineering Principles",
                        "Database Design"
                };
            case "Cyber Security":
                return new String[] {
                        "Computer and Network Security",
                        "Ethical Hacking Fundamentals",
                        "Cyber Security Management",
                        "Cryptography Basics"
                };
            case "Artificial Intelligence":
                return new String[] {
                        "Introduction to Artificial Intelligence",
                        "Machine Learning",
                        "Data Science Fundamentals",
                        "Deep Learning"
                };

            // GIM Department Majors
            case "Mechanical Engineering":
                return new String[] {
                        "Engineering Mechanics",
                        "Thermodynamics",
                        "Machine Design",
                        "Fluid Mechanics"
                };
            case "Manufacturing Engineering":
                return new String[] {
                        "Manufacturing Processes",
                        "Computer-Aided Manufacturing",
                        "Production Planning and Control",
                        "Industrial Equipment"
                };
            case "Industrial Engineering":
                return new String[] {
                        "Operations Research",
                        "Quality Control and Assurance",
                        "Supply Chain Management",
                        "Work Study and Ergonomics"
                };

            // GEE Department Majors
            case "Electrical Engineering":
                return new String[] {
                        "Electrical Circuits",
                        "Electrical Machines",
                        "Power Systems",
                        "Power Electronics"
                };
            case "Electronics Engineering":
                return new String[] {
                        "Analog Electronics",
                        "Digital Electronics",
                        "Microprocessors and Microcontrollers Systems",
                        "Embedded Systems"
                };
            case "Automation Engineering":
                return new String[] {
                        "Control Systems",
                        "Industrial Automation",
                        "PLC and SCADA Systems",
                        "Robotics Fundamentals"
                };

            default:
                return null;
        }
    }

    // For GIC Department
    public static String getSECourse() {
        System.out.println("\nSelect your Course in Software Engineering:");
        System.out.println("1. Programming Fundamentals");
        System.out.println("2. Object-Oriented Programming");
        System.out.println("3. Software engineering Principles");
        System.out.print("Enter your choice : ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Programming Fundamentals";
                System.out.println("You selected Programming Fundamentals.");
                break;
            case 2:
                major = "Object-Oriented Programming";
                System.out.println("You selected Object-Oriented Programming.");
                break;
            case 3:
                major = "Software engineering Principles";
                System.out.println("You selected Software engineering Principles.");
                break;
            default:
                System.out.println("Invalid choice!");
                return null;
        }
        return major;

    }

    public static String getCyberCourse() {
        System.out.println("\nSelect your Course in Cyber Security:");
        System.out.println("1. Computer and Network Security");
        System.out.println("2. Ethical Hacking Fundamentals");
        System.out.println("3. Cyber Security Management");
        System.out.print("Enter your choice : ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Computer and Network Security";
                System.out.println("You selected Computer and Network Security.");
                break;
            case 2:
                major = "Ethical Hacking Fundamentals";
                System.out.println("You selected Ethical Hacking Fundamentals.");
                break;
            case 3:
                major = "Cyber Security Management";
                System.out.println("You selected Cyber Security Management.");
                break;
            default:
                System.out.println("Invalid choice!");
                return null;
        }
        return major;

    }

    public static String getAICourse() {
        System.out.println("\nSelect your Course in Artificial Intelligence:");
        System.out.println("1. Introduction to Artificial Intelligence");
        System.out.println("2. Machine Learning");
        System.out.println("3. Data science Fundamentals");
        System.out.print("Enter your choice : ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Introduction to Artificial Intelligence";
                System.out.println("You selected Introduction to Artificial Intelligence.");
                break;
            case 2:
                major = "Machine Learning";
                System.out.println("You selected Machine Learning.");
                break;
            case 3:
                major = "Data science Fundamentals";
                System.out.println("You selected Data science Fundamentals.");
                break;
            default:
                System.out.println("Invalid choice!");
                return null;
        }
        return major;
    }

    // For GIM Department
    public static String getMechanicCourse() {
        System.out.println("\nSelect your Course in Mechanical Engineering:");
        System.out.println("1. Engineering Mechanics");
        System.out.println("2. Thermodynamics");
        System.out.println("3. Machine Design");
        System.out.print("Enter your choice : ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Engineering Mechanics";
                System.out.println("You selected Engineering Mechanics.");
                break;
            case 2:
                major = "Thermodynamics";
                System.out.println("You selected Thermodynamics.");
                break;
            case 3:
                major = "Machine Design";
                System.out.println("You selected Machine Design.");
                break;
            default:
                System.out.println("Invalid choice!");
                return null;
        }
        return major;
    }

    public static String getManufactCourse() {
        System.out.println("\nSelect your Course in Manufacturing Engineering:");
        System.out.println("1. Manufacturing Processes");
        System.out.println("2. Computer-Aided Manufacturing");
        System.out.println("3. Production Planning and Control");
        System.out.print("Enter your choice : ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Manufacturing Processes";
                System.out.println("You selected Manufacturing Processes.");
                break;
            case 2:
                major = "Computer-Aided Manufacturing";
                System.out.println("You selected Computer-Aided Manufacturing.");
                break;
            case 3:
                major = "Production Planning and Control";
                System.out.println("You selected Production Planning and Control.");
                break;
            default:
                System.out.println("Invalid choice!");
                return null;
        }
        return major;
    }

    public static String getIndustCourse() {
        System.out.println("\nSelect your Course in Industrial Engineering:");
        System.out.println("1. operations Research");
        System.out.println("2. Quality Control and Assurance");
        System.out.println("3. Supply Chain Management");
        System.out.print("Enter your choice : ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Operations Research";
                System.out.println("You selected Operations Research.");
                break;
            case 2:
                major = "Quality Control and Assurance";
                System.out.println("You selected Quality Control and Assurance.");
                break;
            case 3:
                major = "Supply Chain Management";
                System.out.println("You selected Supply Chain Management.");
                break;
            default:
                System.out.println("Invalid choice!");
                return null;
        }
        return major;
    }

    // For GEE Department
    public static String getAutomationCourse() {
        System.out.println("\nSelect your Course in Automation Engineering:");
        System.out.println("1. Control Systems");
        System.out.println("2. Industrial Automation");
        System.out.println("3. PLC and SCADA Systems");
        System.out.print("Enter your choice : ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Control Systems";
                System.out.println("You selected Control Systems.");
                break;
            case 2:
                major = "Industrial Automation";
                System.out.println("You selected Industrial Automation.");
                break;
            case 3:
                major = "PLC and SCADA Systems";
                System.out.println("You selected PLC and SCADA Systems.");
                break;
            default:
                System.out.println("Invalid choice!");
                return null;
        }
        return major;
    }

    public static String getElectricCourse() {
        System.out.println("\nSelect your Course in Electrical Engineering:");
        System.out.println("1. Electrical Circuits");
        System.out.println("2. Electrical Machines");
        System.out.println("3. Power Systems");
        System.out.print("Enter your choice : ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Electrical Circuits";
                System.out.println("You selected Electrical Circuits.");
                break;
            case 2:
                major = "Electrical Machines";
                System.out.println("You selected Electrical Machines.");
                break;
            case 3:
                major = "Power Systems";
                System.out.println("You selected Power Systems.");
                break;
            default:
                System.out.println("Invalid choice!");
                return null;
        }
        return major;
    }

    public static String getElectronicCourse() {
        System.out.println("\nSelect your Course in Electronic Engineering:");
        System.out.println("1. Analog Electronics");
        System.out.println("2. Digital Electronics");
        System.out.println("3. Microprocessors and Microcontrollers Systems");
        System.out.print("Enter your choice : ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        String major = "";

        switch (choice) {
            case 1:
                major = "Analog Electronics";
                System.out.println("You selected Analog Electronics.");
                break;
            case 2:
                major = "Digital Electronics";
                System.out.println("You selected Digital Electronics.");
                break;
            case 3:
                major = "Microprocessors and Microcontrollers Systems";
                System.out.println("You selected Microprocessors and Microcontrollers Systems.");
                break;
            default:
                System.out.println("Invalid choice!");
                return null;
        }
        return major;
    }
}
