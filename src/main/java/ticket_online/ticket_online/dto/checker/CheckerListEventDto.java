package ticket_online.ticket_online.dto.checker;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckerListEventDto {
    private Long id;
    private String eventTitle;
    private String venue;
    private String image;
    private LocalDateTime schedule;
    private String description;
    private LocalDateTime createdAt;
    private String slug;
    private Integer totalChecker;

}
