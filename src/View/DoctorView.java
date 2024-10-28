package View;

import CustomTypes.OperationMode;
import Model.Appointment;
import Model.Staff;
import Singletons.AppointmentManager;
import Singletons.InputManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Controller.Controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class DoctorView extends UserView { // to do: implement all the methods
    private Staff staff;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public DoctorView(Staff staff) {
        super(staff);
        this.staff = staff;
        System.out.println("Welcome, " + staff.getName());
        actions.add(new Action("View Personal Schedule", this::viewPersonalSchedule));
        actions.add(new Action("Set Availability", this::setAvailability));
        actions.add(new Action("Accept/Decline Appointment Requests", this::manageAppointmentRequests));
        actions.add(new Action("View Upcoming Appointments", this::viewUpcomingAppointments));
        actions.add(new Action("Record Appointment Outcome", this::recordAppointmentOutcome));
    }

    @Override
    public void display() {
        System.out.println("Doctor Menu");
        System.out.println("----------------");
        printActions();
        getInput();
    }

    private void viewPersonalSchedule() {
        Controller.getInstance().setPreviousView(this);
        List<Appointment> personalSchedule = AppointmentManager.getInstance().getAppointmentsByDoctorId(staff.getId());
        personalSchedule.forEach(appointment -> System.out.println(appointment.toString()));
        InputManager.getInstance().getString("Press enter to go back");
        Controller.getInstance().navigateBack();
    }

    private void setAvailability() {
        Controller.getInstance().setPreviousView(this);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the available date (yyyy-MM-dd): ");
        String dateString = scanner.nextLine();
        System.out.println("Enter the start time (HH:mm): ");
        String startTimeString = scanner.nextLine();
        System.out.println("Enter the end time (HH:mm): ");
        String endTimeString = scanner.nextLine();

        try {
            Date date = dateFormat.parse(dateString + " " + startTimeString);
            Date endTime = dateFormat.parse(dateString + " " + endTimeString);

            while (date.before(endTime)) {
                AppointmentManager.getInstance().addAppointment(new Appointment(staff.getId(), date, null, null, null));
                // Increment the time by 30 minutes for each slot
                date = new Date(date.getTime() + 30 * 60 * 1000);
            }
        } catch (ParseException e) {
            System.out.println("Invalid date or time format.");
        }
        InputManager.getInstance().getString("Press enter to go back");
        Controller.getInstance().navigateBack();
    }

    private void manageAppointmentRequests() {
        Controller.getInstance().setPreviousView(this);
        List<AppointmentRequest> requests = AppointmentManager.getInstance().getAppointmentRequests(staff);
        requests.forEach(request -> {
            System.out.println(request.toString());
            String decision = InputManager.getInstance().getString("Accept or Decline (A/D): ");
            if (decision.equalsIgnoreCase("A")) {
                AppointmentManager.getInstance().acceptRequest(request);
                System.out.println("Request accepted.");
            } else {
                AppointmentManager.getInstance().declineRequest(request);
                System.out.println("Request declined.");
            }
        });
        InputManager.getInstance().getString("Press enter to go back");
        Controller.getInstance().navigateBack();
    }

    private void viewUpcomingAppointments() {
        Controller.getInstance().setPreviousView(this);
        List<Appointment> upcomingAppointments = AppointmentManager.getInstance().getUpcomingAppointments(staff);
        upcomingAppointments.forEach(appointment -> System.out.println(appointment.toString()));
        InputManager.getInstance().getString("Press enter to go back");
        Controller.getInstance().navigateBack();
    }

    private void recordAppointmentOutcome() {
        Controller.getInstance().setPreviousView(this);
        Date appointmentDate = InputManager.getInstance().getDate("Enter the date of the appointment (yyyy-mm-dd): ");
        String serviceType = InputManager.getInstance().getString("Enter the type of service provided: ");
        String medicationName = InputManager.getInstance().getString("Enter the name of any prescribed medication: ");
        String medicationStatus = InputManager.getInstance().getString("Enter the status of the medication (default is pending): ");
        String consultationNotes = InputManager.getInstance().getString("Enter consultation notes: ");

        AppointmentOutcome outcome = new AppointmentOutcome(appointmentDate, serviceType, medicationName, medicationStatus, consultationNotes);
        AppointmentManager.getInstance().recordAppointmentOutcome(staff, outcome);

        System.out.println("Appointment outcome recorded.");
        InputManager.getInstance().getString("Press enter to go back");
        Controller.getInstance().navigateBack();
    }
}