package View;

import Controller.Controller;
import Model.Staff;
import View.AdministratorSubViews.AppointmentManagementView;
import View.AdministratorSubViews.InventoryManagementView;
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
        do {
            System.out.println("Administrator Menu");
            System.out.println("----------------");
            printActions();
        } while (getInput() != 0);
    }

    private int staffManagement() {
        Controller.getInstance().setPreviousView(this);
        new StaffManagementView().display();
        return 1;
    }

    private int appointmentManagement() {
        Controller.getInstance().setPreviousView(this);
        new AppointmentManagementView().display();
        return 1;
    }

    private int inventoryManagement() {
        Controller.getInstance().setPreviousView(this);
        new InventoryManagementView().display();
        return 1;
    }
}
