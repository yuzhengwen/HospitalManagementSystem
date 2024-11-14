package View;

import Controller.Controller;
import Model.Staff;
import View.AdministratorSubViews.StaffManagementView;

public class AdministratorView extends UserView<Staff> {
    public AdministratorView(Staff staff) {
        super(staff);
        System.out.println("Welcome " + staff.getName());
        actions.add(new Action("Staff Management", this::staffManagement));
        actions.add(new Action("Appointment Management", this::appointmentManagement));
        actions.add(new Action("Inventory Management", this::inventoryManagement));
    }
    @Override
    public void display() {
        System.out.println("Administrator Menu");
        System.out.println("----------------");
        printActions();
        getInput();
    }
    private void staffManagement() {
        Controller.getInstance().setPreviousView(this);
        new StaffManagementView().display();
    }
    private void appointmentManagement() {
    }
    private void inventoryManagement() {
    }
}
