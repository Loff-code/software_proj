package app;

import java.time.LocalDate;

// interface used in MockDateServer and RealDateServer

public interface DateServer {
    LocalDate getDate();

    int getWeek();

    int getYear();

    LocalDate parseDate(String date);

    String dateToString(LocalDate date);




}
