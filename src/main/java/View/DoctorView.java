package View;

import Controller.Controller;
import CustomTypes.ServiceProvided;
import DataHandling.SaveManager;
import Model.Appointment;
import Model.AppointmentOutcomeRecord;
import Model.Prescription;
import Model.ScheduleManagement.Schedule;
import Model.Staff;
import Singletons.AppointmentManager;
import Singletons.InputManager;

import java.time.DayOfWeek;
import java.util.List;

public class DoctorView extends UserView<Staff> {
    public DoctorView(Staff staff) {
        super(staff);
        System.out.println("Welcome, " + user.getName());
        actions.add(new Action("View/Edit Personal Schedule", this::manageSchedule));
        actions.add(new Action("View Accepted Appointments", this::viewAcceptedAppointments));
        actions.add(new Action("Accept/Decline Appointment Requests", this::manageAppointmentRequests));
        actions.add(new Action("Record Appointment Outcome", this::recordAppointmentOutcome));
        actions.add(new Action("View Completed Appointments", this::viewCompletedAppointments));
    }

    @Override
    public void display() {
        do {
            System.out.println("Doctor Menu");
            System.out.println("----------------");
            printActions();
        } while (getInput() != 0);
    }

    private int viewCompletedAppointments() {
        Controller.getInstance().setPreviousView(this);
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsByDoctorId(user.getId(), Appointment.Status.COMPLETED);
        if (appointments.isEmpty()) {
            System.out.println("No completed appointments found.");
        } else {
            Appointment selected = InputManager.getInstance().getSelection("Select an appointment to view: ", appointments, true);
            if (selected == null) return 1;
            System.out.println(selected.getFullDetails());
        }
        return InputManager.getInstance().goBackPrompt();
    }

    private int recordAppointmentOutcome() {
        Controller.getInstance().setPreviousView(this);

        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsByDoctorId(user.getId(), Appointment.Status.ACCEPTED);
        if (appointments.isEmpty()) {
            System.out.println("No appointments accepted yet");
        } else {
            Appointment selected = InputManager.getInstance().getSelection("Select an appointment to record outcome for: ", appointments, true);
            if (selected == null) return 1;

            System.out.println("Enter the following details for the appointment outcome");
            System.out.println("--------------------------------------------------------");
            ServiceProvided service = InputManager.getInstance().getEnum("Enter the type of service provided: ", ServiceProvided.class);
            String medicationName = InputManager.getInstance().getString("Enter the name of any prescribed medication: ");
            String notes = InputManager.getInstance().getString("Enter consultation notes: ");
            String prescriptionId = InputManager.getInstance().getString("Enter the prescription ID: ");

            AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(new Prescription(prescriptionId, medicationName), service, notes);
            AppointmentManager.getInstance().recordAppointmentOutcome(user.getId(), selected, outcome);

            System.out.println("Appointment outcome recorded.");
        }
        return InputManager.getInstance().goBackPrompt();
    }

    private int viewAcceptedAppointments() {
        Controller.getInstance().setPreviousView(this);
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsByDoctorId(user.getId(), Appointment.Status.ACCEPTED);
        if (appointments.isEmpty()) {
            System.out.println("No appointments accepted yet");
        } else {
            Appointment selected = InputManager.getInstance().getSelection("Select an appointment to view: ", appointments, true);
            if (selected == null) return 1;
            System.out.println(selected.getFullDetails());
        }
        return InputManager.getInstance().goBackPrompt();
    }

    private int manageAppointmentRequests() {
        Controller.getInstance().setPreviousView(this);

        System.out.println("Choose an appointment request to accept: ");
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsByDoctorId(user.getId(), Appointment.Status.PENDING);
        if (appointments.isEmpty()) {
            System.out.println("No appointment requests found.");
        } else {
            Appointment selectedAppointment = InputManager.getInstance().getSelection("Select an appointment to accept: ", appointments, true);
            if (selectedAppointment == null) return 1;
            AppointmentManager.getInstance().acceptAppointment(selectedAppointment, user.getId());
            System.out.println("Appointment accepted.");
            System.out.println(selectedAppointment);
        }
        return InputManager.getInstance().goBackPrompt();
    }

    private int manageSchedule() {
        Controller.getInstance().setPreviousView(this);
        Schedule schedule = AppointmentManager.getInstance().getScheduleOfDoctor(user);
        System.out.println(schedule.printScheduleCompact());
        while (InputManager.getInstance().getBoolean("Change schedule? (Y/Any other key to confirm changes): ")) {
            DayOfWeek day = InputManager.getInstance().getEnum("Select day of week: ", DayOfWeek.class);
            int startHour = InputManager.getInstance().getInt("Enter start hour: ");
            int endHour = InputManager.getInstance().getInt("Enter end hour: ");
            InputManager.getInstance().getScanner().nextLine(); // consume newline
            schedule.setWorkingHours(day, startHour, endHour);
            AppointmentManager.getInstance().setSchedule(user.getId(), schedule);
        }
        System.out.println("Schedule updated.");
        System.out.println(schedule.printScheduleCompact());
        SaveManager.getInstance().saveDoctorSchedules();
        return InputManager.getInstance().goBackPrompt();
    }
}