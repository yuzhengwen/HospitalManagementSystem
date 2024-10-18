package Controller;

import CustomTypes.OperationMode;
import Model.Appointment;
import Model.Patient;
import Model.User;
import Singletons.AppointmentManager;
import View.EnumView;
import View.PatientView;
import View.SelectionView;
import View.ViewObject;

import java.util.ArrayList;
import java.util.Date;

public class Controller {
    private static Controller instance;

    private Controller() {
    }

    public static synchronized Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    private User currentUser;

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    private ViewObject previousView;

    public void navigateBack() {
        previousView.showMenu();
    }

    public void setPreviousView(ViewObject view) {
        previousView = view;
    }

    /**
     * Start the main menu for the current user
     */
    public void startMainMenu() {
        if (currentUser instanceof Patient) {
            new PatientView((Patient) currentUser).showMenu();
        }
    }

    public boolean showAppointments(OperationMode mode) {
        ArrayList<Appointment> list = AppointmentManager.getInstance().getAppointments();
        if (currentUser instanceof Patient) {
            list = AppointmentManager.getInstance().getAppointmentsByPatientId((currentUser.getId()));

            if (mode == OperationMode.SCHEDULE) {
                // show view to select date from available dates
                Date selectedDate = getSelectedDate(AppointmentManager.getInstance().getAvailableDates());
                if (selectedDate != null) {
                    EnumView<Appointment.Type> aptTypeView = new EnumView<>(Appointment.Type.class);
                    aptTypeView.showMenu();
                    AppointmentManager.getInstance().add(new Appointment(currentUser.getId(), selectedDate, aptTypeView.getSelected()));
                    return true;
                }
            } else if (mode == OperationMode.EDIT) {
                // show view to select appointment from patient's appointments
                Appointment selected = getSelectedAppointment(AppointmentManager.getInstance().getAppointmentsByPatientId(currentUser.getId()));
                if (selected != null) {
                    Date selectedDate = getSelectedDate(AppointmentManager.getInstance().getAvailableDates());
                    if (selectedDate != null) {
                        selected.setDate(selectedDate);
                        return true;
                    }
                }
            } else if (mode == OperationMode.DELETE) {
                // show view to select appointment from patient's appointments
                Appointment selected = getSelectedAppointment(AppointmentManager.getInstance().getAppointmentsByPatientId(currentUser.getId()));
                if (selected != null) {
                    AppointmentManager.getInstance().remove(selected);
                    return true;
                }
            }
        }
        return false;
    }

    private Date getSelectedDate(ArrayList<Date> list) {
        System.out.println("Choose date: ");
        SelectionView<Date> view = new SelectionView<>(list);
        view.showMenu();
        return view.getSelected();
    }

    private Appointment getSelectedAppointment(ArrayList<Appointment> list) {
        SelectionView<Appointment> view = new SelectionView<>(list);
        view.showMenu();
        return view.getSelected();
    }
}
