package View;

import Controller.Controller;
import CustomTypes.ServiceProvided;
import DataHandling.SaveManager;
import Email.GmailSender;
import Model.*;
import Model.ScheduleManagement.Schedule;
import Singletons.AppointmentManager;
import Singletons.InputManager;
import Singletons.UserLoginManager;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

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
            String apptNotes = InputManager.getInstance().getString("Enter consultation notes: ");

            // Prescription start ---------------
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
            // Prescription end ---------------

            AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(prescriptionId, service, apptNotes);
            AppointmentManager.getInstance().recordAppointmentOutcome(user.getId(), selected, outcome, prescription);

            boolean followUp = InputManager.getInstance().getBoolean("Is a follow-up appointment required? (Y/N): ");
            if (followUp) {
                // TODO: Implement follow-up appointment creation
            }
            boolean emailResults = InputManager.getInstance().getBoolean("Email results to patient? (Y/N): ");
            if (emailResults) {
                Patient p = (Patient) UserLoginManager.getInstance().getUserById(selected.getPatientId());
                try {
                    GmailSender.sendEmail(p.getContactInfo().email, "Appointment Outcome", convertEmail(selected, outcome));
                } catch (GeneralSecurityException | IOException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println("Appointment outcome recorded.");
        }
        SaveManager.getInstance().saveAppointments();
        return InputManager.getInstance().goBackPrompt();
    }

    private String convertEmail(Appointment a, AppointmentOutcomeRecord r) {
        Patient p = (Patient) UserLoginManager.getInstance().getUserById(a.getPatientId());
        Staff d = (Staff) UserLoginManager.getInstance().getUserById(a.getDoctorId());
        return "Dear " + p.getName() + ",\n\n" +
                "Your appointment with Dr. " + d.getName() + " has been completed.\n\n" +
                "Appointment Details:\n" +
                "Date: " + a.getDate() + "\n" +
                "Time: " + a.getTimeSlot() + "\n" +
                "Service Provided: " + r.getServiceProvided() + "\n" +
                "Prescription: " + r.getPrescription() + "\n" +
                "Notes: " + r.getNotes() + "\n\n" +
                "Thank you for choosing our services.\n\n" +
                "Sincerely,\n" +
                "Hospital Management System";
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
        SaveManager.getInstance().saveAppointments();
        return InputManager.getInstance().goBackPrompt();
    }

    private int manageSchedule() {
        Controller.getInstance().setPreviousView(this);
        Schedule schedule = AppointmentManager.getInstance().getScheduleOfDoctor(user);
        System.out.println(schedule.printScheduleCompact());
        while (InputManager.getInstance().getBoolean("Change schedule? (Y/Any other key to confirm changes): ")) {
            DayOfWeek day = InputManager.getInstance().getEnum("Select day of week: ", DayOfWeek.class);
            int startHour, endHour;
            do {
                startHour = InputManager.getInstance().getInt("Enter start hour: (0-23)");
            } while (startHour < 0 || startHour > 23);
            do {
                endHour = InputManager.getInstance().getInt("Enter end hour: (0-23)");
            } while (endHour < 0 || endHour > 23);
            schedule.setWorkingHours(day, startHour, endHour);
            AppointmentManager.getInstance().setSchedule(user.getId(), schedule);
        }
        System.out.println("Schedule updated.");
        System.out.println(schedule.printScheduleCompact());

        SaveManager.getInstance().saveDoctorSchedules();
        return InputManager.getInstance().goBackPrompt();
    }
}