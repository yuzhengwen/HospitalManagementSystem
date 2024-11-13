package Singletons;

import Controller.Controller;
import View.EnumView;
import View.SelectionView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class InputManager {
    private static InputManager instance;
    private final Scanner scanner = new Scanner(System.in);

    private InputManager() {
    }

    public static synchronized InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    public int getInt() {
        String input = scanner.nextLine(); // avoid scanner.nextInt() to prevent issues with nextLine()
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return getInt();
        }
    }

    public String getString() {
        return scanner.nextLine();
    }

    public int getInt(String message) {
        System.out.println(message);
        return scanner.nextInt();
    }

    public String getString(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    public boolean getBoolean(String message) {
        System.out.println(message);
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("Y");
    }

    public void goBackPrompt() {
        getString("Press enter to go back");
        Controller.getInstance().navigateBack();
    }

    public LocalTime getTime() {
        System.out.println("Enter time (HH:mm): ");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = null;
        boolean valid = false;
        while (!valid) {
            String timeString = scanner.nextLine();
            try {
                time = LocalTime.parse(timeString, timeFormat);
                valid = true;
            } catch (Exception e) {
                System.out.println("Invalid time format. Please enter the time in HH:mm format.");
            }
        }
        return time;
    }

    public LocalDate getDate() {
        return getDate("Enter date (dd-MM-yyyy): ");
    }

    public LocalDate getDate(String message) {
        System.out.println(message);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = null;
        boolean valid = false;
        while (!valid) {
            String dateString = scanner.nextLine();
            try {
                date = LocalDate.parse(dateString, dateFormat);
                valid = true;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in dd-MM-yyyy format.");
            }
        }
        return date;
    }

    public LocalDateTime getDateTime() {
        LocalDate date = getDate();
        LocalTime time = getTime();
        return LocalDateTime.of(date, time);
    }

    public Scanner getScanner() {
        return scanner;
    }

    public <E extends Enum<E>> E getEnum(String s, Class<E> enumClass) {
        System.out.println(s);
        EnumView<E> enumView = new EnumView<>(enumClass);
        enumView.display();
        return enumView.getSelected();
    }

    public <T> T getSelection(String s, List<T> list) {
        System.out.println(s);
        SelectionView<T> selectionView = new SelectionView<>(list);
        selectionView.display();
        return selectionView.getSelected();
    }

    /***
     * Gets a new password input from user
     * Repeatedly asks for password until a valid password is entered
     * Requires confirmation of password
     * @param passwordValidation can toggle whether password validation is required (Mainly for testing)
     * @return valid password entered by user
     */
    public String getNewPasswordInput(boolean passwordValidation) {
        String password, confirmPassword;
        do {
            password = InputManager.getInstance().getString("Enter Password: ");
        } while (passwordValidation && !validatePassword(password));
        do {
            confirmPassword = InputManager.getInstance().getString("Confirm Password: ");
            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
            }
        } while (!password.equals(confirmPassword));
        return password;
    }

    /***
     * Validate password
     * @param password password to validate
     * @return true if password is valid, false otherwise
     */
    public boolean validatePassword(String password) {
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            System.out.println("Password must contain at least one digit.");
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            System.out.println("Password must contain at least one lowercase letter.");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            System.out.println("Password must contain at least one uppercase letter.");
            return false;
        }
        if (!password.matches(".*[!@#$%^&*].*")) {
            System.out.println("Password must contain at least one special character.");
            return false;
        }
        if (password.contains(" ")) {
            System.out.println("Password must not contain spaces.");
            return false;
        }
        if (password.trim().equalsIgnoreCase("password")) {
            System.out.println("Password cannot be 'password'.");
            return false;
        }
        return true;
    }
}