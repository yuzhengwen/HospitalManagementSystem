package View.AdministratorSubViews;

import Model.Inventory;
import Model.ReplenishmentRequest;
import Singletons.InputManager;
import Singletons.InventoryManager;
import View.Action;
import View.ViewObject;

import java.util.List;
import java.util.Map;

public class InventoryManagementView extends ViewObject {
    Inventory inventory = InventoryManager.getInstance().getInventory();

    public InventoryManagementView() {
        actions.add(new Action("Back to Main Menu", () -> 0));
        actions.add(new Action("View Inventory", this::viewInventory));
        actions.add(new Action("Add Inventory Item", this::addInventoryItem));
        actions.add(new Action("Remove Inventory Item", this::removeInventoryItem));
        actions.add(new Action("Update Inventory Item", this::updateInventoryItem));
        actions.add(new Action("Set Low Stock Threshold", this::setLowStockThreshold));
        actions.add(new Action("View Items At Low Stock", this::viewLowStockItems));
        actions.add(new Action("View Replenishment Requests", this::viewReplenishmentRequests));
        actions.add(new Action("Approve Replenishment Request", this::approveReplenishmentRequest));
    }

    @Override
    public void display() {
        do {
            System.out.println("Inventory Management Menu");
            System.out.println("----------------");
            printActions();
        } while (getInput() != 0);
    }

    private int viewInventory() {
        System.out.println("Medication Inventory:");
        System.out.println("--------------------");
        for (Map.Entry<String, Integer[]> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + "Quantity: " + entry.getValue()[0] + ", Low Stock Threshold: " + entry.getValue()[1]);
        }
        return 1;
    }

    private int addInventoryItem() {
        String medicine = InputManager.getInstance().getString("Enter medicine name:");
        int quantity = InputManager.getInstance().getInt("Enter quantity to add:");
        inventory.addMedicine(medicine, quantity);
        System.out.println("Medicine added successfully");
        return 1;
    }

    private int removeInventoryItem() {
        String medicine = InputManager.getInstance().getString("Enter medicine name:");
        int quantity = InputManager.getInstance().getInt("Enter quantity to remove:");
        inventory.removeMedicine(medicine, quantity);
        return 1;
    }

    private int updateInventoryItem() {
        String medicine = InputManager.getInstance().getString("Enter medicine name:");
        int quantity = InputManager.getInstance().getInt("Enter new quantity:");
        inventory.setMedicineCount(medicine, quantity);
        return 1;
    }

    private int setLowStockThreshold() {
        String medicine = InputManager.getInstance().getString("Enter medicine name:");
        int threshold = InputManager.getInstance().getInt("Enter low stock threshold:");
        inventory.setLowStockThreshold(medicine, threshold);
        System.out.println("Low stock threshold set successfully");
        return 1;
    }

    private int viewLowStockItems() {
        String[] lowStockItems = inventory.getLowStockItems();
        if (lowStockItems.length == 0) {
            System.out.println("No items at low stock");
            return 1;
        }
        System.out.println("Items at low stock:");
        System.out.println("--------------------");
        for(String item : lowStockItems) {
            System.out.println(item+ ": " + "Quantity: " + inventory.getMedicineCount(item) + ", Low Stock Threshold: " + inventory.getLowStockThreshold(item));
        }
        return 1;
    }

    private int viewReplenishmentRequests() {
        List<ReplenishmentRequest> pendingRequests = InventoryManager.getInstance().getRequestsByStatus(false);
        System.out.println("Pending Replenishment Requests:");
        System.out.println("--------------------");
        for (ReplenishmentRequest request : pendingRequests) {
            System.out.println(request);
        }
        return 1;
    }

    private int approveReplenishmentRequest() {
        List<ReplenishmentRequest> pendingRequests = InventoryManager.getInstance().getRequestsByStatus(false);
        ReplenishmentRequest request = InputManager.getInstance().getSelection("Select a request to approve:", pendingRequests, true);
        if (request == null) return 1;
        request.setFulfilled(true);
        inventory.addMedicine(request.getMedicineName(), request.getQuantity());
        System.out.println(request.getMedicineName() + " replenished by " + request.getQuantity());
        System.out.println("Replenishment request approved successfully");
        return 1;
    }
}
