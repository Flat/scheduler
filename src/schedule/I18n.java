package schedule;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
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

    public static int ToWeekOfYear(LocalDateTime localDateTime) {
        TemporalField temporalField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        return localDateTime.get(temporalField);
    }

    public static LocalDate startOfWeek(int weekOfYear) {
        LocalDate localDate = LocalDate.now().with(ChronoField.ALIGNED_WEEK_OF_YEAR, weekOfYear);
        return localDate.with(DayOfWeek.MONDAY);
    }

}
