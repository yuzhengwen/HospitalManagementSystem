package DataHandling;

import Model.ReplenishmentRequest;

public class ReplenishmentRequestSerializer implements ISerializer<ReplenishmentRequest> {
    @Override
    public String serialize(ReplenishmentRequest object) {
        return object.getMedicineName() + SEPARATOR + object.getQuantity() + SEPARATOR + object.isFulfilled();
    }

    @Override
    public ReplenishmentRequest deserialize(String data) {
        String[] parts = data.split(SEPARATOR);
        return new ReplenishmentRequest(parts[0], Integer.parseInt(parts[1]), Boolean.parseBoolean(parts[2]));
    }
}
