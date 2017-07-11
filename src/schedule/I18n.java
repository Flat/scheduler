package schedule;

import java.io.UnsupportedEncodingException;
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

}
