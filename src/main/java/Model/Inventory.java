package Model;

import java.util.HashMap;
import java.util.Map;

/**
 * A custom HashMap to store the inventory of the clinic
 * Maps a medicine name to an array of two integers: the first integer is the quantity of the medicine in stock, the second integer is the low stock alert threshold
 */
public class Inventory extends HashMap<String, Integer[]> {
    /**
     * Adds a medicine to inventory </br>
     *
     * @param medicine          the name of the medicine
     * @param quantity          the quantity of the medicine to add
     * @param lowStockThreshold the low stock threshold for the medicine
     */
    public void addMedicine(String medicine, int quantity, int lowStockThreshold) {
        Integer[] values = new Integer[2];
        int currentQuantity = getOrDefault(medicine, new Integer[]{0, 0})[0];
        values[0] = currentQuantity + quantity;
        values[1] = lowStockThreshold;
        put(medicine, values);
    }

    /**
     * Adds a medicine to inventory </br>
     * If the medicine already exists in inventory, the quantity is updated </br>
     * If the medicine does not exist in inventory, it is added to inventory with default low stock threshold of 0
     *
     * @param medicine the name of the medicine
     * @param quantity the quantity of the medicine to add
     */
    public void addMedicine(String medicine, int quantity) {
        int lowStockThreshold = getOrDefault(medicine, new Integer[]{0, 0})[1];
        addMedicine(medicine, quantity, lowStockThreshold);
    }

    public void setLowStockThreshold(String medicine, int lowStockThreshold) {
        Integer[] values = getOrDefault(medicine, new Integer[]{0, 0});
        values[1] = lowStockThreshold;
        put(medicine, values);
    }

    public int getLowStockThreshold(String medicine) {
        return getOrDefault(medicine, new Integer[]{0, 0})[1];
    }

    /**
     * Checks if a medicine is low in stock
     * If the medicine does not exist in inventory, it is considered low stock
     *
     * @param medicine the name of the medicine
     * @return true if the quantity of the medicine is less than or equal to the low stock threshold
     */
    public boolean isLowStock(String medicine) {
        Integer[] values = getOrDefault(medicine, new Integer[]{0, 0});
        return values[0] <= values[1];
    }

    public String[] getLowStockItems() {
        return entrySet().stream()
                .filter(entry -> entry.getValue()[0] <= entry.getValue()[1])
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
    }

    /**
     * Removes a quantity of a medicine from inventory
     *
     * @param medicine the name of the medicine
     * @param quantity the quantity of the medicine to remove
     * @return true if the medicine was successfully removed, false if the quantity to remove is greater than the current quantity
     */
    public boolean removeMedicine(String medicine, int quantity) {
        Integer[] currentValues = getOrDefault(medicine, new Integer[]{0, 0});
        int currentQuantity = currentValues[0];
        int lowStockThreshold = currentValues[1];

        if (currentQuantity - quantity <= 0)
            return false;
        put(medicine, new Integer[]{currentQuantity - quantity, lowStockThreshold});
        return true;
    }

    public int getMedicineCount(String medicine) {
        return getOrDefault(medicine, new Integer[]{0, 0})[0];
    }

    public void setMedicineCount(String medicine, int quantity) {
        Integer[] values = getOrDefault(medicine, new Integer[]{0, 0});
        values[0] = quantity;
        put(medicine, values);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Medication Inventory:\n");
        sb.append("--------------------\n");
        for (Map.Entry<String, Integer[]> entry : entrySet()) {
            sb.append(entry.getKey()).append(": ").append("Quantity: ").append(entry.getValue()[0]).append(", Low Stock Threshold: ").append(entry.getValue()[1]);
            sb.append("\n");
        }
        return sb.toString();
    }
}
