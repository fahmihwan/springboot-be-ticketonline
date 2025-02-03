package ticket_online.ticket_online.dto.transaction;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ticket_online.ticket_online.model.DetailTransaction;
import ticket_online.ticket_online.model.Visitor;

import java.util.List;

@Setter
@Getter
@ToString
public class AddCartTicketReqDto {
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

