package Singletons;

import Model.Inventory;
import Model.ReplenishmentRequest;

import java.util.ArrayList;
import java.util.List;

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

    public void createReplenishmentRequest(String medicine, int quantity) {
        requests.add(new ReplenishmentRequest(medicine, quantity));
    }

    public List<ReplenishmentRequest> getRequests() {
        return requests;
    }
    public List<ReplenishmentRequest> getRequestsByStatus(boolean fulfilled) {
        List<ReplenishmentRequest> filteredRequests = new ArrayList<>();
        for (ReplenishmentRequest request : requests) {
            if (request.isFulfilled() == fulfilled) {
                filteredRequests.add(request);
            }
        }
        return filteredRequests;
    }

    public Inventory getInventory() {
        return inventory;
    }

    // ONLY FOR Loading Data ------------------------------------------
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setRequests(List<ReplenishmentRequest> requests) {
        this.requests = requests;
    }
}
