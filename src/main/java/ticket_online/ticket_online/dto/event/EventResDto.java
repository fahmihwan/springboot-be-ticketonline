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
@NoArgsConstructor
public class EventResDto {
    private Long id;
    private String eventTitle;
    private String venue;
    private String image;
    private LocalDateTime schedule;
    private String description;
    private LocalDateTime createdAt;
}
