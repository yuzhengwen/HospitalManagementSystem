package Model;

import CustomTypes.Gender;
import CustomTypes.Role;

import java.util.Objects;


// Staff ID,Name,Role,Gender,Age
public class Staff extends User {
    private int age;

    public Staff(String id, String password, String name, Role role, Gender gender, int age) {
        super(id, password, role, name ,gender);
        this.role = role;
        this.age = age;
    }


    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return Objects.equals(name, staff.name) && role == staff.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, role);
    }

    @Override
    public String toString() {
        return name;
    }
}

