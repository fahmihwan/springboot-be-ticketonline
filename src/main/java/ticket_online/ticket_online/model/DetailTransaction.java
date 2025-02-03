package ticket_online.ticket_online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "detail_transactions")
@ToString
@Getter
@Setter
public class DetailTransaction extends BaseModel {

    private Integer total;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "transaction_id")
    private Long transactionId;


}



