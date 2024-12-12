package ticket_online.ticket_online.dto.event;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventHomeResDto {
    private Long id;
    private String event_title;
    private String image;
    private Timestamp schedule;
    private String description;
    private Integer start_from;
}

