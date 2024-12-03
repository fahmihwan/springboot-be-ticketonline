package ticket_online.ticket_online.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "transactions")
@ToString
@Getter
@Setter
public class Transaction extends BaseModel{

    @Column(name = "user_id")
    private Long userId;

    private Integer total;
}
