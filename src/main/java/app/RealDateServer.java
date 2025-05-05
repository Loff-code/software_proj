package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;



public class RealDateServer implements DateServer {
    private LocalDate today = LocalDate.now();

    @Override
    public LocalDate getDate() {
        return LocalDate.now();
    }

    // returns the current week number, based on the geographic location
    @Override
    public int getWeek() {
        return today.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    }

    @Override
    public int getYear() {
        return today.getYear();
    }



    // changes a date string to a LocalDate object
    private static final DateTimeFormatter Date_Format =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate parseDate(String date) {
        return LocalDate.parse(date, Date_Format);
    }

    // changes a LocalDate object to a date string
    @Override
    public String dateToString(LocalDate date) {
        return date.format(Date_Format);
    }


}
