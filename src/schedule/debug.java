package schedule;

import java.time.*;

public class debug {
    public static void log(String s){
        String now = LocalTime.now().toString();
        System.out.println(now + ": " + s);
    }
}
