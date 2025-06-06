package ticket_online.ticket_online.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ticket_online.ticket_online.constant.ETransactionStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checker")
@ToString
@Getter
@Setter
public class Checker extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private Event eventId;

    @ManyToOne
    @JoinColumn(name = "userId")
    @ToString.Exclude
    private User userId;

    @OneToMany(mappedBy = "checker")
    private List<DetailTransaction> detailTransactions = new ArrayList<>();

}
