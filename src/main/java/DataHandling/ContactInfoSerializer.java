package DataHandling;

import CustomTypes.ContactInfo;

public class ContactInfoSerializer implements ISerializer<ContactInfo> {

    @Override
    public String serialize(ContactInfo object) {
        return object.email + "|" + object.phoneNumber;
    }

    @Override
    public ContactInfo deserialize(String data) {
        String[] parts = data.split("\\|");
        return new ContactInfo(parts[0], parts[1]);
    }
}
