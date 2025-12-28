package Backend.src;

public class PasswordUtils {

    /**
     * Mask password with asterisks
     */
    public static String maskPassword(String password) {
        return "*".repeat(password.length());
    }

    /**
     * Read password from console with masked input showing asterisks
     */
    public static String readMaskedPassword() {
        if (System.console() != null) {
            // For console applications
            char[] password = System.console().readPassword();
            return new String(password);
        } else {
            // For IDEs - read password and show asterisks as user types
            try {
                StringBuilder password = new StringBuilder();
                int ch;
                while ((ch = System.in.read()) != '\n' && ch != '\r' && ch != -1) {
                    if (ch == '\b' || ch == 127) { // Backspace
                        if (password.length() > 0) {
                            password.deleteCharAt(password.length() - 1);
                            System.out.print("\b \b");
                        }
                    } else {
                        password.append((char) ch);
                        System.out.print("*");
                    }
                }
                System.out.println(); // New line after password entry
                return password.toString();
            } catch (Exception e) {
                return "";
            }
        }
    }
}
