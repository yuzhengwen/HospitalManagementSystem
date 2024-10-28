package Singletons;

import Controller.Controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class InputManager {
    private static InputManager instance;
    private Scanner scanner = new Scanner(System.in);

    private InputManager() {}

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

    public void goBackPrompt() {
        getString("Press enter to go back");
        Controller.getInstance().navigateBack();
    }

    public Date getDate(String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        boolean valid = false;
        while (!valid) {
            System.out.println(message);
            String dateString = scanner.nextLine();
            try {
                date = dateFormat.parse(dateString);
                valid = true;
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }
        return date;
    }

    public Date getTime(String message) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date time = null;
        boolean valid = false;
        while (!valid) {
            System.out.println(message);
            String timeString = scanner.nextLine();
            try {
                time = timeFormat.parse(timeString);
                valid = true;
            } catch (ParseException e) {
                System.out.println("Invalid time format. Please enter the time in HH:mm format.");
            }
        }
        return time;
    }
}