package ticket_online.ticket_online.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventDetailResDto {
    private Long id;
    private String eventTitle;
    private String venue;
    private String image;
    private LocalDateTime schedule;
    private String description;
    private LocalDateTime createdAt;
    private Integer startFromPrice;
    private String slug;
}
