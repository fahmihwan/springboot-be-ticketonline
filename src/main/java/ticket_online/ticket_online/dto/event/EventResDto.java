package ticket_online.ticket_online.dto.event;



import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventResDto {
    private Long id;
    private String eventTitle;
    private String slug;
    private String venue;
    private String image;
    private LocalDateTime schedule;
    private String description;
    private LocalDateTime createdAt;
}
