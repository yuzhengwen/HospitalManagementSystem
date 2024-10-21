package Model.ScheduleManagement;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class CalendarUtils {
    public static LocalDate getNextDayOfWeek(DayOfWeek targetDay) {
        LocalDate today = LocalDate.now(); // Get the current date
        DayOfWeek currentDay = today.getDayOfWeek(); // Get today's day of the week

        // Calculate how many days to add to reach the target day
        int daysToAdd = targetDay.getValue() - currentDay.getValue();

        // If the target day is earlier in the week or today, move to the next week
        if (daysToAdd <= 0) {
            daysToAdd += 7;
        }

        return today.plusDays(daysToAdd); // Add the days and return the result
    }
}
