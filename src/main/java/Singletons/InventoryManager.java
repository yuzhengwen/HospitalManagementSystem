package Singletons;

import Model.Inventory;
import Model.ReplenishmentRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
