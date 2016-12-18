package be.ghostwritertje.utilities;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class DateUtilities {

    public static LocalDate toLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneOffset.systemDefault()).toLocalDate();
    }

    public static Date toUtilDate(LocalDate date) {
        return java.sql.Date.valueOf(date);
    }
}
