package CustomTypes;

public class ContactInfo{
    public String email;
    public String phoneNumber;
    public ContactInfo(){
        this.email = "";
        this.phoneNumber = "";
    }
    public ContactInfo(String email, String phoneNumber){
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public String toString(){
        return "Email: " + email + " Phone: " + phoneNumber;
    }
}
