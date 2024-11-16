package View.AdministratorSubViews;

import DataHandling.SaveManager;
import Model.Inventory;
import Model.ReplenishmentRequest;
import Singletons.InputManager;
import Singletons.InventoryManager;
import Singletons.SelectionResult;
import View.Action;
import View.SelectionView;
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
        actions.add(new Action("View Completed Replenishment Requests", this::viewCompletedReplenishmentRequests));
        actions.add(new Action("View Unfulfilled Replenishment Requests", this::viewUnfulfilledReplenishmentRequests));
        actions.add(new Action("Approve Replenishment Request", this::approveReplenishmentRequest));
    }

    @Override
    public void display() {
        do {
            System.out.println("Inventory Management Menu");
            System.out.println("----------------");
            printActions();
        } while (getInput() != 0);
        SaveManager.getInstance().saveInventory();
    }

    private int viewInventory() {
        System.out.println(inventory);
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
        for (String item : lowStockItems) {
            System.out.println(item + ": " + "Quantity: " + inventory.getMedicineCount(item) + ", Low Stock Threshold: " + inventory.getLowStockThreshold(item));
        }
        return 1;
    }

    private int viewUnfulfilledReplenishmentRequests() {
        List<ReplenishmentRequest> pendingRequests = InventoryManager.getInstance().getRequestsByStatus(false);
        System.out.println("Pending Replenishment Requests:");
        System.out.println("--------------------");
        for (ReplenishmentRequest request : pendingRequests) {
            System.out.println(request);
        }
        return 1;
    }
    private int viewCompletedReplenishmentRequests() {
        List<ReplenishmentRequest> pendingRequests = InventoryManager.getInstance().getRequestsByStatus(true);
        System.out.println("Completed Replenishment Requests:");
        System.out.println("--------------------");
        for (ReplenishmentRequest request : pendingRequests) {
            System.out.println(request);
        }
        return 1;
    }

    private int approveReplenishmentRequest() {
        List<ReplenishmentRequest> pendingRequests = InventoryManager.getInstance().getRequestsByStatus(false);
        SelectionResult<ReplenishmentRequest> selectionResult = InputManager.getInstance().getSelection("Select a request to approve:", pendingRequests, true);
        if (selectionResult.isBack()) return 1;
        ReplenishmentRequest request = selectionResult.getSelected();
        request.setFulfilled(true);
        inventory.addMedicine(request.getMedicineName(), request.getQuantity());
        System.out.println(request.getMedicineName() + " replenished by " + request.getQuantity());
        System.out.println("Replenishment request approved successfully");
        return 1;
    }
}
