package DataHandling;

import CustomTypes.PrescriptionStatus;
import Model.Prescription;

public class PrescriptionSerializer implements ISerializer<Prescription> {
    @Override
    public String serialize(Prescription object) {
        String[] medicineQuantities = object.getMedicineQuantities().entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .toArray(String[]::new);
        String serializedMedicineQuantities = StringUtils.addQuotes(String.join(SEPARATOR, medicineQuantities));
        return object.getId() + SEPARATOR + serializedMedicineQuantities +
                SEPARATOR + object.getStatus() +
                SEPARATOR + StringUtils.addQuotes(object.getNotes());
    }

    @Override
    public Prescription deserialize(String data) {
        //String[] parts = data.split(SEPARATOR, -1);
        String[] parts = StringUtils.parseLine(data);
        assert parts.length == 4; // id, medicineQuantities, status, notes
        String id = parts[0].trim();
        String[] medicineQuantities = parts[1].trim().split(",");
        PrescriptionStatus status = PrescriptionStatus.valueOf(parts[2].trim().toUpperCase());
        String notes = parts[3].trim();

        Prescription p = new Prescription(id, status);
        p.setNotes(notes);
        for (int i = 0; i < medicineQuantities.length; i++) {
            String[] medicineQuantity = medicineQuantities[i].split(":");
            String medicationName = medicineQuantity[0];
            int quantity = Integer.parseInt(medicineQuantity[1]);
            p.addMedicine(medicationName, quantity);
        }
        return p;
    }
}
