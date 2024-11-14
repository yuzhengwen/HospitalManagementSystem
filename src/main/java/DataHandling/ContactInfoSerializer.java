package DataHandling;

import CustomTypes.ContactInfo;

public class ContactInfoSerializer implements ISerializer<ContactInfo> {

    @Override
    public String serialize(ContactInfo object) {
        if (object.phoneNumber.isEmpty() && object.email.isEmpty()) {
            return "";
        }
        return object.email + "|" + object.phoneNumber;
    }

    @Override
    public ContactInfo deserialize(String data) {
        if (data == null || data.isBlank()) {
            return new ContactInfo();
        }
        String[] parts = data.split("\\|");
        return new ContactInfo(parts[0], parts[1]);
    }
}
