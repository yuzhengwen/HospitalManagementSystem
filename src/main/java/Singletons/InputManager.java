package Singletons;

import Model.Appointment;
import Model.Prescription;
import Model.ScheduleManagement.TimeSlot;
import Model.ScheduleManagement.TimeSlotWithDoctor;
import View.EnumView;
import View.SelectionView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Singleton class to manage user input
 */
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

    /**
     * Works just like {@link InputManager#getInt(String)}
     * but with no message
     *
     * @see InputManager#getInt(String)
     */
    public int getInt() {
        String input = scanner.nextLine(); // avoid scanner.nextInt() to prevent issues with nextLine()
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return getInt();
        }
    }

    /***
     * Gets an integer input from user
     * Repeatedly asks for integer until a valid integer is entered
     * @param message message to display to user
     * @return valid integer entered by user
     */
    public int getInt(String message) {
        System.out.println(message);
        return getInt();
    }

    /**
     * Works just like {@link InputManager#getString(String)}
     * but with no message
     *
     * @see InputManager#getString(String)
     */
    public String getString() {
        return scanner.nextLine();
    }

    /**
     * Gets a string input from user
     *
     * @param message message to display to user
     * @return string entered by user
     */
    public String getString(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    /**
     * Gets a unique string input from user (Useful for unique usernames, ids, etc.)
     *
     * @param message        message to display to user
     * @param existingValues set of existing values to check against
     * @return unique string entered by user
     */
    public String getUniqueString(String message, Set<String> existingValues) {
        String input;
        do {
            input = getString(message);
            if (existingValues.contains(input)) {
                System.out.println("Value already exists. Please enter a unique value.");
            }
        } while (existingValues.contains(input));
        return input;
    }

    /**
     * Gets a boolean input from user
     *
     * @param message message to display to user
     * @return true if user enters 'Y', false otherwise
     */
    public boolean getBoolean(String message) {
        System.out.println(message);
        String input = scanner.nextLine();
        return input.trim().equalsIgnoreCase("Y");
    }

    /**
     * Prompts user to press enter to go back
     *
     * @return 1 to indicate to continue showing the previous menu
     */
    public int goBackPrompt() {
        getString("Press enter to go back");
        return 1;
    }

    /**
     * Gets a time input from user
     * Default message is "Enter time (HH:mm): "
     *
     * @return time entered by user
     */
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

    /**
     * Gets a date input from user
     * Default message is "Enter date (dd-MM-yyyy): "
     *
     * @return date entered by user
     */
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

    /**
     * Gets a date and time input from user
     *
     * @return date and time entered by user
     */
    public LocalDateTime getDateTime() {
        LocalDate date = getDate();
        LocalTime time = getTime();
        return LocalDateTime.of(date, time);
    }

    public Scanner getScanner() {
        return scanner;
    }

    /**
     * Gets an enum input from user
     *
     * @param s         message to display to user
     * @param enumClass enum class with values to choose from
     * @param <E>       enum type
     * @return enum value selected by user
     */
    public <E extends Enum<E>> E getEnum(String s, Class<E> enumClass) {
        System.out.println(s);
        EnumView<E> enumView = new EnumView<>(enumClass);
        enumView.display();
        // verify that a valid enum value was selected
        if (enumView.getSelected() == null) {
            return getEnum(s, enumClass);
        }
        return enumView.getSelected();
    }


    /**
     * Works just like {@link InputManager#getSelection(String, List, boolean)}
     * but with no option to go back
     *
     * @see InputManager#getSelection(String, List, boolean)
     */
    public <T> T getSelection(String s, List<T> list) {
        return getSelection(s, list, false).getSelected();
    }

    /**
     * Gets a selection from a list of items
     *
     * @param s         message to display to user
     * @param list      list of items to choose from
     * @param allowBack whether to allow user to go back
     * @param <T>       type of items in list
     * @return item selected by user
     */
    public <T> SelectionResult<T> getSelection(String s, List<T> list, boolean allowBack) {
        System.out.println(s);
        SelectionView<T> selectionView = new SelectionView<>(list);
        if (allowBack) {
            selectionView.allowBack();
        }
        selectionView.display();
        // verify that a valid selection was made
        if (selectionView.getSelected() == null && !selectionView.backSelected()) {
            return getSelection(s, list, allowBack);
        }
        return new SelectionResult<>(selectionView.getSelected(), selectionView.backSelected());
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
     * Validate password <br>
     * Password must be at least 8 characters long<br>
     * Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character<br>
     * Password must not contain spaces<br>
     * Password cannot be 'password'
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

    public Prescription getPrescription() {
        Set<String> existingIds = AppointmentManager.getInstance().getPrescriptions().keySet();
        String prescriptionId = InputManager.getInstance().getUniqueString("Enter prescription ID: ", existingIds);
        Prescription prescription = new Prescription(prescriptionId);
        int noOfMedicines = InputManager.getInstance().getInt("Enter the number of prescribed medications: ");
        for (int i = 0; i < noOfMedicines; i++) {
            String medicationName = InputManager.getInstance().getString("Enter the name of prescribed medication " + (i + 1) + ": ");
            int quantity = InputManager.getInstance().getInt("Enter the quantity of medication " + (i + 1) + ": ");
            prescription.addMedicine(medicationName, quantity);
        }
        String prescriptionNotes = InputManager.getInstance().getString("Enter prescription notes: ");
        prescription.setNotes(prescriptionNotes);
        return prescription;
    }
    /**
     * Shows a list of available timeslots for the selected date and allows them to select one.
     *
     * @param date The date for which to select a timeslot.
     * @return The selected timeslot, or null if the user cancels the selection.
     */
    public TimeSlotWithDoctor selectTimeSlot(LocalDate date, String patientId) {
        // get timeslots with doctors for the selected date
        List<TimeSlotWithDoctor> timeSlotWithDoctors = AppointmentManager.getInstance().getTimeslotWithDoctorList(date);
        // add patientBusy flag to the timeslots where the patient already has an appointment scheduled
        AppointmentFilter filter = new AppointmentFilter().filterByPatient(patientId).filterByDate(date)
                .filterByStatus(Appointment.Status.ACCEPTED).filterByStatus(Appointment.Status.PENDING);
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsWithFilter(filter);
        List<TimeSlot> patientBusyTimeSlots = new ArrayList<>();
        for (Appointment appointment : appointments) {
            patientBusyTimeSlots.add(appointment.getTimeSlot());
        }
        for (TimeSlotWithDoctor timeSlotWithDoctor : timeSlotWithDoctors) {
            if (patientBusyTimeSlots.contains(timeSlotWithDoctor.getTimeSlot())) {
                timeSlotWithDoctor.setPatientBusy(true);
            }
        }
        // display the available timeslots
        SelectionResult<TimeSlotWithDoctor> selectionResult = InputManager.getInstance().getSelection("Select a timeslot: ", timeSlotWithDoctors, true);
        if (selectionResult.isBack()) return null;
        // check if the selected timeslot is available
        TimeSlotWithDoctor selected = selectionResult.getSelected();
        while (!selected.isAvailable()) {
            System.out.println("Timeslot is unavailable. Please select another timeslot.");
            selected = InputManager.getInstance().getSelection("Select a timeslot: ", timeSlotWithDoctors);
        }
        return selected;
    }
}

