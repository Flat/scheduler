package schedule;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.*;

public class log {

    public static void console(String s){
        String now = LocalTime.now().toString();
        System.out.println(now + ": " + s);
    }

    public static void login(String s){
        String now = LocalTime.now().toString();
        Path path = Paths.get("logins.txt");
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            log.console("Writing to " + path.toAbsolutePath());
            bufferedWriter.write(now + ": " + s + "\n");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static void report(String filename, String content) {
        Path path = Paths.get(filename);
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            bufferedWriter.write(content);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

}
