package ticket_online.ticket_online.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConvertUtil {

    public static LocalDateTime convertToLocalDateTime(Object value) {
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        } else if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime();
        } else if (value instanceof String) {
            return LocalDateTime.parse((String) value);
        }else if(value instanceof  DateTimeFormatter){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse((String) value, formatter);
        }else {
            throw new IllegalArgumentException("Value is not a valid type (LocalDateTime, Timestamp, String)");
        }
    }

    // Method untuk mendapatkan ekstensi file dari nama file
    public static String getFileExtension(String filename) {
        int lastIndexOfDot = filename.lastIndexOf('.');
        if (lastIndexOfDot > 0) {
            return filename.substring(lastIndexOfDot); // Misalnya .jpg atau .png
        }
        return "";
    }
}
