package hellocucumber;

import app.DateServer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class MockDateServer implements DateServer {
    private LocalDate currentDate = LocalDate.now();

    @Override
    public LocalDate getDate() {
        return currentDate;
    }

    // sets the current date to a new date
    public void setDate(LocalDate newDate) {
        this.currentDate = newDate;
    }

    // makes it possible to do fx "3 days later"
    public void advanceDateByDays(int days) {
        this.currentDate = this.currentDate.plusDays(days);
    }


    // returns the current week number, based on the geographic location
    @Override
    public int getWeek() {
        return currentDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    }

    @Override
    public int getYear() {
        return currentDate.getYear();
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
