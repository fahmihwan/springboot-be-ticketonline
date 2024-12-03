package ticket_online.ticket_online.dto.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ticket_online.ticket_online.model.DetailTransaction;
import ticket_online.ticket_online.model.Visitor;

import java.util.List;

@Setter
@Getter
@ToString
public class CheckoutReqDto {
    private List<Visitor> visitors;
    private List<DetailTransaction> detailTransactions;
    private Long user_id;
}


