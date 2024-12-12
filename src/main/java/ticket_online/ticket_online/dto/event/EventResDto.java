package ticket_online.ticket_online.dto.event;



import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
//@SqlResultSetMapping(
//        name = "EventResDtoMapping",
//        classes = @ConstructorResult(
//                targetClass = EventResDto.class,
//                columns = {
//                        @ColumnResult(name = "id", type = Long.class),
//                        @ColumnResult(name = "event_title", type = String.class)
//                }
//        )
//)
public class EventResDto {
    private Long id;
    private String event_title;

    // Konstruktor yang sesuai dengan parameter dari query
    public EventResDto(Long id, String event_title) {
        this.id = id;
        this.event_title = event_title;
    }
}
