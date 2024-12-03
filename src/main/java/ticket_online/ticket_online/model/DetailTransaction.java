package ticket_online.ticket_online.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "detail_transactions")
@ToString
@Getter
@Setter
public class DetailTransaction extends BaseModel {

    private Integer total;

    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "category_ticket_id")
    private Long categoryId;
}



