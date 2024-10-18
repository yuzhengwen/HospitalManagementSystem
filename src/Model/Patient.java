package Model;

import CustomTypes.ContactInfo;
import CustomTypes.Gender;
import Singletons.AppointmentManager;

import java.util.Date;
import java.util.Scanner;

public class Patient extends User{
    private String name;
    private Date dob;
    private Gender gender;
    private ContactInfo contactInfo = new ContactInfo();
    private String bloodType;
    // to add blood type, past diagnosis/treatment

    public Patient(String id, String password, String name, Date dob, String bloodType, Gender gender) {
        super(id, password);
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.bloodType = bloodType;
    }

    public String getBloodType() {
        return bloodType;
    }
    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Date getDob() {
        return dob;
    }

    public Gender getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
