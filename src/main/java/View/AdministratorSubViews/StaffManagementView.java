package View.AdministratorSubViews;

import Controller.Controller;
import CustomTypes.Gender;
import CustomTypes.Role;
import Model.Staff;
import Singletons.InputManager;
import Singletons.UserLoginManager;
import View.Action;
import View.ViewObject;

import java.util.ArrayList;
import java.util.List;

public class StaffManagementView extends ViewObject {
    public StaffManagementView() {
        actions.add(new Action("Back to Main", Controller.getInstance()::navigateBack));
        actions.add(new Action("View Staff", this::viewStaff));
        actions.add(new Action("Add Staff", this::addStaff));
        actions.add(new Action("Remove Staff", this::removeStaff));
        actions.add(new Action("Update Staff", this::editStaff));
    }

    @Override
    public void display() {
        System.out.println("Staff Management Menu");
        System.out.println("----------------");
        printActions();
        getInput();
    }

    private void viewStaff() {
        List<Staff> staffList = UserLoginManager.getInstance().getAllStaffs();
        if (InputManager.getInstance().getBoolean("Do you want to filter the staff list?")) {
            StaffFilter filter = getFilter();
            List<Staff> filteredStaff = filter.filter(staffList);
            for (Staff staff : filteredStaff) {
                System.out.println(staff.getDetailedInfo());
            }
        } else {
            for (Staff staff : staffList) {
                System.out.println(staff.getDetailedInfo());
            }
        }
        display();
    }

    private StaffFilter getFilter() {
        StaffFilter filter = new StaffFilter();
        StaffFilterOptions filterOption = InputManager.getInstance().getEnum("Filter by: ", StaffFilterOptions.class);
        switch (filterOption) {
            case ROLE:
                Role role = InputManager.getInstance().getEnum("Role: ", Role.class);
                filter.filterByRole(role);
                break;
            case AGE:
                int age = InputManager.getInstance().getInt("Age: ");
                filter.filterByAge(age);
                break;
            case GENDER:
                Gender gender = InputManager.getInstance().getEnum("Gender", Gender.class);
                filter.filterByGender(gender);
        }
        return filter;
    }

    private void addStaff() {
        System.out.println("Enter account details:");
        String id = InputManager.getInstance().getString("ID: ");
        String password = InputManager.getInstance().getNewPasswordInput(true);
        System.out.println("Enter staff details:");
        Role role = InputManager.getInstance().getEnum("Role: ", Role.class);
        String name = InputManager.getInstance().getString("Name: ");
        int age = InputManager.getInstance().getInt("Age: ");
        Gender gender = InputManager.getInstance().getEnum("Gender: ", Gender.class);

        Staff newStaff = new Staff(id, password, name, role, gender, age);
        UserLoginManager.getInstance().addUser(newStaff);
        System.out.println("Staff added successfully");
        display();
    }

    private void removeStaff() {
        String idOrName = InputManager.getInstance().getString("Enter staff ID or name: ");
        List<Staff> staffList = UserLoginManager.getInstance().getAllStaffs();
        staffList.stream()
                .filter(staff -> staff.getId().equals(idOrName) || staff.getName().equals(idOrName))
                .findFirst().ifPresentOrElse(staff -> {
                    UserLoginManager.getInstance().removeUser(staff);
                    System.out.println("Staff removed successfully");
                }, () -> System.out.println("Staff not found"));
        display();
    }

    private void editStaff() {
        String idOrName = InputManager.getInstance().getString("Enter staff ID or name: ");
        List<Staff> staffList = UserLoginManager.getInstance().getAllStaffs();
        staffList.stream()
                .filter(staff -> staff.getId().equals(idOrName) || staff.getName().equals(idOrName))
                .findFirst().ifPresentOrElse(staff -> {
                    System.out.println("Enter new details:");
                    Role role = InputManager.getInstance().getEnum("Role: ", Role.class);
                    String name = InputManager.getInstance().getString("Name: ");
                    int age = InputManager.getInstance().getInt("Age: ");
                    Gender gender = InputManager.getInstance().getEnum("Gender: ", Gender.class);
                    staff.setRole(role);
                    staff.setName(name);
                    staff.setAge(age);
                    System.out.println("Staff updated successfully");
                }, () -> System.out.println("Staff not found"));
        display();
    }

    public static class StaffFilter {
        private Role role;
        private int age;
        private Gender gender;

        public void filterByRole(Role role) {
            this.role = role;
        }

        public void filterByAge(int age) {
            this.age = age;
        }

        public void filterByGender(Gender gender) {
            this.gender = gender;
        }

        public List<Staff> filter(List<Staff> staffList) {
            if (staffList.isEmpty()) {
                return new ArrayList<>();
            }
            List<Staff> filteredStaff = new ArrayList<>(staffList);
            if (role != null) {
                filteredStaff.removeIf(staff -> staff.getRole() != role);
            }
            if (age != 0) {
                filteredStaff.removeIf(staff -> staff.getAge() != age);
            }
            if (gender != null) {
                filteredStaff.removeIf(staff -> staff.getGender() != gender);
            }
            return filteredStaff;
        }
    }

    private enum StaffFilterOptions {
        ROLE,
        AGE,
        GENDER
    }
}
