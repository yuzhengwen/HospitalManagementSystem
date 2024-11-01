package View;

import Model.ScheduleManagement.Schedule;
import Model.Staff;
import Singletons.AppointmentManager;
import Singletons.InputManager;

import java.time.DayOfWeek;

import Controller.Controller;

public class DoctorView extends UserView { // to do: implement all the methods
    private Staff staff;

    public DoctorView(Staff staff) {
        super(staff);
        this.staff = staff;
        System.out.println("Welcome, " + staff.getName());
        actions.add(new Action("View/Edit Personal Schedule", this::manageSchedule));
        /*
        actions.add(new Action("Accept/Decline Appointment Requests", this::manageAppointmentRequests));
        actions.add(new Action("View Upcoming Appointments", this::viewUpcomingAppointments));
        actions.add(new Action("Record Appointment Outcome", this::recordAppointmentOutcome));*/
    }

    @Override
    public void display() {
        System.out.println("Doctor Menu");
        System.out.println("----------------");
        printActions();
        getInput();
    }

    private void manageSchedule() {
        Controller.getInstance().setPreviousView(this);
        Schedule schedule = AppointmentManager.getInstance().getScheduleOfDoctor(staff);
        System.out.println(schedule.printScheduleCompact());
        while (InputManager.getInstance().getBoolean("Change schedule? (Y/N): ")) {
            System.out.println("Select Day of Week:");
            EnumView<DayOfWeek> dayOfWeekEnumView = new EnumView<>(DayOfWeek.class);
            dayOfWeekEnumView.display();
            DayOfWeek day = dayOfWeekEnumView.getSelected();
            int startHour = InputManager.getInstance().getInt("Enter start hour: ");
            int endHour = InputManager.getInstance().getInt("Enter end hour: ");
            InputManager.getInstance().getScanner().nextLine(); // consume newline
            schedule.setWorkingHours(day, startHour, endHour);
            AppointmentManager.getInstance().setSchedule(staff, schedule);
        }
        System.out.println("Schedule updated.");
        System.out.println(schedule.printScheduleCompact());
        InputManager.getInstance().goBackPrompt();
    }
/*
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

 */
}