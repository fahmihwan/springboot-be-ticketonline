package ticket_online.ticket_online.dto.categoryTicket;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryTicketResDto {
    private Long id;
    private Long eventId;
    private String categoryName;
    private Integer price;
    private Integer quotaTicket;
    private Integer ticketsSold;
    private String description;
}
