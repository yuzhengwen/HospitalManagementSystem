package View;

import java.util.List;
import java.util.Map;

import Controller.Controller;
import CustomTypes.PrescriptionStatus;
import DataHandling.SaveManager;
import Model.Appointment;
import Model.AppointmentOutcomeRecord;
import Model.Inventory;
import Model.Prescription;
import Model.Staff;
import Singletons.AppointmentManager;
import Singletons.InputManager;
import Singletons.InventoryManager;
import Singletons.SelectionResult;

public class PharmacistView extends UserView<Staff> {
    public PharmacistView(Staff staff) {
        super(staff);
        System.out.println("Welcome, " + staff.getName());
        actions.add(new Action("View Appointment Outcomes", this::viewOutcomes));
        actions.add(new Action("Dispense Medicine", this::dispenseMedicine));
        actions.add(new Action("View Medication Inventory", this::viewMedicationInventory));
        actions.add(new Action("Create Replenishment Request", this::createReplenishmentRequest));
    }

    @Override
    public void display() {
        do {
            System.out.println("Pharmacist Menu");
            System.out.println("----------------");
            printActions();
        } while (getInput() != 0);
    }

    private int createReplenishmentRequest() {
        Controller.getInstance().setPreviousView(this);
        String medicine = InputManager.getInstance().getString("Enter medicine name:");
        int quantity = InputManager.getInstance().getInt("Enter quantity:");
        InventoryManager.getInstance().createReplenishmentRequest(medicine, quantity);
        System.out.println("Replenishment request created successfully");
        SaveManager.getInstance().saveReplenishmentRequests();
        return InputManager.getInstance().goBackPrompt();
    }

    private int viewMedicationInventory() {
        Controller.getInstance().setPreviousView(this);
        Inventory inventory = InventoryManager.getInstance().getInventory();
        System.out.println(inventory);
        return InputManager.getInstance().goBackPrompt();
    }

    private int dispenseMedicine() {
        Controller.getInstance().setPreviousView(this);
        Inventory inventory = InventoryManager.getInstance().getInventory();
        List<Appointment> appointments = AppointmentManager.getInstance().getAppointmentsByStatus(Appointment.Status.COMPLETED); // filter only by completed appointments
        if (appointments.isEmpty()) {
            System.out.println("No completed appointments");
        } else {
            AppointmentOutcomeRecord outcome;
            do {
                SelectionResult<Appointment> selectionResult = InputManager.getInstance().getSelection("Here are the completed appointments:\nChoose an appointment to see the outcome", appointments, true);
                if (selectionResult.isBack()) return 1;
                else {
                    outcome = selectionResult.getSelected().getOutcome();
                    System.out.println(outcome.toString()); // print the details of the completed appt
                }
            }
            while (InputManager.getInstance().getBoolean("Dispense medicine for this appointment? (Y/N)"));

            Prescription prescription = outcome.getPrescription();
            // print check list showing required and available quantities of each medicine in the prescription
            StringBuilder checkListBuilder = new StringBuilder();
            boolean canDispense = true;
            for (Map.Entry<String, Integer> entry : prescription.getMedicineQuantities().entrySet()) {
                String medicineName = entry.getKey();
                int requiredQuantity = entry.getValue();
                int availableQuantity = inventory.getMedicineCount(medicineName);
                checkListBuilder.append(medicineName).append(" required: ")
                        .append(requiredQuantity)
                        .append(", available: ")
                        .append(availableQuantity).append("\n");
                if (availableQuantity < requiredQuantity) {
                    canDispense = false;
                }
            }
            System.out.println(checkListBuilder.toString());
            if (!canDispense) {
                System.out.println("Cannot dispense medicine due to insufficient stock");
                return InputManager.getInstance().goBackPrompt();
            } else {
                if (InputManager.getInstance().getBoolean("Dispense medicine? (Y/N)")) {
                    for (Map.Entry<String, Integer> entry : prescription.getMedicineQuantities().entrySet()) {
                        String medicineName = entry.getKey();
                        int requiredQuantity = entry.getValue();
                        inventory.removeMedicine(medicineName, requiredQuantity);
                    }
                    prescription.setStatus(PrescriptionStatus.DISPENSED);
                    System.out.println("Medicine dispensed successfully");
                    SaveManager.getInstance().saveInventory();
                    return InputManager.getInstance().goBackPrompt();
                }
            }
            System.out.println("Failed to dispense medicine");
        }
        return InputManager.getInstance().goBackPrompt();
    }

    private int viewOutcomes() {
        Controller.getInstance().setPreviousView(this);
        List<AppointmentOutcomeRecord> outcomes = AppointmentManager.getInstance().getAllOutcomes();
        if (outcomes.isEmpty()) {
            System.out.println("No outcomes found");
        } else {
            System.out.println("Appointment Outcomes:");
            System.out.println("--------------------");
            for (AppointmentOutcomeRecord outcome : outcomes) {
                System.out.println(outcome.toString());
                System.out.println();
            }
        }
        return InputManager.getInstance().goBackPrompt();
    }
}
