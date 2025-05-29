package ticket_online.ticket_online.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ticket_online.ticket_online.constant.ETransactionStatus;

@Entity
@Table(name = "checker")
@ToString
@Getter
@Setter
public class Checker extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event eventId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;


}
