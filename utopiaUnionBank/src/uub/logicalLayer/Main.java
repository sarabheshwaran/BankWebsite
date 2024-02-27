package uub.logicalLayer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read username
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        // Read password and hide characters with asterisks
        System.out.print("Enter password: ");
        String password = readPassword();

        // Process username and password as needed
        System.out.println("Entered username: " + username);
        System.out.println("Entered password: " + password);
    }

    // Custom method to read password with masking
    private static String readPassword() {
        StringBuilder password = new StringBuilder();
        char asterisk = '*';

        while (true) {
            char c = System.console().readPassword()[0];
            if (c == '\r' || c == '\n') {
                break;
            }
            password.append(c);
        }

        return password.toString();
    }
}
