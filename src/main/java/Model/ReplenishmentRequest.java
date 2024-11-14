package Model;

public class ReplenishmentRequest {
    private String medicineName;
    private int quantity;
    private boolean fulfilled;

    /**
     * used for creating new requests
     * @param medicineName name of the medicine
     * @param quantity quantity of the medicine
     */
    public ReplenishmentRequest(String medicineName, int quantity) {
        this.medicineName = medicineName;
        this.quantity = quantity;
        fulfilled = false;
    }

    /**
     * used for loading from file
      */
    public ReplenishmentRequest(String medicineName, int quantity, boolean fulfilled) {
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.fulfilled = fulfilled;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Medicine: " + medicineName + ", Quantity Requested: " + quantity + ", Fulfilled: " + fulfilled;
    }
}
