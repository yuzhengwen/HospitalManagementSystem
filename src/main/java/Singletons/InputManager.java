package Singletons;

import Controller.Controller;
import CustomTypes.ServiceProvided;
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
        return scanner.nextInt();
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
}