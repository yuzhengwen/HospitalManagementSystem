import CustomTypes.Gender;
import Model.Appointment;
import Model.Patient;
import Model.User;
import Singletons.AppointmentManager;
import Singletons.InputManager;
import Singletons.UserLoginManager;
import Controller.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        createDummyUsers();
        createDummyAppointments();
        createDummyAvailableDates();
        if (showLoginMenu())
            Controller.getInstance().startMainMenu();
    }

    private static void createDummyAvailableDates() {
        AppointmentManager.getInstance().updateAvailableDates();
    }

    private static void createDummyAppointments() {
        AppointmentManager.getInstance().add(new Appointment("1", new Date(2323223232L), Appointment.Type.FOLLOWUP));
        AppointmentManager.getInstance().add(new Appointment("2", new Date(2323223232L), Appointment.Type.EMERGENCY));
    }

    private static boolean showLoginMenu() {
        String id = InputManager.getInstance().getString("Enter ID: ");
        User user = UserLoginManager.getInstance().getUserById(id);
        String password = InputManager.getInstance().getString("Enter Password: ");
        if (user != null && user.getPassword().equals(password)) {
            Controller.getInstance().setCurrentUser(user);
            return true;
        }
        return false;
    }

    private static void createDummyUsers() {
        User user1 = new Patient("1", "password", "John", new Date(2323223232L), Gender.MALE);
        UserLoginManager.getInstance().addUser(user1);
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
