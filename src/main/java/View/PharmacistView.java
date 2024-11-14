package View;

import Controller.Controller;
import CustomTypes.PrescriptionStatus;
import Model.AppointmentOutcomeRecord;
import Model.Inventory;
import Model.Prescription;
import Model.Staff;
import Singletons.AppointmentManager;
import Singletons.InputManager;
import Singletons.InventoryManager;

import java.util.List;
import java.util.Map;

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
        System.out.println("Pharmacist Menu");
        System.out.println("----------------");
        printActions();
        getInput();
    }

    private void createReplenishmentRequest() {
        Controller.getInstance().setPreviousView(this);
        String medicine = InputManager.getInstance().getString("Enter medicine name:");
        int quantity = InputManager.getInstance().getInt("Enter quantity:");
        InventoryManager.getInstance().createReplenishmentRequest(medicine, quantity);
        System.out.println("Replenishment request created successfully");
        InputManager.getInstance().goBackPrompt();
    }

    private void viewMedicationInventory() {
        Controller.getInstance().setPreviousView(this);
        Inventory inventory = InventoryManager.getInstance().getInventory();
        System.out.println("Medication Inventory:");
        System.out.println("--------------------");
        for (Map.Entry<String, Integer[]> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + "Quantity: " + entry.getValue()[0] + ", Low Stock Threshold: " + entry.getValue()[1]);
        }
        InputManager.getInstance().goBackPrompt();
    }

    private void dispenseMedicine() {
        Controller.getInstance().setPreviousView(this);
        Inventory inventory = InventoryManager.getInstance().getInventory();
        List<AppointmentOutcomeRecord> outcomes = AppointmentManager.getInstance().getAllOutcomes();
        if (outcomes.isEmpty()) {
            System.out.println("No outcomes found");
        } else {
            AppointmentOutcomeRecord outcome = InputManager.getInstance().getSelection("Select an outcome to dispense medicine:", outcomes);
            if (outcome != null) {
                Prescription prescription = outcome.getPrescription();
                int quantity = inventory.getMedicineCount(prescription.getMedicationName());
                if (quantity <= 0) {
                    System.out.println(" - Out of stock");
                } else {
                    System.out.print(prescription.getMedicationName() + " available: " + quantity);
                    if (InputManager.getInstance().getBoolean("Dispense medicine? (Y/N)")) {
                        inventory.removeMedicine(prescription.getMedicationName(), 1);
                        prescription.setStatus(PrescriptionStatus.DISPENSED);
                        System.out.println("Medicine dispensed successfully");
                    }
                }
                System.out.println("Failed to dispense medicine");
            }
        }
        InputManager.getInstance().goBackPrompt();
    }

    private void viewOutcomes() {
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
        InputManager.getInstance().goBackPrompt();
    }
}
