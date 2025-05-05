package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class RealDateServer implements DateServer {

    @Override
    public LocalDate getDate() {
        return LocalDate.now();
    }

    // returns the current week number, based on the geographic location
    @Override
    public int getWeek() {
        return LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    }

    @Override
    public int getYear() {
        return LocalDate.now().getYear();
    }

    // changes a date string to a LocalDate object
    @Override
    public LocalDate parseDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // changes a LocalDate object to a date string
    @Override
    public String dateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


}
