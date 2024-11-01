import Controller.Controller;
import Model.Appointment;
import Singletons.AppointmentManager;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Controller.getInstance().showLoginMenu(); // create a new instance of Controller and call showLoginMenu
    }

}
