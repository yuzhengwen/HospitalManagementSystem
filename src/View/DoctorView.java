package View;

import DataHandling.SaveManager;
import Model.Appointment;
import Model.ScheduleManagement.Schedule;
import Model.Staff;
import Singletons.AppointmentManager;
import Singletons.InputManager;

import java.time.DayOfWeek;
import java.util.List;

import Controller.Controller;

public class DoctorView extends UserView { // to do: implement all the methods
    private final Staff staff;

    public DoctorView(Staff staff) {
        super(staff);
        this.staff = staff;
        System.out.println("Welcome, " + staff.getName());
        actions.add(new Action("View/Edit Personal Schedule", this::manageSchedule));
        actions.add(new Action("View Accepted Appointments", this::viewAcceptedAppointments));
        actions.add(new Action("Accept/Decline Appointment Requests", this::manageAppointmentRequests));
        /*
        actions.add(new Action("Record Appointment Outcome", this::recordAppointmentOutcome));*/
    }

    @Override
    public void display() {
        System.out.println("Doctor Menu");
        System.out.println("----------------");
        printActions();
        getInput();
    }

    private void viewAcceptedAppointments() {
        Controller.getInstance().setPreviousView(this);
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsByDoctorId(staff.getId(), Appointment.Status.ACCEPTED);
        if (appointments.isEmpty()) {
            System.out.println("No appointments accepted yet");
        } else
            appointments.forEach(appointment -> System.out.println(appointment.toString()));
        InputManager.getInstance().goBackPrompt();
    }

    private void manageAppointmentRequests() {
        Controller.getInstance().setPreviousView(this);

        System.out.println("Choose an appointment request to accept: ");
        List<Appointment> appointments = AppointmentManager.getInstance().getPendingAppointmentsWithinAvailableTime(staff.getId());
        if (appointments.isEmpty()) {
            System.out.println("No appointment requests found.");
        } else {
            SelectionView<Appointment> selectionView = new SelectionView<>(appointments);
            selectionView.display();
            Appointment selectedAppointment = selectionView.getSelected();

            AppointmentManager.getInstance().acceptAppointment(selectedAppointment, staff.getId());
            System.out.println("Appointment accepted.");
            System.out.println(selectedAppointment.toString());
        }
        InputManager.getInstance().goBackPrompt();
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
            AppointmentManager.getInstance().setSchedule(staff.getId(), schedule);
        }
        System.out.println("Schedule updated.");
        System.out.println(schedule.printScheduleCompact());
        Controller.getInstance().getSaveManager().saveDoctorSchedules();
        InputManager.getInstance().goBackPrompt();
    }
/*
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