package hellocucumber;

import app.RealDateServer;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class RealDateServerTest {

    RealDateServer today = new RealDateServer();

    @Test
    void getDate_returnsToday() {
        assertEquals(LocalDate.now(), today.getDate());
    }

    @Test
    void getWeek_returnsValidWeek() {
        int week = today.getWeek();
        assertTrue(week >= 1 && week <= 53);
    }

    @Test
    void getYear_returnsCurrentYear() {
        assertEquals(LocalDate.now().getYear(), today.getYear());
    }

    @Test
    void parseDate_and_dateToString_areInverses() {
        String dateStr = "2025-02-06";
        assertEquals(dateStr, today.dateToString(today.parseDate(dateStr)));
    }



}
