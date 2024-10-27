package Model;

import CustomTypes.ContactInfo;
import CustomTypes.Gender;
import Singletons.AppointmentManager;
import CustomTypes.Role;

import java.util.Scanner;


// Staff ID,Name,Role,Gender,Age
public class Staff extends User {
    private String name;
    private int age;
    private Role role;
    private Gender gender;

    public Staff(String id, String password, String name, Role role, Gender gender, int age) {
        super(id, password);
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.age = age;
    }

    public Role getRole() {
        return role;
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

    public int getAge() {
        return age;
    }
}

