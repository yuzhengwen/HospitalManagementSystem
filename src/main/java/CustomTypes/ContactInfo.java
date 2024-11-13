package CustomTypes;

public class ContactInfo {
    public String email;
    public String phoneNumber;

    public ContactInfo() {
        this.email = "";
        this.phoneNumber = "";
    }

    public ContactInfo(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String toString() {
        if (email.isEmpty() && phoneNumber.isEmpty())
            return "No contact information available";
        else
            return "Email: " + email + " Phone: " + phoneNumber;
    }
}
