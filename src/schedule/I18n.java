package schedule;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.*;

public class I18n {

    public static String getLocalizedString(String key) {
        log.console("Accessing: "+ key);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n");
        try {
            return new String(resourceBundle.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.console("Unable to reencode string as UTF8");
        }

        return resourceBundle.getString(key);
    }

    static void testLocale(String langCode) {
        Locale.setDefault(new Locale(langCode));
    }

    public static ZonedDateTime toLocalTime (Timestamp timestamp) {
        return timestamp.toLocalDateTime().atZone(TimeZone.getDefault().toZoneId());
    }

    public static Timestamp toUTC (ZonedDateTime zonedDateTime) {
        return new Timestamp(zonedDateTime.toInstant().toEpochMilli());
    }

    public static int timestampToMonth(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        return calendar.get(Calendar.MONTH);
    }

    public static int timestampToYear(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        return calendar.get(Calendar.YEAR);
    }

    public static int timestampToWeekOfYear(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static java.sql.Date startOfWeek(int weekOfYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return (java.sql.Date) calendar.getTime();
    }

    public static java.sql.Date endOfWeek(int weekOfYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return (java.sql.Date) calendar.getTime();
    }

}
