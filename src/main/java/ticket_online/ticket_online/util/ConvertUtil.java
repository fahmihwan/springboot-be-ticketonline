package ticket_online.ticket_online.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ConvertUtil {

    public static LocalDateTime convertToLocalDateTime(Object value) {
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        } else if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime();
        } else if (value instanceof String) {
            return LocalDateTime.parse((String) value);
        } else {
            throw new IllegalArgumentException("Value is not a valid type (LocalDateTime, Timestamp, String)");
        }
    }
}
