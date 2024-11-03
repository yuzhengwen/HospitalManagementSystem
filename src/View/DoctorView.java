package View;

import CustomTypes.ServiceProvided;
import Model.Appointment;
import Model.AppointmentOutcomeRecord;
import Model.Prescription;
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
        actions.add(new Action("Record Appointment Outcome", this::recordAppointmentOutcome));
        actions.add(new Action("View Completed Appointments", this::viewCompletedAppointments));
    }

    private void viewCompletedAppointments() {
        Controller.getInstance().setPreviousView(this);
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsByDoctorId(staff.getId(), Appointment.Status.COMPLETED);
        if (appointments.isEmpty()) {
            System.out.println("No completed appointments found.");
        } else {
            Appointment selected = InputManager.getInstance().getSelection("Select an appointment to view: ", appointments);
            System.out.println(selected.getFullDetails());
        }
        InputManager.getInstance().goBackPrompt();
    }

    private void recordAppointmentOutcome() {
        Controller.getInstance().setPreviousView(this);

        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsByDoctorId(staff.getId(), Appointment.Status.ACCEPTED);
        if (appointments.isEmpty()) {
            System.out.println("No appointments accepted yet");
        } else {
            Appointment selected = InputManager.getInstance().getSelection("Select an appointment to record outcome for: ", appointments);

            System.out.println("Enter the following details for the appointment outcome");
            System.out.println("--------------------------------------------------------");
            ServiceProvided service = InputManager.getInstance().getEnum("Enter the type of service provided: ", ServiceProvided.class);
            String medicationName = InputManager.getInstance().getString("Enter the name of any prescribed medication: ");
            String notes = InputManager.getInstance().getString("Enter consultation notes: ");
            String prescriptionId = InputManager.getInstance().getString("Enter the prescription ID: ");

            AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(new Prescription(prescriptionId, medicationName), service, notes);
            AppointmentManager.getInstance().recordAppointmentOutcome(staff.getId(), selected, outcome);

            System.out.println("Appointment outcome recorded.");
        }
        InputManager.getInstance().goBackPrompt();
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
        } else {
            Appointment selected = InputManager.getInstance().getSelection("Select an appointment to view: ", appointments);
            System.out.println(selected.getFullDetails());
        }
        InputManager.getInstance().goBackPrompt();
    }

    private void manageAppointmentRequests() {
        Controller.getInstance().setPreviousView(this);

        System.out.println("Choose an appointment request to accept: ");
        List<Appointment> appointments = AppointmentManager.getInstance().getPendingAppointmentsWithinAvailableTime(staff.getId());
        if (appointments.isEmpty()) {
            System.out.println("No appointment requests found.");
        } else {
            Appointment selectedAppointment = InputManager.getInstance().getSelection("Select an appointment to accept: ", appointments);
            AppointmentManager.getInstance().acceptAppointment(selectedAppointment, staff.getId());
            System.out.println("Appointment accepted.");
            System.out.println(selectedAppointment);
        }
        InputManager.getInstance().goBackPrompt();
    }

    private void manageSchedule() {
        Controller.getInstance().setPreviousView(this);
        Schedule schedule = AppointmentManager.getInstance().getScheduleOfDoctor(staff);
        System.out.println(schedule.printScheduleCompact());
        while (InputManager.getInstance().getBoolean("Change schedule? (Y/N): ")) {
            DayOfWeek day = InputManager.getInstance().getEnum("Select day of week: ", DayOfWeek.class);
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
}