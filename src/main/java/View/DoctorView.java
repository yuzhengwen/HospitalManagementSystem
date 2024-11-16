package View;

import Controller.Controller;
import CustomTypes.ServiceProvided;
import DataHandling.SaveManager;
import Email.GmailSender;
import Model.*;
import Model.ScheduleManagement.Schedule;
import Singletons.AppointmentFilter;
import Singletons.AppointmentManager;
import Singletons.InputManager;
import Singletons.UserLoginManager;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DoctorView extends UserView<Staff> {
    public DoctorView(Staff staff) {
        super(staff);
        System.out.println("Welcome, " + user.getName());
        actions.add(new Action("View/Edit Patient Records", this::managePatientRecords));
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
    private int managePatientRecords() {
        Controller.getInstance().setPreviousView(this);
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsByDoctorId(user.getId());
        Set<String> patientIds = appointments.stream().map(Appointment::getPatientId).collect(Collectors.toSet());
        if (patientIds.isEmpty()) {
            System.out.println("No patients found.");
            return InputManager.getInstance().goBackPrompt();
        }
        List<Patient> patients = patientIds.stream().map(id -> (Patient) UserLoginManager.getInstance().getUserById(id)).toList();
        Patient p = InputManager.getInstance().getSelection("Select a patient to view/edit records: ", patients);
        System.out.println("Patient records for " + p.getName());
        System.out.println("--------------------------------------------------------");
        System.out.println(p.getMedicalRecord());
        if (InputManager.getInstance().getBoolean("Edit patient records? (Y/N): ")) {
            if (InputManager.getInstance().getBoolean("Edit blood type? (Y/Any other key to skip): ")) {
                String bloodType = InputManager.getInstance().getString("Enter Blood Type: ");
                p.setBloodType(bloodType);
            }
            if (InputManager.getInstance().getBoolean("Add diagnosis? (Y/Any other key to skip): ")) {
                String diagnosis = InputManager.getInstance().getString("Enter diagnosis: ");
                p.addDiagnosis(diagnosis);
            }
            if (InputManager.getInstance().getBoolean("Add treatment? (Y/Any other key to skip): ")) {
                String treatment = InputManager.getInstance().getString("Enter treatment: ");
                p.addTreatment(treatment);
            }
            if (InputManager.getInstance().getBoolean("Add prescription? (Y/Any other key to skip): ")) {
                Prescription prescription = InputManager.getInstance().getPrescription();
                AppointmentManager.getInstance().addPrescription(prescription);
                p.addPrescription(prescription.getId());
            }
            SaveManager.getInstance().savePatients();
            SaveManager.getInstance().saveAppointments();
            System.out.println("Updated patient records for " + p.getName());
            System.out.println("--------------------------------------------------------");
            System.out.println(p.getMedicalRecord());
        }
        return InputManager.getInstance().goBackPrompt();
    }
    private int viewCompletedAppointments() {
        Controller.getInstance().setPreviousView(this);
        AppointmentFilter filter = new AppointmentFilter().filterByDoctor(user.getId())
                .filterByStatus(Appointment.Status.COMPLETED);
        return viewAppointments(filter);
    }

    private int viewScheduledAppointments() {
        Controller.getInstance().setPreviousView(this);
        AppointmentFilter filter = new AppointmentFilter().filterByDoctor(user.getId())
                .filterByStatus(Appointment.Status.ACCEPTED);
        return viewAppointments(filter);
    }

    private int viewAppointments(AppointmentFilter filter) {
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsWithFilter(filter);
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            Appointment selected = InputManager.getInstance().getSelection("Select an appointment to view: ", appointments);
            if (selected != null)
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
            Patient patient = (Patient) UserLoginManager.getInstance().getUserById(selected.getPatientId());
            System.out.println("Enter the following details for the appointment outcome");
            System.out.println("--------------------------------------------------------");
            ServiceProvided service = InputManager.getInstance().getEnum("Enter the type of service provided: ", ServiceProvided.class);
            String apptNotes = InputManager.getInstance().getString("Enter consultation notes: ");
            Prescription p = InputManager.getInstance().getPrescription();
            AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(p.getId(), service, apptNotes);
            AppointmentManager.getInstance().recordAppointmentOutcome(user.getId(), selected, outcome, p);
            patient.addPrescription(p.getId());

            boolean followUp = InputManager.getInstance().getBoolean("Is a follow-up appointment required? (Y/N): ");
            if (followUp) {
                // TODO: Implement follow-up appointment creation
            }
            boolean emailResults = InputManager.getInstance().getBoolean("Email results to patient? (Y/N): ");
            if (emailResults) {
                try {
                    GmailSender.sendEmail(patient.getContactInfo().email, "Appointment Outcome", convertEmail(selected, outcome));
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