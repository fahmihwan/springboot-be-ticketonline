package ticket_online.ticket_online.dto.cart;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartReqDto {
    private Long id;
    private Long detailTransactionId;
    private Long participantId;
    private Long categoryTicketid;
    private Integer total;
}
