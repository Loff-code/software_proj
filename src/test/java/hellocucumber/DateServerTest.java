package app;

import hellocucumber.MockDateServer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DateServerTest {

    @Test
    void mockDateServer_canBeInjectedIntoPMApp_andReturnsCorrectDate() {
        // 1) Opret app og mock
        PM_App app = new PM_App();
        MockDateServer mock = new MockDateServer();

        // 2) Sæt en kendt dato
        LocalDate wanted = LocalDate.of(2025, 5, 3);
        mock.setDate(wanted);

        // 3) Inject i app
        app.setDateServer(mock);

        // 4) Hent dato fra app og sammenlign
        LocalDate actual = app.getDateServer().getDate();
        assertEquals(wanted, actual,
                "PM_App skal returnere den mockede dato via dateServer");

        // 5) Tjek dateToString
        String asString = app.getDateServer().dateToString(actual);
        assertEquals("2025-05-03", asString,
                "dateToString skal give 'yyyy-MM-dd'");
    }

    @Test
    void realDateServer_returnsToday() {
        RealDateServer real = new RealDateServer();
        LocalDate now = LocalDate.now();
        LocalDate realNow = real.getDate();
        assertEquals(now, realNow, "RealDateServer.getDate() bør være i dag");
    }
}
