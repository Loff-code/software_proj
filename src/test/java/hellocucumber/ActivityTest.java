package hellocucumber;

import app.Activity;
import app.RealDateServer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ActivityTest {


    @Test
    void registerTime_fails_whenDateIsNull() {
        Activity activity = new Activity("Demo", 100, 1, 5);
        activity.assignEmployeeToActivity("vict");

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                activity.registerTime("vict", 2.0, null, "Demo", new RealDateServer())
        );

        assertEquals("Not allowed: Date cannot be null", e.getMessage());
    }

    @Test
    void registerTime_fails_whenUserIdIsBlank() {
        Activity activity = new Activity("Demo", 100, 1, 5);
        activity.assignEmployeeToActivity("");

        LocalDate date = LocalDate.of(2025, 2, 6);

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                activity.registerTime("", 2.0, date, "Demo", new RealDateServer())
        );

        assertEquals("Not allowed: User is not assigned to this activity", e.getMessage());
    }



}
