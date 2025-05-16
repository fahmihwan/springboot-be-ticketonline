package ticket_online.ticket_online.dto.cart;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class AddCartTicketReqDto {
    private Long userId;
    private String slug;
    private List<DetailTicketReqDto> detailTransactions;

    @Setter
    @Getter
    @ToString
    public static class DetailTicketReqDto {
        private Integer total;
        private Long categoryTicketId;
    }
}

