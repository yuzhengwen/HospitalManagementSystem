package DataHandling;

import Model.Inventory;

public class InventorySerializer implements ISerializer<Inventory> {
    @Override
    public String serialize(Inventory object) {
        StringBuilder sb = new StringBuilder();
        for (String medicine : object.keySet()) {
            int quantity = object.get(medicine)[0];
            int lowStockThreshold = object.get(medicine)[1];
            sb.append(medicine).append(",").append(quantity).append(",").append(lowStockThreshold).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1); // Remove the last newline character
        return sb.toString();
    }

    @Override
    public Inventory deserialize(String data) {
        String[] lines = data.split("\n");
        Inventory inventory = new Inventory();
        for (String line : lines) {
            String[] parts = line.split(",");
            String medicine = parts[0].trim();
            int quantity = Integer.parseInt(parts[1].trim());
            int lowStockThreshold = Integer.parseInt(parts[2].trim());
            inventory.addMedicine(medicine, quantity, lowStockThreshold);
        }
        return inventory;
    }
}
