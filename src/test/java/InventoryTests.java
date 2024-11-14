import DataHandling.InventorySerializer;
import Model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Inventory class (Model)
 */
public class InventoryTests {
    Inventory inventory = new Inventory();

    @BeforeEach
    public void setUp() {
        inventory.clear();
        inventory.put("Paracetamol", new Integer[]{10, 5});
        inventory.put("Ibuprofen", new Integer[]{5, 2});
    }

    @Test
    public void testAddExistingMedicine() {
        inventory.addMedicine("Paracetamol", 10);
        assert (inventory.get("Paracetamol")[0] == 20);
        assert (inventory.get("Paracetamol")[1] == 5);
    }

    @Test
    public void testAddExistingMedicineWithThreshold() {
        inventory.addMedicine("Paracetamol", 10, 3);
        assert (inventory.get("Paracetamol")[0] == 20);
        assert (inventory.get("Paracetamol")[1] == 3);
    }

    @Test
    public void testAddNewMedicine() {
        inventory.addMedicine("Aspirin", 10);
        assert (inventory.get("Aspirin")[0] == 10);
        assert (inventory.get("Aspirin")[1] == 0);
    }

    @Test
    public void testAddNewMedicineWithThreshold() {
        inventory.addMedicine("Aspirin", 10, 3);
        assert (inventory.get("Aspirin")[0] == 10);
        assert (inventory.get("Aspirin")[1] == 3);
    }

    @Test
    public void testSetLowStockThreshold() {
        inventory.setLowStockThreshold("Paracetamol", 3);
        assert (inventory.get("Paracetamol")[0] == 10); // Quantity should not change
        assert (inventory.get("Paracetamol")[1] == 3);
    }

    @Test
    public void testSetLowStockThresholdNotInInventory() {
        inventory.setLowStockThreshold("Aspirin", 3);
        assert (inventory.get("Aspirin")[1] == 3);
        assert (inventory.get("Aspirin")[0] == 0); // Quantity should be 0
    }

    @Test
    public void testRemoveMedicine() {
        assert (inventory.removeMedicine("Paracetamol", 5));
        assert (inventory.getMedicineCount("Paracetamol") == 5);
    }

    @Test
    public void testRemoveInExcess() {
        assert (!inventory.removeMedicine("Paracetamol", 15));
        assert (inventory.getMedicineCount("Paracetamol") == 10);
    }

    @Test
    public void testRemoveNonExistentMedicine() {
        assert (!inventory.removeMedicine("Aspirin", 5));
        assert (inventory.getMedicineCount("Aspirin") == 0);
    }
    @Test
    public void testSetMedicineCount() {
        inventory.setMedicineCount("Paracetamol", 50);
        assert (inventory.getMedicineCount("Paracetamol") == 50);
    }

    /*
Medicine Name,Initial Stock,Low Stock Level Alert
Paracetamol,100,20
Ibuprofen,50,10
Amoxicillin,75,15
 */
    @Test
    public void serializationTest() {
        InventorySerializer serializer = new InventorySerializer();
        Inventory inventory = new Inventory();
        inventory.addMedicine("Paracetamol", 100, 20);
        inventory.addMedicine("Ibuprofen", 50, 10);
        inventory.addMedicine("Amoxicillin", 75, 15);

        String serialized = serializer.serialize(inventory);
        assert (serialized.equals("Paracetamol,100,20\nIbuprofen,50,10\nAmoxicillin,75,15"));
        Inventory deserialized = serializer.deserialize(serialized);

        assert (deserialized.get("Paracetamol")[0] == 100);
        assert (deserialized.get("Paracetamol")[1] == 20);
        assert (deserialized.get("Ibuprofen")[0] == 50);
        assert (deserialized.get("Ibuprofen")[1] == 10);
        assert (deserialized.get("Amoxicillin")[0] == 75);
        assert (deserialized.get("Amoxicillin")[1] == 15);
    }
}
