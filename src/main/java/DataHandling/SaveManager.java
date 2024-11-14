package DataHandling;

import Model.*;
import Model.ScheduleManagement.Schedule;
import Singletons.AppointmentManager;
import Singletons.InventoryManager;
import Singletons.UserLoginManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SaveManager {
    private static SaveManager instance;

    public static synchronized SaveManager getInstance() {
        if (instance == null) {
            instance = new SaveManager();
        }
        return instance;
    }

    private final ISaveService saveService = new LocalFileHandler();
    private final PatientSerializer patientSerializer = new PatientSerializer();
    private final StaffSerializer staffSerializer = new StaffSerializer();
    private final AppointmentSerializer appointmentSerializer = new AppointmentSerializer();
    private final DoctorScheduleSerializer doctorScheduleSerializer = new DoctorScheduleSerializer();
    private final InventorySerializer inventorySerializer = new InventorySerializer();
    private final ReplenishmentRequestSerializer replenishmentRequestSerializer = new ReplenishmentRequestSerializer();

    private final String APPOINTMENT_FILE = "Appointments.csv";
    private final String SCHEDULE_FILE = "DoctorSchedules.csv";
    private final String PATIENT_FILE = "Patient_List.csv";
    private final String STAFF_FILE = "Staff_List.csv";
    private final String MEDICINE_FILE = "Medicine_List.csv";
    private final String REPLENISHMENT_REQUEST_FILE = "ReplenishmentRequests.csv";

    public void saveAllData() {
        savePatients();
        saveStaffs();
        saveAppointments();
        saveDoctorSchedules();
        saveInventory();
        saveReplenishmentRequests();
    }

    public void loadAllData() {
        loadPatients();
        loadStaffs();
        loadAppointments();
        loadDoctorSchedules();
        loadInventory();
        loadReplenishmentRequests();
    }

    public void saveReplenishmentRequests() {
        String header = "Medicine,Quantity,Fulfilled";
        List<ReplenishmentRequest> requests = InventoryManager.getInstance().getRequests();
        List<String> stringsToWrite = new ArrayList<>();
        stringsToWrite.add(header);
        for (ReplenishmentRequest request : requests) {
            stringsToWrite.add(replenishmentRequestSerializer.serialize(request));
        }
        saveService.saveData(REPLENISHMENT_REQUEST_FILE, stringsToWrite);
    }

    public void loadReplenishmentRequests() {
        List<String> data = saveService.readData(REPLENISHMENT_REQUEST_FILE);
        if (data == null || data.isEmpty()) {
            return;
        }
        data.remove(0); // remove the header
        List<ReplenishmentRequest> requests = new ArrayList<>();
        for (String serializedRequest : data) {
            ReplenishmentRequest request = replenishmentRequestSerializer.deserialize(serializedRequest);
            requests.add(request);
        }
        InventoryManager.getInstance().setRequests(requests);
    }

    public void saveInventory() {
        String header = "Medicine,Quantity,Low Stock Threshold";
        String serializedInventory = inventorySerializer.serialize(InventoryManager.getInstance().getInventory());
        List<String> stringsToWrite = new ArrayList<>(Collections.singletonList(header));
        stringsToWrite.add(serializedInventory);
        saveService.saveData(MEDICINE_FILE, stringsToWrite);
    }

    public void loadInventory() {
        List<String> data = saveService.readData(MEDICINE_FILE);
        if (data == null || data.isEmpty()) {
            return;
        }
        data.remove(0); // remove the header
        String serializedInventory = String.join("\n", data);
        InventoryManager.getInstance().setInventory(inventorySerializer.deserialize(serializedInventory));
    }

    public void savePatients() {
        List<Patient> patients = UserLoginManager.getInstance().getAllPatients();
        List<String> stringsToWrite = new ArrayList<>();
        stringsToWrite.add("Patient ID,Name,Date of Birth,Gender,Blood Type,Contact Information,Password");
        for (Patient patient : patients) {
            stringsToWrite.add(patientSerializer.serialize(patient));
        }
        saveService.saveData(PATIENT_FILE, stringsToWrite);
    }

    public void saveStaffs() {
        List<Staff> staffs = UserLoginManager.getInstance().getAllStaffs();
        List<String> stringsToWrite = new ArrayList<>();
        stringsToWrite.add("Staff ID,Name,Role,Gender,Age,Password");
        for (Staff staff : staffs) {
            stringsToWrite.add(staffSerializer.serialize(staff));
        }
        saveService.saveData(STAFF_FILE, stringsToWrite);
    }

    public void saveAppointments() {
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointments();
        List<String> stringsToWrite = new ArrayList<>();
        //2024-11-04,14:00-15:00,P1001,D001,FOLLOWUP,COMPLETED,DIAGNOSIS,rest more,11,panadol
        stringsToWrite.add("Date,Time,Patient ID,Doctor ID,Type,Status,Service Provided,Notes,Prescription ID,Medicine");
        for (Appointment appointment : appointments) {
            stringsToWrite.add(appointmentSerializer.serialize(appointment));
        }
        saveService.saveData(APPOINTMENT_FILE, stringsToWrite);
    }

    public void saveDoctorSchedules() {
        Map<String, Schedule> doctorScheduleMap = AppointmentManager.getInstance().getDoctorScheduleMap();
        String header = "Doctor ID,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday";
        String serializedDoctorSchedules = doctorScheduleSerializer.serialize(doctorScheduleMap);
        saveService.saveData(SCHEDULE_FILE, List.of(header, serializedDoctorSchedules));
    }

    public void loadPatients() {
        List<String> serializedPatients = saveService.readData(PATIENT_FILE);
        if (serializedPatients == null || serializedPatients.isEmpty()) {
            return;
        }
        serializedPatients.remove(0); // remove the header
        for (String serializedPatient : serializedPatients) {
            Patient p = patientSerializer.deserialize(serializedPatient);
            UserLoginManager.getInstance().addUser(p);
        }
    }

    public void loadStaffs() {
        List<String> serializedStaffs = saveService.readData(STAFF_FILE);
        if (serializedStaffs == null || serializedStaffs.isEmpty()) {
            return;
        }
        serializedStaffs.remove(0); // remove the header
        for (String serializedStaff : serializedStaffs) {
            Staff s = staffSerializer.deserialize(serializedStaff);
            UserLoginManager.getInstance().addUser(s);
        }
    }

    public void loadAppointments() {
        List<String> serializedAppointments = saveService.readData(APPOINTMENT_FILE);
        if (serializedAppointments == null || serializedAppointments.isEmpty()) {
            return;
        }
        serializedAppointments.remove(0); // remove the header
        for (String serializedAppointment : serializedAppointments) {
            Appointment a = appointmentSerializer.deserialize(serializedAppointment);
            AppointmentManager.getInstance().add(a);
        }
    }

    public void loadDoctorSchedules() {
        List<String> serializedDoctorSchedules = saveService.readData(SCHEDULE_FILE);
        if (serializedDoctorSchedules == null || serializedDoctorSchedules.isEmpty()) {
            return;
        }
        serializedDoctorSchedules.remove(0); // remove the header
        Map<String, Schedule> doctorScheduleMap = doctorScheduleSerializer.deserialize(String.join("\n", serializedDoctorSchedules));
        for (String doctorId : doctorScheduleMap.keySet()) {
            AppointmentManager.getInstance().setSchedule(doctorId, doctorScheduleMap.get(doctorId));
        }
    }
}
