package ticket_online.ticket_online.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "carts")
@ToString
@Getter
@Setter
public class Cart extends BaseModel{

    @Column(name ="detail_transaction_id")
    private Long detailTransactionId;

    @Column(name ="participant_id")
    private Long participantId;

    @Column(name = "category_ticket_id")
    private Long categoryTicketid;

    private Integer total;
}
