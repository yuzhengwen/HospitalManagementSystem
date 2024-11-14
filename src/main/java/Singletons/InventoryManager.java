package Singletons;

import DataHandling.SaveManager;
import Model.Inventory;
import Model.ReplenishmentRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to manage the inventory and replenishment requests
 */
public class InventoryManager {
    // Singleton Implementation ------------------------------------------
    private static InventoryManager instance;

    public static synchronized InventoryManager getInstance() {
        if (instance == null) {
            instance = new InventoryManager();
        }
        return instance;
    }
    private InventoryManager() {
    }

    // Code for InventoryManager start ------------------------------------------
    private Inventory inventory = new Inventory();
    private List<ReplenishmentRequest> requests = new ArrayList<>();

    /**
     * Create a new replenishment request
     * @param medicine name of the medicine
     * @param quantity quantity of the medicine
     */
    public void createReplenishmentRequest(String medicine, int quantity) {
        requests.add(new ReplenishmentRequest(medicine, quantity));
        SaveManager.getInstance().saveReplenishmentRequests();
    }

    /**
     * Get all replenishment requests
     */
    public List<ReplenishmentRequest> getRequests() {
        return requests;
    }
    /**
     * Get all replenishment requests by status
     * @param fulfilled status of the request
     */
    public List<ReplenishmentRequest> getRequestsByStatus(boolean fulfilled) {
        List<ReplenishmentRequest> filteredRequests = new ArrayList<>();
        for (ReplenishmentRequest request : requests) {
            if (request.isFulfilled() == fulfilled) {
                filteredRequests.add(request);
            }
        }
        return filteredRequests;
    }

    /**
     * Get the inventory
     * @return inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    // ONLY FOR Loading Data ------------------------------------------
    /**
     * Set the entire inventory
     * Only meant for loading data from file
     * @param inventory inventory
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Set the entire list of replenishment requests
     * Only meant for loading data from file
     * @param requests list of replenishment requests
     */
    public void setRequests(List<ReplenishmentRequest> requests) {
        this.requests = requests;
    }
}
