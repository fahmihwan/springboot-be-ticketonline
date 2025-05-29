package ticket_online.ticket_online.dto.event;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.model.LineUp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventTicketResDto {
    private Long id;
    private String eventTitle;
    private String venue;
    private String image;
    private LocalDateTime schedule;
    private String description;
    private LocalDateTime createdAt;
    private String slug;
    private List<CategoryTicket> categoryTickets;
}
