package Model;

import CustomTypes.Gender;
import CustomTypes.Role;

public abstract class User {
    protected String id;
    protected String password;
    protected Role role;
    protected Gender gender;
    protected String name;

    public User(String id, String password, Role role, String name, Gender gender) {
        this.id = id;
        this.role = role;
        this.password = password;
        this.name = name;
        this.gender = gender;
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

    public String getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
