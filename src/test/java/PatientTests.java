import CustomTypes.ContactInfo;
import CustomTypes.Gender;
import CustomTypes.Role;
import Model.Appointment;
import Model.Patient;
import Model.ScheduleManagement.Schedule;
import Model.Staff;
import Singletons.AppointmentManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PatientTests {
    Patient p;
    ContactInfo info;
    Appointment a;
    Staff d;

    @BeforeEach
    public void setUp() {
        info = Mockito.mock(ContactInfo.class);
        p = Mockito.mock(Patient.class);
        Mockito.when(p.getId()).thenReturn("P1001");
        Mockito.when(p.getContactInfo()).thenReturn(info);
        d = Mockito.mock(Staff.class);
        Mockito.when(d.getId()).thenReturn("D001");
        a = Mockito.mock(Appointment.class);
        Mockito.when(a.getPatientId()).thenReturn("P1001");
        Mockito.when(a.getDoctorId()).thenReturn("D001");
    }

    @Test
    public void updatePatientInfo() {
        ContactInfo newInfo = Mockito.mock(ContactInfo.class);
        Mockito.when(p.getContactInfo()).thenReturn(newInfo);
        p.setContactInfo(newInfo);
        Assertions.assertEquals(newInfo, p.getContactInfo());
    }

    @Test
    public void workingHours() {
        Staff d = Mockito.mock(Staff.class);
        Mockito.when(d.getRole()).thenReturn(Role.DOCTOR);
        Schedule schedule = new Schedule(d);
        schedule.setWorkingHours(DayOfWeek.MONDAY, 8, 17);
        LocalDateTime dateTime = LocalDateTime.of(2022, 11, 7, 9, 0);
        Assertions.assertTrue(schedule.isWorking(dateTime.getDayOfWeek(), dateTime.toLocalTime()));
    }

    @Test
    public void testScheduleAppointment() {
        AppointmentManager.getInstance().add(a);
        Assertions.assertTrue(AppointmentManager.getInstance().getAppointmentsByPatientId(p.getId()).contains(a));
    }

    @Test
    public void testAcceptAppointment() {
        Appointment a = new Appointment("P1001", LocalDate.of(2024,11,4), null, Appointment.Type.FOLLOWUP);
        a.setStatus(Appointment.Status.PENDING);
        AppointmentManager.getInstance().add(a);
        AppointmentManager.getInstance().acceptAppointment(a, d.getId());
        Assertions.assertEquals(Appointment.Status.ACCEPTED, a.getStatus());
    }
}
