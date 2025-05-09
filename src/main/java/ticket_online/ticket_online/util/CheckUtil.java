package ticket_online.ticket_online.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CheckUtil {

    public static Boolean checkIsExpiredTransaction(Integer expiryPeriod, Timestamp createdAtTimestamp){
        LocalDateTime createdAt = createdAtTimestamp.toLocalDateTime();
        LocalDateTime expiryTime = createdAt.plusMinutes(expiryPeriod);
        return expiryTime.isBefore(LocalDateTime.now());
    }
}
