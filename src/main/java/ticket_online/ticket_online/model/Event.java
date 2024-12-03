package ticket_online.ticket_online.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "events")
@ToString
@Getter
@Setter
public class Event extends BaseModel{

    private String event_title;
    private String image;
    private LocalDateTime schedule;
    private String description;
    private Long admin_id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private List<CategoryTicket> category_tickets;
}
